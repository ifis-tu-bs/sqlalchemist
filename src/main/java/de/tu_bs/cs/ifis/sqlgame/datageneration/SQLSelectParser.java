package de.tu_bs.cs.ifis.sqlgame.datageneration;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author Tobias
 */
public class SQLSelectParser {
    
    /**
     * Select statement as a string.
     */
    private String selectStatement;
    
    /**
     * Constructor SQLSelectParser.
     * 
     * Set up defaults.
     * 
     * @param selectStatement String select statement
     */
    public SQLSelectParser(String selectStatement) {
        this.selectStatement = selectStatement;
    }
    
    /**
     * Method getFromInformation.
     * 
     * Get the column names from where is selected.
     * 
     * @return String from column name
     */
    public String getFromInformation() {
        String [] splitFrom = this.selectStatement.split("FROM");
        StringTokenizer st = new StringTokenizer(splitFrom[1].trim());
        
        return st.nextToken();
    }
    
    /**
     * Method getWhereInformation.
     * 
     * Get the where clause information.
     * 
     * @return ArrayList list with the where clause information
     */
    public ArrayList<ArrayList<String>> getWhereInformation() {
        ArrayList<ArrayList<String>> whereInformation = new ArrayList<>();
        
        if (this.selectStatement.contains("WHERE")) {
            String [] splitWhere = this.selectStatement.split("WHERE");
            String [] splitAnd = splitWhere[1].split("AND");
            for (String whereClause : splitAnd) {
                //New row of where information
                ArrayList<String> whereInformationRow = new ArrayList<>();

                //Split the where clause
                StringTokenizer st = new StringTokenizer(whereClause);

                //First token of the where clause is the column name
                StringTokenizer stt = new StringTokenizer(st.nextToken(), ".");
                String columnName = stt.nextToken();
                if (stt.hasMoreTokens()) {
                    columnName = stt.nextToken();
                }
                whereInformationRow.add(columnName);

                //Second token of the where clause is the where comparison type
                String comparisonType = st.nextToken();
                whereInformationRow.add(comparisonType);

                //Get the token comparison token of the where clause
                CharSequence comparisonString = st.nextToken();
                while (st.hasMoreTokens()) {
                    comparisonString += " " + st.nextToken();
                }
                
                String comparison = "";
                if (comparisonString.charAt(0) == '"') {
                    //varchar
                    for (int i = 1; i < comparisonString.length(); i++) {
                        if (comparisonString.charAt(i) == '"') {
                            break;
                        } else {
                            comparison += comparisonString.charAt(i);
                        }
                    }
                    
                } else if (comparisonString.charAt(0) == '\'') {
                    //varchar
                    for (int i = 1; i < comparisonString.length(); i++) {
                        if (comparisonString.charAt(i) == '\'') {
                            break;
                        } else {
                            comparison += comparisonString.charAt(i);
                        }
                    }
                } else {
                    //int or double
                    StringTokenizer sttt = new StringTokenizer(comparisonString.toString());
                    if (sttt.hasMoreTokens()) {
                        comparison = sttt.nextToken();
                    }
                }
                
                whereInformationRow.add(comparison);

                whereInformation.add(whereInformationRow);
            }
        }
        
        return whereInformation;
    }
}
