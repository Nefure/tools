package org.nefure.tools.entity;

import java.util.List;

/**
 * @author nefure
 * @since 2022/11/25 11:34
 */
public class Table {

    private List<Column> columns;

    private String tableName;

    private String comment;

    public static class Column{
        private final String name;
        private final String typeName;
        private final String className;
        private Boolean isIgnored;

        public Boolean getIgnored() {
            return isIgnored;
        }

        public String getName() {
            return name;
        }

        public String getTypeName() {
            return typeName;
        }

        public String getClassName() {
            return className;
        }

        public void setIgnored(Boolean ignored) {
            isIgnored = ignored;
        }

        public Column(String name, String typeName, String className){
            this.name = name;
            this.typeName = typeName;
            this.className = className;
            isIgnored = false;
        }
    }

    public Table(){}

    public Table(String name, String comm){
        tableName = name;
        comment = comm;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return tableName;
    }
}
