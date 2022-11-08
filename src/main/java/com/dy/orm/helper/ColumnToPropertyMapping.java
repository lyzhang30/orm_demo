package com.dy.orm.helper;

/**
 * @author zhanglianyong
 * 2022/11/8 8:38
 */
public class ColumnToPropertyMapping<V, E> {

    private E column;

    private V property;

    public ColumnToPropertyMapping( V property, E column) {
        this.column = column;
        this.property = property;
    }

    public E getColumn() {
        return column;
    }

    public void setColumn(E column) {
        this.column = column;
    }

    public V getProperty() {
        return property;
    }

    public void setProperty(V property) {
        this.property = property;
    }

    @Override
    public String toString() {
        return "ColumnToPropertyMapping{" +
                "column=" + column +
                ", property=" + property +
                '}';
    }
}
