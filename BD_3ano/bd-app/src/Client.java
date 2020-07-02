
import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class Client extends Thread {
    //To Do: class Database por editar vinda do projeto de SD
    public static boolean login = false;
    public static String nome = "Cláudio";
    public static boolean permission = false;
    public static boolean debug = false;
    private static boolean menu = false;
    private static String[] id_menu = new String[3];  //pos 0 -> select valor  pos 1 id do select
    private static int BUFFER_SIZE = 4096;

    public static HashMap<String, String> request(HashMap<String, String> message, Socket clientSocket) {
        if (debug) {
            System.out.println("-------------------------------------------------------");
            for (HashMap.Entry<String, String> entry : message.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
            System.out.println("--------------------------------------------------------");
        }
        HashMap<String, String> answer = new HashMap<String, String>();
        if (clientSocket == null) {
            System.out.println("Socket error");
        }
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(message);
            byte[] bufferS = byteOut.toByteArray();
            //envia bufferS
            OutputStream enviar = clientSocket.getOutputStream();
            enviar.write(bufferS);
            out.close();
            byteOut.close();
            //recebe resposta
            //receber

            InputStream receber = clientSocket.getInputStream();
            ByteArrayInputStream byteIn;
            ObjectInputStream in;
            byte[] bufferR = new byte[BUFFER_SIZE];
            receber.read(bufferR);
            byteIn = new ByteArrayInputStream(bufferR);
            in = new ObjectInputStream(byteIn);
            try {
                answer = (HashMap<String, String>) in.readObject();
            } catch (ClassNotFoundException e) {
                System.out.println(e.getException());
            }
            in.close();
            byteIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (debug) {
            System.out.println("-------------------------------------------------------");
            for (HashMap.Entry<String, String> entry : answer.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
            System.out.println("--------------------------------------------------------");
        }
        return answer;
    }

    public static void main(String[] args) {
        //db = new Database("BD", args[0]);
        HashMap<String, String> message = new HashMap<>();
        HashMap<String, String> answer = new HashMap<>();
        Scanner read = new Scanner(System.in);
        Socket clientSocket = null;
        try {
            clientSocket = new Socket("localhost", 6000);

        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            answer.clear();
            message.clear();
            if (!login) {
                message.clear();
                System.out.println("\nBemvindo DropMusic");
                switch (select(new String[]{"Login", "Regist"})) {
                    case "Login":
                        message.put("type", "login");
                        System.out.println("Username: ");
                        message.put("username", textConstrainNumber(50));
                        System.out.println("Password: ");
                        message.put("password", textConstrainNumber(50));
                        answer = request(message, clientSocket);


                        if (answer.get("msg").equals("Login efetuado com sucesso.")) { //verifica se o login é realizado com sucesso
                            login = true;
                            permission = Boolean.parseBoolean(answer.get("permissao"));
                            nome = message.get("username");
                        }
                        System.out.println(answer.get("msg"));
                        break;
                    case "Regist":
                        message.put("type", "regist");
                        System.out.println("Username:");
                        message.put("username", textConstrainNumber(50));
                        System.out.println("Password:");
                        message.put("password", textConstrainNumber(50));
                        System.out.println("Permissão:");
                        message.put("permissao", selectBool("editor", "normal"));
                        answer = request(message, clientSocket);

                        System.out.println(answer.get("msg"));
                        break;
                }
            } else if (!menu) {
                message.clear();
                System.out.println("\nBemvindo " + nome);
                String[] escolha = new String[]{"show", "create", "search", "log out"};
                switch (select(escolha)) {
                    case "show":
                        message.put("type", "show");
                        switch (select(new String[]{"exit", "musicos", "bandas", "albuns", "musicas", "concertos", "my playlists", "all playlists", "my reviews"})) {
                            case "musicos":
                                message.put("select", "musicos");
                                break;
                            case "bandas":
                                message.put("select", "bandas");

                                break;
                            case "albuns":
                                message.put("select", "albuns");

                                break;
                            case "musicas":
                                message.put("select", "musicas");

                                break;
                            case "concertos":
                                message.put("select", "concertos");

                                break;
                            case "my playlists":
                                message.put("select", "my playlists");
                                message.put("username", nome);
                                break;
                            case "all playlists":
                                message.put("select", "all playlists");
                                break;
                            case "my reviews":
                                message.put("select", "my reviews");
                                message.put("username", nome);

                                break;
                            case "exit":
                                message.put("select", "exit");
                                break;
                        }
                        break;
                    case "search":
                        message.put("type", "search");
                        System.out.println("O que pretende pesquisar");
                        message.put("searchInput", textConstrainNumber(50));
                        message.put("select", "all");
                        break;

                    case "create":
                        message.put("type", "create");
                        escolha = new String[]{"exit", "playlist", "musica", "album", "editora", "musico", "banda"};
                        if (!permission) {
                            escolha = new String[]{"exit", "playlist"};
                        }
                        switch (select(escolha)) {
                            case "playlist":
                                if (verificar("musicas", clientSocket)) {
                                    message.put("select", "playlist");
                                    System.out.println("O nome que pretende dar á playlist:");
                                    message.put("nome", textConstrainNumber(50));
                                    System.out.println("Tipo de permissão da playlist:");
                                    message.put("permissao", selectBool("private", "public"));
                                    message.put("username", nome);
                                    adicionarMusicas(message, clientSocket);
                                } else {
                                    System.out.println("Não há musicas para colocar na playlist");
                                    message.put("ignore", "ignore");
                                }

                                break;
                            case "album": //tenho de ter editoras e artistas
                                if (verificar("editoras", clientSocket) && verificar("artistas", clientSocket) && verificar("musicas", clientSocket)) {
                                    message.put("select", "album");
                                    //nome genero datalancamento historia editoras_nome id_artist
                                    System.out.println("Nome do Album:");
                                    message.put("nome", textConstrainNumber(50));
                                    System.out.println("Genero do Album:");
                                    message.put("genero", textConstrainNumber(20));
                                    System.out.println("Data de lançamento:");
                                    message.put("datalancamento", getData(false));
                                    System.out.println("Historia:");
                                    message.put("historia", textConstrainNumber(300));
                                    System.out.println("Editora:");
                                    message.put("editora_nome", getID("editoras", clientSocket));
                                    System.out.println("Banda ou musico");
                                    message.put("id_artist", getID("artistas", clientSocket));
                                    adicionarMusicas(message, clientSocket);

                                } else {
                                    System.out.println("Não há musicos ou editoras ou musicas para criar albuns");
                                    message.put("ignore", "ignore");
                                }
                                break;
                            case "musica": //
                                if (verificar("compositores", clientSocket)) {
                                    message.put("select", "musica");
                                    //nome letra duracao compositor
                                    System.out.println("Nome da Musica:");
                                    message.put("nome", textConstrainNumber(50));
                                    System.out.println("Letra:");
                                    message.put("letra", textConstrainNumber(50));
                                    System.out.println("Duração:");
                                    message.put("duracao", getDuracao());
                                    System.out.println("Compositor:");
                                    message.put("compositor", getID("compositores", clientSocket));
                                } else {
                                    System.out.println("Não há compositores na BD para poder criar uma música");
                                    message.put("ignore", "ignore");
                                }
                                break;
                            case "editora":
                                message.put("select", "editora");
                                //nome
                                System.out.println("Nome da Editora:");
                                message.put("nome", textConstrainNumber(50));

                                break;
                            case "musico":
                                message.put("select", "musico");
                                //nome historia
                                System.out.println("Nome do Musico:");
                                message.put("nome", textConstrainNumber(50));
                                System.out.println("Historia:");
                                message.put("historia", textConstrainNumber(300));

                                break;

                            case "banda":
                                if (verificar("musicos", clientSocket)) {
                                    message.put("select", "banda");
                                    //nome historia
                                    System.out.println("Nome da banda:");
                                    message.put("nome", textConstrainNumber(50));
                                    System.out.println("Historia:");
                                    message.put("historia", textConstrainNumber(300));
                                    System.out.println("Data de inicio:");
                                    message.put("inicio", getData(false));
                                    System.out.println("Data de fim, (opcional)");
                                    message.put("fim", getData(true));
                                    adicionarMusicos(message, clientSocket);
                                } else {
                                    System.out.println("Não há musicos");
                                    message.put("ignore", "ignore");
                                }

                                break;
                            case "exit":
                                message.put("select", "exit");
                                break;

                        }
                        break;
                    case "log out":
                        login = false;
                        break;
                }
                if (login && !message.containsKey("ignore")) {
                    answer = request(message, clientSocket);
                    if (message.get("type").equals("create") && !message.get("select").equals("exit")) {
                        System.out.println(answer.get("msg"));
                    }
                    //verificar message.get("type").equals("show")
                    //id_menu[0]= message.get("select");
                    //id_menu[0]= chooseId(answer);
                    else if ((message.get("type").equals("show") && answer.containsKey("1") && !message.get("select").equals("exit")) && message.get("select").equals("my reviews") || message.get("select").equals("concertos")) {
                        printHash(answer);
                    } else if (message.get("type").equals("show") && answer.containsKey("1") && !message.get("select").equals("exit") && !message.get("select").equals("my reviews") && !message.get("select").equals("concertos")) {

                        id_menu[0] = message.get("select");
                        id_menu[1] = chooseIdSubMenu(answer);
                        if (!id_menu[1].equals("0")) {
                            menu = true;
                            if (answer.containsKey("IDNOME1")) {
                                id_menu[2] = specialCase(answer, id_menu[1]);
                            }
                        }
                    } else if (message.get("type").equals("search")) {
                        answer = request(message, clientSocket);
                        if (answer.containsKey("1")) {
                            optionsFromSearch(id_menu, answer);
                            if (!id_menu[1].equals("0")) {
                                menu = true;
                            }
                        } else {
                            System.out.println("Pesquisa sem sucesso");
                        }
                    }
                }
            } else if (menu) {
                //"albuns","musicos","bandas","musicas","all playlists","my playlists"
                String[] escolha;
                switch (id_menu[0]) {
                    case "albuns":
                        message.put("select", "albuns");
                        escolha = new String[]{"exit", "edit", "write review", "add music", "info", "reviews"};
                        if (!permission) {
                            escolha = new String[]{"exit", "write review", "add music", "info"};
                        }
                        switch (select(escolha)) {
                            case "add music":
                                message.put("select", "album");
                                message.put("type", "add");
                                message.put("id_album", id_menu[1]);
                                System.out.println("ID da musica:");
                                message.put("id_music", getID("musicas", clientSocket));
                                break;
                            case "exit":
                                menu = false;
                                break;
                            case "info":
                                message.put("type", "show info");
                                message.put("id", id_menu[1]);
                                break;
                            case "write review":
                                message.put("type", "write review");
                                message.put("id_album", id_menu[1]);
                                System.out.print("Escreva aqui a sua critica:");
                                message.put("texto", textConstrainNumber(300));
                                message.put("username", nome);
                                message.put("rate", getRate());
                                break;
                            case "reviews":
                                message.put("type", "show");
                                message.put("select", "reviews album");
                                message.put("id_album", id_menu[1]);
                                break;

                            case "edit":
                                edit("albuns", message, id_menu[1], clientSocket);
                                break;
                        }

                        break;
                    case "musicos":
                        message.put("select", "musicos");
                        escolha = new String[]{"exit", "edit", "make compositor", "add concerto", "info"};
                        if (!permission) {
                            escolha = new String[]{"exit", "make compositor", "add concerto", "info"};
                        }
                        switch (select(escolha)) {

                            case "exit":
                                menu = false;
                                break;
                            case "info":
                                message.put("type", "show info");
                                message.put("id", id_menu[1]);
                                break;
                            case "add concerto":
                                message.put("select", "concerto");
                                message.put("type", "create");
                                //lugar data id_artist
                                System.out.println("Lugar:");
                                message.put("lugar", textConstrainNumber(50));
                                System.out.println("Data:");
                                message.put("data", getData(false));
                                message.put("id_artist", id_menu[1]);
                                break;
                            case "make compositor":
                                message.put("select", "compositor");
                                message.put("type", "create");
                                message.put("id_artist", id_menu[1]);
                                break;
                            case "edit":
                                edit("musicos", message, id_menu[1], clientSocket);
                                break;
                        }
                        break;
                    case "bandas":
                        message.put("select", "bandas");
                        escolha = new String[]{"exit", "edit", "remove", "add concerto", "add musico", "info"};
                        if (!permission) {
                            escolha = new String[]{"exit", "add concerto", "add musico", "info"};
                        }
                        switch (select(escolha)) {
                            case "add concerto":
                                message.put("select", "concerto");
                                message.put("type", "create");
                                //lugar data id_artist
                                System.out.println("Lugar:");
                                message.put("lugar", textConstrainNumber(50));
                                System.out.println("Data:");
                                message.put("data", getData(false));
                                message.put("id_artist", id_menu[1]);
                                break;
                            case "add musico":
                                message.put("type", "add");
                                message.put("id_bandas", id_menu[1]);
                                System.out.println("Id do musico");
                                message.put("id_musico", getID("musicos", clientSocket));
                                break;
                            case "exit":
                                menu = false;
                                break;
                            case "info":
                                message.put("type", "show info");
                                message.put("id", id_menu[1]);
                                break;
                            case "edit":
                                edit("bandas", message, id_menu[1], clientSocket);
                                break;
                            case "remove":
                                if (verificarMusicosBanda(id_menu[1], clientSocket)) {
                                    message.put("type", "remove");
                                    message.put("id_banda", id_menu[1]);
                                    message.put("id_musico", chooseIdBandas(clientSocket, id_menu[1]));
                                } else {
                                    System.out.println("Não há musicos suficientes para remover");
                                    message.put("ignore", "ignore");
                                }
                                break;
                        }
                        break;
                    case "musicas":
                        message.put("select", "musicas");
                        escolha = new String[]{"exit", "edit", "info"};
                        if (!permission) {
                            escolha = new String[]{"exit", "info"};
                        }
                        switch (select(escolha)) {
                            case "exit":
                                menu = false;
                                break;
                            case "info":
                                message.put("type", "show info");
                                message.put("id", id_menu[1]);
                                break;
                            case "edit":
                                edit("musicas", message, id_menu[1], clientSocket);
                                break;
                        }
                        break;
                    case "my playlists":
                        message.put("select", "playlist");
                        escolha = new String[]{"exit", "edit", "remove", "add music", "info"};
                        if (!permission) {
                            escolha = new String[]{"exit", "add music", "remove", "info"};
                        }
                        switch (select(escolha)) {
                            case "add music":
                                if (verificar("musicas", clientSocket)) {
                                    message.put("type", "add");
                                    message.put("nome", id_menu[1]);
                                    message.put("username", nome);
                                    System.out.println("ID da musica:");
                                    message.put("id_music", getID("musicas", clientSocket));
                                } else {
                                    System.out.println("Não há musicas para adicionar");
                                    message.put("ignore", "ignore");
                                }
                                break;
                            case "exit":
                                menu = false;
                                break;
                            case "info":
                                message.put("type", "show");
                                message.put("select", "playlist musics");
                                message.put("playlistName", id_menu[1]);
                                message.put("playlistOwner", nome);
                                System.out.println("Musicas:");
                                break;
                            case "edit":
                                edit("my playlists", message, id_menu[1], clientSocket);
                                break;
                            case "remove":
                                if (verificarRemoveMusica(nome, id_menu[1], clientSocket)) {
                                    message.put("type", "remove");
                                    message.put("select", "playlists");
                                    message.put("playlistNome", id_menu[1]);
                                    message.put("username", nome);
                                    message.put("id_musica", chooseIdPlaylist(clientSocket, id_menu[1], nome));
                                } else {
                                    System.out.println("Não há musicas para remover");
                                    message.put("ignore", "ignore");
                                }
                                break;
                        }
                        break;
                    case "all playlists":
                        switch (select(new String[]{"exit", "info"})) {
                            case "exit":
                                menu = false;
                                break;
                            case "info":
                                message.put("type", "show");
                                message.put("select", "playlist musics");
                                message.put("playlistName", id_menu[1]);
                                message.put("playlistOwner", id_menu[2]);

                                break;
                        }
                        break;
                }
                //send hashmap over tcp to Server
                if (menu && !message.containsKey("ignore")) {
                    answer = request(message, clientSocket);
                    printHash(answer);
                }
                if (menu && !message.containsKey("ignore") && message.containsKey("type") && message.get("type").equals("edit")) {
                    if (message.containsKey("select") && message.get("select").equals("playlists")) {
                        if (message.get("atributo").equals("nome")) {
                            if (answer.containsKey("msg") && answer.get("msg").contains("sucesso")) {
                                id_menu[1] = message.get("new");
                            }
                        }
                    }
                }
            }
        }
    }

    public static String chooseIdPlaylist(Socket socket, String playlistNome, String nome) {
        HashMap<String, String> message = new HashMap<String, String>();
        message.put("type", "show");
        message.put("select", "playlist musics");
        message.put("playlistName", playlistNome);
        message.put("playlistOwner", nome);
        message = request(message, socket);
        return chooseId(message);
    }

    public static String chooseIdBandas(Socket socket, String id) {
        HashMap<String, String> message = new HashMap<String, String>();
        message.put("type", "show");
        message.put("select", "musicos bandas");
        message.put("id", id);
        message = request(message, socket);
        return chooseId(message);
    }

    public static boolean verificarMusicosBanda(String id, Socket socket) {
        HashMap<String, String> message = new HashMap<String, String>();
        message.put("type", "show");
        message.put("select", "bandas");
        message.put("playlistName", id);
        message = request(message, socket);
        if (message.size() > 1) {
            return false;
        }
        return true;
    }

    public static boolean verificarRemoveMusica(String username, String playlistNome, Socket socket) {
        HashMap<String, String> message = new HashMap<String, String>();
        //show musicas duma playlist
        message.put("type", "show");
        message.put("select", "playlist musics");
        message.put("playlistName", playlistNome);
        message.put("playlistOwner", username);
        message = request(message, socket);
        if (message.size() == 0) {
            return false;
        }
        return true;
    }

    public static void edit(String select, HashMap<String, String> message, String id, Socket socket) {
        //message já vem com o select
        message.put("type", "edit");
        switch (select) {
            case "musicas":
                message.put("id", id);
                switch (select(new String[]{"nome", "letra", "duracao", "compositor"})) {
                    case "nome":
                        message.put("atributo", "nome");
                        System.out.println("Novo nome:");
                        message.put("new", textConstrainNumber(50));
                        break;
                    case "letra":
                        message.put("atributo", "letra");
                        System.out.println("Nova letra:");
                        message.put("new", textConstrainNumber(600));
                        break;
                    case "duracao":
                        message.put("atributo", "duracao");
                        System.out.println("Nova duração:");
                        message.put("new", getDuracao());
                        break;
                    case "compositor":
                        message.put("atributo", "compositor");
                        System.out.println("Novo compositor:");
                        message.put("new", getID("compositores", socket));
                        break;
                }
                break;
            case "albuns":
                message.put("id", id);
                switch (select(new String[]{"nome", "genero", "historia", "data lancamento", "artista"})) {
                    case "nome":
                        message.put("atributo", "nome");
                        System.out.println("Novo nome:");
                        message.put("new", textConstrainNumber(50));
                        break;
                    case "historia":
                        message.put("atributo", "historia");
                        System.out.println("Novo historia:");
                        message.put("new", textConstrainNumber(300));
                        break;
                    case "genero":
                        message.put("atributo", "genero");
                        System.out.println("Novo genero:");
                        message.put("new", textConstrainNumber(20));
                        break;
                    case "data lancamento":
                        System.out.println("Nova data lançamento:");
                        message.put("atributo", "datalancamento");
                        message.put("new", getData(false));
                        break;
                    case "artista":
                        message.put("atributo", "artista");
                        System.out.println("Novo artista:");
                        message.put("new", getID("artistas", socket));
                        break;
                }

                break;
            case "musicos":
                message.put("id", id);
                switch (select(new String[]{"nome", "historia"})) {
                    case "nome":
                        message.put("atributo", "nome");
                        System.out.println("Novo nome:");
                        message.put("new", textConstrainNumber(50));
                        break;
                    case "historia":
                        message.put("atributo", "historia");
                        System.out.println("Nova historia:");
                        message.put("new", textConstrainNumber(300));
                        break;
                }
                break;
            case "bandas":
                message.put("id", id);
                switch (select(new String[]{"nome", "historia", "inicio", "fim"})) {
                    case "nome":
                        message.put("atributo", "nome");
                        System.out.println("Novo nome:");
                        message.put("new", textConstrainNumber(50));
                        break;
                    case "historia":
                        message.put("atributo", "historia");
                        System.out.println("Nova historia:");
                        message.put("new", textConstrainNumber(300));
                        break;
                    case "inicio":
                        message.put("atributo", "inicio");
                        System.out.println("Nova data:");
                        message.put("new", getData(false));
                        break;
                    case "fim":
                        message.put("atributo", "fim");
                        System.out.println("Nova data:");
                        message.put("new", getData(true));
                        break;
                }
                break;
            case "my playlists": //done
                switch (select(new String[]{"nome", "permission"})) {
                    case "nome":
                        message.put("atributo", "nome");
                        message.put("select", "playlists");
                        message.put("owner", nome);
                        message.put("nomeplaylist", id);
                        System.out.println("Novo nome");
                        message.put("new", textConstrainNumber(50));
                        break;
                    case "permission":
                        message.put("atributo", "permissao");
                        message.put("select", "playlists");
                        message.put("owner", nome);
                        message.put("nomeplaylist", id);
                        System.out.println("Nova permissao:");
                        message.put("new", selectBool("private", "public"));
                        break;
                }

                break;
        }
    }

    public static String getData(boolean state) {
        Scanner read = new Scanner(System.in);
        String aux;
        String[] teste;
        while (true) {
            System.out.println("yyyy-mm-dd");
            aux = read.nextLine(); //2018-11-24
            teste = aux.split("-");
            if (teste.length == 3) {
                if (teste[0].length() <= 4 && teste[1].length() <= 2 && teste[2].length() <= 2) {
                    try {
                        if (Integer.parseInt(teste[0]) <= 9999 && Integer.parseInt(teste[1]) <= 12 && Integer.parseInt(teste[2]) <= 31) {
                            return aux;
                        }
                    } catch (Exception e) {
                    }
                }
            } else if (aux.equals("") && state) {
                return "null";
            }
        }

    }

    public static String textConstrainNumber(int contraint) {
        Scanner read = new Scanner(System.in);
        String aux;
        do {
            aux = read.nextLine();
        } while (aux.length() > contraint);
        return aux;
    }

    public static void optionsFromSearch(String[] id_menu, HashMap<String, String> answer) {
        String aux = chooseIdSubMenu(answer);
        //aux select-nome or 0
        if (!aux.equals("0")) {
            id_menu[0] = aux.split("-")[0];
            id_menu[1] = aux.split("-")[1];
        } else {
            id_menu[1] = "0";
        }

    }

    public static String specialCase(HashMap<String, String> message, String nomePlaylist) {
        for (int i = 1; message.containsKey(String.valueOf(i)); i++) {
            if (message.get("ID" + i).equals(nomePlaylist)) {
                return message.get("IDNOME" + i);
            }
        }
        return "ERROR";
    }

    public static String select(String[] options) {
        Scanner read = new Scanner(System.in);
        if (options[0].equals("exit")) {
            while (true) {
                for (int i = 0; i < options.length; i++) {
                    System.out.println(i + " - " + options[i]);
                }
                try {
                    int option = Integer.parseInt(read.nextLine());
                    if (option <= options.length) {
                        return options[option];
                    }
                } catch (Exception e) {
                }
                System.out.println("Opção inválida, tente de novo");
            }
        } else {
            while (true) {
                for (int i = 1; i <= options.length; i++) {
                    System.out.println(i + " - " + options[i - 1]);
                }
                try {
                    int option = Integer.parseInt(read.nextLine());
                    if (option <= options.length) {
                        return options[option - 1];
                    }
                } catch (Exception e) {
                }
                System.out.println("Opção inválida, tente de novo");
            }
        }
    }

    public static String selectBool(String verd, String falsa) {
        if (verd.equals(select(new String[]{verd, falsa}))) {
            return "true";
        } else {
            return "false";
        }
    }

    public static String chooseIdSubMenu(HashMap<String, String> message) {
        Scanner read = new Scanner(System.in);
        System.out.println("0 - exit");
        for (int i = 1; message.containsKey(String.valueOf(i)); i++) {
            System.out.println(i + " - " + message.get(String.valueOf(i)));
        }
        String aux;
        while (true) {
            aux = read.nextLine();
            if (message.containsKey(aux)) {
                return message.get("ID" + aux);
            } else if (aux.equals("0")) {
                return aux;
            }
        }
    }

    public static String chooseId(HashMap<String, String> message) {
        Scanner read = new Scanner(System.in);

        for (int i = 1; message.containsKey(String.valueOf(i)); i++) {
            System.out.println(i + " - " + message.get(String.valueOf(i)));
        }
        String aux;
        while (true) {
            aux = read.nextLine();
            if (message.containsKey(aux)) {
                return message.get("ID" + aux);
            }
        }
    }

    public static void printHash(HashMap<String, String> message) {
        for (int i = 1; message.containsKey(String.valueOf(i)); i++) {
            System.out.println(message.get(String.valueOf(i)));
        }
        if (message.containsKey("msg")) {
            System.out.println(message.get("msg"));
        }
        System.out.println();
    }

    public static String getDuracao() { //hh:mm:ss
        Scanner read = new Scanner(System.in);
        String aux;
        String[] teste;
        while (true) {
            System.out.println("hh:mm:ss");
            aux = read.nextLine();
            teste = aux.split(":");
            if (teste.length == 3) {
                if (teste[0].length() == 2 && teste[1].length() == 2 && teste[2].length() == 2) {
                    try {
                        if (Integer.parseInt(teste[0]) <= 24 && Integer.parseInt(teste[1]) < 60 && Integer.parseInt(teste[2]) <= 60) {
                            return aux;
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    public static String getRate() {
        Scanner read = new Scanner(System.in);
        Double rate;
        while (true) {
            System.out.println("Rate:");
            try {
                rate = Double.parseDouble(read.nextLine());
                if (rate <= 10 && rate >= 0) {
                    return String.valueOf(rate);
                }
            } catch (Exception e) {
            }
        }
    }

    public static String getID(String select, Socket clientSocket) {
        Scanner read = new Scanner(System.in);
        HashMap<String, String> message = new HashMap<>();
        message.put("type", "show");
        message.put("select", select);
        return chooseId(request(message, clientSocket));
    }

    public static boolean verificar(String select, Socket socket) {
        HashMap<String, String> message = new HashMap<>();
        message.put("type", "show");
        message.put("select", select);
        int size = request(message, socket).size();

        if (size != 0) {
            if (message.get("select").equals("musicos")) {
                if (size < 2) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static void adicionarMusicas(HashMap<String, String> message, Socket socket) {
        System.out.println("Musicas:");

        HashMap<String, String> show = new HashMap<String, String>();
        HashMap<String, String> search = new HashMap<String, String>();
        show.put("type", "show");
        show.put("select", "musicas");
        show = request(show, socket);
        String aux;
        String valor = "";
        String[] escolha;
        while (true) {
            if (!valor.equals("")) {
                escolha = new String[]{"exit", "search", "show"};
            } else {
                escolha = new String[]{"search", "show"};
            }
            switch (select(escolha)) {
                case "show":
                    if (valor.equals("")) {
                        aux = chooseId(show);
                        valor = aux;
                    } else {
                        aux = chooseIdSubMenu(show);
                        if (!aux.equals("0")) {
                            valor = valor + "-" + aux;
                        }
                    }
                    break;
                case "search":
                    search.put("type", "search");
                    System.out.println("O que pretende pesquisar:");
                    search.put("searchInput", textConstrainNumber(50));
                    search.put("select", "musicas");
                    search = request(search, socket);
                    if (valor.equals("")) {
                        aux = getIdFromSearch("musicas", search, true);
                        valor = aux;
                    } else {
                        aux = getIdFromSearch("musicas", search, false);
                        if (!aux.equals("0")) {
                            valor = valor + "-" + aux;
                        }
                    }
                    break;
                case "exit":
                    message.put("id_musics", valor);
                    return;
            }
        }
    }

    public static String getIdFromSearch(String select, HashMap<String, String> answer, boolean first) {
        String aux;
        if (first) {
            aux = chooseId(answer);
        } else {
            aux = chooseIdSubMenu(answer);
        }
        if (!aux.equals("0")) {
            return aux.split("-")[1];
        }
        return "0";
    }

    public static void adicionarMusicos(HashMap<String, String> message, Socket socket) {


        HashMap<String, String> show = new HashMap<String, String>();
        HashMap<String, String> search = new HashMap<String, String>();
        show.put("type", "show");
        show.put("select", "musicos");
        show = request(show, socket);
        String aux;
        String valor = "";
        String[] escolha;
        int n = 0;

        while (true) {
            if (!valor.equals("")) {
                escolha = new String[]{"exit", "search", "show"};
            } else {
                escolha = new String[]{"search", "show"};
            }
            switch (select(escolha)) {
                case "show":
                    System.out.println("Musicos:");
                    if (n < 2) {
                        aux = chooseId(show);
                        valor = aux;
                        n++;
                    } else {
                        aux = chooseIdSubMenu(show);
                        if (!aux.equals("0")) {
                            valor = valor + "-" + aux;
                            n++;
                        }
                    }
                    break;
                case "search":
                    System.out.println("Musicos:");
                    search.put("type", "search");
                    System.out.println("O que pretende pesquisar:");
                    search.put("searchInput", textConstrainNumber(50));
                    search.put("select", "musicos");
                    search = request(search, socket);
                    if (valor.equals("")) {
                        aux = getIdFromSearch("musicos", search, true);
                        valor = aux;
                        n++;
                    } else {
                        aux = getIdFromSearch("musicos", search, false);
                        if (!aux.equals("0")) {
                            valor = valor + "-" + aux;
                            n++;
                        }
                    }
                    break;
                case "exit":
                    message.put("id_musicos", valor);
                    return;
            }
        }
    }
}