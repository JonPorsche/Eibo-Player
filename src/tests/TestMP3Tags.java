package tests;

import com.mpatric.mp3agic.*;

import javax.swing.*;
import java.io.IOException;

public class TestMP3Tags {
    public static void main(String[] args)
    {
        Mp3File mp3file;
        try {
            JFileChooser jfc = new JFileChooser();
            int fileResult = jfc.showOpenDialog(null);
            if (fileResult == JFileChooser.APPROVE_OPTION) {
                String path = jfc.getSelectedFile().getPath();
                System.out.println("Path: "+path);
                mp3file = new Mp3File(path);
                if (mp3file.hasId3v1Tag()) {
                    ID3v1 id3v1Tag = mp3file.getId3v1Tag();
                    System.out.println("Track: " + id3v1Tag.getTrack());
                    System.out.println("Artist: " + id3v1Tag.getArtist());
                    System.out.println("Title: " + id3v1Tag.getTitle());
                    System.out.println("Album: " + id3v1Tag.getAlbum());
                    System.out.println("Year: " + id3v1Tag.getYear());
                    System.out.println("Genre: " + id3v1Tag.getGenre() + "("+id3v1Tag.getGenreDescription() + ")");
                    System.out.println("Comment: " + id3v1Tag.getComment());
                } else {
                    System.out.println("The mp3 file does not exists or does not have a ID3v1Tag");
                }
            }
        } catch (UnsupportedTagException | InvalidDataException | IOException e) {
            System.err.println("File not found.");
            e.printStackTrace();
        }
    }
}
