package com.example.bikesharing.bean;

public class bike {
    public String bikeid;     //@Id 下面的第一个属性就是主键
    public String longitude;
    public String latitude;
    public String RFID;
    public String statu;
    public String speed;
    public String lasttime;
    public String voltage;
    public String temperature;

    public bike(){

    }
    public bike(String bikeId, String longitude, String latitude, String RFID, String statu, String speed, String lastTime, String voltage, String temperature) {
        this.bikeid = bikeId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.RFID = RFID;
        this.statu = statu;
        this.speed = speed;
        this.lasttime = lastTime;
        this.voltage = voltage;
        this.temperature = temperature;
    }




    public String getBikeId() {
        return bikeid;
    }

    public void setBikeId(String bikeId) {
        this.bikeid = bikeId;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getRFID() {
        return RFID;
    }

    public void setRFID(String RFID) {
        this.RFID = RFID;
    }

    public String getStatu() {
        return statu;
    }

    public void setStatu(String statu) {
        this.statu = statu;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getLastTime() {
        return lasttime;
    }

    public void setLastTime(String lastTime) {
        this.lasttime = lastTime;
    }

    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }



}
