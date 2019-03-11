package at.schindlerdavid.justplaylists.entity.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class PostTrack {
    @SerializedName("uri")
    @Expose
    private String[] uris;

    public PostTrack(String[] uris) {
        this.uris = uris;
    }

    public PostTrack() {
    }

    public String[] getUris() {
        return uris;
    }

    public void setUris(String[] uris) {
        this.uris = uris;
    }

    @Override
    public String toString() {
        return "PostTrack{" +
                "uris=" + Arrays.toString(uris) +
                '}';
    }
}
