package controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * @author fabiomazzone
 */
public class ColumnDefinitionController extends Controller{

    public Result dataTypes() {
        ArrayNode dataTypesNode = JsonNodeFactory.instance.arrayNode();
        String[] dataTypes = {"VARCHAR(255)", "BOOLEAN", "INTEGER", "BIGINT", "DECIMAL", "NUMERIC", "FLOAT", "DATE", "TIME", "TIMESTAMP"};
        for(String dataType : dataTypes) {
            ObjectNode dataTypeNode = Json.newObject();

            dataTypeNode.put("dataType",    dataType);

            dataTypesNode.add(dataTypeNode);
        }
        return ok(dataTypesNode);
    }
}
