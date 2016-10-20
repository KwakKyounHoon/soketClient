package com.obigo.tmutestapp.request;

import com.obigo.tmutestapp.manager.NetworkRequest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016-08-08.
 */
public class RemoteStartRequest extends NetworkRequest<String> {
    private static final String TSTORE_DETAIL_URL = "http://192.168.0.61:3000/remotestart";
    String url;
    String body;
    public RemoteStartRequest(String body) {
        this.body = body;
    }

    @Override
    public URL getURL() throws MalformedURLException {
        return new URL(TSTORE_DETAIL_URL);
    }

    @Override
    protected void setRequestProperty(HttpURLConnection conn) {
        super.setRequestProperty(conn);
//        conn.setRequestProperty("Accept", "application/json");
//        conn.setRequestProperty("appKey","2bc7afe3-fc89-3125-b699-b9fb7cfe2fae");
        try {
            conn.setDoInput(true);

            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            OutputStream os = conn.getOutputStream();

            os.write( body.getBytes("euc-kr") );

            os.flush();
            os.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String parse(InputStream is) {
        return null;
    }


}
