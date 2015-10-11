package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * @author fabiomazzone
 */
@Entity
@Table(name = "TableDefinition")
public class TableDefinition extends Model {
    @Id
    private long id;

    private String                  tableName;
    @OneToMany(cascade = CascadeType.ALL)
    private List<ColumnDefinition>  columnDefinitions;
    @Column(columnDefinition = "Text")
    private String                  extension;

    public Finder<Long, TableDefinition> find = new Finder<>(Long.class, TableDefinition.class);

//////////////////////////////////////////////////
//  constructor
//////////////////////////////////////////////////

    public TableDefinition(String tableName, List<ColumnDefinition> columnDefinitions, String extension) {
        this.tableName = tableName;
        this.columnDefinitions = columnDefinitions;
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

    public String getExtension() {
        return extension;
    }
}
