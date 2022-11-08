package com.dy.orm.helper;

/**
 * @author zhanglianyong
 * 2022/11/8 9:23
 */
public class PropertyFieldMapping<E, V> {

    private E property;

    private V annotation;

    public PropertyFieldMapping() {
    }

    public PropertyFieldMapping(E property, V annotation) {
        this.property = property;
        this.annotation = annotation;
    }

    public E getProperty() {
        return property;
    }

    public void setProperty(E property) {
        this.property = property;
    }

    public V getAnnotation() {
        return annotation;
    }

    public void setAnnotation(V annotation) {
        this.annotation = annotation;
    }
}
