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

    @ManyToOne(cascade = CascadeType.ALL)
    private TaskSet                 taskSet;

    private String                  tableName;
    @OneToMany(mappedBy = "tableDefinition", cascade = CascadeType.ALL)
    private List<ColumnDefinition>  columnDefinitions;
    @Column(columnDefinition = "Text")
    private String                  extension;

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
}
