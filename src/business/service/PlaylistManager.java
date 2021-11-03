package business.service;

import business.data.Playlist;

import java.io.*;

public class PlaylistManager {

    private Playlist playlist;

/*    public void getAllTracks(String sDir) throws IOException {
        File[] faFiles = new File(sDir).listFiles(); // load files of dir into array
        for (File file : faFiles) {
            System.out.println("for");
            if (file.getName().matches("^(.*?)")) {
                writeM3UFile(file.getAbsolutePath());
                System.out.println(file.getAbsolutePath());
            }
            if (file.isDirectory()) {
                getAllTracks(file.getAbsolutePath());
            }
        }
    }*/

    public void getAllTracks(String sDir) throws IOException {
        // load files of dir into array
        File[] faFiles = new File(sDir).listFiles();
        // create new M3U playlist file
        File m3uFile = new File("/Users/jonporsche/Documents/Dev Projects.nosync/eibo_test1/playlists/playlist.m3u");
        // output stream to write to the file
        FileOutputStream fOutStream = new FileOutputStream(m3uFile);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fOutStream));

        // write file header

        for (File file : faFiles) {
            if (file.getName().matches("^(.*?)")) {
                bw.write(file.getAbsolutePath());
                bw.newLine();
                // id3 tags von Datei lesen und Track object anlegen
            }
            if (file.isDirectory()) {
                getAllTracks(file.getAbsolutePath());
            }
        }
        bw.close();
    }

    public void writeM3UFile(String path) throws IOException {
            File m3uFile = new File("/Users/jonporsche/Documents/Dev Projects.nosync/eibo_test1/playlists/playlist.m3u");
            FileOutputStream fOutStream = new FileOutputStream(m3uFile);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fOutStream));
            bw.write(path); // in der Schleife
            bw.newLine(); // in der Schleife
            bw.close(); // außer der Schleife

    }

/*    public void writeM3UFile(String path) throws IOException {
        try (FileWriter file = new FileWriter("playlist.m3u")) {
            File fout = new File("/Users/jonporsche/Documents/Dev Projects.nosync/eibo_test1/playlists/playlist.m3u");
            FileOutputStream fos = new FileOutputStream(fout);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(path); // in der Schleife
            bw.newLine(); // in der Schleife
            bw.close(); // außer der Schleife
        }

        catch(IOException e) {
            e.printStackTrace();
        }
    }*/
}
