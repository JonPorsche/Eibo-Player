package tests;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ListFiles {
    public static void main(String[] args) {

        System.out.println("Files!!");
        try {
            Files.walk(Paths.get("."))
                    .filter(Files::isRegularFile)
                    .filter(c ->
                            c.getFileName().toString().substring(c.getFileName().toString().length()-4).contains(".mp3")
/*                                    ||
                                    c.getFileName().toString().substring(c.getFileName().toString().length()-5).contains(".jpeg")*/
                    )
                    .forEach(System.out::println);

        } catch (IOException e) {
            System.out.println("No mp3 files");
        }

/*        System.out.println("\nDirectories!!\n");
        try {
            Files.walk(Paths.get("."))
                    .filter(Files::isDirectory)
                    .forEach(System.out::println);

        } catch (IOException e) {
            System.out.println("No Jpeg files");
        }*/
    }
}
