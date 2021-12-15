package sample;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SearchMedialabFolder {
    public List<String> enemies = new ArrayList<String>();
    public List<String> players = new ArrayList<String>();


    public void FileNames() {
        File[] files = new File("./src/medialab/").listFiles();

        // If this pathname does not denote a directory, then listFiles() returns null.
        for (File file : files) {
            if (file.isFile()) {
                String x = file.getName();
                String[] parts = x.split("_");
                String part1 = parts[0];
                String part2 = parts[1];
                String[] ids = part2.split("\\.");
                String id = ids[0];

                if (part1.equals("enemy")) {
                    enemies.add(id);
                } else {
                    players.add(id);
                }
            }
        }
    }
}
