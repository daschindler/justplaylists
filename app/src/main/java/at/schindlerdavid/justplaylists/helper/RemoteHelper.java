package at.schindlerdavid.justplaylists.helper;


import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import at.schindlerdavid.justplaylists.data.DataRepository;
import at.schindlerdavid.justplaylists.entity.Playlist;
import at.schindlerdavid.justplaylists.entity.api.PostPlaylist;
import at.schindlerdavid.justplaylists.entity.api.PostTrack;
import at.schindlerdavid.justplaylists.entity.Track;
import at.schindlerdavid.justplaylists.entity.api.PutTrack;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteHelper {
    public static void playTrackOnSpotify(Track track) {
        playOnSpotify(track.getId(), "track");
    }

    public static void playTrackOnSpotify(Track track, List<Track> tracks, int positionInList) {
        if (DataRepository.getQueuePlaylistId() != null) {
            DataRepository.getApiService().insertTrackToPlaylist(DataRepository.getQueuePlaylistId(), CreateTrackUrisAsString(tracks, positionInList)).enqueue(new Callback<PostTrack>() {
                @Override
                public void onResponse(Call<PostTrack> call, Response<PostTrack> response) {
                    Log.d("insertTracks", String.valueOf(response.code()));
                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                        playPlaylistOnSpotify(DataRepository.getQueuePlaylistId());
                        new ClearTracksFromQueuePlaylistTask().execute();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<PostTrack> call, Throwable t) {

                }
            });
        }

    }

    public static void playAlbumOnSpotify(String albumId) {
        playOnSpotify(albumId, "album");
    }

    private static void playPlaylistOnSpotify(String playlistId) {
        playOnSpotify(playlistId, "playlist");
    }

    public static void playPlaylistOnSpotify(Playlist playlist) {
        playOnSpotify(playlist.getId(), "playlist");
    }

    private static void playOnSpotify(String id, String type) {
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

    public static void queueTrackOnSpotify(Track track) {
        DataRepository.getCurrentQueue().add(track);
        addToQueue(track.getId(), "track");
    }

    private static void addToQueue(String id, String type) {
        DataRepository.getSpotifyAppRemote().getPlayerApi().queue("spotify:" + type + ":" + id);
    }

    private static String CreateTrackUrisAsString(List<Track> tracks, int positionInList) {
        StringBuilder uriList = new StringBuilder();
        int maxSize = tracks.size();

        if (maxSize-positionInList > 100) {
            maxSize = 100;
        }
        for (int i = positionInList; i < maxSize; i++){
            uriList.append("spotify:track:" + tracks.get(i).getId()+",");
        }
        return uriList.toString();
    }

    private static class ClearTracksFromQueuePlaylistTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                TimeUnit.SECONDS.sleep(2);
                DataRepository.getApiService().clearQueuePlaylist(DataRepository.getQueuePlaylistId(), "").enqueue(new Callback<PutTrack>() {
                    @Override
                    public void onResponse(Call<PutTrack> call, Response<PutTrack> response) {
                        Log.d("clearTracks", String.valueOf(response.code()));
                    }

                    @Override
                    public void onFailure(Call<PutTrack> call, Throwable t) {

                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            return null;
        }
    }
}
