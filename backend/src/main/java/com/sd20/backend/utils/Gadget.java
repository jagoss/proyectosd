package com.sd20.backend.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;

public class Gadget {
    // el write only hace que no se serialize al mandarlo por REST
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private WebSocketSession session;
    private String name;
    private List<String> extensiones;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Request lastRequest;
    private boolean success;

    public Gadget(){}

    public Gadget(WebSocketSession session) {
        this.session = session;
        this.name = null;
        this.extensiones = new ArrayList<>();
        this.lastRequest = null;
        this.success = false;
    }

    public void addExtension(String ext) {
        extensiones.add(ext);
    }
    public boolean containsExtension(String ext) {
        return extensiones.contains(ext);
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
}
