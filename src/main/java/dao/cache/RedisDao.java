package dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisDao {

    private JedisPool jedisPool;

    //schema即模式，序列化和反序列化的“标准”
    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public RedisDao(String ip,int port){
        jedisPool = new JedisPool(ip,port);
    }

    public Seckill getSeckill(long seckillId){
        try {
            Jedis jedis = jedisPool.getResource();
            try{
                String key = "seckill:"+seckillId;
                //没有实现内部序列化操作
                //get->byte[]->返序列化->Object(Seckill)
                //采用自定义序列化protostuff
                //protostuff将对象转化为字节传入redis，但对象的class必须是pojo（有getter、setter方法
                byte[] bytes = jedis.get(key.getBytes());
                if (bytes!=null){
                    Seckill seckill = schema.newMessage();
                    ProtostuffIOUtil.mergeFrom(bytes,seckill,schema);//seckill被反序列
                    return seckill;
                }
            }finally {
                jedis.close();
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;

    }

    public String putSeckill(Seckill seckill){
        //Object(Seckill) ----序列化-----> bytes[] ---------> 发送给redis
        try{
            Jedis jedis = jedisPool.getResource();
            try{
                String key = "seckill:"+seckill.getSeckillId();
                byte[] bytes = ProtostuffIOUtil.toByteArray(seckill,schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                int timeout = 60*60;
                String result = jedis.setex(key.getBytes(),timeout,bytes);
                return result;
            }finally {
                jedis.close();
            }
        }catch (Exception e){

        }

        return null;
    }
}
