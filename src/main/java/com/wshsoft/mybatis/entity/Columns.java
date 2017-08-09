package com.wshsoft.mybatis.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 查询字段
 * </p>
 * 
 * @author Carry xie
 * @Date 2017-07-28
 */
public class Columns implements Serializable {

    private static final long serialVersionUID = 1L;

    private Columns() {
    }

    /**
     * 获取实例
     */
    public static Columns create() {
        return new Columns();
    }

    public Columns column(String column) {
        Column oneColumn = Column.create().column(column);
        this.columns.add(oneColumn);
        return this;
    }

    public Columns column(String column, String as) {
        Column oneColumn = Column.create().column(column).as(as);
        this.columns.add(oneColumn);
        return this;
    }

    public Columns column(String column, String as, boolean escape) {
        Column oneColumn = Column.create().column(column).as(as);
        oneColumn.setEscape(escape);
        this.columns.add(oneColumn);
        return this;
    }

    //字段
    private List<Column> columns = new ArrayList<>();

    public Column[] getColumns() {
        Column[] columnArray = new Column[columns.size()];
        return columns.toArray(columnArray);
    }
}
