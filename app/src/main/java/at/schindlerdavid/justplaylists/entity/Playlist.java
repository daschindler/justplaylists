package at.schindlerdavid.justplaylists.entity;


//https://api.spotify.com/v1/users/{user_id}/playlists
public class Playlist {
    private String href;
    private String id;
    private String name;
    private AlbumCover[] images;
    private ResponseTracks tracks;

    public Playlist(String href, String id, String name, AlbumCover[] images, ResponseTracks tracks) {
        this.href = href;
        this.id = id;
        this.name = name;
        this.images = images;
        this.tracks = tracks;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AlbumCover[] getImages() {
        return images;
    }

    public void setImages(AlbumCover[] images) {
        this.images = images;
    }

    public ResponseTracks getTracks() {
        return tracks;
    }

    public void setTracks(ResponseTracks tracks) {
        this.tracks = tracks;
    }

    public String getCoverURL300() {
        String coverUrl = "";

        if (this.getImages().length > 0) {
            if (this.getImages().length < 2) {
                if (this.getImages().length != 0) {
                    coverUrl = this.getImages()[0].getUrl();
                }
            } else {
                if (this.getImages().length != 0) {
                    coverUrl = this.getImages()[1].getUrl();
                }
            }
        }

        return coverUrl;
    }

    public String getCoverURL640() {
        String coverUrl = "";

        if (this.getImages().length > 0) {
            coverUrl = this.getImages()[0].getUrl();
        }

        return coverUrl;
    }
}
