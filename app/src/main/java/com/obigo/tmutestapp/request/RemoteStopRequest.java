package com.obigo.tmutestapp.request;

import com.obigo.tmutestapp.manager.NetworkRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Kwak on 2016-10-20.
 */

public class RemoteStopRequest extends NetworkRequest<String> {
    private static final String DETAIL_URL = "http://192.168.0.61:3000/remotestop";

    String vehicleid = "";
    public RemoteStopRequest(String vehicleid){
        this.vehicleid = vehicleid;
    }

    public RemoteStopRequest(){

    }

    @Override
    public URL getURL() throws MalformedURLException {
        return new URL(DETAIL_URL);
    }

    @Override
    protected String getRequestMethod() {
        return GET;
    }

    @Override
    protected void setRequestProperty(HttpURLConnection conn) {
        super.setRequestProperty(conn);
        conn.setRequestProperty("vehicleid",vehicleid);
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




