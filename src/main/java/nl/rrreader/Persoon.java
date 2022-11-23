package nl.rrreader;

import java.util.ArrayList;

public class Persoon {

    private final String voornaam;
    private final String achternaam;
    private final int start_cell;
    private int end_cell;
    private ArrayList<Werktijd> werktijden;
    private String afdeling = "";

// --Commented out by Inspection START (22-11-2022 20:52):
//    public Persoon(String voornaam, String achternaam, int start_cell, int end_cell) {
//        this.voornaam = voornaam;
//        this.achternaam = achternaam;
//        this.start_cell = start_cell;
//        this.end_cell = end_cell;
//        werktijden = new ArrayList<>();
//    }
// --Commented out by Inspection STOP (22-11-2022 20:52)

    public Persoon(String voornaam, String achternaam, int naam_cell) {
        this.voornaam = voornaam;
        this.achternaam = achternaam;
        this.start_cell = naam_cell;
        this.end_cell = naam_cell;
        werktijden = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(voornaam).append(" ").append(achternaam).append("\n");
        for (Werktijd w : werktijden) {
            sb.append(w).append("\n");
        }
        sb.append("\n");
        return sb.toString();
    }

    public ArrayList<Werktijd> getWerktijden() {
        return werktijden;
    }

    public void setWerktijden(ArrayList<Werktijd> werktijden) {
        this.werktijden = werktijden;
    }

    public void addWerktijd(Werktijd w) {
        werktijden.add(w);
    }

    public int s_cell() {
        return start_cell;
    }

    public int e_cell() {
        return end_cell;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public String getFullName () {
        return voornaam + " " + achternaam;
    }

    public void set_e_cell(int e_cell){ this.end_cell = e_cell; }

    public void set_afdeling(String afdeling){
        this.afdeling = afdeling;
    }

    public String getAfdeling(){
        return afdeling;
    }
}
