package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;

import javax.persistence.*;
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

    @Column(columnDefinition = "Text")
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
}
