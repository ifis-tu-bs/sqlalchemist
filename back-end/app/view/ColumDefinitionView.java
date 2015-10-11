package view;

import models.ColumnDefinition;

import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;


/**
 * @author fabiomazzone
 */
public class ColumDefinitionView {
    public static ObjectNode toJson(ColumnDefinition column) {
        ObjectNode columnNode = Json.newObject();

        columnNode.put("id",            column.getId());
        columnNode.put("columnName",    column.getColumnName());
        columnNode.put("data_type",     column.getDataType());
        columnNode.put("primaryKey",    column.isPrimaryKey());
        columnNode.put("notNull",       column.isNotNullable());
        columnNode.put("datagenSet",    column.getDatagenSet());

        return columnNode;
    }

}
