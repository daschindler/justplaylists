package at.schindlerdavid.justplaylists.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import at.schindlerdavid.justplaylists.api.APIClient;
import at.schindlerdavid.justplaylists.api.APIService;
import at.schindlerdavid.justplaylists.entity.Playlist;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataRepository {
    private static final String CLIENT_ID = "9d3d1efaa02a4b86ac4cff6e3bf09f41";
    private static final String REDIRECT_URI = "at.schindlerdavid.justplaylists://callback";
    private static String responseToken = "";

    private static List<Playlist> playlists = null;

    private APIService apiService;

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

    public List<Playlist> getPlaylists() {
        if (playlists == null) {
            playlists = new ArrayList<>();

            Call<String> call = apiService.doGetAllPlaylists();
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.d("code", String.valueOf(response.code()));
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d("failed", "failed");
                }
            });
        }

        return playlists;
    }
}
