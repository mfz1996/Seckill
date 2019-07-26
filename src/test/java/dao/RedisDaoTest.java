package dao;

import dao.cache.RedisDao;
import entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件位置
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest {
    @Autowired
    private RedisDao redisDao;

    private long id = 1000;
    @Autowired
    private SeckillDao seckillDao;


    @Test
    public void testSeckill() {
        Seckill seckill = redisDao.getSeckill(id);
        if(seckill == null){
            seckill =seckillDao.queryById(id);
            if (seckill!=null){
                String result = redisDao.putSeckill(seckill);
                System.out.println("result");
                System.out.println(result);
                seckill = redisDao.getSeckill(id);
                System.out.println("seckill");
                System.out.println(seckill);
            }
        }
    }
}