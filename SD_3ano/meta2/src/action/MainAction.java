package action;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import model.User;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainAction extends ActionSupport implements SessionAware, ModelDriven {
    private Map<String,Object> session;
    private User user ;
    private String option;
    private String select;
    private String id;
    private ArrayList<String> teste;
    private String text;
    private String review;
    private String rate;
    private String description;
    private String compositor, duracao, genre, idartist, idalbum;
    private String value;
    private ArrayList<String> showD;
    private ArrayList<String> teste2;
    private String code;
    private HashMap<String,String> links;
    private String idmusic;
    private String idficheiro;

    public String getIdmusic() {
        return idmusic;
    }

    public void setIdmusic(String idmusic) {
        this.idmusic = idmusic;
    }

    public String getIdficheiro() {
        return idficheiro;
    }

    public void setIdficheiro(String idficheiro) {
        this.idficheiro = idficheiro;
    }

    public ArrayList<String> getTeste() {
        return teste;
    }

    public ArrayList<String> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<String> files) {
        this.files = files;
    }

    public ArrayList<String> getMusicas() {
        return musicas;
    }

    public void setMusicas(ArrayList<String> musicas) {
        this.musicas = musicas;
    }

    public ArrayList<String> files;
    public ArrayList<String> musicas;
    public void setTeste(ArrayList<String> teste) {
        this.teste = teste;
    }

    public Map<String, Object> getSession() {
        return session;
    }

    public String execute() {
        user = getUser();
        System.out.println("User: "+getUser().getUsername());
        switch (option){
            case "show all":
                return "showall";
            case "search music":
                return "searchmusic";
            case "promote":
                return "promote";
            case "edit":
                return "edit";
            default:
                session.put("option",option);
                teste = user.showAll("album");
                teste2 = user.showAll("artist");
                session.put("albums",teste);
                session.put("artistas",teste2);
                return "insert";
        }
    }

    public  String updateNow(){
        session.put("permission","true");
        return SUCCESS;
    }

    public String playmusic(){
        user = getUser();

        //session.put("links",user.playmusic((String)session.get("USER_API_TOKEN")));
        if(((String)session.get("USER_API_TOKEN")).equals("")){
            return SUCCESS;
        }
        links = user.playmusic((String)session.get("USER_API_TOKEN"));
        return "playmusic";
    }

    public String linktofilemusic(){
        user = getUser();
        if(((String)session.get("USER_API_TOKEN")).equals("")){
            return SUCCESS;
        }
        musicas = user.showAll("music");
        files = user.listFiles((String)session.get("USER_API_TOKEN"));
        return "linktofilemusic";
    }

    public String sharefile(){
        user = getUser();
        if(((String)session.get("USER_API_TOKEN")).equals("")){
            return SUCCESS;
        }
        HashMap<String,String> message = new HashMap<>();
        message.put("type","dropbox");
        message.put("select","get");
        message.put("iduser",user.getIdUser());
        try {
            files = new ArrayList<>();
            teste2 = new ArrayList<>();
            message = user.getServer().request(message);
            for (HashMap.Entry<String, String> entry : message.entrySet()) {
                files.add(entry.getKey());
                teste2.add(entry.getValue());
            }
            session.put("files",files);
            session.put("idfi",teste2);
        }catch (RemoteException e){
            e.printStackTrace();
        }
        return "sharefile";
    }

    public String sharelink(){
        user = getUser();
        files=((ArrayList<String>)session.get("files"));
        int aux=0;
        for(int i=0; i< files.size();i++){
            if(files.get(i).equals(idficheiro)){
                aux=i;
            }
        }
        System.out.println(((ArrayList<String>)session.get("idfi")).get(aux));
        System.out.println(text);
        user.share((String)session.get("USER_API_TOKEN"),((ArrayList<String>)session.get("idfi")).get(aux),text);
        return SUCCESS;
    }

    public String insertDropbox(){
        user = getUser();
        HashMap<String,String> message = new HashMap<>();
        message.put("type","dropbox");
        message.put("select","insert");
        message.put("idmusic",getIdmusic().split(":")[0]);
        message.put("idficheiro",getIdficheiro().split("=")[1]);
        message.put("iduser",user.getIdUser());
        try{
            user.getServer().request(message);
        } catch (RemoteException e){
            e.printStackTrace();
        }
        return SUCCESS;
    }

    public String logout(){
        user = getUser();
        user.logOut();
        return "logout";
    }

    public String connect(){
        user = getUser();
        session.put("url",user.connect((String)session.get("USER_API_TOKEN")));
        return SUCCESS;
    }

    public String recebetoken(){
        user = getUser();
        session.put("url","");
        System.out.println(getCode());
        session.put("USER_API_TOKEN",user.connect2(getCode()));
        return SUCCESS;
    }


    public String promote(){
        user = getUser();
        user.promote(text);
        return SUCCESS;
    }

    public String edit(){
        user = getUser();
        user.edit((String)session.get("select"),text.toLowerCase(),value.toLowerCase(),(String)session.get("id"));
        return SUCCESS;
    }

    public String remove(){
        user = getUser();
        System.out.println("ID remove: "+ session.get("id"));
        user.remove((String)session.get("select"),(String)session.get("id"));
        return SUCCESS;
    }

    public String insert(){
        user = getUser();
        switch ((String)session.get("option")){
            case "artist":
                user.insert((String)session.get("option"),text,description);
                break;
            case "album":
                user.insert((String)session.get("option"),text,description);
                break;
            case "music":
                user.insert((String)session.get("option"),text,compositor,duracao,genre,idartist.split(":")[0],idartist.split(":")[0]);
        }
        return SUCCESS;
    }

    public String showdetails(){
        user = getUser();
        if(!id.equals("Empty")){
            id=id.split(":")[0];
            showD = user.showDetails((String)session.get("select"),id);
        }
        System.out.println(session.get("select"));
        session.put("id",id);
        return "showdetails";
    }

    public String searchmusic() {
        user = getUser();
        select = select.toLowerCase();
        session.put("select", "music");
        teste = user.searchMusic(select, text);
        return "searchmusic";
    }
    public String writereview(){
        user = getUser();
        user.writereview((String)session.get("id"),rate,review);
        showD = user.showDetails((String)session.get("select"),(String)session.get("id"));
        return "showdetails";
    }

    public String showall(){
        user = getUser();
        select = select.toLowerCase();
        session.put("select",select);
        teste = user.showAll(select);
        return "showall";
    }

    public void setOption(String option) {
        this.option = option;
    }

    @Override
    public Object getModel() {
        return user;
    }

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }

    public User getUser() {
        if(session.containsKey("user"))
            return (User) session.get("user");
        return new User();
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public String getSelect() {
        return select;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getShowD() {
        return showD;
    }

    public void setShowD(ArrayList<String> showD) {
        this.showD = showD;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getReview() {
        return review;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getRate() {
        return rate;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public String getCompositor() {
        return compositor;
    }

    public void setCompositor(String compositor) {
        this.compositor = compositor;
    }

    public ArrayList<String> getTeste2() {
        return teste2;
    }

    public void setTeste2(ArrayList<String> teste2) {
        this.teste2 = teste2;
    }

    public String getIdartist() {
        return idartist;
    }

    public void setIdartist(String idartist) {
        this.idartist = idartist;
    }

    public String getIdalbum() {
        return idalbum;
    }

    public void setIdalbum(String idalbum) {
        this.idalbum = idalbum;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public HashMap<String, String> getLinks() {
        return links;
    }

    public void setLinks(HashMap<String, String> links) {
        this.links = links;
    }
}
