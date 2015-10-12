package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * @author fabiomazzone
 */
@Entity
@Table(name = "ColumnDefinition")
public class ColumnDefinition extends Model {
    @Id
    private long id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "foreignKey")
    private List<ColumnDefinition> referencedColumns;

    @ManyToOne
    private TableDefinition tableDefinition;

    private String  columnName;
    private String  dataType;
    private boolean isPrimaryKey;
    private boolean isNotNullable;

    @ManyToOne
    private ColumnDefinition foreignKey;

    private int     datagenSet;

    public static Finder<Long, ColumnDefinition> find = new Finder<>(Long.class, ColumnDefinition.class);


//////////////////////////////////////////////////
//  constructor
//////////////////////////////////////////////////

    public ColumnDefinition(
            TableDefinition tableDefinition,
            String columnName,
            String dataType,
            boolean isPrimaryKey,
            boolean isNotNullable,
            int datagenSet) {
        this.tableDefinition= tableDefinition;
        this.columnName     = columnName;
        this.dataType       = dataType;
        this.isPrimaryKey   = isPrimaryKey;
        this.isNotNullable  = isNotNullable;
        this.datagenSet     = datagenSet;
    }

//////////////////////////////////////////////////
//  Getter & Setter
//////////////////////////////////////////////////


    public long getId() {
        return id;
    }

    public TableDefinition getTableDefinition() {
        return tableDefinition;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getDataType() {
        return dataType;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public boolean isNotNullable() {
        return isNotNullable;
    }

    public boolean isForeignKey() {
        return (this.foreignKey != null);
    }

    public ColumnDefinition getForeignKey() {
        return foreignKey;
    }

    public void setForeignKey(ColumnDefinition foreignKey) {
        this.foreignKey = foreignKey;
    }

    public int getDatagenSet() {
        return datagenSet;
    }

}
