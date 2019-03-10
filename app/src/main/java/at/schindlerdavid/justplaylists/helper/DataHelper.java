package at.schindlerdavid.justplaylists.helper;

import java.util.List;

import at.schindlerdavid.justplaylists.data.DataRepository;
import at.schindlerdavid.justplaylists.entity.Playlist;

public class DataHelper {
    public static void RemoveOwnPlaylistsFromView() {
        List<Playlist> playlists = DataRepository.getPlaylists();

        for (int i = 0; i < playlists.size(); i++) {
            if (playlists.get(i).getName().equals(DataRepository.QUEUE_PLAYLIST_NAME)) {
                DataRepository.getPlaylists().remove(i);
            }
        }
    }
}
