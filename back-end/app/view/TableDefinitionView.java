package view;

import com.fasterxml.jackson.databind.JsonNode;
import models.ColumnDefinition;
import models.TableDefinition;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fabiomazzone
 */
public class TableDefinitionView {
    public static TableDefinition fromJsonForm(JsonNode tableDefinitionNode) {
        JsonNode                ColumnArray         = tableDefinitionNode.path("columnDefinitions");
        String                  tableName           = tableDefinitionNode.path("tableName").asText();
        List<ColumnDefinition>  columnDefinitions   = new ArrayList<>();
        String                  extension           = tableDefinitionNode.path("extensions").asText();
        TableDefinition tableDefinition             = new TableDefinition(tableName, extension);
        for(JsonNode columnNode : ColumnArray) {
            columnDefinitions.add(ColumnDefinitionView.fromJsonForm(columnNode, tableDefinition));
        }
        tableDefinition.setColumnDefinitions(columnDefinitions);
        return tableDefinition;
    }

    public static ObjectNode toJson(TableDefinition tableDefinition) {
        ObjectNode tableNode = Json.newObject();
        ArrayNode columnNode = JsonNodeFactory.instance.arrayNode();

        for(ColumnDefinition column : tableDefinition.getColumnDefinitions()) {
            columnNode.add(ColumnDefinitionView.toJson(column));
        }

        tableNode.put("id", tableDefinition.getId());
        tableNode.put("tableName", tableDefinition.getTableName());
        tableNode.set("columns", columnNode);
        tableNode.put("extensions", tableDefinition.getExtension());

        return tableNode;
    }

    public static String toString(TableDefinition tableDefinition) {
        String columns = "";
        List<ColumnDefinition> columnDefinitions = tableDefinition.getColumnDefinitions();
        for(int i = 0; i < columnDefinitions.size(); i++) {
            columns = columns + ColumnDefinitionView.toString(columnDefinitions.get(i));
            if(i < columnDefinitions.size() - 1)
                columns = columns + ",";
        }

        return "#" + tableDefinition.getTableName() + "{" + columns + "}";
    }

}
