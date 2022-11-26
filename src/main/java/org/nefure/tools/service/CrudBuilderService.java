package org.nefure.tools.service;

import org.nefure.fxscaffold.annotion.Component;
import org.nefure.tools.entity.Table;
import org.nefure.tools.util.StringUtils;
import org.nefure.tools.util.TimeUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

/**
 * @author nefure
 * @since 2022/11/25 21:35
 */
@Component
public class CrudBuilderService {

    public void build(Table table, String prefix, String simpleClassName, String targetDic) throws IOException {
        buildEntity(table, prefix, targetDic, simpleClassName);
        buildMapper(table, prefix, targetDic, simpleClassName);
    }

    private void buildEntity(Table table, String prefix, String targetDic, String simpleClassName) throws IOException {
        String path = targetDic + "\\entity\\" + simpleClassName + ".java";
        new File(targetDic + "\\entity\\").mkdirs();
        File file = new File(path);
        if (file.createNewFile()){
            try (FileWriter fileWriter = new FileWriter(path)){
                StringBuilder builder = new StringBuilder();
                builder.append("package ").append(prefix).append(".entity;\n");
                //添加import语句
                HashSet<String> clazz = new HashSet<>();
                for (Table.Column column : table.getColumns()) {
                    String className = column.getClassName();
                    if ( (!column.getIgnored()) && clazz.add(className) &&!className.startsWith("java.lang")){
                        builder.append("import ").append(className).append(";\n");
                    }
                }
                builder.append("import java.io.Serial;\nimport java.io.Serializable;\nimport lombok.Data;\nimport lombok.experimental.Accessors;\n");

                appendSign(builder);
                builder.append("@Data\n@Accessors(chain = true)\n");

                builder.append("public class ").append(simpleClassName).append(" implements Serializable {\n");
                builder.append("\n    @Serial\n    private static final long serialVersionUID = ").append((long) (Math.random() * Long.MAX_VALUE)).append("L;\n");
                for (Table.Column column : table.getColumns()) {
                    if (column.getIgnored()){
                        continue;
                    }
                    String className = column.getClassName();
                    builder.append("    private ").append(className.substring(className.lastIndexOf('.') + 1)).append(" ").append(StringUtils.smallHump(column.getName())).append(";\n");
                }
                builder.append('}');

                fileWriter.write(builder.toString());
                fileWriter.flush();
            }

        }
//        else {
//            throw new RuntimeException("can't create file: "+ file.getAbsoluteFile());
//        }

    }

    private static void appendSign(StringBuilder builder) {
        builder.append("/**\n *\n * @author nefure\n * @since ").append(TimeUtils.getTime()).append("\n").append(" */\n");
    }


    private void buildMapper(Table table, String prefix, String targetDic, String simpleClassName) throws IOException {
        String path = targetDic + "\\mapper\\" + simpleClassName + "Mapper.java";
        new File(targetDic + "\\mapper\\").mkdirs();
        File file = new File(path);
        if (file.createNewFile()){
            try (FileWriter fileWriter = new FileWriter(path)){
                StringBuilder builder = new StringBuilder();
                builder.append("package ").append(prefix).append(".mapper;\n");
                //添加import语句
                builder.append("import ").append(prefix).append(".entity.").append(simpleClassName).append(";\n");
                builder.append("import org.apache.ibatis.annotations.*;\n").
                        append("import java.io.Serializable;\n").
                        append("import java.util.List;\n");

                appendSign(builder);

                builder.append("public interface ").append(simpleClassName).append("Mapper{\n").
                        append("    String TABLE_NAME = \"`").append(table.getTableName()).append("`\";\n\n");

                // insertOne
                builder.append("    /**\n     * 添加一条记录\n     * @param item 要添加的记录\n     */\n");
                builder.append("    @Options(useGeneratedKeys=true, keyProperty=\"id\", keyColumn=\"id\")\n").
                        append("    @Insert(").
                        append("\"<script>INSERT INTO \" + TABLE_NAME + \" VALUE (");

                appendFields(builder,table.getColumns());

                builder.append(")</script>\")\n");
                builder.append("    void insert(").append(simpleClassName).append(" item);\n\n");

                //insertAll
                builder.append("    /**\n     * 批量添加记录\n     * @param items 要添加的记录集合\n     */\n");
                builder.append("    @Insert(");
                builder.append("\"<script><foreach collection='list' item='item' open=' INSERT INTO \" + TABLE_NAME + \" VALUE' separator=','>(");
                //         #{item.key},#{item.value},,
                appendFields(builder,table.getColumns());
                builder.append(")</foreach></script>\")\n");
                builder.append("    void insert(List<").append(simpleClassName).append("> items);\n\n");

                //deleteById
                builder.append("    /**\n     * 根据id删除一条记录\n     * @param id 主键\n     */\n");
                builder.append("    @Delete(\"DELETE FROM \"+TABLE_NAME+\" WHERE `id`=#{id}\")\n").
                        append("    void delete(Serializable id);\n\n");

                //updateById
                builder.append("    /**\n     * 根据id更新记录\n     * @param item 需要更新的记录信息\n     * @return 影响行数\n     */\n");
                builder.append("    @Update(\"UPDATE \"+TABLE_NAME+\" <set>");
                  //填充set条件
                for (Table.Column column : table.getColumns()) {
                    String fieldName = StringUtils.smallHump(column.getName());
                    builder.append("<if test='item.").append(fieldName).append("!=null'>`").
                            append(column.getName()).append("`=#{item.").append(fieldName).
                            append("},</if>");
                }
                builder.append("</set> WHERE `id` = #{item.id}\")\n");
                builder.append("    int updateById(").append(simpleClassName).append(" item);\n\n");

                //selectById
                builder.append("    /**\n     * 根据id查询记录\n     * @param id 主键\n     * @return 记录封装类\n     */\n");
                builder.append("    @Select(\"SELECT ");
                for (Table.Column column : table.getColumns()) {
                    if (!column.getIgnored()){
                        builder.append('`').append(column.getName()).append('`').append(',');
                    }
                }
                builder.deleteCharAt(builder.length() -1);
                builder.append(" FROM \"+TABLE_NAME+\"WHERE `id`=#{id}\")\n");
                builder.append("    ").append(simpleClassName).append(" selectById(Serializable id);\n\n");

                builder.append('}');

                fileWriter.write(builder.toString());
                fileWriter.flush();
            }
        }
    }

    private void appendFields(StringBuilder builder, List<Table.Column> columns){
        for (Table.Column column : columns) {
            String fieldName = StringUtils.smallHump(column.getName());
            if (column.getIgnored()){
                builder.append("DEFAULT,");
            }
            else {
                builder.append("<choose><when test='item.").
                        append(fieldName).append(" != null'>#{item.").append(fieldName).
                        append("}</when><otherwise> DEFAULT </otherwise></choose>,");
            }
        }
        builder.deleteCharAt(builder.length() -1);
    }


}
