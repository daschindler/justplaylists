package at.schindlerdavid.justplaylists.entity.api;

import java.util.Date;
import java.util.List;

import at.schindlerdavid.justplaylists.entity.Track;

public class ResponseTrackItem {
    private Date added_at;
    private Track track;

    public ResponseTrackItem() {
    }

    public ResponseTrackItem(Date added_at, Track track) {
        this.added_at = added_at;
        this.track = track;
    }

    public Date getAdded_at() {
        return added_at;
    }

    public void setAdded_at(Date added_at) {
        this.added_at = added_at;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }
}
