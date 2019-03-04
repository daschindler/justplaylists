package at.schindlerdavid.justplaylists.api;

import java.util.List;

import at.schindlerdavid.justplaylists.entity.Playlist;
import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService {
    @GET("v1/me/playlists")
    Call<String> doGetAllPlaylists();
}
