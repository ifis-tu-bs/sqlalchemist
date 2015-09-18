package helper;

/**
 * Helper class for RATable.java
 *
 * @version 1.0
 * @author gabrielahlers
 */
public class TableColumn {

    private String name;
    private final String dataType;

    private boolean primary = false;
    private String foreign = null;


//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////


    TableColumn (
            String statement
    ) {

        this.name       = statement.trim().split(" ")[0];
        String dataType   = statement.trim().split(" ")[1];

        if(dataType.contains("varchar")) {
            this.dataType = "string";
        } else {
            this.dataType = dataType;
        }

        if (statement.contains("primary key")){
            setPrimary();
        }

        if (statement.contains("references")){
            int iRef = statement.indexOf("references");
            int iKlA2 = statement.indexOf("(", iRef);

            String tableName = statement.substring(iRef + 10, iKlA2).trim();

            setForeign(tableName);
        }





    }


//////////////////////////////////////////////////
//  Set- & Get-Methods
//////////////////////////////////////////////////

    public void setPrimary(){
        this.primary = true;
    }

    public void setForeign(String foreign){
        this.foreign = foreign;
    }

    public String getName(){
        return this.name;


    }


//////////////////////////////////////////////////
//  ToString-Method
//////////////////////////////////////////////////

    public String toString(){

        String element = this.name;


        if (this.primary){
            element = "!" + element + "!";
        }

        element = "(" + this.dataType + ")" + element;

        if (this.foreign != null){
            element = element + " -> " + this.foreign;
        }

        return element;
    }

}
