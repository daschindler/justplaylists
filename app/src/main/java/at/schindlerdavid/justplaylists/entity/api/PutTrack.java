package at.schindlerdavid.justplaylists.entity.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PutTrack {
    @SerializedName("uris")
    @Expose
    private String uris;

    public PutTrack() {
    }


    public PutTrack(String uris) {
        this.uris = uris;
    }

    @Override
    public String toString() {
        return "PutTrack{" +
                "uris='" + uris + '\'' +
                '}';
    }

    public String getUris() {
        return uris;
    }

    public void setUris(String uris) {
        this.uris = uris;
    }
}
