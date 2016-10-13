package com.subaru.maneki.dao;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.subaru.maneki.model.Order;

/**
 * @author zhangchaojie
 * @since 2016-09-01
 */
public interface OrderDao {

    int insert(Order order);

    int update(Order order);

    int deleteByOrderNumber(long orderNumber);

    Order selectByOrderNumber(long orderNumber);

    List<Order> selectByTime(@Param(value = "startTime") Timestamp startTime,
                             @Param(value = "endTime") Timestamp endTime,
                             @Param(value = "start") int start, @Param(value = "limit") int limit);

    Integer countByTime(@Param(value = "startTime") Timestamp startTime,
                        @Param(value = "endTime") Timestamp endTime);

    List<Order> selectByUserId(@Param(value = "userId") int userId,
                               @Param(value = "start") int start, @Param(value = "limit") int limit);

    Integer countByUserId(int userId);

    Integer countPaiedByUserId(@Param(value = "userId") int userId);

    List<Order> selectNeedToPush();

}