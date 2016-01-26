package models;

import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;
import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.List;

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


  public ForeignKeyRelation(String sourceTable, String sourceColumn, String destinationTable, String destinationColumn) {
    this.sourceTable = sourceTable;
    this.sourceColumn = sourceColumn;
    this.destinationTable = destinationTable;
    this.destinationColumn = destinationColumn;
  }

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
        this.combinedKeyId = combinedKeyId;
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

    @JsonProperty("isCombined")
    public boolean getIsCombined() {
        return this.combinedKeyId != null;
    }

    public Integer getCombinedKeyId() {
        return this.combinedKeyId;
    }

    public void setCombinedKeyId(Integer combinedKeyId) {
        this.combinedKeyId = combinedKeyId;
    }

    @JsonIgnore
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

  public static String getSQLStatement(List<ForeignKeyRelation> combinedForeignKeyRelation) {
    String sourceTable  = combinedForeignKeyRelation.get(0).getSourceTable();
    String destTable    = combinedForeignKeyRelation.get(0).getDestinationTable();

    String sourceColumn = "";
    String destinationColumn = "";

    for(ForeignKeyRelation foreignKeyRelation : combinedForeignKeyRelation) {
      sourceColumn = sourceColumn + foreignKeyRelation.getSourceColumn();
      destinationColumn = destinationColumn + foreignKeyRelation.getDestinationColumn();

      if(combinedForeignKeyRelation.indexOf(foreignKeyRelation) < combinedForeignKeyRelation.size() - 1) {
        sourceColumn = sourceColumn + ", ";
        destinationColumn = destinationColumn + ", ";
      }
    }

    return "ALTER TABLE "
        + sourceTable
        + " ADD FOREIGN KEY ("
        + sourceColumn
        + ") REFERENCES "
        + destTable
        + "("
        + destinationColumn
        + ");";
  }
}
