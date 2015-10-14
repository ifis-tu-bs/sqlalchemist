package models;

import play.db.ebean.Model;

import javax.persistence.*;

/**
 * @author fabiomazzone
 */
@Entity
@Table(name = "ForeignKeyRelation")
public class ForeignKeyRelation extends Model {
    @Id
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private TaskSet taskSet;

    private final String sourceTable;
    private final String sourceColumn;
    private final String destinationTable;
    private final String destinationColumn;

    public ForeignKeyRelation(String sourceTable, String sourceColumn, String destinationTable, String destinationColumn) {
        this.sourceTable = sourceTable;
        this.sourceColumn = sourceColumn;
        this.destinationTable = destinationTable;
        this.destinationColumn = destinationColumn;
    }

    public long getId() {
        return id;
    }

    public String getSourceTable() {
        return sourceTable;
    }

    public String getSourceColumn() {
        return sourceColumn;
    }

    public String getDestinationTable() {
        return destinationTable;
    }

    public String getDestinationColumn() {
        return destinationColumn;
    }
}
