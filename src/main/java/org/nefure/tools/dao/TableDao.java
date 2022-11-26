package org.nefure.tools.dao;

import org.nefure.fxscaffold.annotion.Component;
import org.nefure.tools.entity.Table;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author nefure
 * @since 2022/11/25 14:19
 */
@Component
public class TableDao {

    public List<Table> selectAll(String url, String userName, String passwd){
        ArrayList<Table> tables = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(url,userName,passwd)){
            //
            String sql = "SELECT TABLE_NAME,TABLE_COMMENT FROM information_schema.`TABLES` WHERE TABLE_SCHEMA = (SELECT DATABASE());";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()){
                String tableName = resultSet.getString(1);
                String comment = resultSet.getString(2);
                tables.add(new Table(tableName,comment));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tables;
    }

    public void selectAllColumns(String url, String userName, String passwd, Table table){
        try (Connection con = DriverManager.getConnection(url,userName,passwd)){
            String sql = "select * from `"+ table.getTableName() + "` limit 1";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.execute();
            ResultSetMetaData metaData = preparedStatement.getResultSet().getMetaData();
            ArrayList<Table.Column> columns = new ArrayList<>();
            table.setColumns(columns);
            for (int i = 1; i <= metaData.getColumnCount(); i++){
                columns.add(new Table.Column(metaData.getColumnName(i), metaData.getColumnTypeName(i) ,metaData.getColumnClassName(i)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
