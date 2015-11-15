package com.example.smarthome;

import android.app.Fragment;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.widget.ToggleButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;


public class AutoFragment extends Fragment {
    private View rootView;
    private TextView tv,lv,posv,lsv;
    private Context cxt;
    private Data app;
    private ToggleButton tb;

    //spinner and its content and adapter
    private List<String> list_content = new ArrayList<String>();
    private Spinner mySpinner;
    private ArrayAdapter<String> adapter;
    private List<ScanResult> list;
    //wifi scan list and buffer
    private WifiAdmin wifi;
    private ScanResult mScanResult;
    private StringBuffer sb=new StringBuffer();
    //threshold for wifi
    private int THRESHOLD = -99;
    private String [][] indoor;
    private String [][] outdoor;
    private boolean [] status = {false,false,false};
    private int status_itr = 0;
    private String [][] fp = new String [10][3];//fingerprint
    private Timer timer;
    private int indoor_outdoor_threshold = 35;
    public AutoFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_auto, container, false);
        tv = (TextView) rootView.findViewById(R.id.txtlabel);
        tb = (ToggleButton) rootView.findViewById(R.id.tb);
        lsv = (TextView) rootView.findViewById(R.id.lsview);
        posv = (TextView) rootView.findViewById(R.id.posview);
        lv = (TextView) rootView.findViewById(R.id.lv);
        cxt = getActivity().getApplicationContext();
        app = (Data)getActivity().getApplication();
        mySpinner = (Spinner) rootView.findViewById(R.id.sp);
        if(app.getLS()){//get light status
            status[0] = true;
            status[1] = true;
            status[2] = true;
            lsv.setText("light is on");
        }
        initView();
    	return rootView;
	}

    public void initView(){
        initList();
        initButton();
    }
    public void initList(){
        if(app.getRoomwifi_outdoor()!=null&&app.getRoomwifi()!=null){
            list_content.add("room");
        }
        if(app.getRoomdef_outdoor()!=null&&app.getRoomwifi()!=null){
            list_content.add("room default");
        }
        if(app.getHallwifi_outdoor()!=null&&app.getHallwifi()!=null){
            list_content.add("hall");
        }
        if(app.getHalldef()!=null&&app.getHalldef_outdoor()!=null){
            list_content.add("hall default");
        }

        adapter = new ArrayAdapter<String>(cxt,android.R.layout.simple_spinner_item,list_content);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(adapter);
        mySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                TextView textselected = (TextView) arg1;
                textselected.setTextColor(Color.BLUE);
                String [][] indoor_model = null;
                String [][] outdoor_model = null;
                tv.setText("Select trained model：" + adapter.getItem(arg2));
                if(adapter.getItem(arg2).equalsIgnoreCase("room")){
                        indoor_model = app.getRoomwifi();
                        outdoor_model = app.getRoomwifi_outdoor();
                }
                if(adapter.getItem(arg2).equalsIgnoreCase("hall")){
                    indoor_model = app.getHallwifi();
                    outdoor_model = app.getHallwifi_outdoor();
                }
                if(adapter.getItem(arg2).equalsIgnoreCase("room default")){
                    indoor_model = app.getRoomdef();
                    outdoor_model = app.getRoomdef_outdoor();
                }
                if(adapter.getItem(arg2).equalsIgnoreCase("hall default")){
                    indoor_model = app.getHalldef();
                    outdoor_model = app.getHalldef_outdoor();
                }
                indoor = indoor_model;
                outdoor = outdoor_model;
             //   System.out.println(indoor_model[0][0]);
             //   System.out.println(outdoor_model[0][0])
                arg0.setVisibility(View.VISIBLE);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                tv.setText("NONE");
                arg0.setVisibility(View.VISIBLE);
            }
        });
    }
    public void initButton(){
        tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){//start job
                    mySpinner.setEnabled(false);
                    WifiSetup();
                }
                else{//end job
                    mySpinner.setEnabled(true);
                    timer.cancel();
                }
            }
        });
    }
    public void WifiSetup(){
        wifi = new WifiAdmin(getActivity().getApplicationContext());
        wifi.openWifi();
        class TimerTaskTest extends java.util.TimerTask{
            @Override
            public void run() {
            //    wifi.restartWifi(1);
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }
        timer = new Timer();
        timer.schedule(new TimerTaskTest(), 0, 2000);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1)
                fp = getAllNetWorkList();
        }
    };

    public String [][] getAllNetWorkList(){
        String [][] fingerprint = new String [10][3];
        if(sb!=null){
            sb=new StringBuffer();
            sb.append("Log:\n");
        }
        wifi.startScan();
        list=wifi.getWifiList();
        if(list!=null){
            for(int i=0;i<list.size()&&i<fingerprint.length;i++){
                mScanResult=list.get(i);
                if(mScanResult.level>THRESHOLD) {
                    sb = sb.append(mScanResult.BSSID + "  ").append(mScanResult.SSID + "   ").append(mScanResult.level + "\n");
                    fingerprint[i][0] = mScanResult.BSSID;
                    fingerprint[i][1] = mScanResult.level + "";
                    fingerprint[i][2] = "1";
                    //sb.append(fingerprint[k][0]+"  ").append(fingerprint[k][1]+"  ").append(fingerprint[k][2]+"\n");
                }
            }
            lv.setText(sb.toString());
        }
        boolean position = Regression(fingerprint,indoor,outdoor);
        System.out.println("get position: "+position+" previous status: "+status[0]+status[1]+status[2]);
        status[status_itr]=position;
        if(position!=app.getLS()&&status[0]==status[1]&&status[1]==status[2]){//change status
            Toast toast;
            if(status[0]){
                toast=Toast.makeText(getActivity().getApplicationContext(),"turn on light", Toast.LENGTH_SHORT);
                httpRequest h = new httpRequest("on",app);
                System.out.println("send http request light on");
                String res = h.sendRequest();
                lsv.setText("light is on");
            }
            else{
                toast=Toast.makeText(getActivity().getApplicationContext(),"turn off light", Toast.LENGTH_SHORT);
                httpRequest h = new httpRequest("off",app);
                System.out.println("send http request light on");
                String res = h.sendRequest();
                lsv.setText("light is off");
            }
            toast.show();
        }
        status_itr++;
        status_itr%=3;
        return fingerprint;
    }
    public boolean Regression(String [][] sample,String [][] indoor,String [][] outdoor){
        boolean pos = true;//indoor -true outdoor -false
        double indist = distance(sample,indoor);
        double outdist = distance(sample,outdoor);
        DecimalFormat    df   = new DecimalFormat("######0.00");
        if(sample[0][0]==null){//no signal
            posv.setText("no signal detected");
            pos = false;
        }
        else {
            posv.setText("indoor" + df.format(indist) + " ; " + "outdoor" + df.format(outdist));
        }
        if(indist>outdist||indist>indoor_outdoor_threshold)
            pos = false;
        return pos;
    }
    public double distance(String [][] sample, String [][] model){
        double dist = 0;
        double cnt = 0;
 outer: for(int j=0;j<model.length;j++){
            if(model[j][0]==null)
                continue;
            String modelAdd = model[j][0];
            double modellevel = Integer.parseInt(model[j][1]);
     inner: for(int i=0;i<sample.length;i++){
                if(sample[i][0]==null)
                    continue;
                double samplelevel = Integer.parseInt(sample[i][1]);
                if(sample[i][0].equalsIgnoreCase(model[j][0])) {
                        dist+=Math.abs(samplelevel-modellevel);
                        cnt++;
                        sample[i][2] = "0";//mark it
                        continue outer;
                }
            }
            //no match from the sample
            dist+=Math.abs(120+modellevel);
            cnt++;
        }
        for(int i=0;i<sample.length;i++) {
            if (sample[i][0] == null||sample[i][2].equalsIgnoreCase("0"))//select unmarked ones
                continue;
            dist+=Math.abs(120+Integer.parseInt(sample[i][1]));
            cnt++;
        }
   //     System.out.println("dist/cnt="+dist+"/"+cnt+"="+(dist/cnt));
        return dist/cnt;
    }
 }
