package at.schindlerdavid.justplaylists.entity.api;

import java.util.List;

public class ResponseTracks {
    private String href;
    private List<ResponseTrackItem> items;
    private int limit;
    private String next;
    private int offset;
    private String previous;
    private int total;

    public ResponseTracks() {
    }

    public ResponseTracks(String href, List<ResponseTrackItem> items, int limit, String next, int offset, String previous, int total) {
        this.href = href;
        this.items = items;
        this.limit = limit;
        this.next = next;
        this.offset = offset;
        this.previous = previous;
        this.total = total;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public List<ResponseTrackItem> getItems() {
        return items;
    }

    public void setItems(List<ResponseTrackItem> items) {
        this.items = items;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
