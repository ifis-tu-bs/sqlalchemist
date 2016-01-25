package models;

import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;
import com.avaje.ebean.Model;

import javax.persistence.*;

/**
 * @author fabiomazzone
 */
@Entity
@Table(name = "foreignkeyrelation")
@EntityConcurrencyMode(ConcurrencyMode.NONE)
public class ForeignKeyRelation extends Model {
    @Id
    private long id;

    @ManyToOne
    private TaskSet taskSet;

    private final String sourceTable;
    private final String sourceColumn;
    private final String destinationTable;
    private final String destinationColumn;

    private Integer combinedKeyId;

    /**
     *  This is the constructor for ForeignKeyRelations
     */
    public ForeignKeyRelation(
            String sourceTable,
            String sourceColumn,
            String destinationTable,
            String destinationColumn,
            Integer combinedKeyId) {
        this.sourceTable = sourceTable;
        this.sourceColumn = sourceColumn;
        this.destinationTable = destinationTable;
        this.destinationColumn = destinationColumn;
        this.combinedKeyId = null;
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

    public Integer getCombinedKeyId() {
        return this.combinedKeyId;
    }

    public void setCombinedKeyId(Integer combinedKeyId) {
        this.combinedKeyId = combinedKeyId;
    }

    public String getSQLStatement() {
    return "ALTER TABLE "
        + this.sourceTable
        + " ADD FOREIGN KEY ("
        + this.sourceColumn
        + ") REFERENCES "
        + this.destinationTable
        + "("
        + this.destinationColumn
        + ");";
  }
}
