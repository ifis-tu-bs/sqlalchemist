package bootstrap;

import dao.ProfileDAO;

import models.Profile;
import models.TaskSet;

import view.TaskSetView;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;

/**
 *
 * @author fabiomazzone
 */
public class TaskSetBootstrap {
    public static void init(){
        JsonNode node = Json.parse("{\n " +
                "   \"taskSetName\":          \"DefaultTaskSet\"," +
                "    \"tableDefinitions\": [\n" +
                "        {\n" +
                "            \"tableName\": \"User\",\n" +
                "            \"columns\": [\n" +
                "                {\n" +
                "                    \"columnName\": \"id\",\n" +
                "                    \"dataType\": \"bigint\",\n" +
                "                    \"primaryKey\": true,\n" +
                "                    \"notNull\": false,\n" +
                "                    \"datagenSet\": 0\n" +
                "                },\n" +
                "                {\n" +
                "                    \"columnName\": \"EMail\",\n" +
                "                    \"dataType\": \"Varchar(255)\",\n" +
                "                    \"primaryKey\": true,\n" +
                "                    \"notNull\": false,\n" +
                "                    \"datagenSet\": 0\n" +
                "                },\n" +
                "                {\n" +
                "                    \"columnName\": \"FirstName\",\n" +
                "                    \"dataType\": \"Varchar(255)\",\n" +
                "                    \"primaryKey\": false,\n" +
                "                    \"notNull\": false,\n" +
                "                    \"datagenSet\": 0\n" +
                "                },\n" +
                "                {\n" +
                "                    \"columnName\": \"LastName\",\n" +
                "                    \"dataType\": \"Varchar(255)\",\n" +
                "                    \"primaryKey\": false,\n" +
                "                    \"notNull\": false,\n" +
                "                    \"datagenSet\": 0\n" +
                "                }\n" +
                "\n" +
                "            ],\n" +
                "            \"extension\": null\n" +
                "        },\n" +
                "        {\n" +
                "            \"tableName\": \"Profile\",\n" +
                "            \"columns\": [\n" +
                "                {\n" +
                "                    \"columnName\": \"id\",\n" +
                "                    \"dataType\": \"bigint\",\n" +
                "                    \"primaryKey\": true,\n" +
                "                    \"notNull\": false,\n" +
                "                    \"datagenSet\": 0\n" +
                "                },\n" +
                "                {\n" +
                "                    \"columnName\": \"user_id\",\n" +
                "                    \"dataType\": \"bigint\",\n" +
                "                    \"primaryKey\": true,\n" +
                "                    \"notNull\": false,\n" +
                "                    \"datagenSet\": 0\n" +
                "                },\n" +
                "                {\n" +
                "                    \"columnName\": \"username\",\n" +
                "                    \"dataType\": \"Varchar(255)\",\n" +
                "                    \"primaryKey\": true,\n" +
                "                    \"notNull\": false,\n" +
                "                    \"datagenSet\": 0\n" +
                "                }\n" +
                "            ],\n" +
                "            \"extension\": null\n" +
                "        },\n" +
                "        {\n" +
                "            \"tableName\": \"Mir\",\n" +
                "            \"columns\": [\n" +
                "                {\n" +
                "                    \"columnName\": \"id\",\n" +
                "                    \"dataType\": \"bigint\",\n" +
                "                    \"primaryKey\": true,\n" +
                "                    \"notNull\": false,\n" +
                "                    \"datagenSet\": 0\n" +
                "                },\n" +
                "                {\n" +
                "                    \"columnName\": \"user_id\",\n" +
                "                    \"dataType\": \"bigint\",\n" +
                "                    \"primaryKey\": true,\n" +
                "                    \"notNull\": false,\n" +
                "                    \"datagenSet\": 0\n" +
                "                },\n" +
                "                {\n" +
                "                    \"columnName\": \"mirName\",\n" +
                "                    \"dataType\": \"Varchar(255)\",\n" +
                "                    \"primaryKey\": true,\n" +
                "                    \"notNull\": false,\n" +
                "                    \"datagenSet\": 0\n" +
                "                }\n" +
                "            ],\n" +
                "            \"extension\": null\n" +
                "        }\n" +
                "    ],\n" +
                "    \"foreignKeys\": [\n" +
                "        {\n" +
                "            \"sourceTable\":\"Profile\",\n" +
                "            \"sourceColumn\":\"user_id\",\n" +
                "            \"destinationTable\":\"User\",\n" +
                "            \"destinationColumn\":\"id\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"sourceTable\":\"Mir\",\n" +
                "            \"sourceColumn\":\"user_id\",\n" +
                "            \"destinationTable\":\"User\",\n" +
                "            \"destinationColumn\":\"id\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"tasks\": [" +
                "       {\n" +
                "               \"taskSet\":            1,\n" +
                "               \"taskText\":           \"Find all user\",\n" +
                "               \"refStatement\":       \"SELECT * FROM User\",\n" +
                "               \"evaluationstrategy\":       1,\n" +
                "               \"points\":             1,\n" +
                "               \"requiredTerm\":       2\n" +
                "       }" +
                "   ],\n" +
                "    \"isHomework\":   true\n" +
                "}\n");

        Profile profile = ProfileDAO.getByUsername("admin");

        TaskSet taskSet = TaskSetView.fromJsonForm(profile, node);
        taskSet.save();
    }
}
