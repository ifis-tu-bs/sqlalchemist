package bootstrap;

import dao.ProfileDAO;

import models.Profile;
import models.TaskSet;

import play.Logger;
import sqlparser.SQLParser;
import sqlparser.SQLStatus;
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
                "                    \"columnName\": \"EMail\",\n" +
                "                    \"dataType\": \"Varchar(255)\",\n" +
                "                    \"primaryKey\": true,\n" +
                "                    \"notNull\": true,\n" +
                "                    \"datagenSet\": 2\n" +
                "                },\n" +
                "                {\n" +
                "                    \"columnName\": \"FirstName\",\n" +
                "                    \"dataType\": \"Varchar(255)\",\n" +
                "                    \"primaryKey\": false,\n" +
                "                    \"notNull\": true,\n" +
                "                    \"datagenSet\": 3\n" +
                "                },\n" +
                "                {\n" +
                "                    \"columnName\": \"LastName\",\n" +
                "                    \"dataType\": \"Varchar(255)\",\n" +
                "                    \"primaryKey\": false,\n" +
                "                    \"notNull\": true,\n" +
                "                    \"datagenSet\": 3\n" +
                "                }\n" +
                "            ],\n" +
                "            \"extensions\": \"INSERT INTO User(EMail, FirstName, LastName) VALUES ('fabio.mazzone@me.com', 'Fabio', 'Mazzone');\\n \"\n" +
                "        },\n" +
                "        {\n" +
                "            \"tableName\": \"Profile\",\n" +
                "            \"columns\": [\n" +
                "                {\n" +
                "                    \"columnName\": \"user\",\n" +
                "                    \"dataType\": \"VARCHAR(255)\",\n" +
                "                    \"primaryKey\": true,\n" +
                "                    \"notNull\": true,\n" +
                "                    \"datagenSet\": 2\n" +
                "                },\n" +
                "                {\n" +
                "                    \"columnName\": \"username\",\n" +
                "                    \"dataType\": \"Varchar(255)\",\n" +
                "                    \"primaryKey\": true,\n" +
                "                    \"notNull\": false,\n" +
                "                    \"datagenSet\": 3\n" +
                "                }\n" +
                "            ],\n" +
                "            \"extensions\": \"INSERT INTO Profile(user, username) VALUES ('fabio.mazzone@me.com', 'fabiomazzone'); \\n \"\n" +
                "        } \n" +
                "    ],\n" +
                "    \"foreignKeyRelations\": [\n" +
                "        {\n" +
                "            \"sourceTable\":\"Profile\",\n" +
                "            \"sourceColumn\":\"user\",\n" +
                "            \"destinationTable\":\"User\",\n" +
                "            \"destinationColumn\":\"EMail\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"tasks\": [" +
                "       {\n" +
                "               \"taskSet\":            1,\n" +
                "               \"taskText\":           \"Find all user\",\n" +
                "               \"refStatement\":       \"SELECT * FROM User\",\n" +
                "               \"evaluationStrategy\": 1,\n" +
                "               \"points\":             1,\n" +
                "               \"requiredTerm\":       2\n" +
                "       }" +
                "   ],\n" +
                "    \"isHomework\":   true\n" +
                "}\n");

        Profile profile = ProfileDAO.getByUsername("admin");

        TaskSet taskSet = TaskSetView.fromJsonForm(profile, node);
        taskSet.save();
        SQLStatus err;
        if((err = SQLParser.checkTaskSetConfiguration(taskSet)) != null) {
            Logger.warn("TaskSetController.create - " + err.getSqlException().getMessage());
            taskSet.delete();
        }
    }
}
