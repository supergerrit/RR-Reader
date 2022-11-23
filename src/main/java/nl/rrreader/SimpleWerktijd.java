package nl.rrreader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class SimpleWerktijd {

    private final String voornaam;
    private final String achternaam;
    private final LocalDate datum;
    private LocalTime begin;
    private LocalTime eind;
    private LocalTime pauze;
    private String[] colors;

    public SimpleWerktijd(String voornaam, String achternaam, LocalDate datum, LocalTime begin, LocalTime eind, LocalTime pauze, String[] colors) {
        this.voornaam = voornaam;
        this.achternaam = achternaam;
        this.datum = datum;
        this.begin = begin;
        this.eind = eind;
        this.pauze = pauze;
        this.colors = colors;
    }

    public JSONObject getJsonObject() {
        JSONObject wtijd = new JSONObject();
        wtijd.put("voornaam", voornaam);
        wtijd.put("achternaam", achternaam);
        wtijd.put("datum", datum);
        wtijd.put("begin", begin);
        wtijd.put("eind", eind);
        wtijd.put("pauze", pauze);

        JSONArray col = new JSONArray();
        for (String c : colors) {
            col.put(c);
        }

        wtijd.put("colors", col);

        return wtijd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleWerktijd that = (SimpleWerktijd) o;
        return Objects.equals(voornaam, that.voornaam) &&
                Objects.equals(achternaam, that.achternaam) &&
                Objects.equals(datum, that.datum) &&
                Objects.equals(begin, that.begin) &&
                Objects.equals(eind, that.eind) &&
                Objects.equals(pauze, that.pauze);
    }

    @Override
    public int hashCode() {
        return Objects.hash(voornaam, achternaam, datum);
    }

    public LocalTime getBegin() {
        return begin;
    }

    public LocalTime getEind() {
        return eind;
    }

    public LocalTime getPauze() {
        return pauze;
    }

    public void merge(SimpleWerktijd swt) {
        if (swt.getBegin().isBefore(begin)) {
            begin = swt.getBegin();
            pauze = maxTime(pauze, swt.getPauze());
            colors = maxColors(colors, swt.colors);
        }
        if (swt.getEind().isAfter(eind)) {
            eind = swt.getEind();
            pauze = maxTime(pauze, swt.getPauze());
            colors = maxColors(colors, swt.colors);
        }
        // pauze = pauze.plus(swt.getPauze());
    }

    private String[] maxColors(String[] colors, String[] colors1) {
        if (colors.length > colors1.length) {
            return colors;
        } else {
            return colors1;
        }
    }

    private LocalTime maxTime(LocalTime pauze, LocalTime pauze1) {
        if (pauze.isAfter(pauze1)) {
            return pauze;
        } else {
            return pauze1;
        }
    }
}
