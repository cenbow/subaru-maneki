package com.subaru.maneki.dao;

import com.subaru.maneki.model.PropView;

/**
 * @author zhangchaojie
 * @since 2016-08-24
 */
public interface PropViewDao {

    int insert(PropView propView);

    int update(PropView propView);

    void delete(int spuId);

    PropView select(int spuId);

}
