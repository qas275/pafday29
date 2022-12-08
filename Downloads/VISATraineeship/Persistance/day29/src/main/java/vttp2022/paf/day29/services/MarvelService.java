package vttp2022.paf.day29.services;

import java.io.StringReader;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.LinkedList;
import java.util.List;

import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2022.paf.day29.model.Character;

@Service
public class MarvelService {
    
    
    //asd
    //@Value("${MARVEL.PUBLIC.KEY}")
    private String publicKey;

    //@Value("${MARVEL.PRIVATE.KEY}")
    private String privateKey;
    public static final String urlChara = "https://gateway.marvel.com:443/v1/public/characters?";

    public List<Character> search(String name){
        // https://gateway.marvel.com:443/v1/public/characters?nameStartsWith=dog&limit=10&apikey=95d16dbf54ac923d22b1eb44c3788caa
        // IF WANNA USE ENV VARIABLES
        publicKey = System.getenv("MARVEL_PUBLIC_KEY");
        privateKey = System.getenv("MARVEL_PRIVATE_KEY");
        Long ts = System.currentTimeMillis();
        String signature = "%d%s%s".formatted(ts,privateKey,publicKey);
        String hash = "";
        //message digst --> md5 or sha1, sha512
        MessageDigest md5 = null;
        try {
            //get an instance of MD5
            md5 = MessageDigest.getInstance("MD5");
            //update message digest --> add all the details you want to be calulcated  
            md5.update(signature.getBytes());
            // calculate the hash from all the details added
            byte[] h = md5.digest();
            //stringify the MD5 digest
            hash = HexFormat.of().formatHex(h);
        } catch (Exception e) {
           
        }


        String url = UriComponentsBuilder.fromUriString(urlChara)
            .queryParam("nameStartsWith", name)
            .queryParam("limit", 10)
            .queryParam("ts", ts)
            .queryParam("apikey", publicKey)
            .queryParam("hash", hash)
            .toUriString();
        System.out.println(publicKey);
        System.out.println(privateKey);
        System.out.println(url);

        RequestEntity<Void> req = RequestEntity.get(url).accept(MediaType.APPLICATION_JSON).build();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> resp = null;
        resp = restTemplate.exchange(req,String.class);
        String payload = resp.getBody();
        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject result = reader.readObject();
        JsonArray data = result.getJsonObject("data").getJsonArray("results");

        List<Character> res = new LinkedList<>();
        for(int i=0; i< data.size();i++){
            res.add(Character.create(data.getJsonObject(i)));
        }
        return res;
    }



}
