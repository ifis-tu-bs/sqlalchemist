package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fabiomazzone
 */
@Entity
@Table(name = "tabledefinition")
@EntityConcurrencyMode(ConcurrencyMode.NONE)
public class TableDefinition extends Model {
    @Id
    private Long id;

    @ManyToOne
    private TaskSet                 taskSet;

    private final String                  tableName;
    @OneToMany(mappedBy = "tableDefinition", cascade = CascadeType.ALL)
    private List<ColumnDefinition>  columnDefinitions;

    @Column(columnDefinition = "MEDIUMTEXT")
    private final String                  extension;

//////////////////////////////////////////////////
//  constructor
//////////////////////////////////////////////////

    public TableDefinition(String tableName, String extension) {
        this.tableName = tableName;
        this.extension = extension;
    }


//////////////////////////////////////////////////
//  Getter & Setter
//////////////////////////////////////////////////


    public long getId() {
        return id;
    }

    public String getTableName() {
        return tableName;
    }

    public List<ColumnDefinition> getColumnDefinitions() {
        return columnDefinitions;
    }

    public void setColumnDefinitions(List<ColumnDefinition> columnDefinitions) {
        this.columnDefinitions = columnDefinitions;
    }

    public String getExtension() {
        return extension;
    }

  public String getSQLStatement() {
    String tableStatement = "CREATE TABLE "
        + this.tableName
        + " (";
    List<String> primaryKeys = new ArrayList<>();

    for(ColumnDefinition columnDefinition : this.columnDefinitions) {
      String column = " "
          + columnDefinition.getColumnName() + " "
          + columnDefinition.getDataType()
          + ((columnDefinition.isNotNullable())? " NOT NULL" : "");

      if(columnDefinition.isPrimaryKey()) {
        primaryKeys.add(columnDefinition.getColumnName());
      }

      tableStatement =
          tableStatement + column
          + ((columnDefinitions.indexOf(columnDefinition) < columnDefinitions.size() - 1)? "," : "");
    }
    if(primaryKeys.size() > 0) {
      tableStatement = tableStatement + ", PRIMARY KEY (";
      for (String primaryKeyI : primaryKeys) {
        tableStatement = tableStatement
            + primaryKeyI
            + ((primaryKeys.indexOf(primaryKeyI) < primaryKeys.size() - 1 )? ", " : "");

      }
      tableStatement = tableStatement + ")";
    }
    return tableStatement + ");";
  }
}
