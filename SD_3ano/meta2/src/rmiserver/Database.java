package rmiserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.*;
import java.sql.ResultSet;
import java.util.HashMap;

/**
 * Classe para fazer todas as operações sobre a base de dados.
 */
public class Database {
    private Connection c;
    private PreparedStatement st;
    private ResultSet rs;
    private Statement cs;

    /**
     * Construtor da classe Database, conecta à base de dados. Cria uma base de dados se esta não existir se já existir liga-se.
     * @param num Identificador da base de dados (nome)
     * @param pw Password de acesso ao pgadmin do Postgres
     */
    public Database(String num,String pw) {
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/", "postgres", pw);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/dropmusic" + num, "postgres", pw);
        } catch (SQLException e) {
            try {
                cs = c.createStatement();
                cs.executeUpdate("create database dropmusic" + num);
                c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/dropmusic" + num, "postgres", pw);
                cs = c.createStatement();
                cs.executeUpdate("create table public.users" +
                        "(" +
                        "    id serial primary key," +
                        "    name character varying not null," +
                        "    password character varying not null," +
                        "    permission boolean not null," +
                        "    status boolean not null default false" +
                        ");");
                cs.executeUpdate("create table public.artists" +
                        "(" +
                        "    id serial primary key," +
                        "    title character varying NOT NULL," +
                        "    description character varying NOT NULL" +
                        ");");
                cs.executeUpdate("CREATE TABLE public.albums" +
                        "(" +
                        "    id serial primary key," +
                        "    title character varying NOT NULL," +
                        "    description character varying NOT NULL," +
                        "    rate double precision NOT NULL DEFAULT 0.0" +
                        ");");
                cs.executeUpdate("CREATE TABLE public.musics" +
                        "(" +
                        "    id serial primary key," +
                        "    title character varying NOT NULL," +
                        "    compositor character varying NOT NULL," +
                        "    duration character varying NOT NULL," +
                        "    genre character varying NOT NULL," +
                        "    idalbum serial constraint fk_idalbum_musics references public.albums(id) on delete cascade," +
                        "    idartist serial constraint fk_idartist_musics references public.artists(id) on delete cascade" +
                        ");");
                cs.executeUpdate("CREATE TABLE public.reviews" +
                        "(" +
                        "    id serial primary key," +
                        "    text character varying(300) not null," +
                        "    rate double precision not null,"+
                        "idalbum serial constraint fk_idalbum_reviews references public.albums(id) on delete cascade" +
                        ")");
                cs.executeUpdate("CREATE TABLE public.notifications" +
                        "(" +
                        "iduser serial constraint fk_iduser_notifications references public.users(id) on delete cascade," +
                        "    msg character varying not null " +
                        ");");
                cs.executeUpdate("CREATE TABLE public.history" +
                        "(" +
                        "iduser serial constraint fk_iduser_history references public.users(id) on delete cascade," +
                        "    idalbum serial constraint fk_idalbum_history references public.albums(id) on delete cascade" +
                        ");");
                cs.executeUpdate("CREATE TABLE public.files" +
                        "(" +
                        "    idmusic serial primary key constraint fk_idmusic_files references public.musics(id) on delete cascade," +
                        "    datam bytea not null," +
                        "    iduser serial constraint fk_iduser_files references public.users(id) on delete cascade," +
                        "    iddb character varying not null,"+
                        "    ext character varying not null"+
                        ") ;");
                cs.executeUpdate("CREATE TABLE public.share" +
                        "(" +
                        "    idmusic integer constraint fk_idmusic_share references public.files(idmusic) on delete cascade," +
                        "    iduser integer constraint fk_iduser_share references public.users(id) on delete cascade" +
                        ") ;");
                cs.executeUpdate("create table public.dropbox( " +
                        "idmusic integer primary key  constraint fk_idmusic_dropbox references public.musics(id) on delete cascade," +
                        "idficheiro character varying," +
                        "iduser integer constraint fk_iduser_dropbox references public.users);");
            } catch (Exception e1) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Método que verifica o type do protocolo udp, e executa a correspondente função.
     * @param message Mensagem a processar
     * @return Resposta após processamento
     */
    public HashMap<String, String> process(HashMap<String, String> message) {
        HashMap<String, String> reply = new HashMap<String, String>();
        switch (message.get("type")) {
            case "regist":
                reply = regist(message);
                break;
            case "login":
                reply = login(message);
                break;
            case "show all":
                reply = showall(message);
                break;
            case "show details":
                reply = showdetails(message);
                break;
            case "write review":
                reply = writereview(message);
                break;
            case "search music":
                reply = searchmusic(message);
                break;
            case "insert":
                reply = insert(message);
                break;
            case "remove":
                reply = remove(message);
                break;
            case "edit":
                reply = edit(message);
                break;
            case "promote":
                reply = promote(message);
                break;
            case "insert notification":
                reply = insertnotification(message);
                break;
            case "remove notification":
                reply = removenotification(message);
                break;
            case "get history":
                reply = gethistory(message);
                break;
            case "log out":
                reply = logout(message);
                break;
            case "get port":
                reply = getport(message);
                break;
            case "share":
                reply = share(message);
                break;
            case "dropbox":
                reply = dropbox(message);
                break;
            default:
                System.out.println("Error on process function");
                break;
        }
        return reply;
    }
    public HashMap<String,String> dropbox(HashMap<String,String> message){
        HashMap<String, String> reply = new HashMap<String, String>();
        try{
            if(message.get("select").equals("insert")){
                st = c.prepareStatement("insert into public.dropbox (idmusic,idficheiro,iduser) values(?,?,?);");
                st.setInt(1,Integer.parseInt(message.get("idmusic")));
                st.setString(2,message.get("idficheiro"));
                st.setInt(3,Integer.parseInt(message.get("iduser")));
                st.executeUpdate();
            }
            if(message.get("select").equals("get")){
                st = c.prepareStatement("select idmusic,idficheiro from public.dropbox where iduser=?;");
                st.setInt(1,Integer.parseInt(message.get("iduser")));
                rs = st.executeQuery();
                while (rs.next()){
                    st = c.prepareStatement("select title from public.musics where id=?;");
                    st.setInt(1,rs.getInt(1));
                    ResultSet rs2 = st.executeQuery();
                    rs2.next();
                    reply.put(String.valueOf(rs.getInt(1))+":"+rs2.getString(1),rs.getString(2));
                }
                return reply;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return reply;
    }
    /**
     * Método para registar utilizador utilizador na base de dados.
     * @param message Mensagem a processar
     * @return Resposta após processamento
     */
    public HashMap<String, String> regist(HashMap<String, String> message) {
        HashMap<String, String> reply = new HashMap<String, String>();
        //exemplo --> type|regist;username|name;password|pass
        try {
            st = c.prepareStatement("select name from public.users where name = '" + message.get("username") + "'");
            rs = st.executeQuery();
            if (rs.next()) {
                reply.put("type", "status");
                reply.put("regist", "failed");
                reply.put("msg", "Username already in use.");
                return reply;
            }
            st = c.prepareStatement("select count(name) from public.users");
            rs = st.executeQuery();
            st = c.prepareStatement("insert into public.users(id,name, password, permission) values (DEFAULT,?, ?, ?);");
            st.setString(1, message.get("username"));
            st.setString(2, message.get("password"));
            if (rs.next() && rs.getInt(1) == 0) {
                st.setBoolean(3, true);
            } else {
                st.setBoolean(3, false);
            }
            st.executeUpdate();
            reply.put("type", "status");
            reply.put("regist", "successful");
            reply.put("msg", "Registry done.Enjoy DropMusic.");
            return reply;
        } catch (Exception e) {
            reply.put("type", "status");
            reply.put("regist", "failed");
            reply.put("msg", e.getMessage());
            return reply;
        }
    }

    /**
     * Método para dar login do utilizador na base de dados.
     * @param message Mensagem a processar
     * @return Resposta após processamento
     */
    public HashMap<String, String> login(HashMap<String, String> message) {
        //exemplo --> type|regist;username|name;password|pass;
        HashMap<String, String> reply = new HashMap<String, String>();
        try {
            st = c.prepareStatement("select exists( select * from public.users where not status and name='" + message.get("username") + "' and password='" + message.get("password") + "' );");
            rs = st.executeQuery();
            if (rs.next() && rs.getBoolean(1)) {
                st = c.prepareStatement("update public.users set status=true where name='" + message.get("username") + "';");
                st.executeUpdate();
                st = c.prepareStatement("select id,permission from public.users where name='" + message.get("username") + "';");
                rs = st.executeQuery();
                rs.next();
                reply.put("type", "status");
                reply.put("login", "successful");
                reply.put("msg", "You're logged in.");
                reply.put("identifier", String.valueOf(rs.getInt(1)));
                reply.put("permission",String.valueOf(rs.getBoolean(2)));
                return reply;
            }
            reply.put("type", "status");
            reply.put("login", "failed");
            reply.put("msg", "Credentials wrong.");
            return reply;
        } catch (Exception e) {
            reply.put("type", "status");
            reply.put("login", "failed");
            reply.put("msg", e.getMessage());
            return reply;
        }
    }

    /**
     * Método para listar artistas, albúns ou músicas (sem detalhe).
     * @param message Mensagem a processar
     * @return Resposta após processamento
     */
    public HashMap<String, String> showall(HashMap<String, String> message) {
        //exemplo --> type|show all;select|(artist,album,music)
        HashMap<String, String> reply = new HashMap<String, String>();
        try {
            st = c.prepareStatement("select id,title from public." + message.get("select") + "s;");
            rs = st.executeQuery();
            while (rs.next()) {
                reply.put(String.valueOf(rs.getInt(1)), rs.getString(2));
            }
            return reply;
        } catch (Exception e) {
            reply.put("type", "show all");
            reply.put("msg", e.getMessage());
            return reply;
        }
    }

    /**
     * Método para listar artistas, albúns ou músicas (com detalhe).
     * @param message Mensagem a processar
     * @return Resposta após processamento
     */
    public HashMap<String, String> showdetails(HashMap<String, String> message) {
        //exemplo --> type|show details;select|(artist,album,music);identifier|(1,5,19)
        HashMap<String, String> reply = new HashMap<String, String>();
        try {
            if (message.get("select").equals("artist")) {
                st = c.prepareStatement("select * from public.artists where id=" + message.get("identifier") + ";");
                rs = st.executeQuery();
                if (rs.next()) {
                    reply.put("details", String.valueOf(rs.getInt(1)) + " Name:" + rs.getString(2) + " Description:" + rs.getString(3));
                    return reply;
                }
                reply.put("type", "show details");
                reply.put("identifier", "Don't exists.");
                return reply;
            } else if (message.get("select").equals("music")) {
                st = c.prepareStatement("select id,title,compositor,duration,genre from public.musics where id=" + message.get("identifier") + ";");
                rs = st.executeQuery();
                if (rs.next()) {
                    reply.put("Music:" + String.valueOf(rs.getInt(1)), "Title:" + rs.getString(2) + " Compositor:" + rs.getString(3) + " Duration:" + rs.getString(4) + " Genre:" + rs.getString(5));
                    return reply;
                }
                reply.put("type", "show details");
                reply.put("identifier", "Don't exists.");
                return reply;
            } else {
                st = c.prepareStatement("select * from public.albums where id=" + message.get("identifier") + ";");
                rs = st.executeQuery();
                if (rs.next()) {
                    reply.put("details", String.valueOf(rs.getInt(1)) + " Title:" + rs.getString(2) + " Description:" + rs.getString(3) + " Rate:" + String.valueOf(rs.getDouble(4)));
                    st = c.prepareStatement("select id,title,compositor,duration,genre from public.musics where idalbum=" + message.get("identifier") + ";");
                    rs = st.executeQuery();
                    while (rs.next()) {
                        reply.put("Music:" + String.valueOf(rs.getInt(1)), "Title:" + rs.getString(2) + " Compositor:" + rs.getString(3) + " Duration:" + rs.getString(4) + " Genre:" + rs.getString(5));
                    }
                    st = c.prepareStatement("select id,text,rate from public.reviews where idalbum=" + message.get("identifier") + ";");
                    rs = st.executeQuery();
                    while (rs.next()) {
                        reply.put("Review:" + String.valueOf(rs.getInt(1)), "Rate:" + String.valueOf(rs.getDouble(3)) + " Text:" + rs.getString(2));
                    }
                    return reply;
                } else {
                    reply.put("type", "show details");
                    reply.put("identifier", "Don't exists.");
                    return reply;
                }
            }
        } catch (Exception e) {
            reply.put("type", "show details");
            reply.put("msg", e.getMessage());
            return reply;
        }
    }
    /**
     * Método para inserir uma review de um albúm.
     * @param message Mensagem a processar
     * @return Resposta após processamento
     */
    public HashMap<String, String> writereview(HashMap<String, String> message) {
        //exemplo --> type|write review;identifier|idalbum;rate|4.5;text|asdkjasdkasjkd
        HashMap<String, String> reply = new HashMap<String, String>();
        try {
            st = c.prepareStatement("insert into public.reviews (id,text,idalbum,rate) values (default,?,?,?)");
            st.setString(1, message.get("text"));
            st.setInt(2, Integer.parseInt(message.get("identifier")));
            st.setDouble(3, Double.parseDouble(message.get("rate")));
            st.executeUpdate();
            st = c.prepareStatement("update public.albums set rate=(select avg(public.reviews.rate) from public.albums,public.reviews where public.reviews.idalbum=" + message.get("identifier") + "and public.albums.id=" + message.get("identifier") + ");");
            st.executeUpdate();
            reply.put("type", "write review");
            reply.put("msg", "sucessful");
            return reply;
        } catch (Exception e) {
            reply.put("type", "write review");
            reply.put("msg", e.getMessage());
            return reply;
        }
    }
    /**
     * Método para procurar música por género, nome do albúm ou nome do artista.
     * @param message Mensagem a processar
     * @return Resposta após processamento
     */
    public HashMap<String, String> searchmusic(HashMap<String, String> message) {
        //exemplo --> type|search music;select|(artist,album,music);text|asdasd
        HashMap<String, String> reply = new HashMap<String, String>();
        try {
            if (message.get("select").equals("artist")) {
                st = c.prepareStatement("select id,title from public.musics where idartist=(select id from public.artists where title='" + message.get("text") + "');");
            } else if (message.get("select").equals("album")) {
                st = c.prepareStatement("select id,title from public.musics where idalbum=(select id from public.albums where title='" + message.get("text") + "');");
            } else {
                st = c.prepareStatement("select id,title from public.musics where genre='" + message.get("text") + "';");
            }
            rs = st.executeQuery();
            while (rs.next()) {
                reply.put(String.valueOf(rs.getInt(1)), rs.getString(2));
            }
            return reply;
        } catch (Exception e) {
            reply.put("type", "search music");
            reply.put("msg", e.getMessage());
            return reply;
        }
    }
    /**
     * Método para inserir artistas,albúns ou músicas.
     * @param message Mensagem a processar
     * @return Resposta após processamento
     */
    public HashMap<String, String> insert(HashMap<String, String> message) {
        //exemplo -->type|insert;select|(artist,album,music);key|value;identifier|2
        HashMap<String, String> reply = new HashMap<String, String>();
        try {
            if (checkPermission(message)) {
                if (message.get("select").equals("artist")) {
                    st = c.prepareStatement("insert into public.artists(id,title,description) values(default,?,?);");
                    st.setString(1, message.get("name"));
                    st.setString(2, message.get("description"));
                } else if (message.get("select").equals("album")) {
                    st = c.prepareStatement("insert into public.albums(id,title,description,rate) values(default,?,?,default);");
                    st.setString(1, message.get("title"));
                    st.setString(2, message.get("description"));
                } else { //music
                    st = c.prepareStatement("insert into public.musics (id,title,compositor,duration,genre,idalbum,idartist) values(default,?,?,?,?,?,?);");
                    st.setString(1, message.get("title"));
                    st.setString(2, message.get("compositor"));
                    st.setString(3, message.get("duration"));
                    st.setString(4, message.get("genre"));
                    st.setInt(5, Integer.parseInt(message.get("idalbum")));
                    st.setInt(6, Integer.parseInt(message.get("idartist")));
                }
                st.executeUpdate();
                reply.put("type", "insert");
                reply.put("select", message.get("select"));
                reply.put("msg", "sucessful");
                return reply;
            }
            reply.put("type", "insert");
            reply.put("select", message.get("select"));
            reply.put("msg", "Não possui privilégios de editor.");
            return reply;
        } catch (Exception e) {
            reply.put("type", "insert");
            reply.put("select", message.get("select"));
            reply.put("msg", e.getMessage());
            return reply;
        }
    }
    /**
     * Método para remover artistas,albúns ou músicas.
     * @param message Mensagem a processar
     * @return Resposta após processamento
     */
    public HashMap<String, String> remove(HashMap<String, String> message) {
        // exemplo --> type|remove;select|artist,album,music;identifier|1
        HashMap<String, String> reply = new HashMap<String, String>();
        try {
            if (checkPermission(message)) {
                if (message.get("select").equals("artist")) {
                    st = c.prepareStatement("delete from public.artists where id=" + message.get("id") + ";");
                } else if (message.get("select").equals("album")) {
                    st = c.prepareStatement("delete from public.albums where id=" + message.get("id") + ";");
                } else { //music
                    st = c.prepareStatement("delete from public.musics where id=" + message.get("id") + ";");
                }
                st.executeUpdate();
                reply.put("type", "remove");
                reply.put("select", message.get("select"));
                reply.put("msg", "sucessful");
                return reply;
            }
            reply.put("type", "remove");
            reply.put("select", message.get("select"));
            reply.put("msg", "Não possui privilégios de editor.");
            return reply;
        } catch (Exception e) {
            reply.put("type", "remove");
            reply.put("select", message.get("select"));
            reply.put("msg", e.getMessage());
            return reply;
        }
    }
    /**
     * Método para editar informação dos artistas,albúns ou músicas.
     * @param message Mensagem a processar
     * @return Resposta após processamento
     */
    public HashMap<String, String> edit(HashMap<String, String> message) {
        //exemplo --> type|edit;select|artist,album,music;identifier|2;key|description;value|bananas sao boas
        HashMap<String, String> reply = new HashMap<String, String>();
        try {
            if (checkPermission(message)) {
                if (message.get("key").equals("idalbum") || message.get("key").equals("idartist")) {
                    st = c.prepareStatement("update public." + message.get("select") + "s set " + message.get("key") + "=" + message.get("value") + " where id=" + message.get("id") + ";");
                } else {
                    st = c.prepareStatement("update public." + message.get("select") + "s set " + message.get("key") + "='" + message.get("value") + "' where id=" + message.get("id") + ";");
                }
                st.executeUpdate();
                if (message.get("key").equals("description") && message.get("select").equals("album")) {
                    st = c.prepareStatement("insert into public.history (iduser,idalbum) values(?,?);");
                    st.setInt(1, Integer.parseInt(message.get("identifier")));
                    st.setInt(2, Integer.parseInt(message.get("id")));
                    st.executeUpdate();
                }
                reply.put("type", "edit");
                reply.put("select", message.get("select"));
                reply.put("msg", "sucessful");
                return reply;
            }
            reply.put("type", "edit");
            reply.put("select", message.get("select"));
            reply.put("msg", "Não possui privilégios de editor.");
            return reply;
        } catch (Exception e) {
            reply.put("type", "remove");
            reply.put("select", message.get("select"));
            reply.put("msg", e.getMessage());
            return reply;
        }
    }
    /**
     * Método para atribuir privilégio de editor a um utilizador.
     * @param message Mensagem a processar
     * @return Resposta após processamento
     */
    public HashMap<String, String> promote(HashMap<String, String> message) {
        // type|promote;identifier|1;username|"Claudio"
        HashMap<String, String> reply = new HashMap<String, String>();
        try {
            st = c.prepareStatement("select id from public.users where name='" + message.get("username") + "';");
            rs = st.executeQuery();
            if (rs.next()) { //se a pessoa a que vai ser atribuida o privilegio existir
                reply.put("identifier", String.valueOf(rs.getInt(1)));
                if (checkPermission(reply)) { //se já tiver privilégio
                    reply.clear();
                    reply.put("type", "promote");
                    reply.put("username", message.get("username"));
                    reply.put("msg", "O utilizador já possui previlégio de editor.");
                    return reply;
                } else if (checkPermission(message)) { //verificar se quem vai atribuir possui privilégio
                    st = c.prepareStatement("update public.users set permission=true where id=" + reply.get("identifier") + ";");
                    st.executeUpdate();
                    reply.put("type", "promote");
                    reply.put("username", message.get("username"));
                    reply.put("msg", "successful");
                    return reply;
                }
                reply.clear();
                reply.put("type", "promote");
                reply.put("username", message.get("username"));
                reply.put("msg", "Não possui permissões para atribuir privilégio.");
                return reply;
            }
            reply.put("type", "promote");
            reply.put("username", message.get("username"));
            reply.put("msg", "O utilizador ao qual pretende atribuir privilégio não existe.");
            return reply;
        } catch (Exception e) {
            reply.clear();
            reply.put("type", "promote");
            reply.put("username", message.get("username"));
            reply.put("msg", e.getMessage());
            return reply;
        }
    }

    /**
     * Método para verificar se um dado utilizador possui privilégio de editor.
     * @param message Mensagem a processar
     * @return Resposta após processamento
     * @throws Exception É tratada nos métodos que usam este método.
     */
    public boolean checkPermission(HashMap<String, String> message) throws Exception {
        st = c.prepareStatement("select permission from public.users where id=" + message.get("identifier") + ";");
        rs = st.executeQuery();
        if (rs.next() && rs.getBoolean(1)) {
            return true;
        }
        return false;
    }
    /**
     * Método para guardar notificações quando o utilizador não se encontra online.
     * @param message Mensagem a processar
     * @return Resposta após processamento
     */
    public HashMap<String, String> insertnotification(HashMap<String, String> message) {
        // --> type|insert notification;msg|akjsfnfas;identifier|2
        HashMap<String, String> reply = new HashMap<String, String>();
        try {
            st = c.prepareStatement("insert into public.notifications (iduser,msg) values(?,?);");
            st.setInt(1, Integer.parseInt(message.get("identifier")));
            st.setString(2, message.get("msg"));
            st.executeUpdate();
            reply.put("type", "insert notification");
            reply.put("msg", "successful");
            return reply;
        } catch (Exception e) {
            reply.put("type", "insert notification");
            reply.put("msg", e.getMessage());
            return reply;
        }
    }
    /**
     * Método para devolver todas as notificações de utilizador que acabe de dar login.
     * @param message Mensagem a processar
     * @return Resposta após processamento
     */
    public HashMap<String, String> removenotification(HashMap<String, String> message) {
        // --> type|remove notification;identifier|3
        HashMap<String, String> reply = new HashMap<String, String>();
        try {
            st = c.prepareStatement("select msg from public.notifications where iduser=" + message.get("identifier") + ";");
            rs = st.executeQuery();
            String s = "Notification: ";
            while (rs.next()) {
                s = s + rs.getString(1) + "\n";
            }
            st = c.prepareStatement("delete from public.notifications where iduser=" + message.get("identifier") + ";");
            st.executeUpdate();
            reply.put("type", "remove notification");
            reply.put("msg", s);
            return reply;
        } catch (Exception e) {
            reply.put("type", "remove notification");
            reply.put("msg", e.getMessage());
            return reply;
        }
    }
    /**
     * Método para devolver todos os utilizadores que já alteraram a descrição de um certo albúm.
     * @param message Mensagem a processar
     * @return Resposta após processamento
     */
    public HashMap<String, String> gethistory(HashMap<String, String> message) {
        // --> type|get history;idalbum|2
        HashMap<String, String> reply = new HashMap<String, String>();
        try {
            st = c.prepareStatement("select iduser from public.history where idalbum=" + message.get("idalbum") + ";");
            rs = st.executeQuery();
            while (rs.next()) {
                reply.put(String.valueOf(rs.getInt(1)), String.valueOf(rs.getInt(1)));
            }
            return reply;
        } catch (Exception e) {
            reply.put("type", "get history");
            reply.put("msg", e.getMessage());
            return reply;
        }
    }
    /**
     * Método para dar logout do utilizador da base de dados.
     * @param message Mensagem a processar
     * @return Resposta após processamento
     */
    public HashMap<String, String> logout(HashMap<String, String> message) {
        // --> type|log out;identifier|2
        HashMap<String, String> reply = new HashMap<String, String>();
        try {
            st = c.prepareStatement("update public.users set status=false where id=" + message.get("identifier") + ";");
            st.executeUpdate();
            reply.put("type", "log out");
            reply.put("msg", "successful");
            return reply;
        } catch (Exception e) {
            reply.put("type", "log out");
            reply.put("msg", e.getMessage());
            return reply;
        }
    }
    /**
     * Método que devolve o porto para a ligação de tcp e, no caso de download a base de dados onde a música de encontra.
     * @param message Mensagem a processar
     * @return Resposta após processamento
     */
    public HashMap<String, String> getport(HashMap<String, String> message) {
        HashMap<String, String> replyM = new HashMap<>();
        replyM.putAll(message);
        replyM.put("port", "6000");
        try {
            if (message.get("status").equals("download")) {
                st = c.prepareStatement("select title,iddb,ext from public.musics, public.files where public.files.idmusic=" + message.get("idmusic") + " and public.musics.id=" + message.get("idmusic") + ";");
                rs = st.executeQuery();
                if(rs.next()){
                    replyM.put("title",rs.getString(1)+"."+rs.getString(3));
                    replyM.put("iddb", rs.getString(2));
                }
            }
            return replyM;
        }catch (Exception e){
            replyM.put("msg",e.getMessage());
            return replyM;
        }
    }

    /**
     * Método para inserir um ficheiro de música na base de dados.
     * @param data Dados binários da música.
     * @param idmusic Id correspondente à música existente na tabela de músicas.
     * @param iduser Utilizador que carregou a música.
     * @param iddb Identificar da base de dados.
     * @param ext Extensão da música
     * @return true se não apanhar nenhum excepção.
     */
    public boolean upload(byte[] data, int idmusic, int iduser,String iddb,String ext) {
        try {
            byte[] buffer = null;
            st = c.prepareStatement("insert into public.files (idmusic,datam,iduser,iddb,ext) values(?,?,?,?,?);");
            st.setInt(1, idmusic);
            st.setBytes(2, data);
            st.setInt(3, iduser);
            st.setString(4, iddb);
            st.setString(5, ext);
            st.executeUpdate();
            st = c.prepareStatement("insert into public.share (idmusic,iduser) values(?,?);");
            st.setInt(1, idmusic);
            st.setInt(2, iduser);
            st.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Método para ir buscar um ficheiro de música à base de dados.
     * @param idmusic Identificar da música no ficheiro.
     * @param iduser Utilizador que pretende efetuar o download.
     * @return Os dados da música o ficheiro tiver sido partilhado com o utilizador ou null se não tiver, ou não existir.
     */
    public byte[] download(int idmusic, int iduser) {
        try {
            byte[] buffer = null;
            st = c.prepareStatement("select exists(select * from public.share where idmusic=" + String.valueOf(idmusic) + " and iduser=" + String.valueOf(iduser) +");");
            rs = st.executeQuery();
            if (rs.next() && rs.getBoolean(1)) {
                st = c.prepareStatement("select datam from public.files where idmusic=" + String.valueOf(idmusic) + ";");
                rs = st.executeQuery();
                while (rs.next()) {
                    buffer = rs.getBytes(1);
                }
                return buffer;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Método para partilhar o ficheiro de música com vários utilizadores.
     * @param message Mensagem a processar
     * @return Resposta após processamento
     */
    public HashMap<String, String> share(HashMap<String, String> message) {
        HashMap<String, String> reply = new HashMap<String, String>();
        try {
            st = c.prepareStatement("select exists (select * from public.files where idmusic=" + message.get("idmusic") + " and iduser=" + message.get("identifier") + ");");
            rs = st.executeQuery();
            if (rs.next() && rs.getBoolean(1)) {
                st = c.prepareStatement("insert into public.share (idmusic,iduser) values(?,?);");
                st.setInt(1, Integer.parseInt(message.get("idmusic")));
                st.setInt(2, Integer.parseInt(message.get("iduser")));
                st.executeUpdate();
                reply.put("type", "share");
                reply.put("msg", "successful");
            } else {
                reply.put("type", "share");
                reply.put("msg", "You're trying to share a music who don't belong to you");
            }
            return reply;
        } catch (Exception e) {
            reply.put("type", "share");
            reply.put("msg", e.getMessage());
            return reply;
        }
    }
}