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

import com.obigo.tmutestapp.fragment.RemoteSettingFragment;
import com.obigo.tmutestapp.manager.NetworkManager;
import com.obigo.tmutestapp.manager.NetworkRequest;
import com.obigo.tmutestapp.request.DoorLockRequest;
import com.obigo.tmutestapp.request.DoorUnlockRequest;
import com.obigo.tmutestapp.request.GetVehicleDTCsRequest;
import com.obigo.tmutestapp.request.GetVehicleInfoRequest;
import com.obigo.tmutestapp.request.GetVehicleStatusRequest;
import com.obigo.tmutestapp.request.HornRequest;
import com.obigo.tmutestapp.request.LightRequest;
import com.obigo.tmutestapp.request.RemoteStartRequest;
import com.obigo.tmutestapp.request.RemoteStopRequest;
import com.obigo.tmutestapp.request.ResetMaintenanceRequest;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements RemoteSettingFragment.OnRemoteDailogListener {
    private EditText iPView, portView, vinView;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.makeCelsiusTable();
        iPView = (EditText)findViewById(R.id.edit_ip);
        portView = (EditText)findViewById(R.id.edit_port);
        recieveView = (TextView)findViewById(R.id.text_recieve);
        vinView = (EditText)findViewById(R.id.edit_vin);
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
                String vehicleId = vinView.getText().toString();
                GetVehicleStatusRequest request = new GetVehicleStatusRequest(vehicleId);
                NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<String>() {
                    @Override
                    public void onSuccess(NetworkRequest<String> request, String result) {
                        setToast(result);
                        recieveView.setText(result);
                    }

                    @Override
                    public void onFail(NetworkRequest<String> request, int errorCode, String errorMessage) {

                    }
                });
            }
        });

        btn = (Button)findViewById(R.id.btn_get_vehicle_info);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vehicleId = vinView.getText().toString();
                GetVehicleInfoRequest request = new GetVehicleInfoRequest(vehicleId);
                NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<String>() {
                    @Override
                    public void onSuccess(NetworkRequest<String> request, String result) {
                        setToast(result);
                        recieveView.setText(result);
                    }

                    @Override
                    public void onFail(NetworkRequest<String> request, int errorCode, String errorMessage) {

                    }
                });
            }
        });

        btn = (Button)findViewById(R.id.btn_get_dtcs);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vehicleId = vinView.getText().toString();
                GetVehicleDTCsRequest request = new GetVehicleDTCsRequest(vehicleId);
                NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<String>() {
                    @Override
                    public void onSuccess(NetworkRequest<String> request, String result) {
                        setToast(result);
                        recieveView.setText(result);
                    }

                    @Override
                    public void onFail(NetworkRequest<String> request, int errorCode, String errorMessage) {

                    }
                });
            }
        });

        btn = (Button)findViewById(R.id.btn_Remote_stop);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vehicleId = vinView.getText().toString();
                RemoteStopRequest request = new RemoteStopRequest(vehicleId);
                NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<String>() {
                    @Override
                    public void onSuccess(NetworkRequest<String> request, String result) {
                        setToast(result);
                    }

                    @Override
                    public void onFail(NetworkRequest<String> request, int errorCode, String errorMessage) {

                    }
                });
            }
        });

        btn = (Button)findViewById(R.id.btn_door_lock);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vehicleId = vinView.getText().toString();
                DoorLockRequest request = new DoorLockRequest(vehicleId);
                NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<String>() {
                    @Override
                    public void onSuccess(NetworkRequest<String> request, String result) {
                        setToast(result);
                    }

                    @Override
                    public void onFail(NetworkRequest<String> request, int errorCode, String errorMessage) {

                    }
                });
            }
        });

        btn = (Button)findViewById(R.id.btn_door_unlock);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vehicleId = vinView.getText().toString();
                DoorUnlockRequest request = new DoorUnlockRequest(vehicleId);
                NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<String>() {
                    @Override
                    public void onSuccess(NetworkRequest<String> request, String result) {
                        setToast(result);
                    }

                    @Override
                    public void onFail(NetworkRequest<String> request, int errorCode, String errorMessage) {

                    }
                });
            }
        });

        btn = (Button)findViewById(R.id.btn_hron);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vehicleId = vinView.getText().toString();
                HornRequest request = new HornRequest(vehicleId);
                NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<String>() {
                    @Override
                    public void onSuccess(NetworkRequest<String> request, String result) {
                        setToast(result);
                    }

                    @Override
                    public void onFail(NetworkRequest<String> request, int errorCode, String errorMessage) {

                    }
                });
            }
        });

        btn = (Button)findViewById(R.id.btn_Light);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vehicleId = vinView.getText().toString();
                LightRequest request = new LightRequest(vehicleId);
                NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<String>() {
                    @Override
                    public void onSuccess(NetworkRequest<String> request, String result) {
                        setToast(result);
                    }

                    @Override
                    public void onFail(NetworkRequest<String> request, int errorCode, String errorMessage) {

                    }
                });
            }
        });

        btn = (Button)findViewById(R.id.btn_reset_maintenance);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vehicleId = vinView.getText().toString();
                ResetMaintenanceRequest request = new ResetMaintenanceRequest(vehicleId);
                NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<String>() {
                    @Override
                    public void onSuccess(NetworkRequest<String> request, String result) {
                        setToast(result);
                    }

                    @Override
                    public void onFail(NetworkRequest<String> request, int errorCode, String errorMessage) {

                    }
                });
            }
        });


    }

    @Override
    public void onOkBtnClick(JSONObject settings) {
        String vehicleId = vinView.getText().toString();
        RemoteStartRequest remoteStartRequest = new RemoteStartRequest("requestData="+settings.toString(),vehicleId);
        NetworkManager.getInstance().getNetworkData(remoteStartRequest, new NetworkManager.OnResultListener<String>() {
            @Override
            public void onSuccess(NetworkRequest<String> request, String result) {
                Log.i("test",result);
                Toast.makeText(MainActivity.this,"success"+result,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(NetworkRequest<String> request, int errorCode, String errorMessage) {
                Log.i("test send",errorCode+","+errorMessage);
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

            }

        }
    }

    void setToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}