import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.*;
import java.sql.ResultSet;
import java.util.HashMap;

public class Database {
    private Connection c;
    private PreparedStatement st;
    private ResultSet rs;
    private Statement cs;

    public Database(String num, String pw) {
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
                cs.executeUpdate("CREATE TABLE public.utilizadores ( " +
                        "nome	 VARCHAR(50) NOT NULL ," +
                        " password varchar(50) NOT NULL," +
                        " permissao	 BOOL NOT NULL DEFAULT false," +
                        " PRIMARY KEY(nome)," +
                        " CONSTRAINT nome_tamanho CHECK ((char_length(nome))>0)," +
                        " CONSTRAINT password_tamanho CHECK ((char_length(password))>0)" +
                        ");");

                cs.executeUpdate("CREATE TABLE public.artistas ( idartist BIGSERIAL," +
                        " nome	 VARCHAR(50) NOT NULL," +
                        " historia VARCHAR(300)," +
                        " PRIMARY KEY(idartist)," +
                        " CONSTRAINT nome_tamanho CHECK ((char_length(nome))>0) );");

                cs.executeUpdate("CREATE TABLE public.concertos ( lugar		 VARCHAR(50) NOT NULL," +
                        " data		 DATE NOT NULL," +
                        " artistas_idartist BIGINT NOT NULL," +
                        " PRIMARY KEY(lugar,data,artistas_idartist)," +
                        " CONSTRAINT concertos_fk1 FOREIGN KEY (artistas_idartist) REFERENCES artistas(idartist)," +
                        " CONSTRAINT lugar_tamanho CHECK ((char_length(lugar))>0) );");

                cs.executeUpdate("CREATE TABLE public.musicos ( artistas_idartist BIGINT," +
                        " PRIMARY KEY(artistas_idartist)," +
                        " CONSTRAINT musicos_fk1 FOREIGN KEY (artistas_idartist) REFERENCES artistas(idartist) );");

                cs.executeUpdate("CREATE TABLE public.compositores ( musicos_artistas_idartist BIGINT," +
                        " PRIMARY KEY(musicos_artistas_idartist)," +
                        " CONSTRAINT compositores_fk1 FOREIGN KEY (musicos_artistas_idartist) REFERENCES musicos(artistas_idartist) );");

                cs.executeUpdate("CREATE TABLE public.bandas ( inicio		 DATE NOT NULL," +
                        " fim		 DATE DEFAULT NULL," +
                        " artistas_idartist BIGINT," +
                        " PRIMARY KEY(artistas_idartist)," +
                        " CONSTRAINT bandas_fk1 FOREIGN KEY (artistas_idartist) REFERENCES artistas(idartist)," +
                        " CONSTRAINT data_inicio_fim CHECK ((fim>inicio) or (fim is null))," +
                        " CONSTRAINT data_inicio CHECK (inicio<=current_date)," +
                        " CONSTRAINT data_fim CHECK ((fim<=current_date) or (fim is null)) );");

                cs.executeUpdate("CREATE TABLE public.bandas_musicos ( bandas_artistas_idartist	 BIGINT," +
                        " musicos_artistas_idartist BIGINT," +
                        " PRIMARY KEY(bandas_artistas_idartist,musicos_artistas_idartist)," +
                        " CONSTRAINT bandas_musicos_fk1 FOREIGN KEY (bandas_artistas_idartist) REFERENCES bandas(artistas_idartist)," +
                        " CONSTRAINT bandas_musicos_fk2 FOREIGN KEY (musicos_artistas_idartist) REFERENCES musicos(artistas_idartist) );");

                cs.executeUpdate("CREATE TABLE public.musicas ( idmusicas				 BIGSERIAL," +
                        " letra					 VARCHAR(600) NOT NULL," +
                        " nome					 VARCHAR(50) NOT NULL," +
                        " duracao                time NOT NULL," +
                        " compositores_musicos_artistas_idartist BIGINT NOT NULL," +
                        " PRIMARY KEY(idmusicas)," +
                        " CONSTRAINT musicas_fk1 FOREIGN KEY (compositores_musicos_artistas_idartist) REFERENCES compositores(musicos_artistas_idartist)," +
                        " CONSTRAINT letra_tamanho CHECK ((char_length(letra))>0)," +
                        " CONSTRAINT nome_tamanho CHECK ((char_length(nome))>0)," +
                        " CONSTRAINT duracao_tempo CHECK ((extract(hour from duracao)>0) or (extract(minute from duracao)>0) or (extract(second from duracao)>0)) );");

                cs.executeUpdate("CREATE TABLE public.editoras ( nome VARCHAR(50)," +
                        " PRIMARY KEY(nome)," +
                        " CONSTRAINT nome_tamanho CHECK ((char_length(nome))>0));");

                cs.executeUpdate("CREATE TABLE public.albuns ( idalbuns		 BIGSERIAL," +
                        " nome		 VARCHAR(50) NOT NULL," +
                        " genero		 VARCHAR(20) NOT NULL," +
                        " datalancamento	 DATE NOT NULL," +
                        " historia		 VARCHAR(300)," +
                        " editoras_nome	 VARCHAR(50) NOT NULL," +
                        " artistas_idartist BIGINT NOT NULL," +
                        " PRIMARY KEY(idalbuns)," +
                        " CONSTRAINT albuns_fk1 FOREIGN KEY (editoras_nome) REFERENCES editoras(nome)," +
                        " CONSTRAINT albuns_fk2 FOREIGN KEY (artistas_idartist) REFERENCES artistas(idartist)," +
                        " CONSTRAINT nome_tamanho CHECK ((char_length(nome))>0)," +
                        " CONSTRAINT data_lancamento CHECK (datalancamento<=current_date)," +
                        " CONSTRAINT genero_tamanho CHECK ((char_length(genero))>0) );");

                cs.executeUpdate("CREATE TABLE public.albuns_musicas ( albuns_idalbuns	 BIGINT," +
                        " musicas_idmusicas BIGINT," +
                        " PRIMARY KEY(albuns_idalbuns,musicas_idmusicas)," +
                        " CONSTRAINT albuns_musicas_fk1 FOREIGN KEY (albuns_idalbuns) REFERENCES albuns(idalbuns), " +
                        " CONSTRAINT albuns_musicas_fk2 FOREIGN KEY (musicas_idmusicas) REFERENCES musicas(idmusicas) );");

                cs.executeUpdate("CREATE TABLE public.criticas ( idcritica		 BIGSERIAL," +
                        " rate			 DOUBLE PRECISION NOT NULL," +
                        " texto			 VARCHAR(300) NOT NULL," +
                        " utilizadores_nome VARCHAR(50) NOT NULL," +
                        " albuns_idalbuns		 BIGINT NOT NULL," +
                        " PRIMARY KEY(idcritica,albuns_idalbuns,utilizadores_nome)," +
                        " CONSTRAINT criticas_fk1 FOREIGN KEY (utilizadores_nome) REFERENCES utilizadores(nome)," +
                        " CONSTRAINT criticas_fk2 FOREIGN KEY (albuns_idalbuns) REFERENCES albuns(idalbuns)," +
                        " CONSTRAINT texto_tamanho CHECK (char_length(texto) > 0)," +
                        " CONSTRAINT rate_entre CHECK ((rate>=0.0) and (rate<=10.0)) );");

                cs.executeUpdate("CREATE TABLE public.playlists ( nome			 VARCHAR(50) NOT NULL," +
                        " privada			 BOOL NOT NULL DEFAULT false," +
                        " utilizadores_nome VARCHAR(50)," +
                        " PRIMARY KEY(nome,utilizadores_nome)," +
                        " CONSTRAINT playlists_fk1 FOREIGN KEY (utilizadores_nome) REFERENCES utilizadores(nome)," +
                        " CONSTRAINT nome_tamanho CHECK ((char_length(nome))>0) );");

                cs.executeUpdate("CREATE TABLE public.playlists_musicas ( playlists_nome			 VARCHAR(50)," +
                        " playlists_utilizadores_nome VARCHAR(50)," +
                        " musicas_idmusicas			 BIGINT," +
                        " PRIMARY KEY(playlists_nome,playlists_utilizadores_nome,musicas_idmusicas)," +
                        " CONSTRAINT playlists_musicas_fk1 FOREIGN KEY (playlists_nome,playlists_utilizadores_nome) REFERENCES playlists(nome,utilizadores_nome) on update cascade," +
                        " CONSTRAINT playlists_musicas_fk2 FOREIGN KEY (musicas_idmusicas) REFERENCES musicas(idmusicas) );");
            } catch (Exception e1) {
                System.out.println(e.getMessage());
            }
        }
    }

    public HashMap<String, String> process(HashMap<String, String> message) throws SQLException {
        HashMap<String, String> reply = new HashMap<String, String>();
        switch (message.get("type")) {
            case "regist":
                reply = regist(message);
                break;
            case "login":
                reply = login(message);
                break;
            case "show":
                reply = show(message);
                break;
            case "show info":
                reply = showinfo(message);
                break;
            case "create":
                reply = create(message);
                break;
            case "write review":
                reply = writereview(message);
                break;
            case "add":
                reply = add(message);
                break;
            case "search":
                reply = search(message);
                break;
            case "edit":
                reply = edit(message);
                break;
            case "remove":
                reply = remove(message);
                break;
            default:
                System.out.println("Error on process function");
                break;
        }
        for (HashMap.Entry<String, String> entry : reply.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        return reply;
    }

    public HashMap<String, String> regist(HashMap<String, String> message) {
        HashMap<String, String> reply = new HashMap<>();
        try {
            st = c.prepareStatement("select exists (select nome from public.utilizadores where nome='" + message.get("username") + "');");
            rs = st.executeQuery();
            if (rs.next() && rs.getBoolean(1)) {
                reply.put("msg", "O username já se encontra em uso.");
                return reply;

            }
            st = c.prepareStatement("insert into public.utilizadores(nome,password,permissao) values(?,?,?);");
            st.setString(1, message.get("username"));
            st.setString(2, message.get("password"));
            System.out.println(Boolean.getBoolean(message.get("permissao")));
            st.setBoolean(3, Boolean.parseBoolean(message.get("permissao")));
            st.executeUpdate();
            reply.put("msg", "Registado com sucesso.");
        } catch (SQLException e) {
            if (e.getMessage().contains("violates check constraint \"nome_tamanho\""))
                reply.put("msg", "Introduza um username válido");
            else if (e.getMessage().contains("violates check constraint \"password_tamanho\""))
                reply.put("msg", "Introduza uma password válida");
            else
                reply.put("msg", e.getMessage());
        }
        return reply;
    }

    public HashMap<String, String> login(HashMap<String, String> message) {
        HashMap<String, String> reply = new HashMap<>();
        try {
            st = c.prepareStatement("select exists (select nome from public.utilizadores where nome='" + message.get("username") + "');");
            rs = st.executeQuery();
            if (rs.next() && rs.getBoolean(1)) {
                st = c.prepareStatement("select permissao from public.utilizadores where nome='" + message.get("username") + "' and password='" + message.get("password") + "';");
                rs = st.executeQuery();
                if (rs.next()) {
                    reply.put("msg", "Login efetuado com sucesso.");
                    reply.put("permissao", String.valueOf(rs.getBoolean(1)));
                    return reply;
                }
                reply.put("msg", "Password incorreta");
                return reply;
            } else {
                reply.put("msg", "Utilizador não registado.");
                return reply;
            }
        } catch (SQLException e) {
            reply.put("msg", e.getMessage());
        }
        return reply;
    }

    public HashMap<String, String> show(HashMap<String, String> message) {
        HashMap<String, String> reply = new HashMap<>();
        int num = 1;
        try {
            switch (message.get("select")) {
                case "musicos":
                    st = c.prepareStatement("select artistas_idartist,nome from public.musicos, public.artistas where idartist=artistas_idartist order by 2;");
                    rs = st.executeQuery();
                    while (rs.next()) {
                        reply.put(String.valueOf(num), rs.getString(2));
                        reply.put("ID" + String.valueOf(num), String.valueOf(rs.getInt(1)));
                        num++;
                    }
                    break;
                case "compositores":
                    st = c.prepareStatement("select nome,idartist from public.compositores,public.artistas where musicos_artistas_idartist=idartist order by 1;");
                    rs = st.executeQuery();
                    while (rs.next()) {
                        reply.put(String.valueOf(num), rs.getString(1));
                        reply.put("ID" + String.valueOf(num), String.valueOf(rs.getInt(2)));
                        num++;
                    }
                    break;
                case "bandas":
                    st = c.prepareStatement("select artistas_idartist,nome from public.bandas, public.artistas where idartist=artistas_idartist order by 2;");
                    rs = st.executeQuery();
                    while (rs.next()) {
                        reply.put(String.valueOf(num), rs.getString(2));
                        reply.put("ID" + String.valueOf(num), String.valueOf(rs.getInt(1)));
                        num++;
                    }
                    break;
                case "albuns":
                    st = c.prepareStatement("select idalbuns, nome from public.albuns order by 2;");
                    rs = st.executeQuery();
                    while (rs.next()) {
                        reply.put(String.valueOf(num), rs.getString(2));
                        reply.put("ID" + String.valueOf(num), String.valueOf(rs.getInt(1)));
                        num++;
                    }
                    break;
                case "musicas":
                    st = c.prepareStatement("select idmusicas,nome from public.musicas order by 2;");
                    rs = st.executeQuery();
                    while (rs.next()) {
                        reply.put(String.valueOf(num), rs.getString(2));
                        reply.put("ID" + String.valueOf(num), String.valueOf(rs.getInt(1)));
                        num++;
                    }
                    break;
                case "concertos":
                    st = c.prepareStatement("select data,lugar,nome from public.concertos, public.artistas where artistas_idartist = idartist order by 1;");
                    rs = st.executeQuery();
                    while (rs.next()) {
                        reply.put(String.valueOf(num), String.valueOf(rs.getDate(1)) + " - " + rs.getString(2) + " - " + rs.getString(3));
                        num++;
                    }
                    break;
                case "my playlists":
                    st = c.prepareStatement("select playlists.nome,privada from public.playlists, public.utilizadores where utilizadores_nome=utilizadores.nome and utilizadores.nome='" + message.get("username") + "' order by 1;");
                    rs = st.executeQuery();
                    while (rs.next()) {
                        if (rs.getBoolean(2)) {
                            reply.put(String.valueOf(num), rs.getString(1) + " - Privada");
                        } else {
                            reply.put(String.valueOf(num), rs.getString(1) + " - Pública");
                        }
                        reply.put("ID" + String.valueOf(num), rs.getString(1));
                        num++;
                    }
                    break;
                case "all playlists":
                    st = c.prepareStatement("select playlists.nome,utilizadores_nome from public.playlists, public.utilizadores where utilizadores_nome=utilizadores.nome and privada=false order by 1;");
                    rs = st.executeQuery();
                    while (rs.next()) {
                        reply.put(String.valueOf(num), rs.getString(1) + " - By: " + rs.getString(2));
                        reply.put("ID" + String.valueOf(num), rs.getString(1));
                        reply.put("IDNOME" + String.valueOf(num), rs.getString(2));
                        num++;
                    }
                    break;
                case "playlist musics":
                    st = c.prepareStatement("select idmusicas,nome from public.musicas, public.playlists_musicas where idmusicas=musicas_idmusicas and playlists_nome='" + message.get("playlistName") + "' and playlists_utilizadores_nome='" + message.get("playlistOwner") + "' order by 2;");
                    rs = st.executeQuery();
                    while (rs.next()) {
                        reply.put(String.valueOf(num), rs.getString(2));
                        reply.put("ID" + String.valueOf(num), String.valueOf(rs.getInt(1)));
                        num++;
                    }
                    break;
                case "my reviews":
                    st = c.prepareStatement("select nome,rate,texto from public.criticas, public.albuns where albuns_idalbuns=idalbuns and utilizadores_nome='" + message.get("username") + "' order by 1");
                    rs = st.executeQuery();
                    while (rs.next()) {
                        reply.put(String.valueOf(num), rs.getString(1) + " - " + String.valueOf(rs.getDouble(2)) + "\n" + rs.getString(3));
                        num++;
                    }
                    break;
                case "reviews album":
                    st = c.prepareStatement("select nome,rate,texto from public.criticas, public.albuns where albuns_idalbuns=idalbuns and idalbuns=" + String.valueOf(message.get("id_album")) + " order by idcritica;");
                    rs = st.executeQuery();
                    while (rs.next()) {
                        reply.put(String.valueOf(num), rs.getString(1) + " - " + String.valueOf(rs.getDouble(2)) + "\n" + rs.getString(3));
                        num++;
                    }
                    break;
                case "artistas":
                    st = c.prepareStatement("select nome,idartist from public.artistas,public.musicos where idartist=musicos.artistas_idartist union select nome,idartist from public.artistas,public.bandas where idartist=bandas.artistas_idartist order by 1;");
                    rs = st.executeQuery();
                    while (rs.next()) {
                        reply.put(String.valueOf(num), rs.getString(1));
                        reply.put("ID" + String.valueOf(num), String.valueOf(rs.getInt(2)));
                        num++;
                    }
                    break;
                case "editoras":
                    st = c.prepareStatement("select nome from public.editoras order by 1;");
                    rs = st.executeQuery();
                    while (rs.next()) {
                        reply.put(String.valueOf(num), rs.getString(1));
                        reply.put("ID" + String.valueOf(num), rs.getString(1));
                        num++;
                    }
                    break;
                case "musicos bandas":
                    st = c.prepareStatement("select nome,musicos_artistas_idartist from public.artistas, public.bandas_musicos where musicos_artistas_idartist=idartist and bandas_artistas_idartist=" + message.get("id") + " order by 1;");
                    rs = st.executeQuery();
                    while (rs.next()) {
                        reply.put(String.valueOf(num), rs.getString(1));
                        reply.put("ID" + String.valueOf(num), String.valueOf(rs.getInt(2)));
                        num++;
                    }
                    break;
            }
        } catch (SQLException e) {
            reply.put("msg", e.getMessage());
        }
        return reply;
    }

    public HashMap<String, String> showinfo(HashMap<String, String> message) {
        HashMap<String, String> reply = new HashMap<>();
        int num = 1;
        try {
            switch (message.get("select")) {
                case "musicos":
                    st = c.prepareStatement("select nome,historia from public.artistas where idartist=" + message.get("id") + ";");
                    rs = st.executeQuery();
                    rs.next();
                    reply.put(String.valueOf(num), rs.getString(1) + "\nHistória: " + rs.getString(2));
                    num++;
                    st = c.prepareStatement("select albuns.nome, genero from public.albuns, public.artistas where artistas_idartist=" + message.get("id") + " and artistas_idartist=idartist order by datalancamento;");
                    rs = st.executeQuery();
                    reply.put(String.valueOf(num), "Albúns: ");
                    num++;
                    while (rs.next()) {
                        reply.put(String.valueOf(num), rs.getString(1) + " - " + rs.getString(2));
                        num++;
                    }
                    break;
                case "bandas":
                    st = c.prepareStatement("select nome,historia,inicio,fim from public.artistas, public.bandas where artistas_idartist=idartist and idartist=" + message.get("id") + ";");
                    rs = st.executeQuery();
                    rs.next();
                    reply.put(String.valueOf(num), rs.getString(1) + "\nInicio: " + String.valueOf(rs.getDate(3)) + "\nFim: " + String.valueOf(rs.getDate(4)) + "\nHistória: " + rs.getString(2));
                    num++;
                    reply.put(String.valueOf(num), "Músicos: ");
                    num++;
                    st = c.prepareStatement("select nome from public.artistas, public.bandas_musicos where musicos_artistas_idartist=idartist and bandas_artistas_idartist=" + message.get("id") + " order by 1;");
                    rs = st.executeQuery();
                    while (rs.next()) {
                        reply.put(String.valueOf(num), rs.getString(1));
                        num++;
                    }
                    st = c.prepareStatement("select albuns.nome, genero from public.albuns, public.artistas where artistas_idartist=" + message.get("id") + " and artistas_idartist=idartist order by datalancamento;");
                    rs = st.executeQuery();
                    reply.put(String.valueOf(num), "Albúns: ");
                    num++;
                    while (rs.next()) {
                        reply.put(String.valueOf(num), rs.getString(1) + " - " + rs.getString(2));
                        num++;
                    }
                    break;
                case "musicas":
                    st = c.prepareStatement("select musicas.nome, letra, duracao, artistas.nome from public.musicas, public.artistas where idmusicas=" + message.get("id") + " and compositores_musicos_artistas_idartist=idartist;");
                    rs = st.executeQuery();
                    rs.next();
                    reply.put(String.valueOf(num), rs.getString(1) + "\nDuracão: " + rs.getTime(3) + "\nCompositor: " + rs.getString(4) + "\nLetra: " + rs.getString(2));
                    num++;
                    st = c.prepareStatement("select albuns.nome, artistas.nome, genero from public.albuns, public.albuns_musicas, public.artistas where musicas_idmusicas=" + message.get("id") + " and albuns_idalbuns=idalbuns and artistas_idartist = idartist order by datalancamento;");
                    rs = st.executeQuery();
                    reply.put(String.valueOf(num), "Albúns: ");
                    num++;
                    while (rs.next()) {
                        reply.put(String.valueOf(num), rs.getString(1) + " - " + rs.getString(2) + " - " + rs.getString(3));
                        num++;
                    }
                    break;
                case "albuns":
                    st = c.prepareStatement("select albuns.nome,artistas.nome,genero,datalancamento,albuns.historia,editoras_nome from public.albuns, public.artistas where idalbuns=" + message.get("id") + " and artistas_idartist=idartist;");
                    rs = st.executeQuery();
                    rs.next();
                    reply.put(String.valueOf(num), rs.getString(1) + "\nArtista: " + rs.getString(2) + "\nGénero: " + rs.getString(3) + "\nData de lançamento: " + String.valueOf(rs.getDate(4)) + "\nEditora: " + rs.getString(6) + "\nHistória: " + rs.getString(5));
                    num++;
                    st = c.prepareStatement("select avg(rate) from public.criticas where albuns_idalbuns=" + message.get("id") + ";");
                    rs = st.executeQuery();
                    rs.next();
                    reply.put(String.valueOf(num), "Rate: " + String.valueOf(rs.getDouble(1)));
                    num++;
                    st = c.prepareStatement("select musicas.nome, artistas.nome from public.albuns_musicas, public.musicas, public.artistas where albuns_idalbuns=" + message.get("id") + " and musicas_idmusicas=idmusicas and compositores_musicos_artistas_idartist=idartist order by 1;");
                    rs = st.executeQuery();
                    reply.put(String.valueOf(num), "Músicas: ");
                    num++;
                    while (rs.next()) {
                        reply.put(String.valueOf(num), rs.getString(1) + " - Compositor: " + rs.getString(2));
                        num++;
                    }
                    break;
            }
        } catch (SQLException e) {
            reply.put("msg", e.getMessage());
        }
        return reply;
    }

    public HashMap<String, String> create(HashMap<String, String> message) throws SQLException {
        HashMap<String, String> reply = new HashMap<>();
        switch (message.get("select")) {
            case "editora":
                try {
                    st = c.prepareStatement("insert into public.editoras (nome) values(?);");
                    st.setString(1, message.get("nome"));
                    st.executeUpdate();
                    reply.put("msg", "Inserido com sucesso.");
                } catch (SQLException e) {
                    if (e.getMessage().contains("violates unique constraint \"editoras_pkey\""))
                        reply.put("msg", "A editora que pretende adicionar já existe.");
                    else if (e.getMessage().contains("violates check constraint \"nome_tamanho\""))
                        reply.put("msg", "Introduza uma editora válida.");
                    else {
                        reply.put("msg", e.getMessage());
                    }
                }
                break;
            case "musico":
                try {
                    st = c.prepareStatement("insert into public.artistas (idartist,nome,historia) values (default,?,?);");
                    st.setString(1, message.get("nome"));
                    st.setString(2, message.get("historia"));
                    st.executeUpdate();
                    st = c.prepareStatement("insert into public.musicos (select max(idartist) from public.artistas);");
                    st.executeUpdate();
                    reply.put("msg", "Inserido com sucesso.");
                } catch (SQLException e) {
                    if (e.getMessage().contains("violates check constraint \"nome_tamanho\""))
                        reply.put("msg", "Introduza um nome válido.");
                    else {
                        reply.put("msg", e.getMessage());
                    }
                }
                break;
            case "banda":
                Savepoint save1 = null;
                try {
                    c.setAutoCommit(false);
                    save1 = c.setSavepoint();
                    st = c.prepareStatement("insert into public.artistas (idartist,nome,historia) values (default,?,?);");
                    st.setString(1, message.get("nome"));
                    st.setString(2, message.get("historia"));
                    st.executeUpdate();
                    st = c.prepareStatement("insert into public.bandas (inicio,fim,artistas_idartist) values (?,?,(select max(idartist) from public.artistas));");
                    st.setDate(1, Date.valueOf(message.get("inicio")));
                    if (message.get("fim").equals("null"))
                        st.setDate(2, null);
                    else
                        st.setDate(2, Date.valueOf(message.get("fim")));
                    st.executeUpdate();
                    String[] ids = message.get("id_musicos").split("-");
                    for (int i = 0; i < ids.length; i++) {
                        st = c.prepareStatement("insert into public.bandas_musicos (bandas_artistas_idartist,musicos_artistas_idartist) values ((select max(artistas_idartist) from public.bandas),?);");
                        st.setInt(1, Integer.parseInt(ids[i]));
                        st.executeUpdate();
                    }
                    c.commit();
                    reply.put("msg", "Inserido com sucesso.");
                } catch (SQLException e) {
                    if (e.getMessage().contains("violates check constraint \"nome_tamanho\""))
                        reply.put("msg", "Introduza um nome válido.");
                    else if (e.getMessage().contains("violates check constraint \"data_inicio_fim\""))
                        reply.put("msg", "Introduza um valor de fim superior ao de inicio de data.");
                    else if (e.getMessage().contains("violates check constraint \"data_inicio\""))
                        reply.put("msg", "Introduza uma data de inicio válida");
                    else if (e.getMessage().contains("violates check constraint \"data_fim\""))
                        reply.put("msg", "Introduza uma data de fim válida.");
                    else if (e.getMessage().contains("violates unique constraint \"bandas_musicos_pkey\""))
                        reply.put("msg", "Não pode inserir dois músicos iguais na mesma banda.");
                    else {
                        reply.put("msg", e.getMessage());
                    }
                    c.rollback(save1);
                } finally {
                    c.setAutoCommit(true);
                }
                break;
            case "concerto":
                try {
                    st = c.prepareStatement("insert into public.concertos (lugar,data,artistas_idartist) values (?,?,?);");
                    st.setString(1, message.get("lugar"));
                    st.setDate(2, Date.valueOf(message.get("data")));
                    st.setInt(3, Integer.parseInt(message.get("id_artist")));
                    st.executeUpdate();
                    reply.put("msg", "Inserido com sucesso.");
                } catch (SQLException e) {
                    if (e.getMessage().contains("violates check constraint \"lugar_tamanho\""))
                        reply.put("msg", "Introduza um lugar válido.");
                    else
                        reply.put("msg", e.getMessage());
                }
                break;
            case "compositor":
                try {
                    st = c.prepareStatement("insert into public.compositores (musicos_artistas_idartist) values (?);");
                    st.setInt(1, Integer.parseInt(message.get("id_artist")));
                    st.executeUpdate();
                    reply.put("1", "Inserido com sucesso.");
                } catch (SQLException e) {
                    if (e.getMessage().contains("violates unique constraint \"compositores_pkey\""))
                        reply.put("1", "O músico já é compositor.");
                    else
                        reply.put("1", e.getMessage());
                }
                break;
            case "playlist":
                Savepoint save3 = null;
                try {
                    c.setAutoCommit(false);
                    save3 = c.setSavepoint();
                    st = c.prepareStatement("insert into public.playlists (nome,privada,utilizadores_nome) values (?,?,?);");
                    st.setString(1, message.get("nome"));
                    st.setBoolean(2, Boolean.parseBoolean(message.get("permissao")));
                    st.setString(3, message.get("username"));
                    st.executeUpdate();
                    String[] ids = message.get("id_musics").split("-");
                    for (int i = 0; i < ids.length; i++) {
                        st = c.prepareStatement("insert into public.playlists_musicas (playlists_nome,playlists_utilizadores_nome,musicas_idmusicas) values (?,?,?);");
                        st.setString(1, message.get("nome"));
                        st.setString(2, message.get("username"));
                        st.setInt(3, Integer.parseInt(ids[i]));
                        st.executeUpdate();
                    }
                    c.commit();
                    reply.put("msg", "Inserida com sucesso.");
                } catch (SQLException e) {
                    if (e.getMessage().contains("violates check constraint\"nome_tamanho\""))
                        reply.put("msg", "Introduza um nome válido.");
                    else if (e.getMessage().contains("violates unique constraint \"playlists_pkey\""))
                        reply.put("msg", "Já possui uma playlist com esse nome.");
                    else if (e.getMessage().contains("violates unique constraint \"playlists_musicas_pkey\""))
                        reply.put("msg", "Não pode inserir duas músicas iguais na mesma playlist.");
                    else
                        reply.put("msg", e.getMessage());
                    c.rollback(save3);
                } finally {
                    c.setAutoCommit(true);
                }
                break;
            case "album":
                Savepoint save2 = null;
                try {
                    c.setAutoCommit(false);
                    save2 = c.setSavepoint();
                    st = c.prepareStatement("insert into public.albuns (idalbuns,nome,genero,datalancamento,historia,editoras_nome,artistas_idartist) values (default,?,?,?,?,?,?) ;");
                    st.setString(1, message.get("nome"));
                    st.setString(2, message.get("genero"));
                    st.setDate(3, Date.valueOf(message.get("datalancamento")));
                    st.setString(4, message.get("historia"));
                    st.setString(5, message.get("editora_nome"));
                    st.setInt(6, Integer.parseInt(message.get("id_artist")));
                    st.executeUpdate();
                    String[] ids = message.get("id_musics").split("-");
                    for (int i = 0; i < ids.length; i++) {
                        st = c.prepareStatement("insert into public.albuns_musicas (albuns_idalbuns,musicas_idmusicas) values ((select max(idalbuns) from public.albuns),?);");
                        st.setInt(1, Integer.parseInt(ids[i]));
                        st.executeUpdate();
                    }
                    c.commit();
                    reply.put("msg", "Inserido com sucesso.");
                } catch (SQLException e) {
                    if (e.getMessage().contains("violates check constraint \"nome_tamanho\""))
                        reply.put("msg", "Introduza um nome válido.");
                    else if (e.getMessage().contains("violates check constraint \"genero_tamanho\""))
                        reply.put("msg", "Introduza um género válido.");
                    else if (e.getMessage().contains("violates check constraint \"data_lancamento\""))
                        reply.put("msg", "Introduza uma data de lançamento válida.");
                    else if (e.getMessage().contains("violates unique constraint \"albuns_musicas_pkey\""))
                        reply.put("msg", "Não pode inserir duas músicas iguais no mesmo albúm.");
                    else
                        reply.put("msg", e.getMessage());
                    c.rollback(save2);
                } finally {
                    c.setAutoCommit(true);
                }
                break;
            case "musica":
                try {
                    st = c.prepareStatement("insert into public.musicas (idmusicas,letra,nome,duracao,compositores_musicos_artistas_idartist) values(default,?,?,?,?);");
                    st.setString(1, message.get("letra"));
                    st.setString(2, message.get("nome"));
                    st.setTime(3, Time.valueOf(message.get("duracao")));
                    st.setInt(4, Integer.parseInt(message.get("compositor")));
                    st.executeUpdate();
                    reply.put("msg", "Inserida com sucesso.");
                } catch (SQLException e) {
                    if (e.getMessage().contains("violates check constraint \"nome_tamanho\""))
                        reply.put("msg", "Introduza um nome válido.");
                    else if (e.getMessage().contains("violates check constraint \"letra_tamanho\""))
                        reply.put("msg", "Introduza uma letra.");
                    else if (e.getMessage().contains("violates check constraint \"duracao_tempo\""))
                        reply.put("msg", "Introduza uma duração válida.");
                    else
                        reply.put("msg", e.getMessage());
                }
                break;
        }
        return reply;
    }

    public HashMap<String, String> writereview(HashMap<String, String> message) {
        HashMap<String, String> reply = new HashMap<String, String>();
        try {
            st = c.prepareStatement("insert into public.criticas (idcritica,rate,texto,utilizadores_nome,albuns_idalbuns) values (default,?,?,?,?);");
            st.setDouble(1, Double.parseDouble(message.get("rate")));
            st.setString(2, message.get("texto"));
            st.setString(3, message.get("username"));
            st.setInt(4, Integer.parseInt(message.get("id_album")));
            st.executeUpdate();
            reply.put("msg", "Inserida com sucesso.");
        } catch (Exception e) {
            if (e.getMessage().contains("violates check constraint \"texto_tamanho\""))
                reply.put("msg", "Introduza a critica(texto).");
            else if (e.getMessage().contains("violates check constraint \"rate_entre\""))
                reply.put("msg", "Introduza um valor de rate válido.");
            else {
                reply.put("msg", e.getMessage());
            }
        }
        return reply;
    }

    public HashMap<String, String> add(HashMap<String, String> message) {
        HashMap<String, String> reply = new HashMap<>();
        switch (message.get("select")) {
            case "playlist":
                try {
                    st = c.prepareStatement("insert into public.playlists_musicas (playlists_nome,playlists_utilizadores_nome,musicas_idmusicas) values (?,?,?);");
                    st.setString(1, message.get("nome"));
                    st.setString(2, message.get("username"));
                    st.setInt(3, Integer.parseInt(message.get("id_music")));
                    st.executeUpdate();
                    reply.put("msg", "Inserida com sucesso.");
                } catch (SQLException e) {
                    if (e.getMessage().contains("violates unique constraint \"playlists_musicas_pkey\""))
                        reply.put("msg", "A música já se encontra inserida nessa playlist.");
                    else
                        reply.put("msg", e.getMessage());
                }
                break;
            case "album":
                try {
                    st = c.prepareStatement("insert into public.albuns_musicas (albuns_idalbuns,musicas_idmusicas) values (?,?);");
                    st.setInt(1, Integer.parseInt(message.get("id_album")));
                    st.setInt(2, Integer.parseInt(message.get("id_music")));
                    st.executeUpdate();
                    reply.put("msg", "Inserida com sucesso.");
                } catch (SQLException e) {
                    if (e.getMessage().contains("violates unique constraint \"albuns_musicas_pkey\""))
                        reply.put("msg", "A música já se encontra inserida nesse albúm.");
                    else
                        reply.put("msg", e.getMessage());
                }
                break;
            case "bandas":
                try {
                    st = c.prepareStatement("insert into public.bandas_musicos (bandas_artistas_idartist,musicos_artistas_idartist) values (?,?);");
                    st.setInt(1, Integer.parseInt(message.get("id_bandas")));
                    st.setInt(2, Integer.parseInt(message.get("id_musico")));
                    st.executeUpdate();
                    reply.put("msg", "Inserido com sucesso.");
                } catch (SQLException e) {
                    if (e.getMessage().contains("violates unique constraint \"bandas_musicos_pkey\""))
                        reply.put("msg", "O músico já se encontra nessa banda.");
                    else
                        reply.put("msg", e.getMessage());
                }
                break;
        }
        return reply;
    }

    public HashMap<String, String> search(HashMap<String, String> message) {
        HashMap<String, String> reply = new HashMap<String, String>();
        int num = 1;
        try {
            if (message.get("select").equals("all") || message.get("select").equals("musicas")) {
                st = c.prepareStatement("select nome,idmusicas from public.musicas where upper(nome) like upper('%" + message.get("searchInput") + "%');");
                rs = st.executeQuery();
                while (rs.next()) {
                    reply.put(String.valueOf(num), "Música - " + rs.getString(1));
                    reply.put("ID" + String.valueOf(num), "musicas-" + String.valueOf(rs.getInt(2)));
                    num++;
                }
            }
            if (message.get("select").equals("all")) {
                st = c.prepareStatement("select nome,idalbuns from public.albuns where upper(nome) like upper('%" + message.get("searchInput") + "%');");
                rs = st.executeQuery();
                while (rs.next()) {
                    reply.put(String.valueOf(num), "Albúm - " + rs.getString(1));
                    reply.put("ID" + String.valueOf(num), "albuns-" + String.valueOf(rs.getInt(2)));
                    num++;
                }
            }
            if (message.get("select").equals("all") || message.get("select").equals("musicos")) {
                st = c.prepareStatement("select nome,idartist from public.artistas, public.musicos where upper(nome) like upper('%" + message.get("searchInput") + "%') and idartist=artistas_idartist;");
                rs = st.executeQuery();
                while (rs.next()) {
                    reply.put(String.valueOf(num), "Músico - " + rs.getString(1));
                    reply.put("ID" + String.valueOf(num), "musicos-" + String.valueOf(rs.getInt(2)));
                    num++;
                }
            }
            if (message.get("select").equals("all")) {
                st = c.prepareStatement("select nome,idartist from public.artistas, public.bandas where upper(nome) like upper('%" + message.get("searchInput") + "%') and idartist=artistas_idartist;");
                rs = st.executeQuery();
                while (rs.next()) {
                    reply.put(String.valueOf(num), "Banda - " + rs.getString(1));
                    reply.put("ID" + String.valueOf(num), "bandas-" + String.valueOf(rs.getInt(2)));
                    num++;
                }
            }
        } catch (SQLException e) {
            reply.put("msg", e.getMessage());
        }
        return reply;
    }

    public HashMap<String, String> edit(HashMap<String, String> message) {
        HashMap<String, String> reply = new HashMap<>();
        switch (message.get("select")) {
            case "playlists":
                try {
                    switch (message.get("atributo")) {
                        case "nome":
                            st = c.prepareStatement("update public.playlists set nome=? where nome=? and utilizadores_nome=?;");
                            st.setString(1, message.get("new"));
                            st.setString(2, message.get("nomeplaylist"));
                            st.setString(3, message.get("owner"));
                            st.executeUpdate();
                            /*st = c.prepareStatement("update public.playlists_musicas set playlists_nome=? where playlists_nome=? and playlists_utilizadores_nome=?;");
                            st.setString(1,message.get("new"));
                            st.setString(2,message.get("nomeplaylist"));
                            st.setString(3,message.get("owner"));
                            st.executeUpdate();*/
                            break;
                        case "permissao":
                            st = c.prepareStatement("update public.playlists set privada=? where nome=? and utilizadores_nome=?;");
                            st.setBoolean(1, Boolean.parseBoolean(message.get("new")));
                            st.setString(2, message.get("nomeplaylist"));
                            st.setString(3, message.get("owner"));
                            st.executeUpdate();
                            break;
                    }
                    reply.put("msg", "Atributo alterado com sucesso.");
                } catch (SQLException e) {
                    if (e.getMessage().contains("violates check constraint\"nome_tamanho\""))
                        reply.put("msg", "Introduza um nome válido.");
                    else if (e.getMessage().contains("violates unique constraint \"playlists_pkey\""))
                        reply.put("msg", "Já possui uma playlist com esse nome.");
                    else
                        reply.put("msg", e.getMessage());
                }
                break;
            case "musicas":
                try {
                    switch (message.get("atributo")) {
                        case "nome":
                            st = c.prepareStatement("update public.musicas set nome=? where idmusicas=?;");
                            st.setString(1, message.get("new"));
                            break;
                        case "letra":
                            st = c.prepareStatement("update public.musicas set letra=? where idmusicas=?;");
                            st.setString(1, message.get("new"));
                            break;
                        case "duracao":
                            st = c.prepareStatement("update public.musicas set duracao=? where idmusicas=?;");
                            st.setTime(1, Time.valueOf(message.get("new")));
                            break;
                        case "compositor":
                            st = c.prepareStatement("update public.musicas set compositores_musicos_artistas_idartist=? where idmusicas=?;");
                            st.setInt(1, Integer.parseInt(message.get("new")));
                            break;
                    }
                    st.setInt(2, Integer.parseInt(message.get("id")));
                    st.executeUpdate();
                    reply.put("msg", "Atributo alterado com sucesso.");
                } catch (SQLException e) {
                    if (e.getMessage().contains("violates check constraint \"nome_tamanho\""))
                        reply.put("msg", "Introduza um nome válido.");
                    else if (e.getMessage().contains("violates check constraint \"letra_tamanho\""))
                        reply.put("msg", "Introduza uma letra.");
                    else if (e.getMessage().contains("violates check constraint \"duracao_tempo\""))
                        reply.put("msg", "Introduza uma duração válida.");
                    else
                        reply.put("msg", e.getMessage());
                }
                break;
            case "albuns":
                try {
                    switch (message.get("atributo")) {
                        case "nome":
                            st = c.prepareStatement("update public.albuns set nome=? where idalbuns=?;");
                            st.setString(1, message.get("new"));
                            break;
                        case "genero":
                            st = c.prepareStatement("update public.albuns set genero=? where idalbuns=?;");
                            st.setString(1, message.get("new"));
                            break;
                        case "datalancamento":
                            st = c.prepareStatement("update public.albuns set datalancamento=? where idalbuns=?;");
                            st.setDate(1, Date.valueOf(message.get("new")));
                            break;
                        case "historia":
                            st = c.prepareStatement("update public.albuns set historia=? where idalbuns=?;");
                            st.setString(1, message.get("new"));
                            break;
                        case "editora":
                            st = c.prepareStatement("update public.albuns set editoras_nome=? where idalbuns=?;");
                            st.setString(1, message.get("new"));
                            break;
                        case "artista":
                            st = c.prepareStatement("update public.albuns set artistas_idartist=? where idalbuns=?;");
                            st.setInt(1, Integer.parseInt(message.get("new")));
                            break;
                    }
                    st.setInt(2, Integer.parseInt(message.get("id")));
                    st.executeUpdate();
                    reply.put("msg", "Atributo alterado com sucesso.");
                } catch (SQLException e) {
                    if (e.getMessage().contains("violates check constraint \"nome_tamanho\""))
                        reply.put("msg", "Introduza um nome válido.");
                    else if (e.getMessage().contains("violates check constraint \"genero_tamanho\""))
                        reply.put("msg", "Introduza um género válido.");
                    else if (e.getMessage().contains("violates check constraint \"data_lancamento\""))
                        reply.put("msg", "Introduza uma data de lançamento válida.");
                    else
                        reply.put("msg", e.getMessage());
                }
                break;
            case "musicos":
                try {
                    switch (message.get("atributo")) {
                        case "nome":
                            st = c.prepareStatement("update public.artistas set nome=? where idartist=?;");
                            st.setString(1, message.get("new"));
                            break;
                        case "historia":
                            st = c.prepareStatement("update public.artistas set historia=? where idartist=?;");
                            st.setString(1, message.get("new"));
                            break;
                    }
                    st.setInt(2, Integer.parseInt(message.get("id")));
                    st.executeUpdate();
                    reply.put("msg", "Atributo alterado com sucesso.");
                } catch (SQLException e) {
                    if (e.getMessage().contains("violates check constraint \"nome_tamanho\""))
                        reply.put("msg", "Introduza um nome válido.");
                    else {
                        reply.put("msg", e.getMessage());
                    }
                }
                break;
            case "bandas":
                try {
                    switch (message.get("atributo")) {
                        case "nome":
                            st = c.prepareStatement("update public.artistas set nome=? where idartist=?;");
                            st.setString(1, message.get("new"));
                            break;
                        case "historia":
                            st = c.prepareStatement("update public.artistas set historia=? where idartist=?;");
                            st.setString(1, message.get("new"));
                            break;
                        case "inicio":
                            st = c.prepareStatement("update public.bandas set inicio=? where artistas_idartist=?;");
                            st.setDate(1, Date.valueOf(message.get("new")));
                            break;
                        case "fim":
                            st = c.prepareStatement("update public.bandas set fim=? where artistas_idartist=?;");
                            if (message.get("new").equals("null"))
                                st.setDate(1, null);
                            else
                                st.setDate(1, Date.valueOf(message.get("new")));
                            break;
                    }
                    st.setInt(2, Integer.parseInt(message.get("id")));
                    st.executeUpdate();
                    reply.put("msg", "Atributo alterado com sucesso.");
                } catch (SQLException e) {
                    if (e.getMessage().contains("violates check constraint \"nome_tamanho\""))
                        reply.put("msg", "Introduza um nome válido.");
                    else if (e.getMessage().contains("violates check constraint \"data_inicio_fim\""))
                        reply.put("msg", "Introduza um valor de fim superior ao de inicio de data.");
                    else if (e.getMessage().contains("violates check constraint \"data_inicio\""))
                        reply.put("msg", "Introduza uma data de inicio válida");
                    else if (e.getMessage().contains("violates check constraint \"data_fim\""))
                        reply.put("msg", "Introduza uma data de fim válida.");
                    else {
                        reply.put("msg", e.getMessage());
                    }
                }
                break;
        }
        return reply;
    }

    public HashMap<String, String> remove(HashMap<String, String> message) {
        HashMap<String, String> reply = new HashMap<>();
        try {
            if (message.get("select").equals("playlists")) {
                st = c.prepareStatement("delete from public.playlists_musicas where playlists_nome=? and playlists_utilizadores_nome=? and musicas_idmusicas=?;");
                st.setString(1, message.get("playlistNome"));
                st.setString(2, message.get("username"));
                st.setInt(3, Integer.parseInt(message.get("id_musica")));
                st.executeUpdate();
                reply.put("msg", "Música removida com sucesso.");
            } else if (message.get("select").equals("bandas")) {
                st = c.prepareStatement("delete from public.bandas_musicos where bandas_artistas_idartist=? and musicos_artistas_idartist=?;");
                st.setInt(1, Integer.parseInt(message.get("id_banda")));
                st.setInt(2, Integer.parseInt(message.get("id_musico")));
                st.executeUpdate();
                reply.put("msg", "Músico removido com sucesso.");
            }
        } catch (SQLException e) {
            reply.put("msg", e.getMessage());
        }
        return reply;
    }
}
