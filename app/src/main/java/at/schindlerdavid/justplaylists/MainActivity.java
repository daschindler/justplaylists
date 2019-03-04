package at.schindlerdavid.justplaylists;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import at.schindlerdavid.justplaylists.data.DataRepository;

import static com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE;

public class MainActivity extends AppCompatActivity {

    private DataRepository dataRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        beginAuthentication();
        dataRepository = new DataRepository(this.getApplication());

    }

    private void beginAuthentication() {
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(DataRepository.getClientId(), AuthenticationResponse.Type.TOKEN, DataRepository.getRedirectUri());

        builder.setScopes(new String[]{"app-remote-control", "playlist-read-private", "playlist-read-collaborative"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(MainActivity.this, REQUEST_CODE, request);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                case TOKEN:
                    Toast.makeText(this, "authenticated", Toast.LENGTH_LONG).show();
                    DataRepository.setResponseToken(response.getAccessToken());
                    /*StatifyWebRepo.setAccessToken(response.getAccessToken());
                    requestTracks(50, TimeRange.short_term);
                    StatifyRepo.setTrackTimeRange(TimeRange.short_term);
                    requestUserData();
                    requestArtists(50, TimeRange.short_term);
                    StatifyRepo.setArtistTimeRang(TimeRange.short_term);*/
                    dataRepository.getPlaylists();
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

}
