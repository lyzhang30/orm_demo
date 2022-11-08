package com.dy.orm.entity;

import com.dy.orm.annotations.Column;
import com.dy.orm.annotations.Id;
import com.dy.orm.annotations.Table;

/**
 * @author zhanglianyong
 * 2022/11/7 20:57
 */
@Table("student")
public class Student {

    @Id(value = "sid")
    private Long sid;

    @Column(value = "sname")
    private String sName;

    @Column(value = "sex")
    private String sex;

    @Column(value = "age", type = "int")
    private Integer age;

    public Student(Long sid, String sName, String sex, Integer age) {
        this.sid = sid;
        this.sName = sName;
        this.sex = sex;
        this.age = age;
    }

    public Student() {
    }

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student{" +
                "sid=" + sid +
                ", sName='" + sName + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                '}';
    }
}
