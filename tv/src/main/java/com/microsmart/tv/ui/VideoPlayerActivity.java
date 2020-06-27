package com.microsmart.tv.ui;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.microsmart.tv.R;
import com.microsmart.tv.model.SendCommand;
import com.microsmart.tv.server.callback.MicorService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class VideoPlayerActivity extends AppCompatActivity {
    private PlayerView playerView;
    private SimpleExoPlayer exoPlayer;
    private List<MediaSource> mediaSources = new ArrayList<>();
    private DefaultTrackSelector trackSelector;
    private ConcatenatingMediaSource source;
    private AudioManager audioManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ativity_player);
        playerView = findViewById(R.id.player_view);
        audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        Intent myServiceIntent = new Intent(this, MicorService.class);
        startService(myServiceIntent);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommand(SendCommand cmd) {
        switch (cmd.getCmd()) {
            case 11:
                if (exoPlayer.isPlaying()) {
                    exoPlayer.setPlayWhenReady(false);
                } else {
                    exoPlayer.setPlayWhenReady(true);
                }
                break;
            case 12:
                if (exoPlayer.hasNext()) {
                    exoPlayer.next();
                    exoPlayer.setPlayWhenReady(true);
                }
                break;
            case 13:
                if (exoPlayer.hasPrevious()) {
                    exoPlayer.retry();
                    exoPlayer.previous();
                    exoPlayer.setPlayWhenReady(true);
                }
                break;
            case 14:
                System.out.println(Arrays.toString(exoPlayer.getCurrentTrackSelections().getAll()));
                trackSelector = new DefaultTrackSelector(this);

                trackSelector.setParameters(trackSelector.buildUponParameters().setPreferredAudioLanguage("und"));
                break;
            case 15:
                String url = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/realM" + File.separator + cmd.getData();
                Log.d("Player", "onCommand: " + url);

                if (exoPlayer.isPlaying() || exoPlayer.isLoading()) {
                    source.addMediaSources(addPlayList(Uri.parse(url)));
                } else {
                    exoPlayer.prepare(createMediaSource(Uri.parse(url)));
                }
                exoPlayer.setPlayWhenReady(true);
                Log.d("Player", "Playlist: " + source.getSize());
                break;
            case 16:
                audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.STREAM_MUSIC);
                break;
            case 17:
                audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.STREAM_MUSIC);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        trackSelector = new DefaultTrackSelector(this);
        exoPlayer = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(exoPlayer);
        trackSelector.setParameters(trackSelector.buildUponParameters().setPreferredAudioLanguage("eng"));
        exoPlayer.setPlayWhenReady(true);

    }

    public void updateTrack() {
        trackSelector = new DefaultTrackSelector(this);
//        trackSelector.setParameters(TrackSelectionUtil.updateParametersWithOverride(trackSelector.getParameters(), 0, exoPlayer.getCurrentTrackGroups()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private MediaSource createMediaSource(Uri uri) {
        source = new ConcatenatingMediaSource();
        source.addMediaSources(addPlayList(uri));
        return source;
    }

    public Collection<MediaSource> addPlayList(Uri uri) {
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(this, "exoplayer-codelab");
        ProgressiveMediaSource.Factory mediaSourceFactory =
                new ProgressiveMediaSource.Factory(dataSourceFactory);
        if (uri != null) {
            MediaSource mediaSource = mediaSourceFactory.createMediaSource(uri);
            mediaSources.clear();
            mediaSources.add(mediaSource);
        }

        return mediaSources;
    }
}
