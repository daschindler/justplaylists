package at.schindlerdavid.justplaylists.callbacks;

import com.spotify.protocol.types.Track;

public interface SpotifyCallback {
    void onTrackChanged(Track track);
    void onPaused();
    void onPlaying();
}
