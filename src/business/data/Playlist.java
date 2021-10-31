package business.data;

import java.util.Date;

public class Playlist {

    private long id;
    private String title;
    private Date creationDate;
    private String coverFile;

    public Playlist(long id, String title, Date creationDate, String coverFile) {
        this.id = id;
        this.title = title;
        this.creationDate = creationDate;
        this.coverFile = coverFile;
    }
}
