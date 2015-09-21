package helper;

import play.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * SQL to RA Converter - Class
 *
 * @version 1.0
 * @author gabrielahlers
 */
public class RATable {
    private String statement;

    private String name;
    private ArrayList<TableColumn> tableColumn;


//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////

    public RATable(
            String statement
    ) {
        this.statement = statement.toLowerCase().trim();
        this.tableColumn = new ArrayList<>();

        int posOfFirstBracket = this.statement.indexOf('(');
        int posOfLastBracket = this.statement.lastIndexOf(')');

        this.name   = this.statement.substring("CREATE TABLE".length(), posOfFirstBracket).trim();
        String body = this.statement.substring(posOfFirstBracket + 1, posOfLastBracket);

        getVariables(body);
    }

//////////////////////////////////////////////////
//  Set- & Get-Methods
//////////////////////////////////////////////////


    public void getVariables(String body){
        boolean atEnd = false;

        int pos = 0;

        do {
            int Komma = body.indexOf(',', pos);

            while(Komma > body.indexOf('(', pos) && Komma < body.indexOf(')', body.indexOf('(', pos))) {
                Komma = body.indexOf(',', body.indexOf(')', body.indexOf('(', pos)));
            }

            String variable;
            if(Komma < 0) {
                variable = body.substring(pos);
                atEnd = true;
            } else {
                variable = body.substring(pos, Komma);
            }

            variable = variable.trim();

            if(variable.indexOf("primary key") == 0) {
                solvePrimaryKey(variable);
            } else if (variable.contains("foreign key")) {
                solveForeignKey(variable);
            } else {
                TableColumn tableColumn = new TableColumn(variable);
                this.tableColumn.add(tableColumn);
            }

            pos = Komma + 1;
        } while (!atEnd);

/*
        String[] allVariables = body.split(",");

        for (String variable : allVariables){
            variable = variable.trim();
            Logger.info(variable);
        }
*/
    }

    private void solveForeignKey(String variable) {

        variable = variable.trim();

        int iRef = variable.indexOf("references");
        int iKlA2 = variable.indexOf("(", iRef);

        String tableName = variable.substring(iRef + 10, iKlA2).trim();

        int iKlA1 = variable.indexOf("(");
        int iKlZ1 = variable.indexOf(")");

        String vari = variable.substring(iKlA1 + 1, iKlZ1);
        String variables[] = vari.split(",");

        for (String foreignKey : variables) {
            foreignKey = foreignKey.trim();
            for (TableColumn tableColumn : this.tableColumn){
                if (tableColumn.getName().equals(foreignKey)) {
                    tableColumn.setForeign(tableName);
                }
            }
        }





    }

    private void solvePrimaryKey(String variable) {

        String primaryKeys[] = variable.split("\\(|\\)");
        String primaryKeyArray[] = primaryKeys[1].split(",");


        if (!(primaryKeys[1].contains(","))) {
            for (TableColumn tableColumn : this.tableColumn) {
                if (tableColumn.getName().equals(primaryKeys[1].trim())) {
                    tableColumn.setPrimary();
                }
            }
        } else {
            for (String primaryKey : primaryKeyArray) {
                for (TableColumn tableColumn : this.tableColumn) {
                    if (tableColumn.getName().equals(primaryKey.trim())) {
                        tableColumn.setPrimary();
                    }
                }
            }
        }

    }


//////////////////////////////////////////////////
//  ToString-Method
//////////////////////////////////////////////////

    public String toString(){
        String relationaleAlgebra = this.name + " (";

        for (int i = 0; i < this.tableColumn.size(); i++){
            relationaleAlgebra = relationaleAlgebra + tableColumn.get(i).toString();
            if(i < this.tableColumn.size() - 1){
                relationaleAlgebra = relationaleAlgebra + ", ";
            }
        }

        return relationaleAlgebra + ")";


    }

}
