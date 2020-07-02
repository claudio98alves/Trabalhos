package model;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.*;
import com.github.scribejava.core.oauth.OAuthService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import uc.sd.apis.DropBoxApi2;

import javax.xml.ws.Service;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

class DropBoxRestClient {


    // Access codes #1: per application used to get access codes #2
    private static final String API_APP_KEY = "b4u9fafqvzt68u4";
    private static final String API_APP_SECRET = "kav3wijn3m3dfxp";


    // Access codes #2: per user per application
    private static final String API_USER_TOKEN = "";

    public String connect2(String token){
        OAuthService service = new ServiceBuilder()
                .provider(DropBoxApi2.class)
                .apiKey(API_APP_KEY)
                .apiSecret(API_APP_SECRET)
                .callback("https://localhost:8443/recebetoken.action")
                .build();
        Verifier verifier = new Verifier(token);
        Token accessToken = service.getAccessToken(null, verifier);
        return accessToken.getToken();
    }


    public String connect(String token) {
        OAuthService service = new ServiceBuilder()
                .provider(DropBoxApi2.class)
                .apiKey(API_APP_KEY)
                .apiSecret(API_APP_SECRET)
                .callback("https://localhost:8443/recebetoken.action")
                .build();
        try {
            if ( token.equals("") ) {
                return service.getAuthorizationUrl(null);
                /*Verifier verifier = new Verifier(in.nextLine());
                Token accessToken = service.getAccessToken(null, verifier);
                System.out.println("Define API_USER_TOKEN: " + accessToken.getToken());
                //System.out.println("Define API_USER_SECRET: " + accessToken.getSecret());
                System.exit(0);*/
            }

            //Token accessToken = new Token( API_USER_TOKEN, "");
        } catch(OAuthException e) {
            e.printStackTrace();
        }
        return "";
    }
    public void share(String token,String id,String email){
        OAuthService service = new ServiceBuilder()
                .provider(DropBoxApi2.class)
                .apiKey(API_APP_KEY)
                .apiSecret(API_APP_SECRET)
                .callback("https://localhost:8443/recebetoken.action")
                .build();
        OAuthRequest request = new OAuthRequest(Verb.POST, "https://api.dropboxapi.com/2/sharing/add_file_member", service);
        request.addHeader("authorization", "Bearer " + token);
        request.addHeader("Content-Type", "application/json");
        JSONObject o = new JSONObject();
        JSONObject w = new JSONObject();
        JSONArray o2 = new JSONArray();
        o.put("file",id);
        w.put(".tag","email");
        w.put("email",email);
        o2.add(w);
        o.put("members",o2);
        o.put("custom_message","DropMusic File aceita já !");
        o.put("quiet",false);
        o.put("access_level","viewer");
        o.put("add_message_as_comment",false);
        request.addPayload("{\"file\": \""+id+"\", \"members\":[ { \".tag\": \"email\", \"email\": \""+email+"\" }], \"custom_message\": \"Aceita\", \"quiet\": false, \"access_level\": \"viewer\", \"add_message_as_comment\": false }");

        System.out.println("{\"file\": \"id:\""+id+"\", \"members\":[ { \".tag\": \"email\", \"email\": \""+email+"\" }], \"custom_message\": \"Aceita\", \"quiet\": false, \"access_level\": \"viewer\", \"add_message_as_comment\": false }");
        Response response = request.send();
        System.out.printf("-------------------------\n");
        System.out.println(response.getCode());
        System.out.println(response.getBody());
    }



    public ArrayList<String> listFiles(String token) {
        OAuthService service = new ServiceBuilder()
                .provider(DropBoxApi2.class)
                .apiKey(API_APP_KEY)
                .apiSecret(API_APP_SECRET)
                .callback("https://localhost:8443/recebetoken.action")
                .build();
        OAuthRequest request = new OAuthRequest(Verb.POST, "https://api.dropboxapi.com/2/files/list_folder", service);
        request.addHeader("authorization", "Bearer " + token);
        request.addHeader("Content-Type", "application/json");
        request.addPayload("{\n" +
                "    \"path\": \"\",\n" +
                "    \"recursive\": false,\n" +
                "    \"include_media_info\": false,\n" +
                "    \"include_deleted\": false,\n" +
                "    \"include_has_explicit_shared_members\": false,\n" +
                "    \"include_mounted_folders\": true\n" +
                "}");
        ArrayList<String> rt = new ArrayList<>();
        Response response = request.send();
        JSONObject rj = (JSONObject) JSONValue.parse(response.getBody());
        JSONArray contents = (JSONArray) rj.get("entries");
        for (int i = 0; i < contents.size(); i++) {
            JSONObject item = (JSONObject) contents.get(i);
            String path = (String) item.get("name");
            String id = (String) item.get("id");
            rt.add(path+"="+id);
        }
        System.out.printf("-------------------------\n");
        System.out.println(response.getCode());
        System.out.println(response.getBody());
        return rt;

    }

        public static HashMap<String,String> playmusic(String token) {
        OAuthService service = new ServiceBuilder()
                .provider(DropBoxApi2.class)
                .apiKey(API_APP_KEY)
                .apiSecret(API_APP_SECRET)
                .callback("https://localhost:8443/recebetoken.action")
                .build();
        OAuthRequest request = new OAuthRequest(Verb.POST, "https://api.dropboxapi.com/2/files/list_folder", service);
        request.addHeader("authorization", "Bearer " + token);
        request.addHeader("Content-Type",  "application/json");
        request.addPayload("{\n" +
                "    \"path\": \"\",\n" +
                "    \"recursive\": false,\n" +
                "    \"include_media_info\": false,\n" +
                "    \"include_deleted\": false,\n" +
                "    \"include_has_explicit_shared_members\": false,\n" +
                "    \"include_mounted_folders\": true\n" +
                "}");

        Response response = request.send();
        JSONObject rj = (JSONObject) JSONValue.parse(response.getBody());
        JSONArray contents = (JSONArray) rj.get("entries");
        for (int i=0; i<contents.size(); i++) {
            JSONObject item = (JSONObject) contents.get(i);
            String path = (String) item.get("name");
            System.out.println(path);
            createSharedLink(token, path,service);
        }
        //lista do shared links + nomes
        request = new OAuthRequest(Verb.POST, "https://api.dropboxapi.com/2/sharing/list_shared_links", service);
        request.addHeader("authorization", "Bearer " + token);
        request.addHeader("Content-Type",  "application/json");
        JSONObject o = new JSONObject();
        o.put("cursor","");
        request.addPayload(o.toJSONString());
        response = request.send();
        System.out.println("----------------------------");
        //System.out.println(response.getBody());
        HashMap<String, String> map = new HashMap<>();
        rj = (JSONObject) JSONValue.parse(response.getBody());
        contents = (JSONArray) rj.get("links");
        for (int i=0; i<contents.size(); i++) {
            JSONObject item = (JSONObject) contents.get(i);
            map.put ((String) item.get("name"),((String) item.get("url")).replace("?dl=0","?dl=1"));
        }
        return map;
    }

    private static void createSharedLink(String token, String path, OAuthService service){
        OAuthRequest request = new OAuthRequest(Verb.POST, "https://api.dropboxapi.com/2/sharing/create_shared_link_with_settings", service);
        request.addHeader("authorization", "Bearer " + token);
        request.addHeader("Content-Type",  "application/json");
        JSONObject o = new JSONObject();
        o.put("path","/"+path);
        JSONObject o2 = new JSONObject();
        o2.put("requested_visibility","public");
        o.put("settings",o2);
        request.addPayload(o.toJSONString());
        request.send();
    }

}


