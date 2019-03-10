package at.schindlerdavid.justplaylists.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import at.schindlerdavid.justplaylists.R;
import at.schindlerdavid.justplaylists.callbacks.PlaylistChosenCallback;
import at.schindlerdavid.justplaylists.entity.Playlist;
import at.schindlerdavid.justplaylists.helper.LayoutHelper;

public class PlaylistRecycleAdapter extends RecyclerView.Adapter<PlaylistRecycleAdapter.MyViewHolder> {

    private List<Playlist> playList;
    private PlaylistChosenCallback playlistChosenCallback;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView nameone, nametwo;
        private ImageView playlistCoverOne, playlistCoverTwo;
        private LinearLayout llFirstPlaylist, llSecondPlaylist;


        public MyViewHolder(View view) {
            super(view);
            nameone = view.findViewById(R.id.tv_list_item_one_playlist_name);
            nametwo = view.findViewById(R.id.tv_list_item_two_playlist_name);
            playlistCoverOne = view.findViewById(R.id.iv_list_item_one_playlist_cover);
            playlistCoverTwo = view.findViewById(R.id.iv_list_item_two_playlist_cover);
            llFirstPlaylist = view.findViewById(R.id.ll_first_playlist);
            llSecondPlaylist = view.findViewById(R.id.ll_second_playlist);
            int largeImagePX = LayoutHelper.getLargeImagePXForDevice();
            int smallImagePX = LayoutHelper.getSmallImagePXForDevice();
            int borderPX = LayoutHelper.getLargeborderSize();

            llFirstPlaylist.getLayoutParams().height = largeImagePX+smallImagePX;
            llSecondPlaylist.getLayoutParams().height = largeImagePX+smallImagePX;

            LinearLayout.LayoutParams paramsLeft = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            paramsLeft.setMargins(borderPX, borderPX, borderPX, 0);
            llFirstPlaylist.setLayoutParams(paramsLeft);

            LinearLayout.LayoutParams paramsRight = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            paramsRight.setMargins(0,borderPX, borderPX,0);
            llSecondPlaylist.setLayoutParams(paramsRight);

            playlistCoverOne.getLayoutParams().width = largeImagePX;
            playlistCoverOne.getLayoutParams().height = largeImagePX;
            playlistCoverTwo.getLayoutParams().width = largeImagePX;
            playlistCoverTwo.getLayoutParams().height = largeImagePX;
        }
    }


    public PlaylistRecycleAdapter(List<Playlist> playList, PlaylistChosenCallback playlistChosenCallback) {
        this.playList = playList;
        this.playlistChosenCallback = playlistChosenCallback;
    }

    @Override
    public PlaylistRecycleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_playlist, parent, false);

        return new PlaylistRecycleAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PlaylistRecycleAdapter.MyViewHolder holder, final int position) {
        final int positionOne = position*2;
        int positionTwo = position*2+1;
        if (position == 0) {
            positionTwo = 1;
        }

        if (position < playList.size()) {
            final Playlist playlistOne = playList.get(positionOne);
            holder.nameone.setText(playlistOne.getName());

            if (!playlistOne.getCoverURL640().isEmpty()) {
                Picasso.get().load(playlistOne.getCoverURL640()).into(holder.playlistCoverOne);
            }

            final int finalPositionOne = positionOne;
            holder.playlistCoverOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playlistChosenCallback.onPlaylistChosen(finalPositionOne);
                }
            });
        }

        if (positionTwo < playList.size()) {
            Playlist playlistTwo = playList.get(positionTwo);
            holder.nametwo.setText(playlistTwo.getName());

            if (!playlistTwo.getCoverURL640().isEmpty()) {
                Picasso.get().load(playlistTwo.getCoverURL640()).into(holder.playlistCoverTwo);
            }

            final int finalPositionTwo = positionTwo;
            holder.playlistCoverTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playlistChosenCallback.onPlaylistChosen(finalPositionTwo);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return (playList.size()+1)/2;
    }
}