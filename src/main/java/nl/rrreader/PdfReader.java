package nl.rrreader;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Pattern;

public class PdfReader {

    protected static final String[] START_CELL_STRINGS = {"AGF", "Goederenverwerking", "Vleeswaren/Kaas", "Kassa", "Bedrijfsleiding",
            "Proces", "Bakkerij", "Schoonmaak", "E-Commerce", "Verslijn", "Verslijn/Verskeuken"};
    protected static final String END_CELL_STRING = "Totaal";
    protected static final String END_CELL_STRING2 = "Rooster";
    protected final int YEAR = Calendar.getInstance().get(Calendar.YEAR);
    protected final Sheet sheet;
    protected final int NAME_OFFSET = 0;
    protected final List<Integer> START_CELLS = new ArrayList<>();
    protected final List<Integer> END_CELLS = new ArrayList<>();

    protected final ArrayList<Persoon> personen;

    public PdfReader(Sheet s) {
        this.sheet = s;
        personen = new ArrayList<>();
        findStartEndCells();
        readPersonen();

        try {
            readWerktijden();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    protected void readPersonen() {
        assert START_CELLS.size() == END_CELLS.size();

        for (int i = 0; i < START_CELLS.size(); i++) {

            for (int j = START_CELLS.get(i); j <= END_CELLS.get(i); j++) {
                String[] naam = sheet.getRow(j).getCell(NAME_OFFSET).getStringCellValue().trim().split(" ");
                String voornaam = naam[0];
                String achternaam = String.join(" ", Arrays.copyOfRange(naam, 1, naam.length));

                // Create Persoon object and add to ArrayList
                Persoon p = new Persoon(voornaam, achternaam, j);
                p.set_afdeling(sheet.getRow(START_CELLS.get(i) - 1).getCell(NAME_OFFSET).getStringCellValue());


                if (sheet.getRow(j + 1).getCell(NAME_OFFSET).getStringCellValue().equals("")) {
                    // Still part of the same person
                    while (sheet.getRow(j + 1).getCell(NAME_OFFSET).getStringCellValue().equals("")) {
                        j++;
                    }
                }

                p.set_e_cell(j);
                personen.add(p);
            }
        }
    }

    protected void findStartEndCells() {
        for (int i = 0; i < sheet.getLastRowNum() + 1; i++) {

            try {
                // Check if cell content equals the START_CELL
                if (Arrays.asList(START_CELL_STRINGS).contains(sheet.getRow(i).getCell(NAME_OFFSET).getStringCellValue())) {
                    START_CELLS.add(i + 1);
                }

                // Check if cell content equals the END_CELL
                if (sheet.getRow(i).getCell(NAME_OFFSET, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue().contains(END_CELL_STRING)) {
                    END_CELLS.add(i - 1);
                }

                if (sheet.getRow(i).getCell(NAME_OFFSET, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue().contains(END_CELL_STRING2)) {
                    if (END_CELLS.size() != 0 && i - END_CELLS.get(END_CELLS.size() - 1) > 3) {
                        END_CELLS.add(i - 1);
                    }
                }

            } catch (NullPointerException e) {
                System.out.println("NPE @ " + i);
            }
        }
    }

    private static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private static LocalTime convertToLocalTime(Date dateToConvert) {
        return LocalDateTime.ofInstant(dateToConvert.toInstant(),
                ZoneId.systemDefault()).toLocalTime();
    }

    protected void readWerktijden() throws ParseException {
        DataFormatter df = new DataFormatter();
        DateFormat format = new SimpleDateFormat("HH:mm");
        DateFormat dateFormat = new SimpleDateFormat("dd-MM");
        WtColor colorMapper = new WtColor();


        for (Persoon p : personen) {
            for (int i = 3; i < 7 * 3 + 3; i += 3) { // For every day; i = col

                String tm = sheet.getRow(START_CELLS.get(0) - 3).getCell(i).getStringCellValue().split(Pattern.quote("("))[1].split(Pattern.quote(")"))[0];
                LocalDate dag = convertToLocalDateViaInstant(dateFormat.parse(tm)).withYear(YEAR);
                Werktijd wt = new Werktijd(dag);

                for (int j = p.s_cell(); j <= p.e_cell(); j++) {

                    if (!df.formatCellValue(sheet.getRow(j).getCell(i)).equals("")) {  // Check if cell is empty
                        String begin = sheet.getRow(j).getCell(i).getStringCellValue();
                        String eind = sheet.getRow(j).getCell(i + 1).getStringCellValue();

                        if (begin.equals("Afwezig")) {
                            continue;
                        }

                        LocalTime o_b = convertToLocalTime(format.parse(begin));
                        LocalTime o_e = convertToLocalTime(format.parse(eind));

                        // Determine cell colors
                        String color = colorMapper.mapColor(p.getAfdeling());

                        if (!df.formatCellValue(sheet.getRow(j).getCell(NAME_OFFSET + 1)).equals("")) {
                            color = colorMapper.mapColor(sheet.getRow(j).getCell(NAME_OFFSET + 1).getStringCellValue());
                        }

                        if (df.formatCellValue(sheet.getRow(j).getCell(i + 2)).equals("")) { // Detect pauze
                            WerkBlok wb = new WerkBlok(o_b, o_e);
                            wt.add_werkblok(wb);
                            wb.setColor(color);
                        } else {
                            String pauze = sheet.getRow(j).getCell(i + 2).getStringCellValue();
                            if (pauze.equals("Afwezig")) {
                                continue;
                            }
                            LocalTime pz = convertToLocalTime(format.parse(pauze));
                            WerkBlok wb = new WerkBlok(o_b, o_e, pz);
                            wt.add_werkblok(wb);
                            wb.setColor(color);
                        }
                    }
                }

                // Add werktijd to persoon
                if (wt.isValid()) {
                    p.addWerktijd(wt);
                }
            }
        }

    }

    public ArrayList<Persoon> getPersonen() {
        return personen;
    }
}
