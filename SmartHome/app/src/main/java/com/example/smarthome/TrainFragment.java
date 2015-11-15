package com.example.smarthome;

import android.app.Fragment;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;

public class TrainFragment extends Fragment {
    private Button indoor;
    private Button outdoor;
    private TextView log;
    private RadioGroup rg;
    private View rootView;
    private RadioButton rb;
    private Data app;
    private Context cxt;
    private List<ScanResult> list;
    private ScanResult mScanResult;
    private StringBuffer sb=new StringBuffer();
    private WifiAdmin wifi;
    private final int THRESHOLD = -90;
    private String [][] fingerprint = null;
    public int cnt = 0;
    public int index = 0;
    public TrainFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        cnt=0;
        fingerprint = new String [10][3];
		rootView = inflater.inflate(R.layout.fragment_train, container, false);
        app = (Data)getActivity().getApplication();
        cxt = getActivity().getApplicationContext();
        initWidget();
        return rootView;
	}

    public void initWidget(){
        indoor = (Button)rootView.findViewById(R.id.button);
        outdoor = (Button)rootView.findViewById(R.id.button2);
        rg = (RadioGroup)rootView.findViewById(R.id.rg);
        log = (TextView)rootView.findViewById(R.id.txtLabel);
        //log.setText(app.getB());
        indoor.setEnabled(false);
        outdoor.setEnabled(false);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                int radioButtonId = arg0.getCheckedRadioButtonId();
                rb = (RadioButton)rootView.findViewById(radioButtonId);
             //   app.setB(rb.getText().toString());
                indoor.setText("Indoor Training");
                outdoor.setText("Outdoor Training");
                if(rb.getText().toString().equalsIgnoreCase("DefaultRoom")||rb.getText().toString().equalsIgnoreCase("DefaultHall")){
                    indoor.setEnabled(false);
                    outdoor.setEnabled(false);
               //     indoor.setBackgroundColor(Color.RED);
               //     outdoor.setBackgroundColor(Color.RED);
                }
                else{
                    indoor.setEnabled(true);
                    outdoor.setEnabled(true);
              //      indoor.setBackgroundColor(Color.GRAY);
               //     outdoor.setBackgroundColor(Color.GRAY);
                }
            }
        });
        indoor.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                cnt=0;
                fingerprint = new String [10][3];
                indoor.setEnabled(false);
                outdoor.setEnabled(false);
                index = 0;
                WifiSetup();
            }
        });
        outdoor.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                cnt=0;
                fingerprint = new String [10][3];
                indoor.setEnabled(false);
                outdoor.setEnabled(false);
                index=1;
                WifiSetup();
            }
        });

    }
    public void WifiSetup(){
        wifi = new WifiAdmin(getActivity().getApplicationContext());
        wifi.openWifi();
        Toast.makeText(cxt, "current wifi status: " + wifi.checkState(), Toast.LENGTH_SHORT).show();

        class TimerTaskTest extends java.util.TimerTask{
            @Override
            public void run() {
                cnt++;
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
                if(cnt>10) {
                    if(index==0)//indoor
                        msg.what = 2;
                    else
                        msg.what=3;
                    cancel();
                    wifi.restartWifi(1);
                }
            }
        }
        Timer timer = new Timer();
        timer.schedule(new TimerTaskTest(), 0, 2000);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1)
                getAllNetWorkList();
            if(msg.what==2||msg.what==3) {
                String ss ="";
                for(int j=0;j<fingerprint.length;j++){
                    if(fingerprint[j][0]!=null)
                        fingerprint[j][1] = (Integer.parseInt(fingerprint[j][1])/Integer.parseInt(fingerprint[j][2]))+"";
                    ss += (fingerprint[j][0]+" "+fingerprint[j][1]+" "+fingerprint[j][2]+"\n");
                }
                log.setText(ss);
                if(msg.what==2) {
                    indoor.setText("training is done");
                    if(!outdoor.getText().toString().equalsIgnoreCase("training is done"))
                        outdoor.setEnabled(true);
                    if(rb.getText().toString().equalsIgnoreCase("Room"))
                        app.setRoomwifi(fingerprint);
                    if(rb.getText().toString().equalsIgnoreCase("Hall"))
                        app.setHallwifi(fingerprint);
                }
                else{
                    outdoor.setText("training is done");
                    if(!indoor.getText().toString().equalsIgnoreCase("training is done"))
                        indoor.setEnabled(true);
                    if(rb.getText().toString().equalsIgnoreCase("Room"))
                        app.setRoomwifi_outdoor(fingerprint);
                    if(rb.getText().toString().equalsIgnoreCase("Hall"))
                        app.setHallwifi_outdoor(fingerprint);
                }
            }
        }
    };

    public void getAllNetWorkList(){
        if(sb!=null){
            sb=new StringBuffer();
        }
        wifi.startScan();
        list=wifi.getWifiList();
        if(list!=null){
            for(int i=0;i<list.size();i++){
                mScanResult=list.get(i);
                if(mScanResult.level>THRESHOLD) {
                    sb = sb.append(mScanResult.BSSID + "  ").append(mScanResult.SSID + "   ").append(mScanResult.level + "\n");
                    int k;
                    for(k=0;k<fingerprint.length;k++){
                        if(fingerprint[k][0]==null||fingerprint[k][0].equalsIgnoreCase(mScanResult.BSSID))
                            break;
                    }
                    if(k>fingerprint.length-1)
                         continue;
                    if(fingerprint[k][0] ==null) {
                        fingerprint[k][0] = mScanResult.BSSID;
                        fingerprint[k][1] = mScanResult.level + "";
                        fingerprint[k][2] = "1";
                    }
                    else{
                        fingerprint[k][1] = (Integer.parseInt(fingerprint[k][1])+mScanResult.level)+"";
                        fingerprint[k][2] = (Integer.parseInt(fingerprint[k][2])+1)+"";
                    }
                    //sb.append(fingerprint[k][0]+"  ").append(fingerprint[k][1]+"  ").append(fingerprint[k][2]+"\n");
                }
            }
            log.setText(sb.toString());
        }
    }

}
