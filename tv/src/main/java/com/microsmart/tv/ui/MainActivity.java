package com.microsmart.tv.ui;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.http.WebSocket;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.microsmart.tv.R;
import com.microsmart.tv.model.Simple;
import com.microsmart.tv.server.callback.ServerReceiver;
import com.microsmart.tv.ui.PlayerActivity;
import com.microsmart.tv.ui.VideoPlayerActivity;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderLayout;
import com.xuhao.didi.core.iocore.interfaces.ISendable;
import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.core.utils.SLog;
import com.xuhao.didi.socket.client.sdk.OkSocket;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.dispatcher.IRegister;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IClient;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IClientIOCallback;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IClientPool;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IServerManager;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IServerShutdown;
import com.xuhao.didi.socket.server.action.ServerActionAdapter;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private SliderLayout sliderLayout;

    private static final int DEFAULT_PORT = 8081;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sliderLayout = findViewById(R.id.imageSlider);
        sliderLayout.setIndicatorAnimation(IndicatorAnimations.SCALE_DOWN); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderLayout.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
        sliderLayout.setScrollTimeInSec(5); //set scroll delay in seconds :
        setSliderViews();
        wifiIpAddress(this);
        SLog.setIsDebug(false);
        ServerReceiver serverReceiver = new ServerReceiver(DEFAULT_PORT);
        IServerManager serverManager = OkSocket.server(DEFAULT_PORT)
                .registerReceiver(serverReceiver);
        serverReceiver.setServerManager(serverManager);
        serverManager.listen();
        Intent intent = new Intent(this, VideoPlayerActivity.class);
        startActivity(intent);

    }

    protected void wifiIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
        assert wifiManager != null;
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();

        // Convert little-endian to big-endianif needed
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddress = Integer.reverseBytes(ipAddress);
        }

        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

        String ipAddressString;
        try {
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
        } catch (UnknownHostException ex) {
            Log.e("WIFIIP", "Unable to get host address.");
            ipAddressString = null;
        }
        System.out.println(ipAddressString);
    }

    /*public void creServer() {
        AsyncHttpServer server = new AsyncHttpServer();
        Log.d("server", "server created");
        List<WebSocket> _sockets = new ArrayList<WebSocket>();
        server.get("/", (request, response) -> {
//                Toast.makeText(MainActivity.this, "server connect", Toast.LENGTH_SHORT).show();
            response.send("Hello!!!");
        });

// listen on port 5000
        server.listen(8080);
        server.websocket("/live", new AsyncHttpServer.WebSocketRequestCallback() {
            @Override
            public void onConnected(WebSocket webSocket, AsyncHttpServerRequest request) {
                _sockets.add(webSocket);
                webSocket.setClosedCallback(new CompletedCallback() {
                    @Override
                    public void onCompleted(Exception ex) {
                        try {
                            if (ex != null)
                                Log.e("WebSocket", "An error occurred", ex);
                        } finally {
                            _sockets.remove(webSocket);
                        }
                    }
                });

                webSocket.setStringCallback(new WebSocket.StringCallback() {
                    @Override
                    public void onStringAvailable(String s) {
                        if ("Hello Server".equals(s))
                            webSocket.send("Welcome Client!");
                    }
                });
            }
        });
    }*/

    /*public void prePareServer() {
        IRegister register = OkSocket.server(8080);
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
                        Log.d("message", str);
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
    }*/

    private void setSliderViews() {
        sliderLayout.setPagerIndicatorVisibility(false);
        for (int i = 0; i <= 3; i++) {

            DefaultSliderView sliderView = new DefaultSliderView(this);

            switch (i) {
                case 0:
                    sliderView.setImageDrawable(R.drawable.lb_background);
                    break;
                case 1:
                    sliderView.setImageUrl("https://images.pexels.com/photos/218983/pexels-photo-218983.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");

                    break;
                case 2:
                    sliderView.setImageUrl("https://images.pexels.com/photos/747964/pexels-photo-747964.jpeg?auto=compress&cs=tinysrgb&h=750&w=1260");
                    break;
                case 3:
                    sliderView.setImageUrl("https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
                    break;
            }

            sliderView.setImageScaleType(ImageView.ScaleType.FIT_XY);
//            sliderView.setDescription("The quick brown fox jumps over the lazy dog.\n" +
//                    "Jackdaws love my big sphinx of quartz. " + (i + 1));

            //at last add this view in your layout :
            sliderLayout.addSliderView(sliderView);
        }

    }


}
