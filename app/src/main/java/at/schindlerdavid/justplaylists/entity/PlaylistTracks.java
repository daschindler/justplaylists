package at.schindlerdavid.justplaylists.entity;

public class PlaylistTracks {
    private int total;
    private String href;
    private Track[] items;

    public PlaylistTracks(int total, String href) {
        this.total = total;
        this.href = href;
    }

    public Track[] getItems() {
        return items;
    }

    public void setItems(Track[] items) {
        this.items = items;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
