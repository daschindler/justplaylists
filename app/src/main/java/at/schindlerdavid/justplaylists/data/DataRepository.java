package at.schindlerdavid.justplaylists.data;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import java.util.ArrayList;
import java.util.List;

import at.schindlerdavid.justplaylists.callbacks.PlaylistsLoadedCallback;
import at.schindlerdavid.justplaylists.callbacks.SpotifyCallback;
import at.schindlerdavid.justplaylists.callbacks.TracksLoadedCallback;
import at.schindlerdavid.justplaylists.api.APIClient;
import at.schindlerdavid.justplaylists.api.APIService;
import at.schindlerdavid.justplaylists.entity.Playlist;
import at.schindlerdavid.justplaylists.entity.ResponsePlaylist;
import at.schindlerdavid.justplaylists.entity.ResponseTracks;
import at.schindlerdavid.justplaylists.entity.Track;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataRepository {
    private static final String CLIENT_ID = "9d3d1efaa02a4b86ac4cff6e3bf09f41";
    private static final String REDIRECT_URI = "at.schindlerdavid.justplaylists://callback";
    private static String responseToken = "";
    private static SpotifyAppRemote spotifyAppRemote;
    private static boolean connectedToSpotify = false;

    private static List<Playlist> playlists = new ArrayList<>();
    private static List<Track> currentShowingTracks = new ArrayList<>();

    private static List<Track> currentQueue = new ArrayList<>();

    private APIService apiService;

    private static SpotifyCallback spotifyCallback;

    public static void initSpotifyRemote(Context context) {
        if(spotifyAppRemote == null) {
            ConnectionParams connectionParams =
                    new ConnectionParams.Builder(getClientId())
                            .setRedirectUri(getRedirectUri())
                            .showAuthView(true)
                            .build();

            if (SpotifyAppRemote.isSpotifyInstalled(context)) {
                SpotifyAppRemote.connect(context, connectionParams,
                        new Connector.ConnectionListener() {

                            public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                                DataRepository.spotifyAppRemote = spotifyAppRemote;
                                Log.d("MainActivity", "Connected! Yay!");
                                connectedToSpotify = true;
                                // Subscribe to PlayerState
                                DataRepository.getSpotifyAppRemote().getPlayerApi()
                                        .subscribeToPlayerState()
                                        .setEventCallback(playerState -> {
                                            final com.spotify.protocol.types.Track track = playerState.track;
                                            if (playerState.isPaused) {
                                                spotifyCallback.onPaused();
                                            } else {
                                                spotifyCallback.onPlaying();
                                            }
                                            if (track != null) {
                                                Log.d("MainActivity", track.name + " by " + track.artist.name);
                                                if (spotifyCallback != null) {
                                                    spotifyCallback.onTrackChanged(track);
                                                }
                                            }
                                        });
                            }

                            public void onFailure(Throwable throwable) {
                                Log.e("MyActivity", throwable.getMessage(), throwable);
                            }
                        });
            }
        }
    }

    public static void setSpotifyCallback (SpotifyCallback spotifyCallback) {
        DataRepository.spotifyCallback = spotifyCallback;
    }

    public static SpotifyAppRemote getSpotifyAppRemote() {
        if(spotifyAppRemote == null) {
            throw new RuntimeException("spotifyappremote is not initialized");
        }
        return spotifyAppRemote;
    }

    public static boolean isConnectedToSpotify() {
        return connectedToSpotify;
    }

    public static String getResponseToken() {
        return responseToken;
    }

    public static void setResponseToken(String responseToken) {
        DataRepository.responseToken = responseToken;
    }

    public static String getClientId() {
        return CLIENT_ID;
    }

    public static String getRedirectUri() {
        return REDIRECT_URI;
    }

    public DataRepository(Application application) {
        apiService = APIClient.getClient().create(APIService.class);
    }

    public static List<Track> getCurrentQueue() {
        return currentQueue;
    }

    public void requestPlaylists(PlaylistsLoadedCallback playlistsLoadedCallback) {
        playlists.clear();
        doPlaylistCall(0, playlistsLoadedCallback);
    }

    public void requestBelongingTracks(final String playlistId, TracksLoadedCallback tracksLoadedCallback) {
        currentShowingTracks.clear();
        doTrackCall(0, playlistId, tracksLoadedCallback);
    }

    private void doTrackCall(final int offset, final String playlistId, final TracksLoadedCallback tracksLoadedCallback) {
        Call<ResponseTracks> call = apiService.doGetTracksFromPlaylist(playlistId, offset);
        call.enqueue(new Callback<ResponseTracks>() {
            @Override
            public void onResponse(Call<ResponseTracks> call, Response<ResponseTracks> response) {
                //currentShowingTracks.addAll(response.body().getItems());
                for (int i = 0; i < response.body().getItems().size(); i++) {
                    DataRepository.currentShowingTracks.add(response.body().getItems().get(i).getTrack());
                }

                if (response.body().getNext() != null) {
                    doTrackCall(100+offset, playlistId, tracksLoadedCallback);
                } else {
                    tracksLoadedCallback.onTracksLoaded();
                }
            }

            @Override
            public void onFailure(Call<ResponseTracks> call, Throwable t) {

            }
        });
    }

    private void doPlaylistCall(final int offset, final PlaylistsLoadedCallback playlistsLoadedCallback) {
        Call<ResponsePlaylist> call = apiService.doGetAllPlaylists(offset);
        call.enqueue(new Callback<ResponsePlaylist>() {
            @Override
            public void onResponse(Call<ResponsePlaylist> call, Response<ResponsePlaylist> response) {
                Log.d("code", String.valueOf(response.code()));
                playlists.addAll(response.body().getItems());
                if (response.body().getNext() != null) {
                    doPlaylistCall(offset+20, playlistsLoadedCallback);
                } else {
                    playlistsLoadedCallback.onRequestFinished();
                }
            }

            @Override
            public void onFailure(Call<ResponsePlaylist> call, Throwable t) {
                Log.d("failed", "failed");
            }
        });
    }

    public static List<Playlist> getPlaylists() {
        return playlists;
    }

    public static List<Track> getCurrentShowingTracks() {
        return currentShowingTracks;
    }
}
