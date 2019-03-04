package at.schindlerdavid.justplaylists.entity;

import android.graphics.Bitmap;

public class Artist {
    private String id;
    private String name;
    private String genres[];
    private String href;
    private AlbumCover images[];
    private int popularity;
    private Bitmap bitmap;

    public Artist(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Artist(String id, String name, String[] genres, String href, AlbumCover[] images, int popularity) {
        this.id = id;
        this.name = name;
        this.genres = genres;
        this.href = href;
        this.images = images;
        this.popularity = popularity;
    }

    public void setImages(AlbumCover[] images) {
        this.images = images;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String[] getGenres() {
        return genres;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public AlbumCover[] getImages() {
        return images;
    }

    public void AlbumCover(AlbumCover[] images) {
        this.images = images;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
