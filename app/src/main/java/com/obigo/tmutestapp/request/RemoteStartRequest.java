package com.obigo.tmutestapp.request;

import com.obigo.tmutestapp.manager.NetworkRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016-08-08.
 */
public class RemoteStartRequest extends NetworkRequest<String> {
    private static final String DETAIL_URL = "http://192.168.0.61:3000/remotestart";
    String body;

    String vehicleid = "";


    public RemoteStartRequest(){

    }

    public RemoteStartRequest(String body, String vehicleid) {
        this.body = body;
        this.vehicleid = vehicleid;
    }

    @Override
    public URL getURL() throws MalformedURLException {
        return new URL(DETAIL_URL);
    }

    @Override
    protected String getRequestMethod() {
        return POST;
    }

    @Override
    protected void setRequestProperty(HttpURLConnection conn) {
        super.setRequestProperty(conn);
//        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("vehicleid",vehicleid);
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
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
