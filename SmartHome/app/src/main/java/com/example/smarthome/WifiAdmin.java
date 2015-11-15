package com.example.smarthome;

/**
 * Created by Administrator on 2015/3/7.
 */
import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;

public class WifiAdmin {
    private WifiManager mWifiManager;
    private WifiInfo mWifiInfo;
    private List<ScanResult> mWifiList;
    private List<WifiConfiguration> mWifiConfigurations;
    WifiLock mWifiLock;
    public WifiAdmin(Context context){
        mWifiManager=(WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        mWifiInfo=mWifiManager.getConnectionInfo();
    }
    public void openWifi(){
        if(!mWifiManager.isWifiEnabled()){
            mWifiManager.setWifiEnabled(true);
        }
    }
    public void closeWifi(){
        if(!mWifiManager.isWifiEnabled()){
            mWifiManager.setWifiEnabled(false);
        }
    }
    public int checkState() {
        return mWifiManager.getWifiState();
    }

    public void startScan(){
        mWifiManager.startScan();
        mWifiList=mWifiManager.getScanResults();
        mWifiConfigurations=mWifiManager.getConfiguredNetworks();
    }
    public List<ScanResult> getWifiList(){/*
        Collections.sort(mWifiList, new Comparator<ScanResult>() {
            public int compare(ScanResult arg0, ScanResult arg1) {
                return (arg0.level+"").compareTo(arg1.level+"");
            }
        });*/
        return mWifiList;
    }

    public void restartWifi(int mode){
        //mode=0; refresh after 100ms;  mode=1;immediately
        mWifiManager.setWifiEnabled(false);
        if(mode==0) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        mWifiManager.setWifiEnabled(true);
    }

}
