package nl.rrreader;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class PlanningReader {

    private final ArrayList<Persoon> personen;

    public PlanningReader(String filename) throws IOException {

        Workbook workbook = WorkbookFactory.create(new File(filename));
        Sheet planning = workbook.getSheetAt(0);
        PdfReader r = new PdfReader(planning);

        personen = new ArrayList<>();
        personen.addAll(r.getPersonen());
    }

    public JSONArray getJson() {

        JSONArray werktijden = new JSONArray();
        HashMap<Integer, SimpleWerktijd> wt = new HashMap<>();

        for (Persoon p : personen) {
            for (Werktijd w : p.getWerktijden()) {
                WerkBlok wb = w.getWerktijd();
                SimpleWerktijd swt = new SimpleWerktijd(p.getVoornaam(), p.getAchternaam(), w.getDag(), wb.getBegin(), wb.getEind(), w.getPauze(), w.getDistinctColors());

                if (wt.containsKey(swt.hashCode())) { // duplicate
                    SimpleWerktijd swt2 = wt.get(swt.hashCode());
                    swt2.merge(swt);
                    wt.put(swt2.hashCode(), swt2);
                } else {
                    wt.put(swt.hashCode(), swt);
                }
            }
        }


        for (SimpleWerktijd swt : wt.values()) {
            werktijden.put(swt.getJsonObject());
        }

        return werktijden;
    }
}
