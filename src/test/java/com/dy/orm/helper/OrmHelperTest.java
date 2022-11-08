package com.dy.orm.helper;

import com.dy.orm.entity.Student;
import org.junit.Test;

import java.util.List;

/**
 * @author zhanglianyong
 * 2022/11/7 21:51
 */
public class OrmHelperTest {

    private OrmHelper<Student> ormHelper = new OrmHelper<>();

    @Test
    public void testAdd() {
        Student student = new Student();
        student.setSid(30L);
        student.setAge(10);
        student.setsName("大勇");
        student.setSex("M");
        ormHelper.insert(student);
    }

    @Test
    public void testSelectByPrimaryKey() {
        Student student = new Student();
        student.setSid(30L);
        student.setAge(10);
        student.setsName("大勇");
        student.setSex("M");
        List<Student> students = ormHelper.selectByPrimaryKey(student);
        students.forEach(System.out::println);
    }
}
