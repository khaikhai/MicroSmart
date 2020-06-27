package com.microsmart.tv.ui.adapter;

import android.media.MediaDataSource;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.microsmart.tv.R;

import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoHolder> {

    private OnItemClickListener listener;
    private List<String> videoList;

    public VideoListAdapter(List<String> videoList) {
        videoList.add("https://archive.org/download/Popeye_forPresident/Popeye_forPresident_512kb.mp4");
        videoList.add("https://vod-progressive.akamaized.net/exp=1591045473~acl=%2A%2F1727191862.mp4%2A~hmac=dcc733c78a053d80097e64a190cdd92ece18cb1c2e3a94a2df591b2bb5d9150a/vimeo-prod-skyfire-std-us/01/755/16/403777550/1727191862.mp4");
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_list, parent, false);
        return new VideoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {
        holder.bind(videoList.get(position));
    }


    @Override
    public int getItemCount() {
        return videoList.size();
    }

    class VideoHolder extends RecyclerView.ViewHolder {
        private TextView btnPlay;
        private TextView vPath;

        public VideoHolder(@NonNull View itemView) {
            super(itemView);
            btnPlay = itemView.findViewById(R.id.btnPlay);
            vPath = itemView.findViewById(R.id.vPathName);

            itemView.setOnClickListener(v -> {
                listener.onItemClick(vPath.getText().toString());
            });
        }

        void bind(String path) {
            vPath.setText(path);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String source);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
