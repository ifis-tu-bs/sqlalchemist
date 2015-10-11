package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author fabiomazzone
 */
@Entity
@Table(name = "ColumnDefinition")
public class ColumnDefinition extends Model {
    @Id
    private long id;

    private String  columnName;
    private String  dataType;
    private boolean isPrimaryKey;
    private boolean isNotNullable;

    private int     datagenSet;

    public static Finder<Long, ColumnDefinition> find = new Finder<>(Long.class, ColumnDefinition.class);


//////////////////////////////////////////////////
//  constructor
//////////////////////////////////////////////////

    public ColumnDefinition(String columnName, String dataType, boolean isPrimaryKey, boolean isNotNullable, int datagenSet) {
        this.columnName = columnName;
        this.dataType = dataType;
        this.isPrimaryKey = isPrimaryKey;
        this.isNotNullable = isNotNullable;
        this.datagenSet = datagenSet;
    }

//////////////////////////////////////////////////
//  Getter & Setter
//////////////////////////////////////////////////


    public long getId() {
        return id;
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

    public int getDatagenSet() {
        return datagenSet;
    }
}
