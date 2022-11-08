package com.dy.orm.mapper;

import com.dy.orm.entity.Student;
import com.dy.orm.helper.ColumnToPropertyMapping;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhanglianyong
 * 2022/11/8 8:11
 */
public class BaseMapperTest {

    private final BaseMapper<Student> baseMapper = new BaseMapper<>();

    @Test
    public void testQuery() {
        String sql = "select * from student";

        List<ColumnToPropertyMapping<Object, Object>> mappings = new ArrayList<>();
        mappings.add(new ColumnToPropertyMapping<>("sid", "sid"));
        mappings.add(new ColumnToPropertyMapping<>("sName", "sname"));
        mappings.add(new ColumnToPropertyMapping<>("sex", "sex"));
        mappings.add(new ColumnToPropertyMapping<>("age", "age"));
        String[] params = {"1"};
        Student student = new Student();
        List<Student> returnData = baseMapper.executeSelect(sql, student, mappings, params);
        returnData.stream().forEach(System.out::println);
    }

}
