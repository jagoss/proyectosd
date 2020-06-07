package com.sd20.backend.websocket;

import com.sd20.backend.services.IOTService;
import com.sd20.backend.utils.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Component
public class WebSocketHandler extends AbstractWebSocketHandler {

    IOTService iot;

    @Autowired
    public WebSocketHandler(IOTService iot){
        this.iot = iot;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        System.out.println("message "+message);
        String m = message.getPayload().toString();
        // message es del tipo "tipo;data"
        // STATUS;LIGHT;estadoLamparita;DIMMER;estadoDimmer;TERMOMETRO;calorDeTermofon
        // INFO;name;LIGHT;DIMMER;TERMOMETRO
        // ORDER:
        // servidor manda ORDER;extension;value
        // gadget manda ORDER;success/failure NO IMPLEMENTADO AUN o usado
        String[] parts = m.split(";");
        System.out.println(parts[0] + " gggg " + parts[1]);
        // pongo switch por si luego el gadget puede mandar mas mensajes
        // como por ejemplo "error llevando a cabo orden" o algo asi
        switch (parts[0]) {
            case Consts.INFO:
                System.out.println("entra info " + Arrays.toString(parts));
                iot.addData(session, parts);
                break;
            case Consts.ORDER:
                iot.updateRequestSuccessOrFailure(session, parts);
                break;
            case Consts.STATUS:
                iot.updateStatus(session, parts);
                break;
            default:
                throw new IOException("Gadget sent invalid message");
                //break;  UNREACHABLE
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        System.out.println("desconectado: " + session.toString());
        iot.remove(session);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("conectado: " + session.toString());
        // tengo la sesion, pero no se nada del arduino
        // le pregunto por sus datos
        iot.add(session);
        TimeUnit.SECONDS.sleep(5);
        session.sendMessage(new TextMessage(Consts.INFO));
        System.out.println("envio data info");
    }
}
