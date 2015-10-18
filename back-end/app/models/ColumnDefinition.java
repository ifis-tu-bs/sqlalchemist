package models;

import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * @author fabiomazzone
 */
@Entity
@Table(name = "ColumnDefinition")
@EntityConcurrencyMode(ConcurrencyMode.NONE)
public class ColumnDefinition extends Model {
    @Id
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "foreignKey")
    private List<ColumnDefinition> referencedColumns;

    @ManyToOne
    private final TableDefinition tableDefinition;

    private final String  columnName;
    private final String  dataType;
    private final boolean isPrimaryKey;
    private final boolean isNotNullable;

    @ManyToOne
    private ColumnDefinition foreignKey;

    private final int     datagenSet;

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
