package com.example.smarthome;

import android.app.Activity;
import android.app.Fragment;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ButtonFragment extends Fragment {
    private ToggleButton tb;
    private ImageView iv1,iv2;
    private Data app;
    private ConnectivityManager con;
    private View rootView;
	public ButtonFragment() {

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_button, container, false);
        tb = (ToggleButton) rootView.findViewById(R.id.tb);
        iv1 = (ImageView) rootView.findViewById(R.id.iv1);
        iv2 = (ImageView) rootView.findViewById(R.id.iv2);
        iv1.setVisibility(View.INVISIBLE);
        iv2.setVisibility(View.VISIBLE);
        app = (Data)getActivity().getApplication();
        con=(ConnectivityManager)getActivity().getSystemService(Activity.CONNECTIVITY_SERVICE);

        initView();
        return rootView;
	}

    private void initView() {
        tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tb.setEnabled(false);
                System.out.println("click");
                if(!wifiConnection())
                    return;
                if(isChecked){
                    Toast toast=Toast.makeText(getActivity().getApplicationContext(), "light on", Toast.LENGTH_SHORT);
                    toast.show();
                    String res = new httpRequest("on",app).sendRequest();
                    Toast toast2=Toast.makeText(getActivity().getApplicationContext(),res, Toast.LENGTH_SHORT);
                    toast2.show();
                    iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.INVISIBLE);
                }else{
                    Toast toast=Toast.makeText(getActivity().getApplicationContext(), "light off", Toast.LENGTH_SHORT);
                    toast.show();
                    String res = new httpRequest("off",app).sendRequest();
                    Toast toast2=Toast.makeText(getActivity().getApplicationContext(),res, Toast.LENGTH_SHORT);
                    toast2.show();
                    iv1.setVisibility(View.INVISIBLE);
                    iv2.setVisibility(View.VISIBLE);
                }
                tb.setEnabled(true);
            }
        });//添加监听事件
    }
    public void updateLightStatus(){
        String opt = new httpRequest("check",app).sendRequest();
        System.out.println(opt);
        if(opt.equalsIgnoreCase("on")) {
            tb.toggle();
            iv1.setVisibility(View.VISIBLE);
            iv2.setVisibility(View.INVISIBLE);
        }
    }
    public boolean wifiConnection(){
        boolean wifi=con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        boolean internet=con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        if(!(wifi|internet)){
            Toast.makeText(getActivity().getApplicationContext(),
                    "No Internet connection", Toast.LENGTH_LONG)
                    .show();
            tb.toggle();
            tb.setEnabled(true);
        }
        return (wifi|internet);
    }
}
