## json model documentation

### Table of Content
- [Avatar](#avatar)
- [Column](#column)
    - [Column.Form](#columnform)
- [Comment](#comment)
    - [Comment.Form](#commentform)
- [ForeignKey](#foreignkey)
    - [ForeignKey.Form](#foreignkeyform)
- [Map](#map)
- [PlayerState](#playerstate)
- [Rating](#rating)
    - [Rating.Form](#ratingform)
- [ShopItem](#shopitem)
- [SQLResult](#sqlresult)
    - [SQLResult.Successfull](#sqlresultsuccessfull)
    - [SQLResult.Failure](#sqlresultfailure)
- [TableDefinition](#tabledefinition)
    - [TableDefinition.Form](#tabledefinitionform)
- [Task](#task)
    - [Task.Form](#taskform)
    - [Task.Exercise](#taskexercise)
- [TaskSet](#taskset)
    - [TaskSet.Form](#tasksetform)
- [UserStatement](#userstatement)


### Avatar
| Name                      | Type                              | Description|
|---------------------------|-----------------------------------|------------|
| ```id```                  | ```Number ```                     |            |
| ```name```                | ```String```                      |            |
| ```desc```                | ```String```                      |            |
| ```avatarFilename```      | ```String```                      |            |
| ```isTeam```              | ```Boolean```                     |            |
| ```attributes```          | [```PlayerState```](#playerstate) |            |

Example:
```json
Avatar:
{
  "id":             12,
  "name":           "FirstName",
  "desc":           "Yeah",
  "avatarFilename": "parryhitter",
  "isTeam":         true,
  "attributes":     PlayerState
}
```
### Column
| Name                     | Type                   | Description|
|--------------------------|------------------------|------------|
| ```id```                 | ```Number ```          |            |
| ```columnName```         | ```String```           |            |
| ```dataType```           | ```String```           |            |
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
  "dataType":   "Varchar(255)",
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
| ```dataType```           | ```String```      |            |
| ```primaryKey```         | ```Boolean```     |            |
| ```notNull```            | ```Boolean```     |            |
| ```datagenSet```         | ```Number```      |            |

Example:
```json
Columns:
{
  "columnName": "FirstName",
  "dataType":   "Varchar(255)",
  "primaryKey": false,
  "notNull":    true,
  "datagenSet": 1
}
```
### Comment
| Name                     | Type                                | Description|
|--------------------------|-------------------------------------|------------|
| ```profile```            | ```[```Profile```](#profile)```     |            |
| ```text```               | ```String```                        |            |
| ```createdAt```          | ```String```                        |            |

Example:
```json
Comment:
{
  "profile":    Profile,
  "text":       "Ein kommentar",
  "createdAt":  "Mon Oct 12 06:18:37 CEST 2015"
}
```
#### Comment.Form
| Name                     | Type              | Description|
|--------------------------|-------------------|------------|
| ```text```               | ```String```     |            |

Example:
```json
Comment:
{
    "text":   "Ein kommentar"
}
```


### ForeignKey
| Name                     | Type              | Description|
|--------------------------|-------------------|------------|
| ```id```                 | ```Number ```     |            |
| ```sourceTable```        | ```String```      |            |
| ```sourceColumn```       | ```String```      |            |
| ```destinationTable```   | ```String```      |            |
| ```destinationColumn```  | ```String```      |            |

Example:
```json
ForeignKey:
{
  "id":                 12,
  "sourceTable":        "Profile",
  "sourceColumn":       "user_id",
  "destinationTable":   "User",
  "destinationColumn":  "id"
}
```
#### ForeignKey.Form
| Name                     | Type              | Description|
|--------------------------|-------------------|------------|
| ```sourceTable```        | ```String```      |            |
| ```sourceColumn```       | ```String```      |            |
| ```destinationTable```   | ```String```      |            |
| ```destinationColumn```  | ```String```      |            |

Example:
```json
ForeignKey:
{
  "sourceTable":        "Profile",
  "sourceColumn":       "user_id",
  "destinationTable":   "User",
  "destinationColumn":  "id"
}
```
### Map
| Name                      | Type                              | Description|
|---------------------------|-----------------------------------|------------|
| ```id```                  | ```Number```                      |            |
| ```level```               | ```Number```                      |            |
| ```path```                | ```String```                      |            |
| ```isBossMap```           | ```Boolean```                     |            |

Example:
```json
Map:
{
  "id":             12,
  "level":          1,
  "path":           "Yeah",
  "isBossMap":      true
}
```
### PlayerState
| Name                      | Type                              | Description|
|---------------------------|-----------------------------------|------------|
| ```health```              | ```Number```                      |            |
| ```defense```             | ```Number```                      |            |
| ```speed```               | ```Number```                      |            |
| ```jump```                | ```Number```                      |            |
| ```slots```               | ```Number```                      |            |

Example:
```json
PlayerState:
{
  "health":         12,
  "defense":        1,
  "speed":          "Yeah",
  "jump":           123,
  "slots":          true
}
```


### Rating
| Name                     | Type              | Description|
|--------------------------|-------------------|------------|
| ```positive```           | ```Boolean```     |            |
| ```negative```           | ```Boolean```     |            |
| ```needReview```         | ```Boolean```     |            |

Example:
```json
Rating:
{
  "positive":   true,
  "negative":   false,
  "needReview": false
}
```
#### Rating.Form
| Name                     | Type              | Description|
|--------------------------|-------------------|------------|
| ```positive```           | ```Boolean```     |            |
| ```negative```           | ```Boolean```     |            |
| ```needReview```         | ```Boolean```     |            |

Example:
```json
Rating:
{
    "positive":   true,
    "negative":   false,
    "needReview": false
}
```

### Settings
| Name                      | Type              | Description|
|---------------------------|-------------------|------------|
| ```music```               | ```Boolean```     |            |
| ```sound```               | ```Boolean```     |            |

Example:
```json
Settings:
{
    "music":   true,
    "sound":   false,
}
```

### ShopItem
| Name                      | Type                              | Description|
|---------------------------|-----------------------------------|------------|
| ```id```                  | ```Number ```                     |            |
| ```name```                | ```String```                      |            |
| ```desc```                | ```String```                      |            |
| ```type```                | ```Number```                      | 0 = Avatar - 1 = Belt |
| ```thumbnailUrl```        | ```String```                      |            |
| ```price```               | ```Number```                      |            |
| ```avatar```              | [```Avatar```](#avatar) |         |            |
| ```bought```              | ```Boolean```                     |            |

Example:
```json
ShopItem:
{
  "id":             12,
  "name":           "FirstName",
  "desc":           "Yeah",
  "type":           1,
  "thumbnailUrl":   "parryhitter",
  "price":          true,
  "avatar":         Avatar,
  "bought":         false
}
```
### SQLResult
#### SQLResult.Successfull
| Name                     | Type                   | Description|
|--------------------------|------------------------|------------|
| ```type```               | ```Number ```          | Always 0   |
| ```terry```              | ```String```           |            |
| ```time```               | ```Number```           |            |
| ```score```              | ```Number```           |            |
| ```coins```              | ```Number```           |            |

Example:
```json
Columns:
{
    "type":     0,
    "terry":    "Your answer was correct",
    "time":     1200,
    "score":    123,
    "coins":     50
}
```
#### SQLResult.Failure
| Name                     | Type                   | Description|
|--------------------------|------------------------|------------|
| ```type```               | ```Number ```          | 1 = semantic error - 2 = syntactic error |
| ```terry```              | ```String```           |            |
| ```time```               | ```Number```           |            |
| ```SQLError```           | ```String```           |            |

Example:
```json
Columns:
{
    "type":     2,
    "terry":    "you are an stupid guy",
    "time":     1200,
    "SQLError": "a random error message"
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
| ```evaluationstrategy``` | ```Number```                |[SET, LIST] |
| ```points```             | ```Number```                |            |
| ```requiredTerm```       | ```Number```                |[WITH, AS, SELECT, FROM, JOIN, ON, WHERE, GROUP BY, HAVING, ORDER BY, COUNT, SUM, LEFT OUTER JOIN, RIGHT OUTER JOIN, AVG, MAX, MIN] |
| ```creator```            | [```Profile```](#profile)   |            |
| ```rating```             | [```Rating```](#rating)     |            |
| ```comments```           | [```Comment[]```](#comment) |            |
| ```createdAt```          | ```String```                |            |
| ```updatedAt```          | ```String```                |            |

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
  "evaluationstrategy": Number,
  "points":             1,
  "requiredTerm":       Number,
  "creator":            Profile,
  "rating":             {
                            "positive":   true,
                            "negative":   false,
                            "needReview": false
                        },
  "comments":           [],
  "createdAt":          "Mon Oct 12 06:18:37 CEST 2015",
  "updatedAt":          "Mon Oct 12 06:18:37 CEST 2015"
}

```

#### Task.Form
| Name                     | Type          | Description|
|--------------------------|---------------|------------|
| ```taskSet```            | ```Number```  |            |
| ```taskText```           | ```String```  |            |
| ```refStatement```       | ```String```  |            |
| ```evaluationstrategy``` | ```Number```  |[SET, LIST] |
| ```points```             | ```Number```  |            |
| ```requiredTerm```       | ```Number```  |[WITH, AS, SELECT, FROM, JOIN, ON, WHERE, GROUP BY, HAVING, ORDER BY, COUNT, SUM, LEFT OUTER JOIN, RIGHT OUTER JOIN, AVG, MAX, MIN] |

Example:  
```json
Task:
{
  "taskSet":            1,
  "taskText":           "Find all user",
  "refStatement":       "SELECT * FROM User",
  "evaluationstrategy": Number,
  "points":             1,
  "requiredTerm":       Number
}

```

#### Task.Exercise
| Name                     | Type                     | Description|
|--------------------------|--------------------------|------------|
| ```id```                 | ```Number ```            |            |
| ```taskName```           | ```String```             |            |
| ```relationsFormatted``` | ```String```             |            |
| ```taskText```           | ```String```             |            |
| ```points```             | ```Number```             |            |
| ```requiredTerm```       | ```Number```             |[WITH, AS, SELECT, FROM, JOIN, ON, WHERE, GROUP BY, HAVING, ORDER BY, COUNT, SUM, LEFT OUTER JOIN, RIGHT OUTER JOIN, AVG, MAX, MIN] |
| ```rating```             | [```Rating```](#rating)  |            |
| ```createdAt```          | ```String```             |            |
| ```updatedAt```          | ```String```             |            |

Example:  
```json
Task:
{
  "id":                 1,
  "taskName":           "taskName",
  "taskText":           "Find all user",
  "relationsFormatted": "User((int) !id!)",
  "points":             1,
  "requiredTerm":       Number,
  "rating":             {
                            "positive":   true,
                            "negative":   false,
                            "needReview": false
                        },
  "createdAt":          "Mon Oct 12 06:18:37 CEST 2015",
  "updatedAt":          "Mon Oct 12 06:18:37 CEST 2015"
}

```

### TaskSet
| Name                     | Type                                        | Description|
|--------------------------|---------------------------------------------|------------|
| ```id```                 | ```Number```                                |            |
| ```taskSetName```        | ```String```                                |            |
| ```tableDefinitions```   | [```TableDefinition[]```](#tabledefinition) |            |
| ```foreignKeys```        | [```ForeignKey.Form[]```](#foreignkey)      |            |
| ```relationsFormatted``` | ```String```                                |            |
| ```tasks```              | [```Task```](#task)                         |            |
| ```creator```            | [```Profile```](#profile)                   |            |
| ```isHomework```         | ```String```                                |            |
| ```rating```             | [```Rating```](#rating)                     |            |
| ```comments```           | [```Comment[]```](#comment)                 |            |
| ```createdAt```          | ```String```                                |            |
| ```updatedAt```          | ```String```                                |            |
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
  "foreignKeys":       [],
  "relationsFormatted": "User(FirstName : String)",
  "tasks":              Task[],
  "creator":            Profile,
  "isHomework":         false,
  "rating":             {
                            "positive":   true,
                            "negative":   false,
                            "needReview": false
                        },
  "comments":           [],
  "createdAt":          "Mon Oct 12 06:18:37 CEST 2015",
  "updatedAt":          "Mon Oct 12 06:18:37 CEST 2015"
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
  "isHomework":         false
}
```
### UserStatement
| Name                     | Type                   | Description|
|--------------------------|------------------------|------------|
| ```notNull```            | ```String```           |            |
| ```datagenSet```         | ```Number```           |            |

Example:
```json
Columns:
{
  "foreignKey": "SELECT * FROM User;",
  "datagenSet": 1000
}
```
