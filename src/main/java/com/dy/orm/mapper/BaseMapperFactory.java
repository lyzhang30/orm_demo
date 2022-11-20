package com.dy.orm.mapper;

import com.dy.orm.entity.Student;

/**
 * @author zhanglianyong
 * 2022/11/20 15:33
 */
public class BaseMapperFactory<T> {

    private BaseMapper<T> mapperInterface;

    public BaseMapperFactory() {
        this.mapperInterface = new BaseMapper<>();
    }

    public BaseMapper<T> getMapperInterface() {
        return mapperInterface;
    }

    public BaseMapperFactory(Boolean autoCommit) {
        this.mapperInterface = new BaseMapper<>(autoCommit);
    }

}
