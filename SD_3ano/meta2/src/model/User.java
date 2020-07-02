package model;

import rmiserver.RMIInterfaceServer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.opensymphony.xwork2.Action.LOGIN;
import static com.opensymphony.xwork2.Action.SUCCESS;

public class User {
    public RMIInterfaceServer getServer() {
        return server;
    }

    public void setServer(RMIInterfaceServer server) {
        this.server = server;
    }

    private RMIInterfaceServer server;
    private String username;
    private String password;
    private String idUser;
    private String permission;

    public User() {
        try {
            server = (RMIInterfaceServer) Naming.lookup("//0.0.0.0:1099/dropmusic");
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
    }
    public String connect(String token){

        DropBoxRestClient dp = new DropBoxRestClient();
        return dp.connect(token);
    }
    public String connect2(String token){

        DropBoxRestClient dp = new DropBoxRestClient();
        return dp.connect2(token);
    }

    public HashMap<String,String> playmusic(String token){
        DropBoxRestClient dp = new DropBoxRestClient();
        return dp.playmusic(token);
    }

    public void share(String token,String id,String email){
        DropBoxRestClient dp = new DropBoxRestClient();
        dp.share(token,id,email);
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean login() throws RemoteException {
        HashMap<String, String> map = server.login(username, password);
        if (map.containsKey("type") && map.get("login").equals("successful")) {
            setIdUser(map.get("identifier"));
            setPermission(map.get("permission"));
            return true;
        }
        return false;
    }

    public boolean regist() throws RemoteException {

        if (server.regist(username, password)) {
            return true;
        }
        return false;
    }

    public void logOut() {
        HashMap<String, String> message = new HashMap<>();
        message.put("type", "log out");
        message.put("identifier", getIdUser());
        try {
            server.logOut(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public ArrayList<String> listFiles(String token){
        DropBoxRestClient dp = new DropBoxRestClient();
        return dp.listFiles(token);
    }
    public ArrayList<String> showAll(String select) {
        HashMap<String, String> message = new HashMap<>();
        message.put("type", "show all");
        message.put("select", select);
        ArrayList<String> array = new ArrayList<>();
        try {
            message = server.request(message);
            for (Map.Entry<String, String> entry : message.entrySet()) {
                    array.add(entry.getKey() + ": " + entry.getValue());
            }
            return array;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        array.add("Empty");
        return array;

    }

    public ArrayList<String> searchMusic(String select, String text) {

        HashMap<String, String> message = new HashMap<>();
        message.put("type", "search music");
        message.put("select", select);
        message.put("text", text);
        ArrayList<String> array = new ArrayList<>();
        try {
            message = server.request(message);
            for (Map.Entry<String, String> entry : message.entrySet()) {
                array.add(entry.getKey() + ": " + entry.getValue());

            }
            return array;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        array.add("Empty");
        return array;
    }

    public ArrayList<String> showDetails(String select, String id) {
        HashMap<String, String> message = new HashMap<>();
        message.put("type", "show details");
        message.put("select", select);
        message.put("identifier", id);
        ArrayList<String> array = new ArrayList<>();
        try {
            message = server.request(message);
            for (Map.Entry<String, String> entry : message.entrySet()) {
                array.add(entry.getKey() + ": " + entry.getValue());
            }
            return array;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        array.add("Empty");
        return array;
    }

    public void writereview(String id, String rate, String review){
        HashMap<String,String> message = new HashMap<>();
        message.put("type", "write review");
        message.put("identifier", id);
        message.put("rate", rate);
        message.put("text", review);
        try {
            server.request(message);
            server.sendNotificationB(id);
        }catch (RemoteException e){
            e.printStackTrace();
        }

    }

    public void insert(String select,String text, String description) {
        HashMap<String, String> message = new HashMap<>();
        message.put("type", "insert");
        message.put("select", select);
        message.put("identifier",getIdUser());
        //sem defesa de inputs
        switch (select) {
            case "artist":
                //Name
                message.put("name", text);
                //Description
                message.put("description", description);
                break;
            case "album":
                //Title
                message.put("title", text);
                //Description
                message.put("description", description);
                break;
        }
        try {
            server.request(message);
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }

    public void insert(String select,String text,String compositor, String duracao, String genre, String idartist, String idalbum) {
        HashMap<String, String> message = new HashMap<>();
        message.put("identifier",getIdUser());
        message.put("type", "insert");
        message.put("select", select);
        //sem defesa de inputs
        message.put("title",text);
        message.put("compositor", compositor);
        message.put("duration", duracao);
        message.put("genre",genre);
        message.put("idartist",idartist);
        message.put("idalbum",idalbum);
        try {
            server.request(message);
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }

    public void promote(String username){
        HashMap<String, String> message = new HashMap<>();
        message.put("type","promote");
        message.put("identifier",getIdUser());
        message.put("username",username);
        try {
            server.promote(message);
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }

    public void remove(String select, String id){
        HashMap<String, String> message = new HashMap<>();
        message.put("identifier", getIdUser());
        message.put("type", "remove");
        message.put("select",select);
        message.put("id",id);
        try {
            server.promote(message);
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }

    public void edit(String select, String key, String value, String id){
        HashMap<String, String> message = new HashMap<>();
        message.put("identifier", getIdUser());
        message.put("type", "edit");
        message.put("select",select); //artist / album / music
        message.put("key",key); //artist ou album --> title or description // music --> title, compositor,duration,genre,idlabum,idartist
        message.put("value",value); //valor novo
        message.put("id",id);// o id do coiso
        HashMap<String, String> reply = new HashMap<>();

        try {
            reply =server.request(message);
            if (message.get("select").equals("album") && message.get("key").equals("description") && reply.get("msg").equals("sucessful")) {
                //quando é preciso enviar notificações
                System.out.println("Enviei notification de edit");
                server.sendNotification(message.get("select"), message.get("id"));
            }

        }catch (RemoteException e){
            e.printStackTrace();
        }

    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission.toLowerCase();
    }
}
