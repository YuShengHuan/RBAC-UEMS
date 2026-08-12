package com.ru.app.common.serverEndpoint;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;
/*
 * @author huan
 * @date 2025/4/24
 * 构建websocket服务端房间端点
 * @description
 */

@ServerEndpoint(
        value = "/api/ws/doc/{id}"
)
@Component
public class WebSocketServerEndpoint {


    @OnOpen
    public void onOpen(@PathParam("id") String id, Session session) {
        System.out.println();
    }

    @OnClose
    public void onClose(@PathParam("id") String id, Session session) {
        System.out.println();
    }

    @OnMessage
    public void onMessage(@PathParam("id") String id,String message, Session session) {
        try{
            System.out.println();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
