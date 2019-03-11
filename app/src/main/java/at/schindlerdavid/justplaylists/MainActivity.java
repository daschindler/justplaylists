package at.schindlerdavid.justplaylists;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.spotify.protocol.types.Track;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import at.schindlerdavid.justplaylists.activities.QueueActivity;
import at.schindlerdavid.justplaylists.adapter.PlaylistDetailRecycleAdapter;
import at.schindlerdavid.justplaylists.adapter.PlaylistRecycleAdapter;
import at.schindlerdavid.justplaylists.callbacks.PlaylistChosenCallback;
import at.schindlerdavid.justplaylists.callbacks.PlaylistsLoadedCallback;
import at.schindlerdavid.justplaylists.callbacks.SpotifyCallback;
import at.schindlerdavid.justplaylists.callbacks.TracksLoadedCallback;
import at.schindlerdavid.justplaylists.data.DataRepository;
import at.schindlerdavid.justplaylists.entity.Playlist;
import at.schindlerdavid.justplaylists.helper.DataHelper;
import at.schindlerdavid.justplaylists.helper.RemoteHelper;

import static com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE;

public class MainActivity extends AppCompatActivity {

    private DataRepository dataRepository;
    private PlaylistRecycleAdapter playlistAdapter;
    private PlaylistDetailRecycleAdapter playlistDetailRecycleAdapter;
    private RecyclerView playlistRecyclerView;
    private PlaylistChosenCallback playlistChosenCallback;
    private ImageView ivBack;
    private TextView tvTitlebar, tvCurrentlyPlayingName, tvCurrentlyPlayingArtist;
    private ProgressBar progressBar;
    private ImageView btPlayAll, btPlayPrev, btPlayNext, btQueue;
    private LinearLayout titleBar, bottomBarPlaying;
    private Playlist currentOpenedPlaylist;

    private boolean isPaused = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataRepository = new DataRepository(this.getApplication());
        beginAuthentication();
        SetupViews();
        SetSpotifyCallback();
        dataRepository.initSpotifyRemote(this);

        playlistChosenCallback = new PlaylistChosenCallback() {
            @Override
            public void onPlaylistChosen(final int position) {
                SetLoadingScreen();

                dataRepository.requestBelongingTracks(DataRepository.getPlaylists().get(position).getId(), new TracksLoadedCallback() {
                    @Override
                    public void onTracksLoaded() {
                        ShowPlaylistDetail(DataRepository.getPlaylists().get(position));
                    }
                });
            }
        };
        playlistAdapter = new PlaylistRecycleAdapter(DataRepository.getPlaylists(), playlistChosenCallback);
        playlistDetailRecycleAdapter = new PlaylistDetailRecycleAdapter(DataRepository.getCurrentShowingTracks());
        playlistRecyclerView.setAdapter(playlistAdapter);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetFocusOnAllPlaylists();
            }
        });

        btPlayAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentOpenedPlaylist != null) {
                    RemoteHelper.playPlaylistOnSpotify(currentOpenedPlaylist);
                }
            }
        });

        btPlayNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoteHelper.playNextTrack();
            }
        });

        btPlayPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoteHelper.playPrevTrack();
            }
        });

        bottomBarPlaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPaused) {
                    RemoteHelper.resumePlay();
                } else {
                    RemoteHelper.pausePlay();
                }
            }
        });

        btQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), QueueActivity.class);
                startActivity(intent);
            }
        });

    }

    private void SetSpotifyCallback() {
        SpotifyCallback spotifyCallback = new SpotifyCallback() {
            @Override
            public void onTrackChanged(Track track) {
                tvCurrentlyPlayingName.setText(track.name);
                tvCurrentlyPlayingArtist.setText(track.artist.name);

                if (DataRepository.getCurrentQueue().size() > 0) {
                    if (DataRepository.getCurrentQueue().get(0).getName().equals(track.name)) {
                        DataRepository.getCurrentQueue().remove(0);
                    }
                }
            }

            @Override
            public void onPaused() {
                isPaused = true;
            }

            @Override
            public void onPlaying() {
                isPaused = false;
            }
        };

        dataRepository.setSpotifyCallback(spotifyCallback);
    }

    private void ShowPlaylistDetail(Playlist playlist) {
        currentOpenedPlaylist = playlist;
        titleBar.setVisibility(View.VISIBLE);
        playlistRecyclerView.setAdapter(playlistDetailRecycleAdapter);
        SetAdapterAnimation();
        playlistDetailRecycleAdapter.notifyDataSetChanged();
        ivBack.setVisibility(View.VISIBLE);
        tvTitlebar.setText(playlist.getName());
        progressBar.setVisibility(View.GONE);
        playlistRecyclerView.setVisibility(View.VISIBLE);
        btPlayAll.setVisibility(View.VISIBLE);

    }

    private void SetAdapterAnimation() {
        playlistRecyclerView.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        playlistRecyclerView.getViewTreeObserver().removeOnPreDrawListener(this);

                        for (int i = 0; i < playlistRecyclerView.getChildCount(); i++) {
                            View v = playlistRecyclerView.getChildAt(i);
                            v.setAlpha(0.0f);
                            v.animate().alpha(1.0f)
                                    .setDuration(300)
                                    .setStartDelay(i * 50)
                                    .start();
                        }

                        return true;
                    }
                });
    }

    private void SetLoadingScreen() {
        playlistRecyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

    }

    private void SetupViews(){
        ivBack = findViewById(R.id.iv_back);
        tvTitlebar = findViewById(R.id.titlebar_text);
        progressBar = findViewById(R.id.progressbar);
        playlistRecyclerView = findViewById(R.id.rv_all_playlists);
        RecyclerView.LayoutManager mLayoutManagerTrack = new LinearLayoutManager(getApplicationContext());
        playlistRecyclerView.setLayoutManager(mLayoutManagerTrack);
        playlistRecyclerView.setItemAnimator(new DefaultItemAnimator());
        btPlayAll = findViewById(R.id.bt_play_all);
        titleBar = findViewById(R.id.main_titlebar);
        btPlayNext = findViewById(R.id.iv_next_track);
        btPlayPrev = findViewById(R.id.iv_last_track);
        tvCurrentlyPlayingName = findViewById(R.id.bottombar_playing_name);
        tvCurrentlyPlayingArtist = findViewById(R.id.bottombar_playing_artist);
        btQueue = findViewById(R.id.iv_bottombar_queue);
        bottomBarPlaying = findViewById(R.id.bottombar_playing);
    }

    private void SetFocusOnAllPlaylists() {
        titleBar.setVisibility(View.GONE);
        playlistRecyclerView.setAdapter(playlistAdapter);
        SetAdapterAnimation();
        ivBack.setVisibility(View.GONE);
        btPlayAll.setVisibility(View.GONE);
        tvTitlebar.setText(R.string.app_name);
    }

    private void beginAuthentication() {
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(DataRepository.getClientId(), AuthenticationResponse.Type.TOKEN, DataRepository.getRedirectUri());

        builder.setScopes(new String[]{"app-remote-control", "playlist-read-private", "playlist-read-collaborative", "playlist-modify-private", "playlist-modify-public"
        , "app-remote-control", "playlist-read-collaborative",});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(MainActivity.this, REQUEST_CODE, request);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                case TOKEN:
                    DataRepository.setResponseToken(response.getAccessToken());
                    dataRepository.loadUser();
                    dataRepository.requestPlaylists(new PlaylistsLoadedCallback() {
                        @Override
                        public void onRequestFinished() {
                            Log.d("data", "loaded");
                            playlistAdapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                            playlistRecyclerView.setVisibility(View.VISIBLE);
                        }
                    });
                    break;

                case ERROR:
                    SetupAnotherTry();
                    break;

                default:
                    SetupAnotherTry();
            }
        }
    }

    private void SetupAnotherTry() {
        /*recyclerView.setVisibility(View.GONE);
        pbRecyclerViewLoading.setVisibility(View.GONE);
        btTryReconnect.setVisibility(View.VISIBLE);
        tvConnectionError.setVisibility(View.VISIBLE);*/
    }

    @Override
    public void onBackPressed() {
        if (ivBack.getVisibility() == View.GONE) {
            DataRepository.getPlaylists().clear();
            DataRepository.getCurrentShowingTracks().clear();
            super.onBackPressed();
        } else {
            SetFocusOnAllPlaylists();
        }
    }
}
