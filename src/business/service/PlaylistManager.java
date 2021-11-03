package business.service;

import business.data.Playlist;

import java.io.*;

public class PlaylistManager {

    private Playlist playlist;

    public void getAllTracks(String sDir) throws IOException {
        File[] faFiles = new File(sDir).listFiles();
        for (File file : faFiles) {
            if (file.getName().matches("^(.*?)")) {
                writeM3UFile();
                System.out.println(file.getAbsolutePath());
            }
            if (file.isDirectory()) {
                getAllTracks(file.getAbsolutePath());
            }
        }
    }

    public void writeM3UFile() throws IOException {
        try (FileWriter file = new FileWriter("playlist.m3u")) {
            File fout = new File("/Users/jonporsche/Documents/Dev Projects.nosync/eibo_test1/playlists/playlist.m3u");
            FileOutputStream fos = new FileOutputStream(fout);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write("something");
            bw.newLine();
            bw.close();
        }

        catch(IOException e) {
            e.printStackTrace();
        }

    }
}
