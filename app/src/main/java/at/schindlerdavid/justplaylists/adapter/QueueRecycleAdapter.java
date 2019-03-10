package at.schindlerdavid.justplaylists.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import at.schindlerdavid.justplaylists.R;
import at.schindlerdavid.justplaylists.entity.Track;
import at.schindlerdavid.justplaylists.helper.LayoutHelper;

public class QueueRecycleAdapter extends RecyclerView.Adapter<QueueRecycleAdapter.MyViewHolder> {

    private List<Track> queue;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvRank, tvName, tvArtist;

        public MyViewHolder(View view) {
            super(view);
            tvRank = view.findViewById(R.id.tv_list_item_queue_rank);
            tvName = view.findViewById(R.id.tv_list_item_queue_name);
            tvArtist = view.findViewById(R.id.tv_list_item_queue_artist);

        }
    }

    public QueueRecycleAdapter(List<Track> trackList) {
        this.queue = trackList;
    }

    @NonNull
    @Override
    public QueueRecycleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_queue, parent, false);

        return new QueueRecycleAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull QueueRecycleAdapter.MyViewHolder myViewHolder, int i) {
        int rank = i+1;

        Track track = queue.get(i);

        myViewHolder.tvRank.setText(String.valueOf(rank));
        myViewHolder.tvName.setText(track.getName());
        myViewHolder.tvArtist.setText(track.artistsToString());

    }

    @Override
    public int getItemCount() {
        return queue.size();
    }
}
