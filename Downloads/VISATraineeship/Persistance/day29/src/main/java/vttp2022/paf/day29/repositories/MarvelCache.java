package vttp2022.paf.day29.repositories;

import java.io.StringReader;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2022.paf.day29.redisConfig;
import vttp2022.paf.day29.model.Character;

@Repository
public class MarvelCache {
    
    @Autowired @Qualifier(redisConfig.CACHE_MARVEL)
    private RedisTemplate<String, String> redisTemplate;

    public void cache(String key, List<Character> characters){
        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        characters.stream().forEach(c -> {arrayBuilder.add(c.toJSONString());});
        ops.set(key, arrayBuilder.build().toString(), Duration.ofSeconds(3000));
    }

    public Optional<List<Character>> get(String name){
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String value = ops.get(name);
        if(null==value){
            return Optional.empty();
        }
        JsonReader reader = Json.createReader(new StringReader(value));
        JsonArray results = reader.readArray();
        List<Character> characters = new LinkedList<>();
        System.out.println("!!!!!!!!!!!!!!!!!!!");
        for(int i=0;i<results.size();i++){
            JsonObject o = results.getJsonObject(i);
            Character c = Character.fromCache(o);
            characters.add(c);
            System.out.println("CACHE>>>>>>>" + c.getName());
        }
        
        //List<Character> characters = results.stream().map(v->(JsonObject)v).map(v-> Character.fromCache(v)).toList();

        return Optional.of(characters);
    }
}
