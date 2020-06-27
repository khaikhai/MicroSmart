package com.microsmart.tv.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class WebServer extends NanoHTTPD {
    private String filename;

    public WebServer(String filename) throws IOException {
        super(8080);
        this.filename = filename;
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    }

   /* @Override
    public Response serve(HTTPSession session) {

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return newChunkedResponse(Response.Status.OK, "application/octet-stream", fis);

    }*/

    @Override
    public Response serve(IHTTPSession session) {

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return newChunkedResponse(Response.Status.OK, "application/octet-stream", fis);
    }
}
