package nl.rrreader;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WtColor {

    final Map<String, String> colorMap = new HashMap<>();
    public WtColor(){
        colorMap.put("AGF", "#008000");
        colorMap.put("Goederenverwerking", "#e5e7e9");
        colorMap.put("Vleeswaren/Kaas", "#993300");
        colorMap.put("Kassa", "#FF0000");
        colorMap.put("Bedrijfsleiding", "#003366");
        colorMap.put("Proces", "#FFCC00");

        colorMap.put("Schoonmaak", "#FF99CC");
        colorMap.put("Schoonma a", "#FF99CC");
        colorMap.put("Vleesware", "#00FF00");
        colorMap.put("Opleiding", "#FF00FF");
        colorMap.put("Bakkerij", "#0066CC");
        colorMap.put("Goederenv", "#e5e7e9");
        colorMap.put("Bedrijfsl", "#003366");
        colorMap.put("E-\nCommerc", "#FFFF00");
    }

    public String mapColor(String afdeling){
        String c = colorMap.get(afdeling);
        return Objects.requireNonNullElse(c, "#e5e7e9");
    }
}