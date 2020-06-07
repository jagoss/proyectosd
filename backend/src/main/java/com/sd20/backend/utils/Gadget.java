package com.sd20.backend.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Gadget {
    // el write only hace que no se serialize al mandarlo por REST
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private WebSocketSession session;
    private String name;
    private Map<String, String> extensiones;
    private String[][] ext;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Request lastRequest;
    private boolean success;

    public Gadget() {
    }

    public Gadget(WebSocketSession session) {
        this.session = session;
        this.name = null;
        this.extensiones = new ConcurrentHashMap<>();
        this.lastRequest = null;
        this.success = false;
    }

    public void addExtension(String ext) {
        if (!extensiones.containsKey(ext)) {
            extensiones.put(ext, "OFF");
        }
    }

    public boolean containsExtension(String ext) {
        return extensiones.containsKey(ext);
    }

    public WebSocketSession getSession() {
        return session;
    }

    public void setSession(WebSocketSession session) {
        this.session = session;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Request getLastRequest() {
        return lastRequest;
    }

    public void setLastRequest(Request lastRequest) {
        this.lastRequest = lastRequest;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "Gadget{" +
                "session=" + session +
                ", name='" + name + '\'' +
                ", extensiones=" + extensiones +
                ", ext=" + Arrays.toString(ext) +
                ", lastRequest=" + lastRequest +
                ", success=" + success +
                '}';
    }

    public void updateStatus(String[] parts) throws IOException {
        if (parts.length % 2 == 0) throw new IOException("Gadget sent invalid message");
        for (int i = 1; i < parts.length; i += 2) {
            extensiones.put(parts[i], parts[i + 1]);
        }
    }

    public String[][] getExt() {
        String[][] a = new String[extensiones.size()][2];
        int i = 0;
        for (Map.Entry<String, String> entry : extensiones.entrySet()) {
            a[i][0] = entry.getKey();
            a[i][1] = entry.getValue();
            i++;
        }
        return a;
    }

}
