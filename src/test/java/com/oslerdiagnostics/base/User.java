package com.oslerdiagnostics.base;

public class User {

    private String userID;
    private String deviceID;
    private String hexCode;
    private String status;

    public User() {
    }

    public User(String userID, String deviceID, String hexCode, String status) {
        this.userID = userID;
        this.deviceID = deviceID;
        this.hexCode = hexCode;
        this.status = status;
    }

    public String getUserID() {
        return userID;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public String getHexCode() {
        return hexCode;
    }

    public String getStatus() {
        return status;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public void setHexCode(String hexCode) {
        this.hexCode = hexCode;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID='" + userID + '\'' +
                ", deviceID=" + deviceID +
                ", hexCode='" + hexCode + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
