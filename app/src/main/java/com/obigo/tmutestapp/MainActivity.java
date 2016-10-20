package com.obigo.tmutestapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.obigo.tmutestapp.fragment.RemoteSettingFragment;
import com.obigo.tmutestapp.manager.NetworkManager;
import com.obigo.tmutestapp.manager.NetworkRequest;
import com.obigo.tmutestapp.request.RemoteStartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity implements RemoteSettingFragment.OnRemoteDailogListener {
    private EditText iPView, portView;
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    private Handler mHandler = new Handler();

    private String GET_VEHICLE_STATUS = "0";
    private String GET_VEHICLE_INFO = "1";
    private String GET_DTCS = "2";
    private String GET_VEHICLE_LOCATION = "3";
    private String REMOTE_START = "4";
    private String REMOTE_STOP = "5";
    private String DOOR_LOCK = "6";
    private String DOOR_UNLOCK = "7";
    private String HORN = "8";
    private String LIGHT = "9";
    private String SET_CLIMATE = "10";
    private String RESET_MAINTENANCE = "11";

    private TextView recieveView;

    private com.github.nkzawa.socketio.client.Socket mSocket;

    private Emitter.Listener listen_start_person = new Emitter.Listener() {

        public void call(Object... args) {
            Log.i("test receive",args[0]+"");
            //서버에서 보낸 JSON객체를 사용할 수 있습니다.

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            mSocket = IO.socket("http://192.168.0.61:3000");
        }
        catch (URISyntaxException e) {
            Log.v("AvisActivity", "error connecting to socket");
        }

        Log.v("AvisActivity", "try to connect");
        mSocket.connect();
        Log.v("AvisActivity", "connection sucessful");

        JSONObject obj = new JSONObject();
        try {
            obj.put("image", "test");
            mSocket.emit("chat message", obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSocket.on("chat message", listen_start_person);

        Utils.makeCelsiusTable();
        iPView = (EditText)findViewById(R.id.edit_ip);
        portView = (EditText)findViewById(R.id.edit_port);
        recieveView = (TextView)findViewById(R.id.text_recieve);
        Button btn = (Button)findViewById(R.id.btn_connect);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new Connect()).start();
            }
        });

        btn = (Button)findViewById(R.id.btn_disconnect);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new Disconnect()).start();
            }
        });

        btn = (Button)findViewById(R.id.btn_start);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoteSettingFragment f = new RemoteSettingFragment();
                f.setOnRemoteDailogListener(MainActivity.this);
                f.show(getSupportFragmentManager(), "remotestart");
            }
        });

        btn = (Button)findViewById(R.id.btn_get_vehicle_status);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new sendMessage("0",GET_VEHICLE_STATUS)).start();
            }
        });

        btn = (Button)findViewById(R.id.btn_get_vehicle_info);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new sendMessage("0",GET_VEHICLE_INFO)).start();
            }
        });

        btn = (Button)findViewById(R.id.btn_get_dtcs);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new sendMessage("0",GET_DTCS)).start();
            }
        });

        btn = (Button)findViewById(R.id.btn_Remote_stop);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new sendMessage("0",REMOTE_STOP)).start();
            }
        });

        btn = (Button)findViewById(R.id.btn_door_lock);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new sendMessage("0",DOOR_LOCK)).start();
            }
        });

        btn = (Button)findViewById(R.id.btn_door_unlock);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new sendMessage("0",DOOR_UNLOCK)).start();
            }
        });

        btn = (Button)findViewById(R.id.btn_hron);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new sendMessage("0",HORN)).start();
            }
        });

        btn = (Button)findViewById(R.id.btn_Light);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new sendMessage("0",LIGHT)).start();
            }
        });

        btn = (Button)findViewById(R.id.btn_reset_maintenance);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new sendMessage("0",RESET_MAINTENANCE)).start();
            }
        });


    }

    @Override
    public void onOkBtnClick(JSONObject settings) {
//        (new sendMessage(settings.toString(),REMOTE_START)).start();
//        NetworkManager.getInstance()
        Log.i("test send",settings.toString());
        RemoteStartRequest remoteStartRequest = new RemoteStartRequest("requestData="+settings.toString());
        NetworkManager.getInstance().getNetworkData(remoteStartRequest, new NetworkManager.OnResultListener<String>() {
            @Override
            public void onSuccess(NetworkRequest<String> request, String result) {
                Toast.makeText(MainActivity.this,"success",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(NetworkRequest<String> request, int errorCode, String errorMessage) {

            }
        });
    }

    class Connect extends Thread {
        public void run() {
            Log.d("Connect", "Run Connect");
            String ip = null;
            int port = 0;

            try {

            } catch (Exception e) {
                final String recvInput = "정확히 입력하세요!";
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        setToast(recvInput);
                    }
                });
            }
            try {
                ip = iPView.getText().toString();
                port = Integer.parseInt(portView.getText().toString());
                socket = new Socket(ip, port);
                dos = new DataOutputStream(socket.getOutputStream());
//                yourOutputStream = new DataOutputStream(socket.getOutputStream()); // OutputStream where to send the map in case of network you get it from the Socket instance.
                dis = new DataInputStream(socket.getInputStream());

                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        setToast("연결에 성공하였습니다.");
                    }

                });
                (new recvSocket()).start();
            } catch (Exception e) {
                final String recvInput = "연결에 실패하였습니다.";
                Log.d("Connect", e.getMessage());
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        setToast(recvInput);
                    }

                });
            }
        }
    }

    class recvSocket extends Thread {
        public void run() {
            try {
                dis = new DataInputStream(socket.getInputStream());

                while (true) {
                    byte[] b = new byte[1024];
                    int ac = dis.read(b, 0, b.length);
                    String input = new String(b, 0, b.length);
                    final String recvInput = input.trim();

                    if(ac==-1)
                        break;

                    mHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            recieveView.setText(recvInput);
                        }
                    });
                }
                mHandler.post(new Runnable(){

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        setToast("연결이 종료되었습니다.");
                    }

                });
            } catch (Exception e) {
                final String recvInput = "연결에 문제가 발생하여 종료되었습니다..";
                Log.d("SetServer", e.getMessage());
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        setToast(recvInput);
                    }

                });

            }

        }
    }

    class Disconnect extends Thread {
        public void run() {
            try {
                if (socket != null) {
                    dos.close();
                    dis.close();
                    socket.close();
                    socket = null;
                    mHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            setToast("연결이 종료되었습니다.");
                        }
                    });

                }

            } catch (Exception e) {
                final String recvInput = "연결에 실패하였습니다.";
                Log.d("Connect", e.getMessage());
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        setToast(recvInput);
                    }

                });

            }

        }
    }

    class sendMessage extends Thread {
        String data = "";
        sendMessage(String data, String category){
            this.data = category+"/"+data;
        }
        public void run() {
            try {
                byte[] b = new byte[1024];
                b = (data+"").getBytes();
                dos.write(b);
            } catch (Exception e) {
                final String recvInput = "메시지 전송에 실패하였습니다.";
                Log.d("SetServer", e.getMessage());
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        setToast(recvInput);
                    }

                });

//            }finally {
//                try {
//                    dos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }

        }
    }

    void setToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}