package com.microsmart.tv.ui;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.MediaController;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microsmart.tv.R;
import com.xuhao.didi.core.iocore.interfaces.ISendable;
import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.socket.client.sdk.OkSocket;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.dispatcher.IRegister;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IClient;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IClientIOCallback;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IClientPool;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IServerManager;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IServerShutdown;
import com.xuhao.didi.socket.server.action.ServerActionAdapter;


import java.io.IOException;
import java.nio.charset.Charset;

public class PlayerActivity extends AppCompatActivity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl {

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private MediaPlayer mediaPlayer;
    private static final int DEFAULT_PORT = 8081;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ativity_player);
        surfaceView = findViewById(R.id.video_surface);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
//        prePareServer();
    }

/*    @Override
    public void onBackPressed() {

        LinearLayout layout = findViewById(R.id.parent);

        if (state) {
            recyclerView.setVisibility(View.INVISIBLE);
            layout.setVisibility(View.GONE);
            state = false;
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            layout.setVisibility(View.VISIBLE);
            state = true;
        }


    }*/

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDisplay(surfaceHolder);


    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
    }


    @Override
    public void start() {
        mediaPlayer.start();
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }

    @Override
    public void seekTo(int pos) {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    public void prePareServer() {
        IRegister register = OkSocket.server(DEFAULT_PORT);
        IServerManager serverManager = (IServerManager) register.registerReceiver(new ServerActionAdapter() {
            @Override
            public void onServerListening(int serverPort) {
                super.onServerListening(serverPort);
            }

            @Override
            public void onServerWillBeShutdown(int serverPort, IServerShutdown shutdown, IClientPool clientPool, Throwable throwable) {
                //This method will be called back when the server has to be closed due to exception information.
                //You should complete the server save and close work in this method.
                //You should call the server's `shutdown()` method after finishing the finishing work.
                shutdown.shutdown();
            }

            @Override
            public void onServerAlreadyShutdown(int serverPort) {
                //This method will be called back when you call the shutdown method.
            }

            @Override
            public void onClientConnected(IClient client, int serverPort, IClientPool clientPool) {
                super.onClientConnected(client, serverPort, clientPool);

                client.addIOCallback(new IClientIOCallback() {
                    @Override
                    public void onClientRead(OriginalData originalData, IClient client, IClientPool<IClient, String> clientPool) {
                        String str = new String(originalData.getBodyBytes(), Charset.forName("utf-8"));
                        Log.d("On read", "onClientRead: " + str);
                        try {
                            JsonObject jsonObject = new JsonParser().parse(str).getAsJsonObject();
                            int cmd = jsonObject.get("cmd").getAsInt();
                            Log.d("Controller:", "Command" + cmd);
                            switch (cmd) {
                                case 11:
                                    mediaPlayer.setDataSource("https://archive.org/download/Popeye_forPresident/Popeye_forPresident_512kb.mp4");
                                    mediaPlayer.prepare();
                                    mediaPlayer.setOnPreparedListener(PlayerActivity.this);
                                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                    break;
                                case 12:
                                    if (mediaPlayer.isPlaying()) {
                                        mediaPlayer.pause();
                                    } else mediaPlayer.start();
                                    break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onClientWrite(ISendable sendable, IClient client, IClientPool<IClient, String> clientPool) {

                    }
                });
            }
        });

        if (!serverManager.isLive()) {
            //Start listen the specific port
            serverManager.listen();
        }
    }

}
