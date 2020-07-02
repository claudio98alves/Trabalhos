package ws;

import rmiserver.RMIInterfaceClient;
import rmiserver.RMIInterfaceServer;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint(value = "/ws/{idUser}")
public class WebSocketAnnotation extends UnicastRemoteObject implements RMIInterfaceClient {
    private static final Set<WebSocketAnnotation> users = new CopyOnWriteArraySet<>();
    private static final AtomicInteger sequence = new AtomicInteger(1);
    private String clientID;
    private Session session;
    private String channel;
    private RMIInterfaceServer rmi;
    public WebSocketAnnotation() throws RemoteException{
        this.channel="general";

    }

    public void print_on_client(String s) throws RemoteException{
        //rmiserver chama esta funçao com a notificaçao
        //String s tem a notificação temos de dar print no brownser
        sendMessageSpecificUser(s);
        return;
    }
    public String getInput() throws RemoteException{
        //para ignorar
        return "";
    }
    public void atualizacaoImediata(String s) throws RemoteException{
        //para enviar notifications so para os clientes do borwser
        System.out.println("atualizaçao :" + s);
        sendMessageSpecificUser(s);
        return;
    }

    @OnOpen
    public void start(@PathParam("idUser")String idUser,Session session){

        this.session = session;
        this.clientID = idUser;
        users.add(this);
        try {
            rmi = (RMIInterfaceServer) Naming.lookup("//localhost:1099/dropmusic");
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            rmi.addRef(clientID, this);
        }catch (RemoteException e){
            System.out.println("RMISERVER mandou remote exception");
        }
    }

    @OnClose
    public void end() {
        users.remove(this);
    }

    @OnMessage
    public void receiveMessage(String message) {

    }


    private void sendMessageSpecificUser(String s) {
        try {
            this.session.getBasicRemote().sendText(s);
        } catch (IOException e) {
            // clean up once the WebSocket connection is closed
            try {
                this.session.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IllegalStateException e){
            //quando envia para ele proprio
        }
    }

    @OnError
    public void handleError(Throwable t) {
    	t.printStackTrace();
    }

    private void sendMessage(String text) {
    	// uses *this* object's session to call sendText()
        //String channel = this.webSockets.get(this);
        for (WebSocketAnnotation user: users) {
            try {
                /*if(this.webSockets.get(user)!=null && this.webSockets.get(user.webSockets.get(user)).equals(channel)) {
                    user.session.getBasicRemote().sendText(text);*/
                if(user.channel.equals(this.channel))
                    user.session.getBasicRemote().sendText(text);

            } catch (IOException e) {
                    //clean up once the WebSocket connection is closed
                try {
                   user.session.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

    }
}
