package service;

import entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"
})
public class SeckillServiceImpTest {
    @Autowired
    private SeckillService seckillService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void getSeckillList() {
        List<Seckill> list = seckillService.getSeckillList();
        System.out.println(list);
        logger.info("list={}",list);
    }

    @Test
    public void getById() {
        long id =1000;
        Seckill seckill = seckillService.getById(id);
        logger.info("id=1000,seckill={}",seckill);
    }

    @Test
    public void exportSeckillUrl() {
    }

    @Test
    public void executeSeckill() {
    }
}