package com.subaru.maneki.dao;

import com.subaru.maneki.model.Prop;
import org.apache.ibatis.annotations.Param;

/**
 * @author zhangchaojie
 * @since 2016-08-17
 */
public interface PropDao {

    int delete(int id);

    int insert(Prop prop);

    Prop selectByNameIdAndValueId(@Param("nameId") int nameId, @Param("valueId") int valueId,
                                  @Param("type") int type);

    Prop select(int id);

    int update(Prop prop);

}