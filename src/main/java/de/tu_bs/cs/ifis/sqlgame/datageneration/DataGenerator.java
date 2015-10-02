package de.tu_bs.cs.ifis.sqlgame.datageneration;

import com.typesafe.config.*;

import de.tu_bs.cs.ifis.sqlgame.dbconnection.DBConnection;
import de.tu_bs.cs.ifis.sqlgame.exception.MySQLAlchemistException;
import de.tu_bs.cs.ifis.sqlgame.exception.MyXMLParserErrorHandler;
import de.tu_bs.cs.ifis.sqlgame.xmlparse.Exercise;
import de.tu_bs.cs.ifis.sqlgame.xmlparse.Relation;

import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;


/**
 * Class DataGenerator.
 * 
 * Class to generate custom insert statements for the tabes of a task.
 * 
 * @author Tobias Gruenhagen, Philip Holzhueter, Tobias Runge
 */
public class DataGenerator {
    
    /**
     * List with the relations of the task.
     */
    private final ArrayList<Relation> relations;
    
    /**
     * List with the exercises of the task.
     */
    private final ArrayList<Exercise> exercises;
    
    /**
     * Database connection to speak with the database.
     */
    private final DBConnection dbConn;
    
    /**
     * Number of columns of the actual relation.
     */
    private int columns = 0;
    
    /**
     * ArrayList of the primary keys assigned to the column index.
     * 
     * Form: [[first primary key name, column index],
     *        [second primary key name, column index], ...]
     */
    private ArrayList<ArrayList<String>> primaryKeyAssignments = new ArrayList<>();
    
    /**
     * ArrayList of the primary values.
     * 
     * Form: [[first primary key name, second primary key name, ...],
     *        [first primary key value 1, second primary key value 1, ...],
     *        [first primary key value 2, second primary key value 2, ...], ...]
     */
    private ArrayList<ArrayList<String>> primaryKeyValues = new ArrayList<>();
    
    /**
     * ArrayList of the reference values.
     * 
     * Form: [[[first reference name], [first reference value 1],
     *         [first reference value 1], ...]
     *        [[second reference name], [second reference value 1]
     *         [second reference value 1], ...], ...]
     */
    private ArrayList<ArrayList<ArrayList<String>>> referenceValues = new ArrayList<>();
    
    /**
     * Config to load dynamic paths.
     */
    private final Config conf = ConfigFactory.load();
    
    
    /**
     * Constructor DataGenerator.
     * 
     * Initialize the local attributes relations and dbConn with the given
     * parameter.
     * 
     * @param relations ArrayList list with the relations of the task
     * @param exercises ArrayList list with the exercises of the task
     * @param dbConn DBConnection database connection to execute sql statements
     */
    public DataGenerator(ArrayList<Relation> relations, ArrayList<Exercise> exercises, DBConnection dbConn) {
        this.relations = relations;
        this.exercises = exercises;
        this.dbConn = dbConn;
    }
    
    /**
     * Method generateFixExtension.
     * 
     * @throws de.tu_bs.cs.ifis.sqlgame.exception.MySQLAlchemistException exception
     *         thrown if there is an error executing the insert statement on db
     */
    public void generateFixExtension() throws MySQLAlchemistException {
        //Nothing to do
    }
    

    /**
     * Method generateRandomExtension.
     * 
     * Method to generate random insert statements.
     * 
     * @throws de.tu_bs.cs.ifis.sqlgame.exception.MySQLAlchemistException exception
     *         thrown if there is an error executing the insert statement on db
     */
    public void generateRandomExtension() throws MySQLAlchemistException {
        //Iterate through all relations
        for (Relation rel : this.relations) {
            String generationTuple = "10;none";
            for (ArrayList columnInformation : rel.getColumnInformation()) {
                String columnInformationString;
                
                if (columnInformation.get(3) != null) {
                    //Ref
                    StringTokenizer st = new StringTokenizer((String) columnInformation.get(3), "(");
                    columnInformationString = "ref," + st.nextToken() + "," + st.nextToken().replace(")", "") ;
                } else {
                    //No ref
                    String type = ((String) columnInformation.get(1)).toLowerCase();
                    if (type.contains("int")) {
                        columnInformationString = "random,int";
                    } else if (type.contains("tinyint")) {
                        columnInformationString = "between,-128,127";
                    } else if (type.contains("smallint")) {
                        columnInformationString = "between,-32768,32767";
                    } else if (type.contains("bigint")) {
                        columnInformationString = "between,-9223372036854775808,9223372036854775807";
                    } else if (type.contains("double")) {
                        columnInformationString = "random,double";
                    } else if (type.contains("varchar")) {
                        int length = Integer.parseInt(type.replaceAll("\\D", ""));
                        Random r = new Random();
                        int random = r.nextInt(length - 5) + 5;
                        columnInformationString = "random,string," + random;
                    } else if (type.contains("boolean")) {
                        columnInformationString = "random,boolean,15";
                    } else if (type.contains("date")) {
                        columnInformationString = "random,date,15";
                    } else {
                        columnInformationString = "random,string,15";
                    }
                }
                
                generationTuple += ";" + columnInformationString;
            }
            
            rel.setDataGeneration(generationTuple);
        }
    }
    
    /**
     * Method generateSelectExtension.
     * 
     * Grab the reference select statements and generate an extension so that
     * there is always enough data if the select statement is executed.
     * 
     * @throws de.tu_bs.cs.ifis.sqlgame.exception.MySQLAlchemistException exception
     *         thrown if there is an error executing the insert statement on db
     */
    public void generateSelectExtension() throws MySQLAlchemistException {
        //Iterate through all exercises of the task
        for (Exercise exe : this.exercises) {
            String selectStatementString = exe.getReferencestatement();
            SQLSelectParser sqlsp = new SQLSelectParser(selectStatementString);
            
            //Get the table name of the select statement
            String tableName = sqlsp.getFromInformation();
            
            //Get the related relation
            Relation relation = null;
            for (Relation rel : this.relations) {
                if (rel.getTableName().equals(tableName.toLowerCase())) {
                    relation = rel;
                }
            }
            
            if (relation != null) {
                //Get the information of the where clause
                ArrayList<ArrayList<String>> whereInformation = sqlsp.getWhereInformation();
                //Generate the insert statements
                if (!whereInformation.isEmpty()) {
                    for (ArrayList<String> whereInformationRow : whereInformation) {
                        String generationTuple = this.createGenerationTuple(relation.getColumnInformation(), whereInformationRow.get(0), whereInformationRow.get(1), whereInformationRow.get(2));
                        relation.setDataGeneration(generationTuple);
                    }
                }
            }
        }
    }
    
    /**
     * Method createGenerationTuple.
     * 
     * Create a generation tuple with the information given by a where clause
     * of a reference statement.
     * 
     * @param columnInformation ArrayList information of each column
     *                          of the relation relatd to the refrence statement
     * @param columnName String name of the column for which data should be created
     * @param compare String comparison type of the where clause of the reference statement
     * @param value String comparison value of the where clause of the reference statement
     * @return String generated generation tuple
     * @throws de.tu_bs.cs.ifis.sqlgame.exception.MySQLAlchemistException exception
     *         thrown if there is an error executing the insert statement on db
     */
    public String createGenerationTuple(ArrayList<ArrayList> columnInformation, String columnName, String compare, String value) throws MySQLAlchemistException {
        String result = "3;none;";
        for (ArrayList tmpList : columnInformation) {
            if (tmpList.get(0).equals(columnName)) {
                switch (compare) {
                    case ("="): {
                        result += "fix," + value + ";";
                        break;
                    }
                    
                    case (">"):
                    case (">="): {
                        if (tmpList.get(1).equals("int") || tmpList.get(1).equals("INT") || tmpList.get(1).equals("INTEGER")) {
                            result += "min,int," + value + ";";
                        } else if (tmpList.get(1).equals("double")|| tmpList.get(1).equals("DOUBLE")) {
                            result += "min,double," + value + ";";
                        }
                        break;
                    }
                    
                    case ("<"):
                    case ("<="): {
                        if (tmpList.get(1).equals("int") || tmpList.get(1).equals("INT") || tmpList.get(1).equals("INTEGER")) {
                            result += "max,int," + value + ";";
                        } else if (tmpList.get(1).equals("double")|| tmpList.get(1).equals("DOUBLE")) {
                            result += "max,double," + value + ";";
                        }
                        break;
                    }
                    
                    case ("like"):
                    case ("LIKE"): {
                        result += "fix," + value + ";";
                        break;
                    }
                }
            } else {
                if (tmpList.get(3) == null) {
                    switch ((String) tmpList.get(1)) {
                        case ("INTEGER"):
                        case ("INT"):
                        case ("int"): {
                            result += "random,int+;";
                            break;
                        }
                        
                        case ("DOUBLE"):
                        case ("double"): {
                            result += "random,double;";
                            break;
                        }
                        
                        case ("DATE"):
                        case ("date"): {
                            result += "random,date;";
                            break;
                        }
                        
                        case ("BOOL"):
                        case ("BOOLEAN"):
                        case ("bool"):
                        case ("boolean"): {
                            result += "random,boolean;";
                            break;
                        }
                        default:{
                            String type = (String) tmpList.get(1);
                            if (type.contains("varchar") || type.contains("VARCHAR")) {
                                result += "random,string;";
                            }
                            break;
                        }
                    }
                } else {
                    String refString = (String) tmpList.get(3);
                    refString = refString.replace("(", ",");
                    refString = refString.replace(")", "");
                    result += "ref," + refString + ";";
                }
            }
        }
        int length = result.length();
        result = result.substring(0, length - 1);
        return result;
    }
    
    /**
     * Method generateDataFromGenerationTuples.
     * 
     * Generate data and insert statements for the given relation with the given
     * generation tuple.
     * 
     * @throws de.tu_bs.cs.ifis.sqlgame.exception.MySQLAlchemistException exception
     *         thrown if there is an error executing the insert statement on db
     */
    public void generateDataFromGenerationTuples() throws MySQLAlchemistException {
        //Iterate through all relations of the task
        for (Relation rel : this.relations) {
            //Break if no data has to be generated
            if (rel.getDataGeneration().isEmpty()) {
                break;
            }
            //Calculate the column number and the primary key assignments of
            //the actual relation
            this.calculateColumns(rel);
            this.calculatePrimaryKeyAssignments(rel);
            
            //Iterate through all data constraints
            for (String generationTuple : rel.getDataGeneration()) {
                StringTokenizer st = new StringTokenizer(generationTuple, ";");

                //First token for the number function
                String numberFunction = st.nextToken();

                //Second token for the reference function
                String refFunction = st.nextToken();
                ArrayList<String> refFunctionList = new ArrayList<>();
                StringTokenizer stt = new StringTokenizer(refFunction, ",");
                while (stt.hasMoreTokens()) {
                    refFunctionList.add(stt.nextToken());
                }

                //The following tokens for the column function of the relation
                ArrayList<ArrayList<String>> columnFunctions = new ArrayList<>();
                while (st.hasMoreTokens()) {
                    ArrayList<String> columnFunction = new ArrayList<>();
                    stt = new StringTokenizer(st.nextToken(), ",");
                    while (stt.hasMoreTokens()) {
                        columnFunction.add(stt.nextToken());
                    }
                    columnFunctions.add(columnFunction);
                }

                //Fill primary key values list
                //Build the sql select statement to get the primary key columns
                String primaryKeyColumns = "*";
                int i = 0;
                for (String primaryKey : rel.getPrimaryKey()) {
                    if (i == 0) {
                        primaryKeyColumns = primaryKey;
                    } else {
                        primaryKeyColumns += ", " + primaryKey;
                    }
                    i++;
                }
                //Execute the sql select statement to get the primary key columns
                this.primaryKeyValues = this.dbConn.executeSQLSelectStatement(
                        this.conf.getString("auth.user"),
                        this.conf.getString("auth.pass"),
                        "SELECT " + primaryKeyColumns + " FROM " + rel.getTableName()
                );

                //Generate and execute the insert statements with the grabed data
                int number = calculateNumber(numberFunction);
                this.generateDataFromFunction(rel, number, refFunctionList, columnFunctions);
            }
            
            //Reset the primarykey and reference lists for the new data constraint
            this.primaryKeyValues = new ArrayList<>();
            this.primaryKeyAssignments = new ArrayList<>();
            this.referenceValues = new ArrayList<>();
        }
    }
    
    /**
     * Method calculatePrimaryKeyAssignments.
     * 
     * Get the primary keys of the given relation and assigne its column index
     * to it.
     * 
     * @param relation Relation relation for wich the primary key assignments
     *                 should be created
     * @throws de.tu_bs.cs.ifis.sqlgame.exception.MySQLAlchemistException exception
     *         thrown if there is an error executing the insert statement on db
     */
    private void calculatePrimaryKeyAssignments(Relation relation) throws MySQLAlchemistException {
        //Get the column names from the given relation/table
        String selectStatement = "SELECT * FROM " + relation.getTableName();
        ArrayList<String> columnNames = this.dbConn.executeSQLSelectStatement(
                this.conf.getString("auth.user"),
                this.conf.getString("auth.pass"),
                selectStatement
        ).get(0);
        
        //Iterate through the list of the column names and build a new
        //primary key assignment for every primary key column
        ArrayList<String> primaryKeyAssignment;
        int i = 0;
        for (String columnName : columnNames) {
            //New primary key assignment if the actual column is a primary key
            if (relation.getPrimaryKey().contains(columnName.toLowerCase())) {
                primaryKeyAssignment = new ArrayList<>();
                primaryKeyAssignment.add(columnName);
                primaryKeyAssignment.add("" + i);
                this.primaryKeyAssignments.add(primaryKeyAssignment);
            }
            i++;
        }
    }
    
    /**
     * Method calculate Columns.
     * 
     * Method to calculate the columns of the given relation/table.
     * 
     * @param relation Relation relation for which the columns should be calculated
     */
    private void calculateColumns(Relation relation) {
        //Count the tokens of the given metadata row (this is the column number
        //minus the token for the number and the token for the reference type
        String tuple = relation.getDataGeneration().get(0);
        StringTokenizer st = new StringTokenizer(tuple, ";");
        this.columns = st.countTokens() - 2;
    }
    
    /**
     * Method calculateNumber.
     * 
     * Generate a number from the given number function. This can be just an
     * integer value of for example a random integer between two values.
     * 
     * @param numberFunction String function to calculate the number
     * @return int number that is calculated
     * @throws de.tu_bs.cs.ifis.sqlgame.exception.MySQLAlchemistException exception
     *         thrown if there is an error executing the insert statement on db
     */
    private int calculateNumber(String numberFunction) throws MySQLAlchemistException {
        StringTokenizer st = new StringTokenizer(numberFunction, ",");
        //Get the function name of the number function
        String functionName = st.nextToken();
        //Get the parameter of the number function
        ArrayList<String> params = new ArrayList<>();
        //If the number function has parameter add them to the list
        //if not, return the function name which is the number
        if (st.hasMoreTokens()) {
            params.add(st.nextToken());
            params.add(st.nextToken());
        } else {
            return Integer.parseInt(functionName);
        }
        
        //Calculate the number by the specific function and parameter
        int number = 1;
        switch(functionName) {
            //Calculate a number in a specific span of two given integer
            case "span": {
                if (params.size() == 2) {
                    int param1 = Integer.parseInt(params.get(0));
                    int param2 = Integer.parseInt(params.get(1));
                    Random rand = new Random();
                    number = rand.nextInt(param2) + param1;
                } else {
                    throw new MySQLAlchemistException("Es werden zwei Parameter bei der Spannen-Funktion benötigt.", new Exception());
                }
                
                break;
            }
        }
        
        return number;
    }
    
    /**
     * Method generateDataFromFunction.
     * 
     * Generate and execute the insert statements with the given data.
     * 
     * @param tableName String name of the given table
     * @param numberFunction number of the inserted statements
     * @param refFunctionList ArrayList list of the reference function
     * @param columnFunctions ArrayList list of the column functions
     * @throws de.tu_bs.cs.ifis.sqlgame.exception.MySQLAlchemistException exception
     *         thrown if there is an error executing the insert statement on db
     */
    private void generateDataFromFunction(Relation rel, int number, ArrayList<String> refFunctionList, ArrayList<ArrayList<String>> columnFunctions) throws MySQLAlchemistException {
        //Iterate through the columnFunctions to save the referencing columns
        int i = 0;
        for (ArrayList<String> columnFunction : columnFunctions) {
            //If the actual column is a referencing column, get the referenced values
            //and save them in the local list
            if (columnFunction.get(0).equals("ref")) {
                ArrayList<ArrayList<String>> referenceColumn = this.dbConn.executeSQLSelectStatement(
                        this.conf.getString("auth.user"),
                        this.conf.getString("auth.pass"),
                        "SELECT " + columnFunction.get(2) + " FROM " + columnFunction.get(1)
                );
                String columnName = referenceColumn.get(0).get(0);
                //Replace the column name of the referenced value by the unique pair of table name and column name
                referenceColumn.get(0).set(0, columnFunction.get(1) + "." + columnName.toLowerCase());
                this.referenceValues.add(referenceColumn);
            }
            i++;
        }
        
        //Switch the given reference type
        switch (refFunctionList.get(0)) {
            //No reference type
            case "none": {
                //Generate insert statements
                this.generateInsertStatements("none", rel, number, columnFunctions, null, 0);
                
                break;
            }
            
            case "refAll": {
                String refTableName = refFunctionList.get(1);
                String refColumnName = refFunctionList.get(2);
                
                ArrayList<ArrayList<String>> referenceList = new ArrayList<>();
                i = 0;
                for (ArrayList<ArrayList<String>> referenceValue : this.referenceValues) {
                    if (referenceValue.get(0).get(0).equals(refTableName + "." + refColumnName)) {
                        referenceList = this.referenceValues.get(i);
                    }
                    i++;
                }
                
                //Iterate through every referenced value
                for (i = 1; i < referenceList.size(); i++) {
                    //Generate insert statements
                    this.generateInsertStatements("refAll", rel, number, columnFunctions, referenceList, i);
                }
                
                break;
            }
            
            case "refRandom": {
                String refTableName = refFunctionList.get(1);
                String refColumnName = refFunctionList.get(2);
                
                ArrayList<ArrayList<String>> referenceList = new ArrayList<>();
                i = 0;
                for (ArrayList<ArrayList<String>> referenceValue : this.referenceValues) {
                    if (referenceValue.get(0).get(0).equals(refTableName + "." + refColumnName)) {
                        referenceList = this.referenceValues.get(i);
                    }
                    i++;
                }
                
                //Generate insert statements
                this.generateInsertStatements("refRandom", rel, number, columnFunctions, referenceList, 0);
                
                break;
            }
        }
    }
    
    /**
     * Method generate InsertStatements.
     * 
     * Generate the insert statements with the given data.
     * 
     * @param generateType String type of generation strategy: none, refAll, refRandom
     * @param tableName String name of the given table
     * @param number int number of the insert statements to be generated
     * @param columnFunctions ArrayList list of the column functions
     * @param referenceList ArrayList list of the referenced values
     * @param referenceIndex int index of the reference value if generateType is refAll
     * @throws de.tu_bs.cs.ifis.sqlgame.exception.MySQLAlchemistException exception
     *         thrown if there is an error executing the insert statement on db
     */
    private void generateInsertStatements(
            String generateType,
            Relation rel,
            int number,
            ArrayList<ArrayList<String>> columnFunctions,
            ArrayList<ArrayList<String>> referenceList,
            int referenceIndex
    ) throws MySQLAlchemistException {
        //Create and execute "number" times insert statements 
        for (int i = 1; i <= number; i++) {
            //New Stringarray for the inserted data
            String[] dataRow = new String[this.columns];

            //Iterate through the column functions to generate a value for every column
            int j = 0;
            for (ArrayList<String> columnFunction : columnFunctions) {
                //Handle the normal columns that are not a primary key
                if (!this.primaryKeyValues.get(0).contains(columnFunction.get(0))) {
                    //Get the parameter for the column function
                    ArrayList<String> params = new ArrayList<>();
                    int k = 0;
                    for (String param : columnFunction) {
                        if (k != 0) {
                            params.add(param);
                        }
                        k++;
                    }

                    //Get a value for the actual column
                    if (!columnFunction.get(0).equals("ref")) {
                        //Generate a new value if it is no referencing column
                        dataRow[j] = this.findAndExecuteFunction(columnFunction.get(0), params);
                    } else {
                        if (generateType.equals("refAll") || generateType.equals("refRandom")) {
                            //Proof if the referencing column is the reference function column
                            if (referenceList.get(0).get(0).equals(columnFunction.get(1) + "." + columnFunction.get(2))) {
                                if (generateType.equals("refAll")) {
                                    //Get one value by order of the reference list
                                    dataRow[j] = referenceList.get(referenceIndex).get(0);
                                }
                                if (generateType.equals("refRandom")) {
                                    //Get one value by random of the reference list
                                    Random rd = new Random();
                                    int randomInt = rd.nextInt(referenceList.size() - 1) + 1;
                                    dataRow[j] = referenceList.get(randomInt).get(0);
                                }
                            } else {
                                //Get a value of the reference list if it is a referencing column
                                //Iterate through reference value list
                                for (ArrayList<ArrayList<String>> referenceValue : this.referenceValues) {
                                    //Get a random value if the actual referenceValue belongs to the actual column
                                    if (referenceValue.get(0).get(0).equals(columnFunction.get(1) + "." + columnFunction.get(2))) {
                                        Random rd = new Random();
                                        int randomInt = rd.nextInt(referenceValue.size() - 1) + 1;
                                        dataRow[j] = referenceValue.get(randomInt).get(0);
                                    }
                                }
                            }
                        } else {
                            //Get a value of the reference list if it is a referencing column
                            //Iterate through reference value list
                            for (ArrayList<ArrayList<String>> referenceValue : this.referenceValues) {
                                //Get a random value if the actual referenceValue belongs to the actual column
                                if (referenceValue.get(0).get(0).equals(columnFunction.get(1) + "." + columnFunction.get(2))) {
                                    Random rd = new Random();
                                    int randomInt = rd.nextInt(referenceValue.size() - 1) + 1;
                                    dataRow[j] = referenceValue.get(randomInt).get(0);
                                }
                            }
                        }
                    }
                }
                j++;
            }

            //Handle the primary keys and search for one until it is a unique one
            boolean primaryKeyExists = true;
            ArrayList<String> primaryKey = new ArrayList<>();
            j = 0;
            while (primaryKeyExists) {
                //Reset the primary key
                primaryKey = new ArrayList<>();
                //Iterate through all primary keys
                for (ArrayList<String> primaryKeyAssignment : this.primaryKeyAssignments) {
                    int columnIndex = Integer.parseInt(primaryKeyAssignment.get(1));

                    //Get the parameter for the column function
                    ArrayList<String> params = new ArrayList<>();
                    int k = 0;
                    for (String param : columnFunctions.get(columnIndex)) {
                        if (k != 0) {
                            params.add(param);
                        }
                        k++;
                    }

                    //Get a value for the actual column
                    if (!columnFunctions.get(columnIndex).get(0).equals("ref")) {
                        //Generate a new value if it is no referencing column
                        primaryKey.add(this.findAndExecuteFunction(columnFunctions.get(columnIndex).get(0), params));
                    } else {
                        if (generateType.equals("refAll") || generateType.equals("refRandom")) {
                            //Proof if the referencing column is the reference function column
                            if (referenceList.get(0).get(0).equals(columnFunctions.get(columnIndex).get(1) + "." + columnFunctions.get(columnIndex).get(2))) {
                                if (generateType.equals("refAll")) {
                                    //Get one value by order of the reference list
                                    primaryKey.add(referenceList.get(referenceIndex).get(0));
                                }
                                if (generateType.equals("refRandom")) {
                                    //Get one value by random of the reference list
                                    Random rd = new Random();
                                    int randomInt = rd.nextInt(referenceList.size() - 1) + 1;
                                    primaryKey.add(referenceList.get(randomInt).get(0));
                                }
                            } else {
                                //Get a value of the reference list if it is a referencing column
                                //Iterate through reference value list
                                for (ArrayList<ArrayList<String>> referenceValue : this.referenceValues) {
                                    //Get a random value if the actual referenceValue belongs to the actual column
                                    if (referenceValue.get(0).get(0).equals(columnFunctions.get(columnIndex).get(1) + "." + columnFunctions.get(columnIndex).get(2))) {
                                        Random rd = new Random();
                                        int randomInt = rd.nextInt(referenceValue.size() - 1) + 1;
                                        primaryKey.add(referenceValue.get(randomInt).get(0));
                                    }
                                }
                            }
                        } else {
                            //Get a value of the reference list if it is a referencing column
                            //Iterate through reference value list
                            for (ArrayList<ArrayList<String>> referenceValue : this.referenceValues) {
                                //Get a random value if the actual referenceValue belongs to the actual column
                                if (referenceValue.get(0).get(0).equals(columnFunctions.get(columnIndex).get(1) + "." + columnFunctions.get(columnIndex).get(2))) {
                                    Random rd = new Random();
                                    int randomInt = rd.nextInt(referenceValue.size() - 1) + 1;
                                    primaryKey.add(referenceValue.get(randomInt).get(0));
                                }
                            }
                        }
                    }
                }

                //Leave the while scope if the new primary key does not exist
                if (!this.primaryKeyValues.contains(primaryKey)) {
                    primaryKeyExists = false;
                }

                //Throw an exception if no primary key is found after 200 iterations
                if (j > 200) {
                    throw new MySQLAlchemistException("Kein freier Primary-Key gefunden.", new Exception());
                }

                j++;
            }

            //Insert the new primary key in the data row
            j = 0;
            for (ArrayList<String> primaryKeyAssignment : this.primaryKeyAssignments) {
                int columnIndex = Integer.parseInt(primaryKeyAssignment.get(1));
                dataRow[columnIndex] = primaryKey.get(j);
                j++;
            }
            //Add the new primary to the primary key values of the actual relation/table
            this.primaryKeyValues.add(primaryKey);

            //Execute an insert statement based on the data row
            this.executeInsertStatements(rel, dataRow);
        }
    }
    
    /**
     * Method generateAndExecuteInsertStatement.
     * 
     * Generate and execute an insert statement with the given data row into
     * the given table.
     * 
     * @param tableName String name of the table in which the data is inserted
     * @param dataRow String[] data which is inserted
     * @throws de.tu_bs.cs.ifis.sqlgame.exception.MySQLAlchemistException exception
     *         thrown if there is an error executing the insert statement on db
     */
    private void executeInsertStatements(Relation rel, String[] dataRow) throws MySQLAlchemistException {
        //Build the insert statement as a string from the data row stringarray
        String insertedValues = "";
        for (int i = 0; i < dataRow.length; i++) {
            String data = dataRow[i];
            
            //Add "'" to a varchar, date or something else that is not int or double
            String columnType = ((String) rel.getColumnInformation().get(i).get(1)).toLowerCase();
            CharSequence csInt = "int";
            CharSequence csDouble = "double";
            if (!(columnType.contains(csInt) || columnType.contains(csDouble))) {
                data = "'" + data + "'";
            }
            
            //Only insert no comma if it is the first value
            if (i == 0) {
                insertedValues += data;
            } else {
                insertedValues += ", " + data;
            }
        }
        
        //Excute the insert statement
        String insert = "INSERT INTO " + rel.getTableName() + " VALUES(" + insertedValues + ")";
        try {
        this.dbConn.executeSQLUpdateStatement(
                this.conf.getString("auth.user"),
                this.conf.getString("auth.pass"),
                insert
        );
        } catch(MySQLAlchemistException e){
            MyXMLParserErrorHandler.addError("Das generierte Insert-Statement konnt nicht ohne Fehler ausgeführt werden. Statement: " + insert);
        }
    }
    
    /**
     * Method findAndExecuteFunction.
     * 
     * This method finds the specific function to generate data from the
     * given function name and other parameters.
     * 
     * @param functionName the name of the fuction
     * @param params a list of paramters for the function
     * @return string with the generated data
     * @throws de.tu_bs.cs.ifis.sqlgame.exception.MySQLAlchemistException
     *         exception thrown if the user gives not the correct parameters
     */
    private String findAndExecuteFunction(String functionName, ArrayList<String> params) throws MySQLAlchemistException{
        String result = "";
        GenerateSpecificData gd = new GenerateSpecificData();
        switch (functionName) {
            case("random"): {
                String para = params.get(0);
                switch (para) {
                    case("int+"):{
                        result = gd.generateIntegerPos();
                        break;
                    }
                    case("int"):{
                        result = gd.generateInteger();
                        break;
                    }
                    case("double"):{
                        result = gd.generateDouble();
                        break;
                    }
                    case("string"):{
                        int para2 = 5;
                        if(params.size() == 2){
                        para2 = Integer.parseInt(params.get(1));
                        }
                        result = gd.generateString(para2);
                        break;
                    }
                    case("word"):{
                        int para2 = 5;
                        if(params.size() == 2){
                        para2 = Integer.parseInt(params.get(1));
                        }
                        result = gd.generateWord(para2);
                        break;
                    }
                    case("text"):{
                        int para2 = 20;
                        if(params.size() == 2){
                        para2 = Integer.parseInt(params.get(1));
                        }
                        result = gd.generateText(para2);
                        break;
                    }
                    case("firstname"):{
                        result = gd.generateFirstName();
                        break;
                    }
                    case("lastname"):{
                        result = gd.generateLastName();
                        break;
                    }
                    case("fullname"):{
                        result = gd.generateFullName();
                        break;
                    }
                    case("date"):{
                        result = gd.generateDate();
                        break;
                    }
                    case("business"):{
                        result = gd.generateBusinessName();
                        break;
                    }
                    case("street"):{
                        result = gd.generateStreetName();
                        break;
                    }
                    case("city"):{
                        result = gd.generateCity();
                        break;
                    }
                    case("adress"):{
                        result = gd.generateAdress();
                        break;
                    }
                    case("email"):{
                        result = gd.generateEmail();
                        break;
                    }
                    case("boolean"):{
                        result = gd.generateBoolean();
                        break;
                    }
                    default:{
                        int random = 100;
                        String def = "NULL";
                        if(params.size() >= 2){
                            random = Integer.parseInt(params.get(1));
                            if(params.size() >= 3){
                                def = params.get(2); 
                            }
                        }
                        result = gd.generateCustomData(para, random, def);
                        break;
                    }
                }
                break;
            }
            
            case("min"):{
                String para1;
                int para2;
                if(params.size() == 2){
                para1 = params.get(0);
                para2 = Integer.parseInt(params.get(1));
                } else {
                    throw new MySQLAlchemistException("2 Parameter werden bei min benötigt", new Exception());
                }
                switch(para1){
                    case("int"):{
                        result = gd.generateMinInteger(para2);
                        break;
                    }
                    case("double"):{
                        result = gd.generateMinDouble(para2);
                        break;
                    }
                }
                break;
            }
            
            case("max"):{
                String para1;
                int para2;
                if(params.size() == 2){
                para1 = params.get(0);
                para2 = Integer.parseInt(params.get(1));
                } else {
                    throw new MySQLAlchemistException("2 Parameter werden bei max benötigt", new Exception());
                }
                switch(para1){
                    case("int"):{
                        result = gd.generateMaxInteger(para2);
                        break;
                    }
                    case("double"):{
                        result = gd.generateMaxDouble(para2);
                        break;
                    }
                }
                break;
            }
            
            case("between"):{
                String para1;
                int para2;
                int para3;
                if(params.size() == 3){
                para1 = params.get(0);
                para2 = Integer.parseInt(params.get(1));
                para3 = Integer.parseInt(params.get(2));
                } else {
                    throw new MySQLAlchemistException("3 Parameter werden bei between benötigt", new Exception());
                }
                switch(para1){
                    case("int"):{
                        result = gd.generateBetweenInteger(para2, para3);
                        break;
                    }
                    case("double"):{
                        result = gd.generateBetweenDouble(para2, para3);
                        break;
                    }
                }
                break;
            }
            
            case("gauss"):{
                String para1;
                double para2;
                double para3;
                if(params.size() == 3){
                    para1 = params.get(0);
                    para2 = Double.parseDouble(params.get(1));
                    para3 = Double.parseDouble(params.get(2));
                } else {
                    throw new MySQLAlchemistException("3 Parameter werden bei gauss benötigt", new Exception());
                }
                switch(para1){
                    case("int"):{
                        result = gd.generateGaussInt(para2, para3);
                        break;
                    }
                    case("double"):{
                        result = gd.generateGaussDouble(para2, para3);
                        break;
                    }
                }
                break;
            }
            
            case("list"):{
                int para;
                if(params.size() == 1){
                para = Integer.parseInt(params.get(0));
                int tmp = para + 1;
                String tmp2 = "" + tmp;
                params.add(0, tmp2);
                params.remove(1);
                } else {
                    throw new MySQLAlchemistException("1 Parameter wird bei list benötigt", new Exception());
                }
                result = gd.generateList(para);
                break;
            }
            
            case("fix"):{
                String para;
                if(params.size() == 1){
                para = params.get(0);
                } else {
                    throw new MySQLAlchemistException("1 Parameter wird bei fix benötigt", new Exception());
                }
                result = para;
                break;
            }
        }
        return result;
    }
}
