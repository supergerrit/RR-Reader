package nl.rrreader;

import java.time.LocalTime;

public class WerkBlok {
    private final LocalTime begin;
    private final LocalTime eind;
    private final LocalTime pauze;
    private String color = "#000000";

    public WerkBlok(LocalTime begin, LocalTime eind, LocalTime pauze) {
        this.begin = begin;
        this.eind = eind;
        this.pauze = pauze;
    }

    public WerkBlok(LocalTime begin, LocalTime eind) {
        this.begin = begin;
        this.eind = eind;
        this.pauze = LocalTime.of(0, 0);
    }

    @Override
    public String toString() {
        return begin + " - " + eind + " [" + pauze + "]";
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

    public boolean hasOverlap(LocalTime StartB, LocalTime EndB) {
        return (begin.isBefore(EndB)) && (eind.isAfter(StartB));
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
