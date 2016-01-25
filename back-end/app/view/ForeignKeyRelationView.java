package view;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.ForeignKeyRelation;
import play.libs.Json;

/**
 * @author fabiomazzone
 */
class ForeignKeyRelationView {
    public static ForeignKeyRelation fromJsonForm(JsonNode foreignKeyNode) {
        String sourceTable         = foreignKeyNode.path("sourceTable").asText();
        String sourceColumn        = foreignKeyNode.path("sourceColumn").asText();
        String destinationTable    = foreignKeyNode.path("destinationTable").asText();
        String destinationColumn   = foreignKeyNode.path("destinationColumn").asText();

        Integer combinedKeyId          = foreignKeyNode.path("combinedKeyId").asInt();

        return new ForeignKeyRelation(
                sourceTable,
                sourceColumn,
                destinationTable,
                destinationColumn,
                combinedKeyId);

    }

    public static ObjectNode toJson(ForeignKeyRelation foreignKeyRelation) {
        ObjectNode foreignKeyRelationNode = Json.newObject();

        foreignKeyRelationNode.put("id",                    foreignKeyRelation.getId());
        foreignKeyRelationNode.put("sourceTable",           foreignKeyRelation.getSourceTable());
        foreignKeyRelationNode.put("sourceColumn",          foreignKeyRelation.getSourceColumn());
        foreignKeyRelationNode.put("destinationTable",      foreignKeyRelation.getDestinationTable());
        foreignKeyRelationNode.put("destinationColumn",     foreignKeyRelation.getDestinationColumn());

        return foreignKeyRelationNode;
    }
}
