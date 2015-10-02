package de.tu_bs.cs.ifis.sqlgame.sandbox;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import de.tu_bs.cs.ifis.sqlgame.datageneration.DataGenerator;
import de.tu_bs.cs.ifis.sqlgame.dbconnection.*;
import de.tu_bs.cs.ifis.sqlgame.exception.MySQLAlchemistException;
import java.util.Iterator;
import org.h2.tools.DeleteDbFiles;
import de.tu_bs.cs.ifis.sqlgame.xmlparse.Exercise;
import de.tu_bs.cs.ifis.sqlgame.xmlparse.Header;
import de.tu_bs.cs.ifis.sqlgame.xmlparse.Relation;
import java.util.ArrayList;

/**
 * Class Task.
 *
 * Manage a task (create task, close task, update DB, load from DB, check
 * existence)
 *
 * @author Tobias Gruenhagen, Philip Holzhueter, Tobias Runge
 */
public class Task {

    /**
     * Name of the task.
     */
    private String name = "";
    
    /**
     * Name of the task db.
     */
    private String dbName = "";
    
    /**
     * Number of the players who play this task.
     */
    private int players = 0;

    /**
     * ArrayList with the headers of the task.
     */
    private ArrayList<Header> myHeader;
    
    /**
     * ArrayList with the relations of the task.
     */
    private ArrayList<Relation> myRelation;
    
    /**
     * ArrayList with the exercises of the task.
     */
    private ArrayList<Exercise> myExercise;
    
    /**
     * DBConnection to the task db.
     */
    private DBConnection tmpDbConn;
    
    /**
     * DBConnection to the fix db.
     */
    private DBConnection fixDbConn;
    
    /**
     * Config to load dynamic paths.
     */
    private final Config conf = ConfigFactory.load();

    /**
     * Getter for name.
     *
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for dbName.
     *
     * @return String
     */
    public String getDbName() {
        return dbName;
    }

    /**
     * Getter for players.
     *
     * @return int
     */
    public int getPlayers() {
        return players;
    }

    /**
     * Getter for tmpDbConn.
     *
     * @return DBConnection
     */
    public DBConnection getTmpDbConn() {
        return tmpDbConn;
    }
    
    /**
     * Getter for myHeader.
     *
     * @return List
     */
    public ArrayList<Header> getMyHeader() {
        return myHeader;
    }
    
    /**
     * Setter for myHeader.
     *
     * @param myHeader List
     */
    public void setMyHeader(ArrayList<Header> myHeader) {
        this.myHeader = myHeader;
    }
    
    /**
     * Getter for myRelation.
     *
     * @return List
     */
    public ArrayList<Relation> getMyRelation() {
        return myRelation;
    }
    
    /**
     * Setter for myRelation.
     *
     * @param myRelation List
     */
    public void setMyRelation(ArrayList<Relation> myRelation) {
        this.myRelation = myRelation;
    }
    
    /**
     * Getter for myExercise.
     *
     * @return List
     */
    public ArrayList<Exercise> getMyExercise() {
        return myExercise;
    }
    
    /**
     * Setter for myExercise.
     *
     * @param myExercise List
     */
    public void setMyExercise(ArrayList<Exercise> myExercise) {
        this.myExercise = myExercise;
    }

    /**
     * Setter for name.
     *
     * @param name String
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter for dbName.
     *
     * @param dbName String
     */
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    /**
     * Setter for players.
     *
     * @param players int
     */
    public void setPlayers(int players) {
        this.players = players;
    }

    /**
     * Setter for tmpDbConn.
     *
     * @param tmpDbConn DBConnection
     */
    public void setTmpDbConn(DBConnection tmpDbConn) {
        this.tmpDbConn = tmpDbConn;
    }
    
    /**
     * Setter for fixDbConn.
     *
     * @param fixDbConn DBConnection
     */
    public void setFixDbConn(DBConnection fixDbConn) {
        this.fixDbConn = fixDbConn;
    }

    /**
     * Constructor Task.
     * 
     * @param name String, name of the task
     * @param myHeader ArrayList list of the header of the task
     * @param myRelation ArrayList list of the relations of the task
     * @param myExercise ArrayList list of the exercises of the task
     */
    public Task(String name, ArrayList<Header> myHeader, ArrayList<Relation> myRelation, ArrayList<Exercise> myExercise) {
        this.name = name;
        this.dbName = name;
        this.myHeader = myHeader;
        this.myRelation = myRelation;
        this.myExercise = myExercise;
    }
    
    /**
     * Method startTask.
     *
     * Start a new task. It is now checked whether the
     * task already exists or not. If there is a task, the taskoptions are
     * loaded. If not, a new task and a new db is created.
     *
     * @param dbType int, 0-local DB, 1-server DB, 2-in-memory DB
     * @return Task, loaded or created task
     * @throws de.tu_bs.cs.ifis.sqlgame.exception.MySQLAlchemistException Exception for the new database
     *         or the xml syntax check
     */
    public Task startTask(String dbType) throws MySQLAlchemistException {
        try {
            this.fixDbConn = new DBConnection(dbType, this.conf.getString("input.fixDbPath"));
            if (this.checkTask()) {
                this.loadTask();
                
                //Set db for task
                String dbPath = this.conf.getString("input.dbsPath") + this.dbName;
                this.tmpDbConn = new DBConnection(dbType, dbPath);
                
                //Update #players
                int playerNum = this.getPlayers() + 1;
                this.setPlayers(playerNum);
                this.updateTask();
            } else {
                this.players = 1;
                this.createTask(dbType);
            }
        } catch(MySQLAlchemistException se){
            this.closeTask();
            throw new MySQLAlchemistException("Fehler beim starten der Task ", se);
        }

        return this;
    }
    
    /**
     * Method checkTask.
     *
     * Check whether the given task already exists in the db or not.
     *
     * @return boolean, true if the given task exist
     * @throws de.tu_bs.cs.ifis.sqlgame.exception.MySQLAlchemistException Exception for the
     * SQLSelectStatement
     */
    protected boolean checkTask() throws MySQLAlchemistException {
        String selectStatement = "SELECT * FROM Task WHERE name = ?";
        String[] variables = new String[1];
        variables[0] = this.name;

        ArrayList<ArrayList<String>> result = this.fixDbConn.executeSQLSelectPreparedStatement(this.conf.getString("auth.user"), this.conf.getString("auth.pass"), selectStatement, variables);
        return !(result.size() == 1);
    }

    /**
     * Method createTask.
     *
     * Insert a new task-entry into the db and build up a new dbconnection.
     *
     * @throws de.tu_bs.cs.ifis.sqlgame.exception.MySQLAlchemistException Exception for the
     *         new database connection
     */
    private void createTask(String dbType) throws MySQLAlchemistException {
        try {
            String insertStatement = "INSERT INTO Task VALUES('" + this.name + "', '" + this.dbName + "', " + this.players + ")";
            
            this.fixDbConn.executeSQLUpdateStatement(this.conf.getString("auth.user"), this.conf.getString("auth.pass"), insertStatement);
            
            //Set db for task
            String dbPath = this.conf.getString("input.dbsPath") + this.dbName;
            this.tmpDbConn = new DBConnection(dbType, dbPath);
            
            //Insert the given values into the db of the task
            this.insertToDb();
            this.generateData("referenceStatement");
            this.generateData("userData");
        } catch(MySQLAlchemistException se){
            throw new MySQLAlchemistException("Fehler beim erstellen der Task ", se);
        }
    }

    /**
     * Method loadTask.
     *
     * Load a task and its properties from db depending on the taskname. Save
     * the properties in the local attributes.
     *
     * @throws de.tu_bs.cs.ifis.sqlgame.exception.MySQLAlchemistException Exception for the
     *         SQLSelectStatement
     */
    private void loadTask() throws MySQLAlchemistException {
        String selectStatement = "SELECT * FROM Task WHERE name = ?";
        String[] variables = new String[1];
        variables[0] = this.name;

        ArrayList<ArrayList<String>> result = fixDbConn.executeSQLSelectPreparedStatement(this.conf.getString("auth.user"), this.conf.getString("auth.pass"), selectStatement, variables);
        ArrayList<String> tmpList = result.get(1);
        this.name = tmpList.get(0);
        this.dbName = tmpList.get(1);
        this.players = Integer.parseInt(tmpList.get(2));
    }

    /**
     * Method updateTask.
     *
     * Update the properties of the given task in the db with the values of the
     * local attributes.
     *
     * @throws de.tu_bs.cs.ifis.sqlgame.exception.MySQLAlchemistException Exception for the
     *         SQLUpdateStatement
     */
    public void updateTask() throws MySQLAlchemistException {
        String[] variables = new String[3];
        variables[0] = this.dbName;
        variables[1] = "" + this.players;
        variables[2] = this.name;
        String updateStatement = "UPDATE Task SET db_name = ?, players = ? WHERE name = ?";

        this.fixDbConn.executeSQLUpdatePreparedStatement(this.conf.getString("auth.user"), this.conf.getString("auth.pass"), updateStatement, variables);
    }    

    /**
     * Method closeTask.
     *
     * Close a task and increment the number of players playing the given task.
     * If it was the last player that played the task, the taskentry in the db
     * is deleted.
     *
     * @throws de.tu_bs.cs.ifis.sqlgame.exception.MySQLAlchemistException Exception for the
     *         SQLUpdateStatement
     */
    public void closeTask() throws MySQLAlchemistException {
        int playerNum = this.players - 1;
        this.players = playerNum;
        this.updateTask();

        //Delete taskentry in DB if #players = 0
        if (this.players == 0) {
            String deleteStatement = "DELETE FROM Task WHERE name = ?";
            String[] variables = new String[1];
            variables[0] = this.name;

            //Delete the database for the task
            DeleteDbFiles.execute(this.conf.getString("input.dbsPath"), this.dbName, true);

            this.fixDbConn.executeSQLUpdatePreparedStatement(this.conf.getString("auth.user"), this.conf.getString("auth.pass"), deleteStatement, variables);
        }
    }
    
    /**
     * Method insertToDb.
     * 
     * Iterate through the list and insert the contents of the xml-file to the
     * database.
     * 
     * @throws de.tu_bs.cs.ifis.sqlgame.exception.MySQLAlchemistException Exception for the
     *         SQLUpdateStatement
     */
    public void insertToDb() throws MySQLAlchemistException{
        Iterator<Relation> it = myRelation.iterator();
        
        while (it.hasNext()) {
            Relation s = it.next();
            ArrayList<String> tmp = s.getTuple();
            ArrayList<String> a = new ArrayList<>();
            for (int i = 0; i < tmp.size(); i++) {
                a.add(i, tmp.get(i).replace('\"', '\''));
            }
            
            this.tmpDbConn.executeSQLUpdateStatement(this.conf.getString("auth.user"), this.conf.getString("auth.pass"), s.getIntension());
            this.tmpDbConn.executeSQLUpdateStatement(this.conf.getString("auth.user"), this.conf.getString("auth.pass"), a);
        }
    }
    
    /**
     * Method generateFixExtension.
     * 
     * Generates insert statements for the task.
     * 
     * @param generateType String type of generation, can be userData for the generated extension
     *                     or referenceStatement for the reference statements
     * @throws de.tu_bs.cs.ifis.sqlgame.exception.MySQLAlchemistException Exception for the
     *         SQLSelectStatement (reference statement)
     */
    public void generateData(String generateType) throws MySQLAlchemistException {
        DataGenerator dg = new DataGenerator(this.myRelation, this.myExercise, this.tmpDbConn);
        
        //Switch the generate type
        switch (generateType) {
            case "userData": {
                dg.generateFixExtension();
                break;
            }
            
            case "randomData": {
                dg.generateRandomExtension();
                break;
            }
            
            case "referenceStatement": {
                dg.generateSelectExtension();
                break;
            }
        }
        
        dg.generateDataFromGenerationTuples();
    }

    /**
     * Method selectFromDB.
     * 
     * Iterate through the list and execute the Statements of the xml-file in
     * the database.
     * 
     * @throws de.tu_bs.cs.ifis.sqlgame.exception.MySQLAlchemistException Exception for the
     *         SQLSelectStatement
     */
    public void selectFromDb() throws MySQLAlchemistException{
        Iterator<Exercise> it = myExercise.iterator();
        
        while (it.hasNext()) {
            Exercise select = it.next();
            String selectString = select.getReferencestatement().replace('\"', '\'');
            this.tmpDbConn.executeSQLSelectStatement(this.conf.getString("auth.user"), this.conf.getString("auth.pass"), selectString);
        }
    }
    
    /**
     * Method executeUserStatement.
     * 
     * Executes the select statement from the user
     * and returns the result set.
     * 
     * @param statement the SQL user statement
     * @return result list with two lists
     *         first a list with the column names
     *         second a list with the content
     * @throws de.tu_bs.cs.ifis.sqlgame.exception.MySQLAlchemistException Exception for the
     *         SQLSelectStatement
     */
    public ArrayList<ArrayList<String>> executeUserStatement(String statement) throws MySQLAlchemistException{
        ArrayList<ArrayList<String>> result = this.tmpDbConn.executeSQLSelectStatement(this.conf.getString("auth.user"), this.conf.getString("auth.pass"), statement);
        return result;
    }
    
    /**
     * Method isUserStatementCorrect.
     * 
     * Checks wheter the given statement is correct or not.
     * 
     * @param statement String select statement to proof
     * @param subtaskid int id of the subtask the statement is assigned to
     * @return boolean true if the statement is correct, false if not
     * @throws de.tu_bs.cs.ifis.sqlgame.exception.MySQLAlchemistException Exception for the
     *         SQLSelectStatement
     */
    public boolean isUserStatementCorrect(String statement, int subtaskid) throws MySQLAlchemistException {
        ArrayList<ArrayList<String>> userResult = this.executeUserStatement(statement);
        ArrayList<ArrayList<String>> tmpList = new ArrayList<>();
        for (int i = 1; i < userResult.size(); i++){
            tmpList.add(userResult.get(i));
        }
        userResult = (ArrayList<ArrayList<String>>) tmpList.clone();
        tmpList.clear();
        ArrayList<ArrayList<String>> refResult;
        Iterator<Exercise> it = myExercise.iterator();

        while (it.hasNext()) {
            Exercise s = it.next();
            String refStatement;
            
            if (s.getSubTaskId() == subtaskid) {
                refStatement = s.getReferencestatement();
                refResult = this.tmpDbConn.executeSQLSelectStatement(this.conf.getString("auth.user"), this.conf.getString("auth.pass"), refStatement);
                for (int i = 1; i < refResult.size(); i++){
                    tmpList.add(refResult.get(i));
                }
                refResult = (ArrayList<ArrayList<String>>) tmpList.clone();
                tmpList.clear();
                if (s.getEvaluationstrategy().equals("LIST")) {
                    return userResult.equals(refResult);
                } else {
                    boolean equal = false;
                    if (userResult.size() == refResult.size()) {
                        
                        for (ArrayList<String> tmpUser : userResult) {
                            for (ArrayList<String> tmpRef : refResult) {
                                if (tmpUser.equals(tmpRef)) {
                                    equal = true;
                                }
                            }
                            if (equal == false) {
                                return false;
                            } else {
                                equal = false;
                            }
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * Method printData.
     * 
     * Iterate through the list and print the contents of the xml-file.
     */
    public void printData() {

        System.out.println("No of Relations '" + myRelation.size() + "'.");

        Iterator it = myRelation.iterator();

        while (it.hasNext()) {
            System.out.println(it.next().toString());
        }

        System.out.println("No of Exercises '" + myExercise.size() + "'.");

        it = myExercise.iterator();
        while (it.hasNext()) {
            System.out.println(it.next().toString());
        }
        System.out.println("No of Header '" + myHeader.size() + "'.");

        it = myHeader.iterator();
        while (it.hasNext()) {
            System.out.println(it.next().toString());
        }
    }
}
