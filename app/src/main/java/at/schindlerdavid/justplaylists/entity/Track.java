package at.schindlerdavid.justplaylists.entity;

import android.graphics.Bitmap;

public class Track {
    private String name;
    private String id;
    private String href;
    private Album album;
    private Artist artists[];
    private int popularity;
    private Bitmap bitmap;

    public Track(String name, String id, String href, Album album, Artist[] artist, int popularity, String cover_url) {
        this.name = name;
        this.id = id;
        this.href = href;
        this.album = album;
        this.artists = artist;
        this.popularity = popularity;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Artist[] getArtists() {
        return artists;
    }

    public void setArtists(Artist[] artist) {
        this.artists = artists;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String artistsToString() {
        String artistsString = "";

        for (int i = 0; i < this.artists.length; i++) {
            artistsString = artistsString + this.artists[i].getName();
            if (i+1 < this.artists.length) {
                artistsString = artistsString + ", ";
            }
        }

        return artistsString;
    }

}
