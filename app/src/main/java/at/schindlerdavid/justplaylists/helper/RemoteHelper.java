package at.schindlerdavid.justplaylists.helper;

import android.util.Log;

import at.schindlerdavid.justplaylists.data.DataRepository;
import at.schindlerdavid.justplaylists.entity.Playlist;
import at.schindlerdavid.justplaylists.entity.Track;

public class RemoteHelper {
    public static void playTrackOnSpotify(Track track) {
        playOnSpotify(track.getId(), "track");
    }

    public static void playTrackOnSpotify(Track track, int positionInList) {
        if (DataRepository.getCurrentQueue().size() == 0) {

        }
        playOnSpotify(track.getId(), "track");

    }

    public static void playAlbumOnSpotify(String albumId) {
        playOnSpotify(albumId, "album");
    }

    public static void playPlaylistOnSpotify(Playlist playlist) {
        playOnSpotify(playlist.getId(), "playlist");
    }

    public static void playOnSpotify(String id, String type) {
        //DataRepository.getCurrentQueue().clear();
        DataRepository.getSpotifyAppRemote().getPlayerApi().play("spotify:" + type + ":" + id);
    }

    public static void playNextTrack() {
        DataRepository.getSpotifyAppRemote().getPlayerApi().skipNext();
    }

    public static void playPrevTrack() {
        DataRepository.getSpotifyAppRemote().getPlayerApi().skipPrevious();
    }

    public static void pausePlay() {
        DataRepository.getSpotifyAppRemote().getPlayerApi().pause();
    }

    public static void resumePlay() {
        DataRepository.getSpotifyAppRemote().getPlayerApi().resume();
    }

    public static void queuePlaylistOnSpotify(String playlistId) {
        addToQueue(playlistId, "playlist");
    }

    public static void queueTrackOnSpotify(Track track) {
        DataRepository.getCurrentQueue().add(track);
        addToQueue(track.getId(), "track");
    }

    public static void queueAlbumOnSpotify(String albumId) {
        addToQueue(albumId, "album");
    }

    public static void addToQueue(String id, String type) {
        DataRepository.getSpotifyAppRemote().getPlayerApi().queue("spotify:" + type + ":" + id);
    }
}
