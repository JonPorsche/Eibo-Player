package tests;

import java.io.File;
import java.io.FilenameFilter;

public class Filter {

    public static void main(String[] args){
        finder("/Users/jonporsche/Documents/Dev Projects.nosync/eibo_test1/music");
    }

    public static File[] finder(String dirName) {
        File dir = new File(dirName);

        return dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".mp3");
            }
        });

    }
}
