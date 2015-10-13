## json model documentaion

### Table of Content
- [Column](#column)
  - [Column.Form](#columnform)
- [ForeignKey](#foreignKey)
  - [ForeignKey.Form](#foreignkeyform)
- [TableDefinition](#tabledefinition)
  - [TableDefinition.Form](#tabledefinitionform)
- [Task](#task)
  - [Task.Form](#taskform)
- [TaskSet](#taskset)
  - [TaskSet.Form](#tasksetform)

### Column
| Name                     | Type                   | Description|
|--------------------------|------------------------|------------|
| ```id```                 | ```Number ```          |            |
| ```columnName```         | ```String```           |            |
| ```data_type```          | ```String```           |            |
| ```primaryKey```         | ```Boolean```          |            |
| ```foreignKey```         | [```Column```](#column)|            |
| ```notNull```            | ```Boolean```          |            |
| ```datagenSet```         | ```Number```           |            |

Example:
```json
Columns:
{
  "id":         12,
  "columnName": "FirstName",
  "data_type":  "Varchar(255)",
  "primaryKey": false,
  "notNull":    true,
  "foreignKey": null,
  "datagenSet": 1
}
```
#### Column.Form
| Name                     | Type              | Description|
|--------------------------|-------------------|------------|
| ```columnName```         | ```String```      |            |
| ```data_type```          | ```String```      |            |
| ```primaryKey```         | ```Boolean```     |            |
| ```notNull```            | ```Boolean```     |            |
| ```datagenSet```         | ```Number```      |            |

Example:
```json
Columns:
{
  "columnName": "FirstName",
  "data_type":  "Varchar(255)",
  "primaryKey": false,
  "notNull":    true,
  "datagenSet": 1
}
```
### ForeignKey
#### ForeignKey.Form
| Name                     | Type              | Description|
|--------------------------|-------------------|------------|
| ```sourceTable```        | ```String```      |            |
| ```sourceColumn```       | ```String```      |            |
| ```destinationTable```   | ```String```      |            |
| ```destinationColumn```  | ```String```      |            |

Example:
```json
Columns:
{
  "sourceTable":"Profile",
  "sourceColumn":"user_id",
  "destinationTable":"User",
  "destinationColumn":"id"
}
```
### TableDefinition
| Name                     | Type                       |Description|
|--------------------------|----------------------------|-----------|
| ```id```                 | ```Number ```              |           |
| ```tableName```          | ```String```               |           |
| ```columns```            | [```Columns[]```](#column) |           |
| ```extension```          | ```String```               |           |
Example:
```json
TableDefinition:
{
  "id":         13,
  "tableName":  "User",
  "columns":    [
    {
        "id":         12,
        "columnName": "FirstName",
        "data_type":  "Varchar(255)",
        "primaryKey": false,
        "notNull":    true,
        "foreignKey":  null,
        "datagenSet": 1
    }
  ],
  "extension":  "INSERT INTO User(FirstName) Values ('Kéqz');"
}
```
#### TableDefinition.Form
| Name                     | Type                               | Description|
|--------------------------|------------------------------------|------------|
| ```tableName```          | ```String```                       |            |
| ```columns```            | [```Column.Form[]```](#columnform) |            |
| ```extension```          | ```String```                       |            |
Example:
```json
TableDefinition:
{
  "tableName":  "User",
  "columns":    [
    {
        "columnName": "FirstName",
        "data_type":  "Varchar(255)",
        "primaryKey": false,
        "notNull":    true,
        "datagenSet": 1
    }
  ],
  "extension":  "INSERT INTO User(FirstName) Values ('Kéqz');"
}
```
### Task
| Name                     | Type                        | Description|
|--------------------------|-----------------------------|------------|
| ```id```                 | ```Number ```               |            |
| ```taskName```           | ```String```                |            |
| ```taskSet```            | ```Number ```               |            |
| ```relationsFormatted``` | ```String```                |            |
| ```taskText```           | ```String```                |            |
| ```refStatement```       | ```String```                |            |
| ```evalstrategy```       | ```Number```                |[SET, LIST] |
| ```points```             | ```Number```                |            |
| ```requiredterm```       | ```Number```                |[WITH, AS, SELECT, FROM, JOIN, ON, WHERE, GROUP BY, HAVING, ORDER BY, COUNT, SUM, LEFT OUTER JOIN, RIGHT OUTER JOIN, AVG, MAX, MIN] |
| ```creator```            | [```Profile```](#profile)   |            |
| ```rating```             | [```Rating```](#rating)     |            |
| ```comments```           | [```Comment[]```](#comment) |            |
| ```created_at```         | ```String```                |            |
| ```updated_at```         | ```String```                |            |

Example:  
```json
Task:
{
  "id":                 2,
  "taskName":           "yeah Namen 1",
  "taskSet":            1,
  "relationsFormatted": "User(FirstName : String)",
  "taskText":           "Find all user",
  "refStatement":       "SELECT * FROM User",
  "evalstrategy":       Number,
  "points":             1,
  "requiredterm":       Number,
  "creator":            Profile,
  "rating":             Rating,
  "comments":           Comment[],
  "created_at":         "Mon Oct 12 06:18:37 CEST 2015",
  "updated_at":         "Mon Oct 12 06:18:37 CEST 2015"
}

```

#### Task.Form
| Name                     | Type          | Description|
|--------------------------|---------------|------------|
| ```taskSet```            | ```Number```  |            |
| ```taskText```           | ```String```  |            |
| ```refStatement```       | ```String```  |            |
| ```evalstrategy```       | ```Number```  |[SET, LIST] |
| ```points```             | ```Number```  |            |
| ```requiredterm```       | ```Number```  |[WITH, AS, SELECT, FROM, JOIN, ON, WHERE, GROUP BY, HAVING, ORDER BY, COUNT, SUM, LEFT OUTER JOIN, RIGHT OUTER JOIN, AVG, MAX, MIN] |

Example:  
```json
Task:
{
  "taskSet":            1,
  "taskText":           "Find all user",
  "refStatement":       "SELECT * FROM User",
  "evalstrategy":       Number,
  "points":             1,
  "requiredterm":       Number,
}

```
### TaskSet
| Name                     | Type                                        | Description|
|--------------------------|---------------------------------------------|------------|
| ```id```                 | ```Number```                                |            |
| ```taskSetName```        | ```String```                                |            |
| ```tableDefinitions```   | [```TableDefinition[]```](#tabledefinition) |            |
| ```relationsFormatted``` | ```String```                                |            |
| ```tasks```              | [```Task```](#task)                         |            |
| ```creator```            | [```Profile```](#profile)                   |            |
| ```isHomework```         | ```String```                                |            |
| ```rating```             | [```Rating```](#rating)                     |            |
| ```comments```           | [```Comment[]```](#comment)                 |            |
| ```created_at```         | ```String```                                |            |
| ```updated_at```         | ```String```                                |            |
Example:  
```json
TaskSet: {
  "id":                 1,
  "taskSetName":        "yeah Namen",
  "tableDefinitions":  [
    {
      "id":         13,
      "tableName":  "User",
      "columns":    [
        {
          "id":         12,
          "columnName": "FirstName",
          "data_type":  "Varchar(255)",
          "primaryKey": false,
          "notNull":    true,
          "foreignKey":  null,
          "datagenSet": 1
        }
      ],
      "extension":  "INSERT INTO User(FirstName) Values ('Kéqz');"
    }
  ],
  "relationsFormatted": "User(FirstName : String)",
  "tasks":              Task[],
  "creator":            Profile,
  "isHomework":         false,
  "rating":             Rating,
  "comments":           Comment[],
  "created_at":         "Mon Oct 12 06:18:37 CEST 2015",
  "updated_at":         "Mon Oct 12 06:18:37 CEST 2015"
}
```



#### TaskSet.Form
| Name                  | Type                                                 | Description|
|-----------------------|------------------------------------------------------|------------|
| ```taskSetName```     | ```String```                                         |            |
| ```tableDefinitions```| [```TableDefinition.Form[]```](#tabledefinitionform) |            |
| ```foreignKeys```     | [```ForeignKey.Form[]```](#foreignkeyform)           |            |
| ```tasks```           | [```Task.Form```](#taskform)                         |            |
| ```isHomework```      | ```String```                                         |            |
Example:  
```json
TaskSet: {
  "taskSetName": "yeah namen",
  "tableDefinitions":  [
    {
      "tableName":  "User",
      "columns":    [
        {
          "columnName": "FirstName",
          "data_type":  "Varchar(255)",
          "primaryKey": false,
          "notNull":    true,
          "foreignKey":  null,
          "datagenSet": 1
        }
      ],
      "extension":  "INSERT INTO User(FirstName) Values ('Kéqz');"
    }
  ],
  "foreignKeys":       [],
  "tasks":             [],
  "isHomework":         false,
}
```
