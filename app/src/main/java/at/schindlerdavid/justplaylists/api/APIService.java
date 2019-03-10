package at.schindlerdavid.justplaylists.api;

import com.spotify.protocol.types.LibraryState;

import java.util.List;

import at.schindlerdavid.justplaylists.entity.Playlist;
import at.schindlerdavid.justplaylists.entity.ResponsePlaylist;
import at.schindlerdavid.justplaylists.entity.ResponseTracks;
import at.schindlerdavid.justplaylists.entity.Track;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
    @GET("v1/me/playlists")
    Call<ResponsePlaylist> doGetAllPlaylists(@Query(value = "offset") int offset);

    @GET("/v1/playlists/{playlist_id}/tracks")
    Call<ResponseTracks> doGetTracksFromPlaylist(@Path(value = "playlist_id", encoded = true) String id, @Query(value = "offset") int offset);
}
