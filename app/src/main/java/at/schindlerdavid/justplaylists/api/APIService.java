package at.schindlerdavid.justplaylists.api;

import at.schindlerdavid.justplaylists.entity.PostPlaylist;
import at.schindlerdavid.justplaylists.entity.PostTrack;
import at.schindlerdavid.justplaylists.entity.ResponsePlaylist;
import at.schindlerdavid.justplaylists.entity.ResponseTracks;
import at.schindlerdavid.justplaylists.entity.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
    @GET("/v1/me/playlists")
    Call<ResponsePlaylist> doGetAllPlaylists(@Query(value = "offset") int offset);

    @GET("/v1/playlists/{playlist_id}/tracks")
    Call<ResponseTracks> doGetTracksFromPlaylist(@Path(value = "playlist_id", encoded = true) String id, @Query(value = "offset") int offset);

    @GET("/v1/me")
    Call<User> doGetCurrentUser();

    @PUT
    

    @POST("/v1/playlists/{playlist_id}/tracks")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<PostTrack> insertTrackToPlaylist(
            @Path(value = "playlist_id", encoded = true) String playlistId,
            @Body PostTrack postTrack);

    @POST("/v1/playlists/{playlist_id}/tracks")
    Call<PostTrack> insertTrackToPlaylist(
            @Path(value = "playlist_id", encoded = true) String playlistId,
            @Query(value = "uris", encoded = true) String uris);

    @POST("/v1/users/{user_id}/playlists")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<PostPlaylist> insertPlaylist(
            @Path(value = "user_id", encoded = true) String userId,
            @Body PostPlaylist postPlaylist);

}
