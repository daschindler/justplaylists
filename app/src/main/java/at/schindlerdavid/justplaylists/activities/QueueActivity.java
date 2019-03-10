package at.schindlerdavid.justplaylists.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import at.schindlerdavid.justplaylists.R;
import at.schindlerdavid.justplaylists.adapter.PlaylistDetailRecycleAdapter;
import at.schindlerdavid.justplaylists.adapter.PlaylistRecycleAdapter;
import at.schindlerdavid.justplaylists.adapter.QueueRecycleAdapter;
import at.schindlerdavid.justplaylists.data.DataRepository;

public class QueueActivity extends AppCompatActivity {

    private RecyclerView rvQueue;
    private QueueRecycleAdapter queueRecycleAdapter;
    private FloatingActionButton fabClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);
        rvQueue = findViewById(R.id.rv_queue);
        fabClose = findViewById(R.id.fab_queue_close);

        RecyclerView.LayoutManager mLayoutManagerTrack = new LinearLayoutManager(getApplicationContext());
        rvQueue.setLayoutManager(mLayoutManagerTrack);
        rvQueue.setItemAnimator(new DefaultItemAnimator());

        queueRecycleAdapter = new QueueRecycleAdapter(DataRepository.getCurrentQueue());
        rvQueue.setAdapter(queueRecycleAdapter);

        fabClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabClose.hide();
                finish();
            }
        });

    }
}
