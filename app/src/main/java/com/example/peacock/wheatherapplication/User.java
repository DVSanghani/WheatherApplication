package com.example.peacock.wheatherapplication;

/**
 * Created by peacock on 26/4/17.
 */

public class User {
    String time;
    String temperture;
    String icon;


    public User() {
    }

    public User(String time, String temperture) {
        this.time = time;
        this.temperture = temperture;
//        this.icon = icon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemperture() {
        return temperture;
    }

    public void setTemperture(String temperture) {
        this.temperture = temperture;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
