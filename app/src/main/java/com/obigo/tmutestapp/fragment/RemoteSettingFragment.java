package com.obigo.tmutestapp.fragment;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.obigo.tmutestapp.R;
import com.obigo.tmutestapp.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class RemoteSettingFragment extends DialogFragment {


    public RemoteSettingFragment() {
        // Required empty public constructor
    }

    TextView airValueView, ignitionView;
    SeekBar seekBarAir, seekBarIgnition;
    Switch defrost, heating, airControl;
    boolean airFlag = false;
    private float tempValue = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_remote_setting, container, false);
        airControl = (Switch) view.findViewById(R.id.switch_air);
        defrost = (Switch)view.findViewById(R.id.switch_defrost);
        heating = (Switch)view.findViewById(R.id.switch_heating);
        seekBarAir = (SeekBar)view.findViewById(R.id.seekBar);
        airValueView = (TextView)view.findViewById(R.id.text_air_value);
        ignitionView = (TextView)view.findViewById(R.id.text_ignition_value);
        seekBarIgnition = (SeekBar)view.findViewById(R.id.seekBar_ignition);
        final JSONObject jsonObject = new JSONObject();
        final JSONObject setting = new JSONObject();
        final JSONObject airTemp = new JSONObject();
        try {
            setting.put("id","1803");
            setting.put("settingName","Christian");
            airTemp.put("cValue", 0);
            airTemp.put("fValue", 0);
            airTemp.put("unit", 0);
            airTemp.put("value", "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        airControl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                airFlag = isChecked;
                try {
                    if(!isChecked){
                        defrost.setChecked(false);
                        heating.setChecked(false);
                        seekBarAir.setEnabled(false);

                        setting.put("airCtrl",0);
                        setting.put("defrost",false);
                        setting.put("heating1",0);
                        airTemp.put("cValue", 0);
                        airTemp.put("fValue", 0);
                        airTemp.put("unit", 0);
                        airTemp.put("value", "0");
                    }else{
                        seekBarAir.setEnabled(true);
                        setting.put("airCtrl",1);
                        airTemp.put("value", Utils.getCodeToTemp(tempValue));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        defrost.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if(isChecked){
                        airControl.setChecked(true);
                        setting.put("defrost",true);
                    }else{
                        setting.put("defrost",false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        heating.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if(isChecked){
                        airControl.setChecked(true);
                        setting.put("heating1",1);
                    }else{
                        setting.put("heating1",0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        seekBarAir.setEnabled(false);
        seekBarAir.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                try {
                    progress += 34;
                    float tempValue = (float)progress/2;
                    RemoteSettingFragment.this.tempValue = tempValue;
                    if(airFlag) {
                        airValueView.setText(tempValue + "°C");
                        airTemp.put("value",Utils.getCodeToTemp(tempValue));
                    }
                    setting.put("airTemp",airTemp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarIgnition.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ignitionView.setText(progress+"");
                try {
                    setting.put("igniOnDuration",progress);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


            }
        });
        Button btn = (Button)view.findViewById(R.id.btn_ok);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    try {
                        setting.put("airTemp",airTemp);
                        jsonObject.put("setting",setting);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    listener.onOkBtnClick(jsonObject);
                }
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().setTitle("Remote Start Setting");
        int width = getResources().getDimensionPixelSize(R.dimen.remote_dialog_width);
        int height = getResources().getDimensionPixelSize(R.dimen.remote_dialog_height);
        getDialog().getWindow().setLayout(width, height);
        WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        getDialog().getWindow().setAttributes(lp);
    }

    public interface OnRemoteDailogListener {
        public void onOkBtnClick(JSONObject settings);
    }

    OnRemoteDailogListener listener;

    public void setOnRemoteDailogListener(OnRemoteDailogListener listener) {
        this.listener = listener;
    }

}