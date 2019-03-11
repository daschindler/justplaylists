package at.schindlerdavid.justplaylists.helper;


import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import at.schindlerdavid.justplaylists.data.DataRepository;
import at.schindlerdavid.justplaylists.entity.Playlist;
import at.schindlerdavid.justplaylists.entity.PostTrack;
import at.schindlerdavid.justplaylists.entity.Track;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteHelper {
    public static void playTrackOnSpotify(Track track) {
        playOnSpotify(track.getId(), "track");
    }

    public static void playTrackOnSpotify(Track track, List<Track> tracks, int positionInList) {
        if (DataRepository.getQueuePlaylistId() != null) {
            PostTrack postTrack = new PostTrack(CreateTrackUris(tracks, positionInList));
            DataRepository.getApiService().insertTrackToPlaylist(DataRepository.getQueuePlaylistId(), CreateTrackUrisAsString(tracks, positionInList)).enqueue(new Callback<PostTrack>() {
                @Override
                public void onResponse(Call<PostTrack> call, Response<PostTrack> response) {
                    Log.d("insertTracks", String.valueOf(response.code()));
                    playPlaylistOnSpotify(DataRepository.getQueuePlaylistId());
                    //todo: clear playlist
                }

                @Override
                public void onFailure(Call<PostTrack> call, Throwable t) {

                }
            });
        }

        //playOnSpotify(track.getId(), "track");

    }

    private static String[] CreateTrackUris(List<Track> tracks, int positionInList) {
        List<String> uriList = new ArrayList<>();
        for (int i = positionInList; i < tracks.size(); i++){
            uriList.add("spotify:track:" + tracks.get(i).getId());
        }
        return uriList.toArray(new String[uriList.size()]);
    }

    private static String CreateTrackUrisAsString(List<Track> tracks, int positionInList) {
        StringBuilder uriList = new StringBuilder();
        for (int i = positionInList; i < tracks.size(); i++){
            uriList.append("spotify:track:" + tracks.get(i).getId()+",");
        }
        return uriList.toString();
    }

    public static void playAlbumOnSpotify(String albumId) {
        playOnSpotify(albumId, "album");
    }

    public static void playPlaylistOnSpotify(String playlistId) {
        playOnSpotify(playlistId, "playlist");
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
