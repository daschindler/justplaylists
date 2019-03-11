package at.schindlerdavid.justplaylists.adapter;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import at.schindlerdavid.justplaylists.R;
import at.schindlerdavid.justplaylists.entity.Playlist;
import at.schindlerdavid.justplaylists.entity.Track;
import at.schindlerdavid.justplaylists.helper.LayoutHelper;
import at.schindlerdavid.justplaylists.helper.RemoteHelper;

public class PlaylistDetailRecycleAdapter extends RecyclerView.Adapter<PlaylistDetailRecycleAdapter.MyViewHolder>{
    private List<Track> trackList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvArtists;
        private ImageView ivCover, ivAddToQueue;
        private LinearLayout llItemDetail;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_track_name);
            tvArtists = itemView.findViewById(R.id.tv_track_artists);
            ivCover = itemView.findViewById(R.id.iv_track);
            llItemDetail = itemView.findViewById(R.id.ll_item_detail);
            ivAddToQueue = itemView.findViewById(R.id.iv_add_to_queue);

            int firstBorderPX = LayoutHelper.getFirstBorderDetail();
            int marginAfterIV = LayoutHelper.getBorderAfterSmallPic();
            int textWidth = LayoutHelper.getTextViewDetailSize();


            LinearLayout.LayoutParams paramsLeft = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            paramsLeft.setMargins(firstBorderPX, 0, 0, 0);
            llItemDetail.setLayoutParams(paramsLeft);

            LinearLayout.LayoutParams paramsAfterPic = new LinearLayout.LayoutParams(textWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
            paramsAfterPic.setMargins(marginAfterIV, 0, 0, 0);
            tvArtists.setLayoutParams(paramsAfterPic);
            tvName.setLayoutParams(paramsAfterPic);

            ivAddToQueue.getLayoutParams().height = firstBorderPX;
            ivAddToQueue.getLayoutParams().width = firstBorderPX;

            LinearLayout.LayoutParams paramsRight = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            paramsRight.setMargins(0, 0, firstBorderPX, 0);
            ivAddToQueue.setLayoutParams(paramsRight);

            ivCover.getLayoutParams().height = LayoutHelper.getSmallImagePXForDevice();
            ivCover.getLayoutParams().width = LayoutHelper.getSmallImagePXForDevice();
        }
    }


    public PlaylistDetailRecycleAdapter(List<Track> trackList) {
        this.trackList = trackList;
    }

    @Override
    public PlaylistDetailRecycleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_playlist_detail, parent, false);

        return new PlaylistDetailRecycleAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PlaylistDetailRecycleAdapter.MyViewHolder holder, final int position) {
        if (position < trackList.size()) {
            Track track = trackList.get(position);
            holder.tvName.setText(track.getName().split("\\(")[0]);
            holder.tvArtists.setText(track.artistsToString());

            String coverUrl = track.getAlbum().getCoverURL300();

            if (!coverUrl.equals("")) {
                Picasso.get().load(track.getAlbum().getCoverURL300()).into(holder.ivCover);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //RemoteHelper.playTrackOnSpotify(track);
                    RemoteHelper.playTrackOnSpotify(track, trackList, position);
                    Toast.makeText(holder.itemView.getContext(), "Track is playing!", Toast.LENGTH_LONG).show();
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    RemoteHelper.queueTrackOnSpotify(track);
                    Toast.makeText(holder.itemView.getContext(), "Track added to queue!", Toast.LENGTH_LONG).show();
                    return true;
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return this.trackList.size();
    }
}
