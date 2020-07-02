package rmiserver;

import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Scanner;

public class RMIClient extends UnicastRemoteObject implements RMIInterfaceClient {
    /**
     * Construtor RMIClient
     * @throws RemoteException
     */
    public RMIClient() throws RemoteException {
        super();
    }

    /**
     * Método para o servidor poder usar o callback o dar print no cliente
     * @param s
     * @throws RemoteException
     */
    public void print_on_client(String s) throws RemoteException {
        System.out.println(s);
    }

    public void atualizacaoImediata(String s) throws RemoteException{
        return;
    }
    /**
     * Método para o servidor poder usar o callback para receber input do cliente
     * @return valor introduzido pelo utilizador
     */
    public String getInput() {
        return new Scanner(System.in).nextLine();
    }

    /**
     * Método para dar upload de um ficheiro para a base de dados através de TCP
     * @param port porto da socket no MulticastServer
     * @param id id da música que vamos dar upload
     * @param file FileInputStream com o path do file que vai ser enviado
     * @param ip endereço da máquina do servidor Multicast
     */
    public static void TCPConnectionUp(String port,String id, FileInputStream file, String ip) {
        Socket s = null;
        DataOutputStream out = null;
        DataInputStream in = null;
        InputStream inputStream = null;
        try {
            s = new Socket(ip, Integer.parseInt(port));

            in = new DataInputStream(s.getInputStream());
            out = new DataOutputStream(s.getOutputStream());

            inputStream = new BufferedInputStream(file);


            byte[] buffer = new byte[1024 * 1024 * 10];
            int size = inputStream.read(buffer);
            //envia o ClientID
            out.writeUTF(id);
            //envia o tamanho do ficheiro
            out.writeUTF(String.valueOf(size));
            //envia o ficheiro
            out.write(buffer,0,size);
            //recebe msg de success ou fail
            System.out.println(in.readUTF());

        } catch (UnknownHostException e) {
            System.out.println("Sock:" + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO:" + e.getMessage());
        } finally {
                try {
                    in.close();
                    out.close();
                    inputStream.close();
                    s.close();

                } catch (IOException e) {
                    System.out.println("close:" + e.getMessage());
                }

        }
    }

    /**
     * Método para fazer download de um ficheiro para a Diretoria de Downloads
     * @param port porto da socket no MulticastServer
     * @param id id da música que vamos dar download
     * @param ip endereço da máquina do servidor Multicast
     * @param nome nome da música
     */
    public static void TCPConnectionDown(String port,String id,String ip,String nome) {
        Socket s = null;
        DataOutputStream out = null;
        DataInputStream in = null;
        try {
            s = new Socket(ip, Integer.parseInt(port));

            System.out.println(ip);
            in = new DataInputStream(s.getInputStream());
            out = new DataOutputStream(s.getOutputStream());
            //envia ClientID
            out.writeUTF(id);
            //cria buffer com tamanho certo
            String control;
            //le o tamanho do ficheiro
            if(!(control=in.readUTF()).equals("ERRO")){
                byte[] array = new byte[Integer.parseInt(control)];
                //lê o ficheiro
                int offset=0;
                int count;
                while((count=in.read(array,offset,array.length-offset))!=0){
                    offset+=count;
                }
                //recebe nome da musica
                OutputStream outputStream = new BufferedOutputStream(new FileOutputStream("C:\\Users\\User\\Downloads\\"+nome));
                outputStream.write(array);
                outputStream.close();
            }

        } catch (UnknownHostException e) {
            System.out.println("Sock:" + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO:" + e.getMessage());
        } finally {
                try {
                    in.close();
                    out.close();
                    s.close();

                } catch (IOException e) {
                    System.out.println("close:" + e.getMessage());
                }

        }
    }

    /**
     * Main do cliente, menu de inputs e construção de HashMaps seguind o protocolo
     * @param args
     * @throws MalformedURLException
     * @throws RemoteException
     * @throws NotBoundException
     */
    public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
        if (args.length != 1) {
            System.out.println("Input invalid. Format:java RMIClient <RMIServerip>");
            System.exit(-1);
        }

        int i=0;
        RMIInterfaceServer rmi=null;
        do {//tentar conectar ao rmiserver durante 30 segundos
            try {
                rmi = (RMIInterfaceServer) Naming.lookup("//"+args[0]+":1099/dropmusic");

            } catch (ConnectException e) {
                try{Thread.sleep(5000);}catch (InterruptedException n){}
            }
            i++;
        }while(i<6 && rmi==null);
        if(i==6){//dá exit do cliente se não se conseguiu conectar nos 30 segundos
            System.out.println("Unable to connect to RMIServer");
            return;
        }

        System.out.println("Client ready...");
        RMIClient c = new RMIClient();
        boolean login = false;
        HashMap<String, String> message = new HashMap<>();
        Scanner keyboardScanner = new Scanner(System.in);
        String ClientID = null;

        while (true) {
            try {
                message.clear();
                if (!login) {
                    System.out.println("Menu: \n1 - Regist\n2 - Login");
                    switch (keyboardScanner.nextLine()) {
                        case "1":
                            message = rmi.regist((RMIInterfaceClient) c);
                            rmi.printMessage(message, (RMIInterfaceClient) c);
                            break;
                        case "2":
                            message = rmi.login((RMIInterfaceClient) c);
                            rmi.printMessage(message, (RMIInterfaceClient) c);

                            if (message.containsKey("type") && message.get("login").equals("successful")) {//verificar se o regist foi bem sucedido
                                //login sucessful
                                ClientID = message.get("identifier");
                                login = true;
                                rmi.addRef(ClientID, (RMIInterfaceClient) c);
                            }
                            break;
                        default:
                            System.out.println("Wrong command");
                            break;

                    }

                } else if (login) {
                    System.out.println("New comand:");
                    switch (keyboardScanner.nextLine()) {
                        case "help":
                            System.out.println("          search music");
                            System.out.println("           show all");
                            System.out.println("          show details");
                            System.out.println("           write review");
                            System.out.println("            log out");
                            System.out.println("            download");
                            System.out.println("             upload");
                            System.out.println("              share");
                            System.out.println(" ---- Editor Permission ----");
                            System.out.println("             insert");
                            System.out.println("             remove");
                            System.out.println("              edit");
                            System.out.println("             promote");

                            break;
                        case "search music":
                            message.put("type", "search music");
                            message.put("select", rmi.selectMusic((RMIInterfaceClient) c));
                            System.out.println("Search input:");
                            message.put("text", keyboardScanner.nextLine());
                            message = rmi.request(message);
                            rmi.printMessage(message, (RMIInterfaceClient) c);
                            //done

                            break;
                        case "show all":
                            message.put("type", "show all");
                            message.put("select", rmi.select((RMIInterfaceClient) c));
                            message = rmi.request(message);
                            rmi.printMessage(message, (RMIInterfaceClient) c);
                            //done
                            break;
                        case "show details":
                            message.put("type", "show details");
                            message.put("select", rmi.select((RMIInterfaceClient) c));
                            message.put("identifier", rmi.selectId((RMIInterfaceClient) c, message.get("select")));
                            message = rmi.request(message);
                            rmi.printMessage(message, (RMIInterfaceClient) c);
                            //done
                            break;
                        case "insert":
                            message.put("identifier", ClientID);
                            message = rmi.insert(message, (RMIInterfaceClient) c);
                            message = rmi.request(message);
                            rmi.printMessage(message, (RMIInterfaceClient) c);
                            //done
                            break;
                        case "remove":
                            message.put("identifier", ClientID);
                            message.put("type", "remove");
                            message.put("select", rmi.select((RMIInterfaceClient) c));
                            message.put("id", rmi.selectId((RMIInterfaceClient) c, message.get("select")));
                            message = rmi.request(message);
                            rmi.printMessage(message, (RMIInterfaceClient) c);
                            //done
                            break;
                        case "write review":
                            String pppppp;
                            message.put("type", "write review");
                            message.put("identifier", rmi.selectId((RMIInterfaceClient) c, "album"));
                            message.put("rate", rmi.rate((RMIInterfaceClient) c));
                            message.put("text", rmi.review((RMIInterfaceClient) c));
                            pppppp= message.get("identifier");
                            message = rmi.request(message);
                            rmi.printMessage(message, (RMIInterfaceClient) c);
                            rmi.sendNotificationB(pppppp);
                            //done
                            break;
                        case "edit":
                            //done
                            message.put("identifier", ClientID);
                            message.put("type", "edit");
                            message.put("select", rmi.select((RMIInterfaceClient) c));
                            message.put("key", rmi.selectKey((RMIInterfaceClient) c, message.get("select")));
                            message.put("value", rmi.selectValue((RMIInterfaceClient) c));
                            message.put("id", rmi.selectId((RMIInterfaceClient) c, message.get("select")));
                            HashMap<String, String> r = message;
                            message = rmi.request(message);
                            if (r.get("select").equals("album") && r.get("key").equals("description") && message.get("msg").equals("sucessful")) {
                                //quando é preciso enviar notificações
                                rmi.sendNotification(r.get("select"), r.get("id"));
                            }
                            rmi.printMessage(message, (RMIInterfaceClient) c);
                            break;
                        case "promote":
                            message.put("type", "promote");
                            message.put("identifier", ClientID);
                            System.out.println("Username to promote:");
                            message.put("username", keyboardScanner.nextLine());
                            message = rmi.promote(message);
                            rmi.printMessage(message, (RMIInterfaceClient) c);
                            break;
                        case "upload":
                            message.put("type", "get port");
                            message.put("status", "upload");
                            System.out.println("Select ID of the music you want to upload");
                            message.put("idmusic", keyboardScanner.nextLine());
                            try{
                                System.out.println("Insert the path to the file");
                                String ext = keyboardScanner.nextLine();
                                String[] aux = ext.split("\\.");
                                FileInputStream file = new FileInputStream(ext);
                                ext = aux[aux.length-1];
                                message.put("ext",ext);
                                //só manda request se o file path for válido
                                message = rmi.request(message);
                                TCPConnectionUp(message.get("port"), ClientID,file,message.get("ip"));
                            }catch (FileNotFoundException e){
                                System.out.println("File not Found");
                            }

                            break;
                        case "download":
                            message.put("type", "get port");
                            message.put("status", "download");
                            System.out.println("Select id of music you want to download");
                            message.put("idmusic", keyboardScanner.nextLine());
                            message = rmi.request(message);
                            if(message.containsKey("port")) {
                                TCPConnectionDown(message.get("port"), ClientID, message.get("ip"),message.get("title"));
                            }
                            break;

                        case "share":
                            message.put("type", "share");
                            message.put("identifier", ClientID);
                            System.out.println("Select id of user you want to share with");
                            message.put("iduser", keyboardScanner.nextLine());
                            System.out.println("Select id from which music you want to share");
                            message.put("idmusic", keyboardScanner.nextLine());
                            message = rmi.request(message);
                            rmi.printMessage(message, (RMIInterfaceClient) c);
                            break;
                        case "log out":
                            message.put("type", "log out");
                            message.put("identifier", ClientID);
                            message = rmi.logOut(message);
                            if (message.get("msg").equals("successful")) {
                                System.out.println("Log Out Done!");
                                login = false;
                                ClientID = null;
                            }
                            break;
                        default:
                            System.out.println("Wrong comand");
                            break;
                        //nota write review
                    }
                }
            } catch (ConnectException e) {
                //Quando o RMIServer vai abaixo
                System.out.println("Solving error on RMIServer...");
                Boolean status = true;
                while (status) { //espera até que o RMIServer volte a estar online
                    try {
                        rmi = (RMIInterfaceServer) Naming.lookup("//"+args[0]+":1099/dropmusic");
                        status = false;
                        System.out.println("Error solved");
                    } catch (Exception q) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException a) {
                        }
                    }
                }

            }
        }
    }
}