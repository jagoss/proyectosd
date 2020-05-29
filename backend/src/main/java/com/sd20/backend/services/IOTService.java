package com.sd20.backend.services;

import com.sd20.backend.utils.Consts;
import com.sd20.backend.utils.Gadget;
import com.sd20.backend.utils.Request;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.toList;


/**
 * Responsabilidad de:
 * - mantener registro de los gadgets, tanto conectados como accesibles
 * - agregar datos a session recien inicializada para que sea accesible
 * - mandar ordenes a cada gadget
 * - mantener registro de si gadget hizo las cosas bien o no
 *
 *
 */
@Component
public class IOTService {


    private List<String> extensiones = Arrays.asList(Consts.LIGHT, Consts.DIMMER);

    AbstractMap<String, Gadget> gadgets = new ConcurrentHashMap<String, Gadget>();
    AbstractMap<String, String> sessionNameMap = new ConcurrentHashMap<String, String>();

    public void remove(WebSocketSession session) {
        gadgets.remove(session.getId());
        sessionNameMap.remove(session.getId());
    }

    public void add(WebSocketSession session) {
        gadgets.put(session.getId(), new Gadget(session));
    }

    public void addData(WebSocketSession session, String[] parts) {
        Gadget g = gadgets.get(session.getId());
        g.setName(parts[1]); sessionNameMap.put(session.getId(), parts[1]);
        for(int i=2; i<parts.length; i++){
            if (extensiones.contains(parts[i])){
                g.addExtension(parts[i]);
            }
        }
    }

    public boolean sendOrder(Request req) {
        Gadget g = null;
        try{
            g = gadgets.get(sessionNameMap.get(req.getDeviceName()));
            g.setLastRequest(req);
            g.setSuccess(false);
            if (g.containsExtension(req.getExtension())) {
                g.getSession().sendMessage(new TextMessage(req.toIOTRepresentation()));
                return true;
            }
            return false;
        } catch(IOException e) {  // ver que error sale de mandar a session desconectada
            g.setLastRequest(req);
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }


    public void updateRequestSuccessOrFailure(WebSocketSession session, String[] parts) {
        Gadget g = gadgets.get(session.getId());
        if (parts[1].equals(Consts.SUCCESS)) {
            g.setSuccess(true); g.setLastRequest(null);
        } else {
            g.setSuccess(false); // el lastRequest ya quedó de la vez pasada
        }
    }

    public List<Gadget> getCurrentGadgets() {
        return gadgets.values().stream()
                .filter(gadget -> gadget.getName() != null).collect(toList());
    }

}
