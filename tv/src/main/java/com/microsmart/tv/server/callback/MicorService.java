package com.microsmart.tv.server.callback;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microsmart.tv.model.SendCommand;
import com.microsmart.tv.ui.VideoPlayerActivity;
import com.xuhao.didi.core.iocore.interfaces.ISendable;
import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IClient;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IClientIOCallback;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IClientPool;

import org.greenrobot.eventbus.EventBus;

import java.nio.charset.StandardCharsets;


public class MicorService extends IntentService implements IClientIOCallback {

    private final String TAG = "MicroService";
    private int cmd;


    public MicorService() {
        super(MicorService.class.getSimpleName());
    }


    @Override
    public void onClientRead(OriginalData originalData, IClient client, IClientPool<IClient, String> clientPool) {
        String str = new String(originalData.getBodyBytes(), StandardCharsets.UTF_8);
        Log.d("On read", "onClientRead: " + str);
        JsonObject jsonObject = new JsonParser().parse(str).getAsJsonObject();
        cmd = jsonObject.get("cmd").getAsInt();
        String path = jsonObject.get("data").getAsString();
        Log.d("Controller:", "Command" + cmd + "Onrecive path" + path);
        EventBus.getDefault().post(new SendCommand(cmd, path));
    }

    @Override
    public void onClientWrite(ISendable sendable, IClient client, IClientPool<IClient, String> clientPool) {
        Log.d(TAG, "Send Data To Client");

    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(VideoPlayerActivity.class);
    }
}
