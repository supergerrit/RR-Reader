package nl.rrreader;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {

        // Check if path is given
        if (args.length == 0) {
            System.out.println("Proper Usage is: java -jar RRreader.jar filename.xlsx");
            System.exit(0);
        }

        File file = new File(args[0]);

        if (file.isFile() && getExtensionByStringHandling(file.getName()).isPresent() && getExtensionByStringHandling(file.getName()).get().equals("xlsx")) {
            PlanningReader r = null;
            try {
                r = new PlanningReader(file.getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Print output
            assert r != null;
            System.out.println(r.getJson().toString());
        } else {
            System.out.println("Invalid file.");
        }

    }

    public static Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }
}