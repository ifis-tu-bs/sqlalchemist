package bootstrap;

import dao.TaskDAO;
import dao.TaskSetDAO;
import dao.UserDAO;

import models.SQLResult;
import models.Task;
import models.TaskSet;
import models.User;

import play.Logger;
import sqlparser.SQLParser;
import sqlparser.SQLStatus;
import view.TaskSetView;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import view.TaskView;

import java.util.List;

/**
 *
 * @author fabiomazzone
 */
public class TaskSetBootstrap {
    public static void init(){
        if(TaskSetDAO.getAll().size() == 0) {
            Logger.info("Initialize 'TaskSet' data");
            User user = UserDAO.getByUsername("sqlalchemist");
            TaskSet taskSet;
            Task task;
            SQLResult result;


            JsonNode node = Json.parse("{\n " +
                                       "   \"taskSetName\":          \"DefaultTaskSet\"," +
                                       "    \"tableDefinitions\": [\n" +
                                       "        {\n" +
                                       "            \"tableName\": \"User\",\n" +
                                       "            \"columnDefinitions\": [\n" +
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
                                       "            \"extension\": \"INSERT INTO User(EMail, FirstName, LastName) VALUES ('fabio.mazzone@me.com', 'Fabio', 'Mazzone');\\n \"\n" +
                                       "        },\n" +
                                       "        {\n" +
                                       "            \"tableName\": \"Profile\",\n" +
                                       "            \"columnDefinitions\": [\n" +
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
                                       "            \"extension\": \"INSERT INTO Profile(user, username) VALUES ('fabio.mazzone@me.com', 'fabiomazzone'); \\n \"\n" +
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
                                       "    \"isHomeWork\":   true\n" +
                                       "}\n");

            taskSet = TaskSetView.fromJsonForm(user, node);
            taskSet.setAvailable(true);
            taskSet.save();
            SQLStatus err;
            if((err = SQLParser.createDB(taskSet)) != null) {
                Logger.warn("TaskSetController.create - " + err.getSqlException().getMessage());
                taskSet.delete();
            }

            node = Json.parse(
                    "{ " +
                    "   \"taskText\":                   \"Find all user\",\n" +
                    "   \"refStatement\":               \"SELECT * FROM User\",\n" +
                    "   \"evaluationStrategy\":         1,\n" +
                    "   \"points\":                     1,\n" +
                    "   \"requiredTerm\":               2,\n" +
                    "   \"availableSyntaxChecks\":      10,\n" +
                    "   \"availableSemanticChecks\":    2\n" +
                    "}");

            task = TaskView.fromJsonForm(node, taskSet.getTaskSetName(), user);

            task.setTaskSet(taskSet);

            result = SQLParser.checkRefStatement(task.getTaskSet(), task);

            task.save();

            if(result.getType() != SQLResult.SUCCESSFULL) {
                Logger.info("RefStatement not runnable");
                task.delete();
            }


            node = Json.parse(
                    "{ " +
                            "   \"taskText\":                   \"Find the user Fabio\",\n" +
                            "   \"refStatement\":               \"SELECT * FROM User Where FirstName = 'Fabio'\",\n" +
                            "   \"evaluationStrategy\":         1,\n" +
                            "   \"points\":                     1,\n" +
                            "   \"requiredTerm\":               2,\n" +
                            "   \"availableSyntaxChecks\":      10,\n" +
                            "   \"availableSemanticChecks\":    2\n" +
                            "}");

            task = TaskView.fromJsonForm(node, taskSet.getTaskSetName(), user);

            task.setTaskSet(taskSet);

            result = SQLParser.checkRefStatement(task.getTaskSet(), task);

            task.save();

            if(result.getType() != SQLResult.SUCCESSFULL) {
                Logger.info("RefStatement not runnable");
                task.delete();
            }


            node = Json.parse("{\n " +
                                       "   \"taskSetName\":          \"DefaultTaskSet\"," +
                                       "    \"tableDefinitions\": [\n" +
                                       "        {\n" +
                                       "            \"tableName\": \"User\",\n" +
                                       "            \"columnDefinitions\": [\n" +
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
                                       "            \"extension\": \"INSERT INTO User(EMail, FirstName, LastName) VALUES ('fabio.mazzone@me.com', 'Fabio', 'Mazzone');\\n \"\n" +
                                       "        },\n" +
                                       "        {\n" +
                                       "            \"tableName\": \"Profile\",\n" +
                                       "            \"columnDefinitions\": [\n" +
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
                                       "            \"extension\": \"INSERT INTO Profile(user, username) VALUES ('fabio.mazzone@me.com', 'fabiomazzone'); \\n \"\n" +
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
                                       "               \"taskSet\":                    1,\n" +
                                       "               \"taskText\":                   \"Find all user\",\n" +
                                       "               \"refStatement\":               \"SELECT * FROM User\",\n" +
                                       "               \"evaluationStrategy\":         1,\n" +
                                       "               \"points\":                     1,\n" +
                                       "               \"requiredTerm\":               2,\n" +
                                       "               \"availableSyntaxChecks\":      10,\n" +
                                       "               \"availableSemanticChecks\":    2\n" +
                                       "       }," +
                                       "       {\n" +
                                       "               \"taskSet\":                    2,\n" +
                                       "               \"taskText\":                   \"Find Me\",\n" +
                                       "               \"refStatement\":               \"SELECT * FROM User Where FirstName = 'Fabio'\",\n" +
                                       "               \"evaluationStrategy\":         1,\n" +
                                       "               \"points\":                     1,\n" +
                                       "               \"requiredTerm\":               2,\n" +
                                       "               \"availableSyntaxChecks\":      10,\n" +
                                       "               \"availableSemanticChecks\":    2\n" +
                                       "       }" +
                                       "   ],\n" +
                                       "    \"isHomeWork\":   false\n" +
                                       "}\n");

            taskSet = TaskSetView.fromJsonForm(user, node);
            taskSet.setAvailable(true);
            taskSet.save();

            if((err = SQLParser.createDB(taskSet)) != null) {
                Logger.warn("TaskSetController.create - " + err.getSqlException().getMessage());
                taskSet.delete();
            }

            node = Json.parse(
                    "{ " +
                            "   \"taskText\":                   \"Find all user\",\n" +
                            "   \"refStatement\":               \"SELECT * FROM User\",\n" +
                            "   \"evaluationStrategy\":         1,\n" +
                            "   \"points\":                     1,\n" +
                            "   \"requiredTerm\":               2,\n" +
                            "   \"availableSyntaxChecks\":      10,\n" +
                            "   \"availableSemanticChecks\":    2\n" +
                            "}");

            task = TaskView.fromJsonForm(node, taskSet.getTaskSetName(), user);

            task.setTaskSet(taskSet);

            result = SQLParser.checkRefStatement(task.getTaskSet(), task);

            task.save();

            if(result.getType() != SQLResult.SUCCESSFULL) {
                Logger.info("RefStatement not runnable");
                task.delete();
            }


            node = Json.parse(
                    "{ " +
                            "   \"taskText\":                   \"Find the user Fabio\",\n" +
                            "   \"refStatement\":               \"SELECT * FROM User Where FirstName = 'Fabio'\",\n" +
                            "   \"evaluationStrategy\":         1,\n" +
                            "   \"points\":                     1,\n" +
                            "   \"requiredTerm\":               2,\n" +
                            "   \"availableSyntaxChecks\":      10,\n" +
                            "   \"availableSemanticChecks\":    2\n" +
                            "}");

            task = TaskView.fromJsonForm(node, taskSet.getTaskSetName(), user);

            task.setTaskSet(taskSet);

            result = SQLParser.checkRefStatement(task.getTaskSet(), task);

            task.save();

            if(result.getType() != SQLResult.SUCCESSFULL) {
                Logger.info("RefStatement not runnable");
                task.delete();
            }

            List<Task> taskList = TaskDAO.getAll();

            for(Task taskI : taskList) {
                taskI.setAvailable(true);
                taskI.update();
            }

            Logger.info("Done initializing");
        }
    }
}
