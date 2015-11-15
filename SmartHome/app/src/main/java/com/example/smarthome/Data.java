package com.example.smarthome;

/**
 * Created by Administrator on 2015/3/7.
 */
import android.app.Application;

public class Data extends Application{
    private String [][] roomwifi_outdoor = null;
    private String [][] hallwifi_outdoor = null;
    private String [][] halldef_outdoor = {{"14:cf:92:6c:f4:80","-82","8"},{"28:2c:b2:e5:c4:7e","-77","8"},{"e8:de:27:7a:de:1c","-81","8"},{"64:66:b3:96:c1:74","-78","3"},{"10:fe:ed:4d:9e:e4","-84","5"},{"14:cc:20:b3:9c:1c","-82","6"},{"c0:4a:00:b9:1e:8e","-75","8"},{"00:22:6b:5e:97:fd","-83","6"},{"ec:88:8f:de:37:c2","-81","6"},{"1c:7e:e5:1f:8d:0c","-87","6"}};
    private String [][] halldef = {{"08:10:76:64:1c:90","-53","10"},{"f8:1a:67:d8:92:ac","-62","10"},{"34:08:04:b5:d9:ac","-64","10"},{"14:75:90:43:36:a0","-70","10"},{"1c:7e:e5:1f:8d:0c","-75","10"},{"04:8d:38:56:c7:b0","-80","6"},{"e8:de:27:7a:de:1c","-75","3"},{"c8:3a:35:4a:8d:70","-82","3"}};
    private String [][] roomwifi = null;
    private String [][] hallwifi = null;
    private String [][] roomdef = {{"24:de:c6:ec:9f:c0","-63","10"},{"24:de:c6:ec:9f:c1","-63","10"},{"24:de:c6:ec:9f:c2","-63","10"},{"24:de:c6:ec:9f:c3","-63","10"},{"00:1a:1e:5b:e0:a2","-85","10"},{"00:1a:1e:5b:e0:a5","-85","10"},{"00:1a:1e:5b:e0:a1","-83","10"},{"00:1a:1e:5b:e0:a0","-83","10"},{"00:1a:1e:5b:e4:80","-83","10"},{"00:1a:1e:5b:e4:82","-83","10"},{"00:1a:1e:5b:e4:85","-83","10"},{"00:1a:1e:5b:e4:81","-83","10"}};
    private String [][] roomdef_outdoor = {{"24:de:c6:dc:e5:00","-80","10"},{"24:de:c6:dc:e5:01","-80","10"},{"24:de:c6:dc:e5:02","-80","10"},{"24:de:c6:dc:e5:03","-80","10"},{"24:de:c6:dc:e5:04","-80","10"},{"24:de:c6:dc:e5:05","-80","10"},{"24:de:c6:dc:e5:06","-80","10"},{"24:de:c6:dc:e5:07","-80","10"},{"00:0b:86:89:79:80","-85","10"},{"00:0b:86:89:79:82","-85","10"}};
    private boolean lightstatus = false;

    public String[][] getRoomwifi() {
        return roomwifi;
    }

    public String[][] getHallwifi() {
        return hallwifi;
    }

    public String[][] getRoomdef() {
        return roomdef;
    }

    public String[][] getHalldef() {
        return halldef;
    }

    public void setRoomwifi(String[][] roomwifi) {
        this.roomwifi = roomwifi;
    }

    public void setHallwifi(String[][] hallwifi) {
        this.hallwifi = hallwifi;
    }

    public void setRoomdef(String[][] roomdef) {
        this.roomdef = roomdef;
    }

    public void setHalldef(String[][] halldef) {
        this.halldef = halldef;
    }


    public String[][] getRoomwifi_outdoor() {
        return roomwifi_outdoor;
    }

    public void setRoomwifi_outdoor(String[][] roomwifi_outdoor) {
        this.roomwifi_outdoor = roomwifi_outdoor;
    }

    public String[][] getHallwifi_outdoor() {
        return hallwifi_outdoor;
    }

    public void setHallwifi_outdoor(String[][] hallwifi_outdoor) {
        this.hallwifi_outdoor = hallwifi_outdoor;
    }

    public String[][] getRoomdef_outdoor() {
        return roomdef_outdoor;
    }

    public void setRoomdef_outdoor(String[][] roomdef_outdoor) {
        this.roomdef_outdoor = roomdef_outdoor;
    }

    public String[][] getHalldef_outdoor() {
        return halldef_outdoor;
    }

    public void setHalldef_outdoor(String[][] halldef_outdoor) {
        this.halldef_outdoor = halldef_outdoor;
    }
    public void setLS(boolean ls){
        lightstatus = ls;
    }
    public boolean getLS(){
        return lightstatus;
    }

    @Override
    public void onCreate(){
        super.onCreate();
    }
}