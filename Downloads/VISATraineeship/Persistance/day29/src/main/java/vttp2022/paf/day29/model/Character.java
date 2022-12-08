package vttp2022.paf.day29.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Character {
    
    private String name;
    private String description;
    private String imgURL;
    private Integer id;
    private String details;
    public String getDetails() {
        return details;
    }
    public void setDetails(String details) {
        this.details = details;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getImgURL() {
        return imgURL;
    }
    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public static Character create (JsonObject doc){
        final Character res = new Character();
        res.setId(doc.getInt("id"));
        res.setName(doc.getString("name"));
        res.setDescription(doc.getString("description", "no description"));
        System.out.println(res.getDescription());
        JsonObject img = doc.getJsonObject("thumbnail");
        res.setImgURL("%s.%s".formatted(img.getString("path"), img.getString("extension")));
        
        JsonArray urls = doc.getJsonArray("urls");
        for(int i=0; i<urls.size();i++){
            JsonObject d = urls.getJsonObject(i);
            if("detail".equals(d.getString("type"))){
                res.setDetails(d.getString("url"));
                break;
            }
        }


        return res;
    }

    public static Character fromCache(JsonObject doc){
        final Character c = new Character();
        c.setDescription(doc.getString("description"));
        c.setImgURL(doc.getString("image"));
        c.setDetails(doc.getString("details"));
        c.setId(doc.getInt("id"));
        c.setName(doc.getString("name"));
        return c;
    }
    

    public JsonObject toJSONString(){
        return Json.createObjectBuilder()
        .add("id", id)
        .add("name", name)
        .add("description", description)
        .add("image", imgURL)
        .add("details", details)
        .build();
    }
}
