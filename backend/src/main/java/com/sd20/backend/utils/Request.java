package com.sd20.backend.utils;

public class Request {
    String deviceName;
    String extension;
    String value; // value["0","1"] or "toggle"

    public String toIOTRepresentation(){
        return String.format("%s;%s;%s", Consts.ORDER, extension, value);
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getExtension() {
        return extension;
    }
}
