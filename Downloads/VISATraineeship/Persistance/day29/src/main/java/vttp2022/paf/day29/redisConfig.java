package vttp2022.paf.day29;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class redisConfig {
    //@Value("${spring.redis.host}")
    //@Value("${REDISHOST}")
    private String redisHost = System.getenv("REDISHOST");
    //@Value("${spring.redis.database}")
    private Integer redisDatabase=1;
    //@Value("${spring.redis.port}")
    //@Value("${REDISPORT}")
    private Integer redisPort;
    //@Value("${spring.redis.user}")
    //@Value("${REDISUSER}")
    private String redisUser;
    //@Value("${spring.redis.password}")
    //@Value("${REDISPASSWORD}")
    private String redisPassword;

    public static final String CACHE_MARVEL="marvel-cache";

    @Bean(CACHE_MARVEL) //need use qualifier for redis template with strin string because spring boot has inbuilt same one so need differentiate
    // singleton(across multiple sessions for caching), session(shopping cart, persists for 1 user across time), request(req body and param only when doing requests), flash -->  where data can be stored/saved
    public RedisTemplate<String, String> redisTemplate(){
        final RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        redisHost = System.getenv("REDISHOST");
        redisPort = Integer.parseInt(System.getenv("REDISPORT"));
        redisUser = System.getenv("REDISUSER");
        redisPassword = System.getenv("REDISPASSWORD");
        config.setDatabase(redisDatabase);
        config.setHostName(redisHost);
        config.setUsername(redisUser);
        config.setPassword(redisPassword);
        config.setPort(redisPort);
        final JedisClientConfiguration jedisClient = JedisClientConfiguration.builder().build();
        
        final JedisConnectionFactory jedisFac = new JedisConnectionFactory(config, jedisClient);
        jedisFac.afterPropertiesSet();;

        final RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisFac);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;

    }
}
