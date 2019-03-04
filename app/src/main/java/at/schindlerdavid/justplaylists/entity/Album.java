package at.schindlerdavid.justplaylists.entity;

public class Album {
    private String id;
    private Artist artist[];
    private AlbumCover images[];
    private String name;

    public Album(String id, Artist[] artist, AlbumCover[] images, String name) {
        this.id = id;
        this.artist = artist;
        this.images = images;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Artist[] getArtist() {
        return artist;
    }

    public void setArtist(Artist[] artist) {
        this.artist = artist;
    }

    public AlbumCover[] getAlbumCovers() {
        return images;
    }

    public void setAlbumCovers(AlbumCover[] images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String artistsToString() {
        String artistsString = "";

        for (int i = 0; i < this.artist.length; i++) {
            artistsString = artistsString + this.artist[i].getName();
            if (i+1 < this.artist.length) {
                artistsString = artistsString + ", ";
            }
        }

        return artistsString;
    }
}
