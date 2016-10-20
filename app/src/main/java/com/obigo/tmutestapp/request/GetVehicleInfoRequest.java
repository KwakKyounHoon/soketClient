package com.obigo.tmutestapp.request;

import com.obigo.tmutestapp.manager.NetworkRequest;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Kwak on 2016-10-20.
 */

public class GetVehicleInfoRequest extends NetworkRequest<String> {
    private static final String TSTORE_DETAIL_URL = "http://218.147.65.20:3000/vehicle?1";
    @Override
    public URL getURL() throws MalformedURLException {
        return null;
    }

    @Override
    protected String getRequestMethod() {
        return null;
    }

    @Override
    protected String parse(InputStream is) {
        return null;
    }


}
