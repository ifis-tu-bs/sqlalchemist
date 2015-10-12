package helper;

/**
 * @author fabiomazzone
 */
public class ForeignKeyRelation {
    public String sourceTable;
    public String sourceColumn;
    public String destinationTable;
    public String destinationColumn;

    public ForeignKeyRelation(String sourceTable, String sourceColumn, String destinationTable, String destinationColumn) {
        this.sourceTable = sourceTable;
        this.sourceColumn = sourceColumn;
        this.destinationTable = destinationTable;
        this.destinationColumn = destinationColumn;
    }
}
