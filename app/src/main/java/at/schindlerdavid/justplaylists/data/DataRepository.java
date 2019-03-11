package at.schindlerdavid.justplaylists.data;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
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
import at.schindlerdavid.justplaylists.entity.api.PostPlaylist;
import at.schindlerdavid.justplaylists.entity.api.ResponsePlaylist;
import at.schindlerdavid.justplaylists.entity.api.ResponseTracks;
import at.schindlerdavid.justplaylists.entity.Track;
import at.schindlerdavid.justplaylists.entity.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataRepository {
    private static final String CLIENT_ID = "9d3d1efaa02a4b86ac4cff6e3bf09f41";
    private static final String REDIRECT_URI = "at.schindlerdavid.justplaylists://callback";
    public static final String QUEUE_PLAYLIST_NAME = "Simpleplaylistsqueue";

    private static String responseToken = "";
    private static SpotifyAppRemote spotifyAppRemote;
    private static boolean connectedToSpotify = false;
    private static String userId = "";

    private static List<Playlist> playlists = new ArrayList<>();
    private static List<Track> currentShowingTracks = new ArrayList<>();
    private static List<Track> currentQueue = new ArrayList<>();

    private static APIService apiService;
    private static SpotifyCallback spotifyCallback;
    private static String queuePlaylistId;

    public static String getQueuePlaylistId() {
        return queuePlaylistId;
    }

    public DataRepository(Application application) {
        apiService = APIClient.getClient().create(APIService.class);
    }

    public static APIService getApiService() {
        return apiService;
    }

    public static SpotifyAppRemote getSpotifyAppRemote() {
        if(spotifyAppRemote == null) {
            throw new RuntimeException("spotifyappremote is not initialized");
        }
        return spotifyAppRemote;
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

    public void setSpotifyCallback (SpotifyCallback spotifyCallback) {
        DataRepository.spotifyCallback = spotifyCallback;
    }

    public static List<Track> getCurrentQueue() {
        return currentQueue;
    }

    public static List<Playlist> getPlaylists() {
        return playlists;
    }

    public static List<Track> getCurrentShowingTracks() {
        return currentShowingTracks;
    }


    public void loadUser() {
        Call<User> userCall = apiService.doGetCurrentUser();
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                DataRepository.userId = response.body().getId();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    public void initSpotifyRemote(Context context) {
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
                    List<Playlist> playlists = DataRepository.getPlaylists();
                    boolean playlistAlreadyThere = false;

                    for (int i = 0; i < playlists.size(); i++) {
                        if (playlists.get(i).getName().equals(DataRepository.QUEUE_PLAYLIST_NAME)) {
                            playlistAlreadyThere = true;
                            DataRepository.setQueuePlaylistId(playlists.get(i).getId());
                            DataRepository.getPlaylists().remove(i);
                        }
                    }

                    if (!playlistAlreadyThere) {
                        if (!userId.equals("")) {
                            Call<PostPlaylist> postPlaylistCall = apiService.insertPlaylist(userId, new PostPlaylist(QUEUE_PLAYLIST_NAME, "is used from simple playlists as queue", false));
                            postPlaylistCall.enqueue(new Callback<PostPlaylist>() {
                                @Override
                                public void onResponse(Call<PostPlaylist> call, Response<PostPlaylist> response) {
                                    Log.d("post", String.valueOf(response.code()));
                                }

                                @Override
                                public void onFailure(Call<PostPlaylist> call, Throwable t) {
                                    Log.d("post", "funkt nicht");
                                }
                            });
                        }
                    }
                    playlistsLoadedCallback.onRequestFinished();
                }
            }

            @Override
            public void onFailure(Call<ResponsePlaylist> call, Throwable t) {
                Log.d("failed", "failed");
            }
        });
    }

    private static void setQueuePlaylistId(String playlistId) {
        DataRepository.queuePlaylistId = playlistId;
    }



    private static class InitQueuePlaylistTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            List<Playlist> playlists = DataRepository.getPlaylists();
            boolean playlistAlreadyThere = false;

            for (int i = 0; i < playlists.size(); i++) {
                if (playlists.get(i).getName().equals(DataRepository.QUEUE_PLAYLIST_NAME)) {
                    playlistAlreadyThere = true;
                    DataRepository.getPlaylists().remove(i);
                }
            }

            if (!playlistAlreadyThere) {
                if (!userId.equals("")) {
                    Call<PostPlaylist> postPlaylistCall = apiService.insertPlaylist(userId, new PostPlaylist(QUEUE_PLAYLIST_NAME, "is used from simple playlists as queue", false));
                    postPlaylistCall.enqueue(new Callback<PostPlaylist>() {
                        @Override
                        public void onResponse(Call<PostPlaylist> call, Response<PostPlaylist> response) {
                            Log.d("post", String.valueOf(response.code()));
                        }

                        @Override
                        public void onFailure(Call<PostPlaylist> call, Throwable t) {
                            Log.d("post", "funkt nicht");
                        }
                    });
                }
            }

            return null;
        }
    }
}
