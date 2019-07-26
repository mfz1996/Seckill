package service;

import dto.Exposer;
import dto.SeckillExecution;
import entity.Seckill;
import exception.ReapeatKillException;
import exception.SeckillCloseException;
import exception.SeckillException;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SeckillService {
    List<Seckill> getSeckillList();

    Seckill getById(long seckillId);

    /**
     * 暴露秒杀接口
     * @param seckollId
     * @return
     */
    Exposer exportSeckillUrl(long seckollId);

    /**
     * 执行秒杀操作
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExecution executeSeckill( long seckillId, long userPhone, String md5)
            throws SeckillException, ReapeatKillException, SeckillCloseException;

    /**
     * 将事务逻辑放在mysql中执行秒杀过程
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExecution executeSeckillProcedure( long seckillId, long userPhone, String md5);
}
