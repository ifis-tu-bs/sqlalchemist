package view;

import models.ColumnDefinition;
import models.TableDefinition;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;


/**
 * @author fabiomazzone
 */
class ColumnDefinitionView {
    public static ColumnDefinition fromJsonForm(JsonNode columnNode, TableDefinition tableDefinition) {
        String  columnName      = columnNode.path("columnName").asText();
        String  dataType        = columnNode.path("dataType").asText();
        boolean isPrimaryKey    = columnNode.path("primaryKey").asBoolean();
        boolean isNotNullable   = columnNode.path("notNull").asBoolean();
        int     datagenSet      = columnNode.path("datagenSet").asInt();

        return new ColumnDefinition(tableDefinition, columnName, dataType, isPrimaryKey, isNotNullable, datagenSet);
    }

    public static ObjectNode toJson(ColumnDefinition column) {
        ObjectNode columnNode = Json.newObject();

        columnNode.put("id",            column.getId());
        columnNode.put("columnName",    column.getColumnName());
        columnNode.put("dataType",     column.getDataType());
        columnNode.put("primaryKey",    column.isPrimaryKey());
        columnNode.put("notNull",       column.isNotNullable());
        columnNode.put("foreignKey",    (column.isForeignKey()) ? column.getForeignKey().getTableDefinition().getTableName() : null);
        columnNode.put("datagenSet",    column.getDatagenSet());


        return columnNode;
    }

    public static String toString(ColumnDefinition columnDefinition) {
        String column = columnDefinition.getColumnName();

        if(columnDefinition.isPrimaryKey()) {
            column = "!" + column + "!";
        }

        column = "(" + columnDefinition.getDataType() +  ") " + column;

        if(columnDefinition.isForeignKey()) {
            column = column + "->" + columnDefinition.getForeignKey().getTableDefinition().getTableName();
        }

        return column;
    }
}
