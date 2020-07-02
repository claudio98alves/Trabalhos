import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Server extends Thread {
    private static Database db;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Input invalid. Format:java MulticastServer <num> <dbpassword>");
            System.exit(-1);
        }
        db = new Database(args[0], args[1]);
        int numero = 0;
        ArrayList<Connection> sockets = new ArrayList<>();
        try {
            int serverPort = 6000;
            System.out.println("A Escuta no Porto 6000");
            ServerSocket listenSocket = new ServerSocket(serverPort);
            System.out.println("LISTEN SOCKET=" + listenSocket);
            while (true) {
                Socket clientSocket = listenSocket.accept(); // BLOQUEANTE
                System.out.println("CLIENT_SOCKET (created at accept())=" + clientSocket);
                numero++;
                Connection c = new Connection(clientSocket, numero, db);
                sockets.add(c);
            }
        } catch (IOException e) {
            System.out.println("Listen:" + e.getMessage());
        }
    }
}

class Connection extends Thread {
    DataInputStream receber;
    DataOutputStream enviar;
    Socket clientSocket;
    int thread_number;
    Database db;

    public Connection(Socket aClientSocket, int numero, Database db) {
        thread_number = numero;
        try {
            clientSocket = aClientSocket;
            this.db = db;
            receber = new DataInputStream(clientSocket.getInputStream());
            enviar = new DataOutputStream(clientSocket.getOutputStream());
            this.start();
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }
    }

    //=============================
    public void run() {
        String resposta;
        try {
            while (true) {
                //an echo server
                HashMap<String, String> message = new HashMap<>();
                byte[] bufferR = new byte[4096];
                receber.read(bufferR);
                ByteArrayInputStream byteIn;
                ObjectInputStream in;
                byteIn = new ByteArrayInputStream(bufferR);
                in = new ObjectInputStream(byteIn);
                try {
                    message = (HashMap<String, String>) in.readObject();
                } catch (ClassNotFoundException e) {
                    System.out.println(e.getException());
                }
                in.close();
                byteIn.close();
                HashMap<String, String> answer = db.process(message);
                ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(byteOut);
                out.writeObject(answer);
                byte[] bufferS = byteOut.toByteArray();
                enviar.write(bufferS);
                out.close();
                byteOut.close();
            }
        } catch (EOFException e) {
            System.out.println("EOF:" + e);
        } catch (SQLException e) {
            System.out.println("SQL:" + e);
        } catch (IOException e) {
            System.out.println("IO:" + e);
        }

    }
}