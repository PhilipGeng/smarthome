package com.example.smarthome;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2015/2/18.
 */
public class httpRequest {
    private String result = "";
    private int res = 0;
    private final String urlon = "http://www.comp.polyu.edu.hk/~12132031d/comp3432/lighton.php";
    private final String urloff = "http://www.comp.polyu.edu.hk/~12132031d/comp3432/lightoff.php";
    private final String urlcheck = "http://www.comp.polyu.edu.hk/~12132031d/comp3432/lightstatus.php";
    private String url = urlon;
    private URL webURL;
    private Data app;
    private String option;

    BufferedReader in = null;
    HttpURLConnection connection = null;
    public httpRequest(String opt, Data data){
        app=data;
        option = opt;
        if(opt.equals("off"))
            url = urloff;
        else if(opt.equals("check"))
            url = urlcheck;

    }
    public String sendRequest() {
        try {
            String urlNameString = url;
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            Map<String, List<String>> map = connection.getHeaderFields();
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("unexpected GET error！" + e);
            result = "-1";
            e.printStackTrace();
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        if(result.length()>5){
            String on_or_off = result.split(";")[0].split(" ")[1];
            if(on_or_off.equalsIgnoreCase("on"))
                app.setLS(true);
            else
                app.setLS(false);
            return on_or_off;
        }
        else {
            int ret = 0;
            if ((ret = Integer.parseInt(result)) > 1)
                result = "Success! (resp code:" + result + ")";
            else
                result = "Fail! (resp code:" + result + ")";
            if(ret==8)
                app.setLS(true);
            else
                app.setLS(false);
        }
        return result;
    }
}
