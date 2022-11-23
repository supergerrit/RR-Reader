package nl.rrreader;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

public class Werktijd {

    private final ArrayList<WerkBlok> werktijden = new ArrayList<>();
    private final LocalDate dag;

    public Werktijd(LocalDate dag) {
        this.dag = dag;
    }

    public void add_werkblok(WerkBlok wb) {
        werktijden.add(wb);
    }

    private LocalTime findBegin() {
        LocalTime min = LocalTime.of(23, 59);

        for (WerkBlok wb : werktijden) {
            if (wb.getBegin().isBefore(min)) {
                min = wb.getBegin();
            }
        }

        return min;
    }

    private LocalTime findEind() {
        LocalTime max = LocalTime.of(0, 1);

        for (WerkBlok wb : werktijden) {
            if (wb.getEind().isAfter(max)) {
                max = wb.getEind();
            }
        }

        return max;
    }

    public WerkBlok getWerktijd() {
        return new WerkBlok(findBegin(), findEind(), getPauze());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(dag);
        sb.append(" -> ");
        for (WerkBlok w : werktijden) {
            sb.append(w);
            sb.append(" ");
        }
        return sb.toString();
    }


    public LocalTime getPauze() {
        LocalTime pauze = LocalTime.of(0, 0);

        for (WerkBlok wb : werktijden) {
            pauze = pauze.plusHours(wb.getPauze().getHour());
            pauze = pauze.plusMinutes(wb.getPauze().getMinute());
        }

        // sort werktijden
        werktijden.sort(Comparator.comparing(WerkBlok::getBegin));

        // find gaps
        for (int i = 0; i < werktijden.size() - 1; i++) {
            LocalTime begin = werktijden.get(i).getEind();
            LocalTime eind = werktijden.get(i + 1).getBegin();
            if (begin != eind) {
                pauze = pauze.plusHours(begin.until(eind, java.time.temporal.ChronoUnit.HOURS));
                pauze = pauze.plusMinutes(begin.until(eind, java.time.temporal.ChronoUnit.MINUTES));
            }
        }


        return pauze;
    }

    public LocalDate getDag() {
        return dag;
    }

    public String[] getDistinctColors() {
        HashSet<String> tmp = new HashSet<>();
        for (WerkBlok wb : werktijden) {
            tmp.add(wb.getColor());
        }

        String[] unique = new String[tmp.size()];
        tmp.toArray(unique);

        return unique;
    }

    public boolean isValid() {
        return werktijden.size() >= 1;
    }
}
