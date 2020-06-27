package com.microsmart.tv.server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.microsmart.tv.model.Simple;
import com.xuhao.didi.core.iocore.interfaces.ISendable;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.List;

public class TestSendData implements ISendable {

    private String str = "";

    public TestSendData(List<Simple> simples) {
        /*JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sim", 11);
            jsonObject.put("data", "{x:2,y:1}");
            str = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Simple>>() {
        }.getType();
        str = gson.toJson(simples, listType);
    }


    @Override
    public byte[] parse() {
        //Build the byte array according to the server's parsing rules
        byte[] payload = str.getBytes(Charset.defaultCharset());
        //4 is package header fixed length and payload length
        ByteBuffer bb = ByteBuffer.allocate(4 + payload.length);
        bb.order(ByteOrder.BIG_ENDIAN);
        bb.putInt(payload.length);
        bb.put(payload);
        return bb.array();
    }
}
