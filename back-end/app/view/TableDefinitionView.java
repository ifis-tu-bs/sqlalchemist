package view;

import models.ColumnDefinition;
import models.TableDefinition;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;

/**
 * @author fabiomazzone
 */
public class TableDefinitionView {

    public static ObjectNode toJson(TableDefinition tableDefinition) {
        ObjectNode tableNode = Json.newObject();
        ArrayNode columnNode = JsonNodeFactory.instance.arrayNode();

        for(ColumnDefinition column : tableDefinition.getColumnDefinitions()) {
            columnNode.add(ColumDefinitionView.toJson(column));
        }

        tableNode.put("id", tableDefinition.getId());
        tableNode.put("tableName", tableDefinition.getTableName());
        tableNode.put("columns", columnNode);
        tableNode.put("extensions", tableDefinition.getExtension());

        return null;
    }
}
