package view;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.ForeignKeyRelation;
import play.libs.Json;

/**
 * @author fabiomazzone
 */
public class ForeignKeyRelationView {
    public static ForeignKeyRelation fromJsonForm(JsonNode foreignKeyNode) {
        String sourceTable         = foreignKeyNode.get("sourceTable").asText();
        String sourceColumn        = foreignKeyNode.get("sourceColumn").asText();
        String destinationTable    = foreignKeyNode.get("destinationTable").asText();
        String destinationColumn   = foreignKeyNode.get("destinationColumn").asText();

        return new ForeignKeyRelation(
                sourceTable,
                sourceColumn,
                destinationTable,
                destinationColumn);

    }

    public static ObjectNode toJson(ForeignKeyRelation foreignKeyRelation) {
        ObjectNode foreignKeyRelationNode = Json.newObject();

        foreignKeyRelationNode.put("sourceTable", foreignKeyRelation.getSourceTable());
        foreignKeyRelationNode.put("sourceColumn", foreignKeyRelation.getSourceColumn());
        foreignKeyRelationNode.put("destinationTable", foreignKeyRelation.getDestinationTable());
        foreignKeyRelationNode.put("destinationColumn", foreignKeyRelation.getDestinationColumn());

        return foreignKeyRelationNode;
    }
}
