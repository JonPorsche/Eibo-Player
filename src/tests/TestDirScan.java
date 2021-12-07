package tests;

import java.io.File;
import java.io.FilenameFilter;

public class TestDirScan {
    public static void main(String[] args) {
        printFnames("/Users/jonporsche/Documents");
    }

    public static void printFnames(String sDir) {
        File[] faFiles = new File(sDir).listFiles();
        for (File file : faFiles) {
            if (file.getName().matches("^(.*?)")) {
                System.out.println(file.getAbsolutePath());
            }
            if (file.isDirectory()) {
                printFnames(file.getAbsolutePath());
            }
        }
    }
}
