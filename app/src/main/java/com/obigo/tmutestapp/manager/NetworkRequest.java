package com.obigo.tmutestapp.manager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016-08-05.
 */
public abstract class NetworkRequest<T> implements Runnable {
    T result;
    NetworkManager.OnResultListener<T> listener;

    public abstract URL getURL() throws MalformedURLException;

    public void setOnResultListener(NetworkManager.OnResultListener<T> listener) {
        this.listener = listener;
    }

    public void setNetworkConfig(HttpURLConnection conn) {
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(10000);
    }

    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";

    protected abstract String getRequestMethod();


    protected void setRequestProperty(HttpURLConnection conn) {

    }

    protected void write(OutputStream out) {
    }

    protected void process(InputStream is) {
        T result = parse(is);
        sendSuccess(result);
    }

    private void sendSuccess(T result) {
        if(isCancel) return;
        this.result = result;
        NetworkManager.getInstance().sendSuccess(this);
    }

    void sendSuccess() {
        if (listener != null && !isCancel) {
            listener.onSuccess(this, result);
        }
    }


    boolean isCancel = false;
    public void setCancel(boolean cancel) {
        if (isCancel != cancel) {
            isCancel = cancel;
        }
    }

    public boolean isCancel() {
        return isCancel;
    }

    abstract protected T parse(InputStream is);

    int retryCount = 3;

    @Override
    public void run() {
        int retry = retryCount;
        while(retry > 0) {
            try {
                if (isCancel) return;
                URL url = getURL();
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                setNetworkConfig(conn);
                String method = getRequestMethod();
                conn.setRequestMethod(method);
                if (method.equals(POST) || method.equals(PUT)) {
                    conn.setDoOutput(true);
                }
                setRequestProperty(conn);
                if (isCancel) return;
                if (conn.getDoOutput()) {
                    write(conn.getOutputStream());
                }
                if (isCancel) return;

                int code = conn.getResponseCode();
                if (isCancel) return;
                if (code >= 200 && code < 300) {
                    InputStream is = conn.getInputStream();
                    process(is);
                    return;
                } else {
                    sendError(code, conn.getResponseMessage());
                    return;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                sendError(-1, e.getMessage());
                return;
            } catch (IOException e) {
                e.printStackTrace();
                retry--;
            }
        }
        sendError(-1, "exception");
    }

    int code;
    String errorMessage;
    protected void sendError(int code, String errorMessage) {
        if (isCancel) return;
        this.code = code;
        this.errorMessage = errorMessage;
        NetworkManager.getInstance().sendFail(this);
    }

    void sendFail() {
        if (listener != null && !isCancel) {
            listener.onFail(this, code, errorMessage);
        }
    }
}
