|Action|URL                         |Controller |Method                |Send          |HTTP - Response Success      |Http - Response Error|
|------|----------------------------|-----------|----------------------|--------------|-----------------------------|---------------------|
|Application|
|GET   |/                           |Application|.index                |              |200-                         |                     |
|GET   |/admin                      |Application|.admin                |              |200-                         |                     |
|GET   |/init                       |Application|.init                 |              |200-                         |                     |
|Session|
|POST  |/login                      |Session    |.create               |Login.Form    |Redirect(Profile.read)       |400-Login.Error      |
|GET   |/logout                     |Session    |.delete               |              |Redirect(Application.index)  |                     |
|User|
|POST  |/signup                     |User       |.create               |Signup.Form   |Redirect(Session.create)     |400-Signup.Error     |
|GET   |/users                      |User       |.getAllUsers          |              |User[]                       |                     |
|GET   |/users/student              |User       |.checkStudent         |              |200-                         |                     |
|POST  |/users                      |User       |.edit                 |User.Form     |200-                         |                     |
|GET   |/users/verify/:code         |User       |.verifyEmail          |              |200-                         |                     |
|POST  |/users/password             |User       |.sendResetPasswordMail|              |200-                         |                     |
|POST  |/users/:id                  |User       |.promote              |              |200-                         |                     |
|DELETE|/users                      |User       |.delete               |              |200-                         |                     |
|Item|
|GET   |/profile/inventory          |Item       |.inventory            |              |200-Inventory                |                     |
|GET   |/profile/scrolls            |Item       |.scrollCollection     |              |200-Scroll[]                 |                     |
|GET   |/profile/belt               |Item       |.belt                 |              |200-Belt[]                   |                     |
|POST  |/profile/belt               |Item       |.edit                 |Belt.Form     |200-                         |                     |
|GET   |/profile/used/:id           |Item       |.used                 |              |200-                         |                     |
|POST  |/profile/collected          |Item       |.collected            |Collected.Form|200-                         |                     |
|Settings|
|GET   |/profile/settings           |Settings   |.index                |              |200-Setting                  |                     |
|POST  |/profile/settings           |Settings   |.edit                 |Settings      |Redirect(Settings.index)     |                     |
|Challenge|
|GET   |/challenge/story            |Challenge  |.story                |              |200-                         |                     |
|GET   |/challenge/skip             |Challenge  |.skip                 |              |200-                         |                     |
|GET   |/challenge/reset            |Challenge  |.reset                |              |200-                         |                     |
|TaskSetController|
|POST  |/TaskSet/                   |TaskSet    |.create               |TaskSet.Form  |Redirect(TaskSet.view)       |400-                 |
|GET   |/TaskSet/                   |TaskSet    |.read                 |              |200-TaskSet[]                |400-                 |
|GET   |/TaskSet/:id                |TaskSet    |.view                 |              |200-TaskSet                  |400-                 |
|PATCH |/TaskSet/:id                |TaskSet    |.update               |TaskSet       |Redirect(TaskSet.view)       |400-                 |
|DELETE|/TaskSet/:id/               |TaskSet    |.delete               |              |Redirect(TaskSet.read)       |400-                 |
|POST  |/TaskSet/:id/rate           |TaskSet    |.rate                 |Rating.Form   |Redirect(TaskSet.view)       |400-                 |
|POST  |/TaskSet/:id/comment        |TaskSet    |.comment              |Comment.Form  |Redirect(TaskSet.view)       |400-                 |
|TaskController
|POST  |/TaskSet/:id/Task           |Task       |.create               |Task.Form     |Redirect(Task.view)          |400-                 |
|GET   |/Task/                      |Task       |.read                 |              |200-Task[]                   |400-                 |
|GET   |/Task/:id/                  |Task       |.view                 |              |200-Task                     |400-                 |
|PATCH |/Task/:id/                  |Task       |.update               |Task          |Redirect(Task.view)          |400-                 |
|DELETE|/Task/:id/                  |Task       |.delete               |              |Redirect(Task.read)          |400-                 |
|POST  |/Task/:id/rate              |Task       |.rate                 |Rating.Form   |Redirect(Task.view)          |400-                 |
|POST  |/Task/:id/comment           |Task       |.comment              |Comment.Form  |Redirect(Task.view)          |400-                 |
|SQLController
|GET   |/SQL/Story/:id              |SQL        |.story                |              |200-Task.Exercise            |400-                 |
|POST  |/SQL/Story/:id              |SQL        |.storySolve(id: Long)
|GET   |/SQL/Trivia/:id             |SQL        |.trivia               |              |200-Task.Exercise            |400-                 |
|POST  |/SQL/Trivia/:id             |SQL        |.triviaSolve          |UserStatement |SQLResult.Successfull        |400-SQLResult.Failure|
|GET   |/SQL/Homework               |SQL        |.homework()             
|POST  |/SQL/Homework/:id           |SQL        |.homeworkSolve(id: Long)
|ProfileController
|GET   |/profile                     |Profile    |.read                |              |200-Profile.PlayerState      |400-                 |
|GET   |/profile/character           |Profile    |.character           |              |200-Profile.CharacterState   |400-                 |
|GET   |/profile/avatar/:id          |Profile    |.avatar              |              |200-Profile.Attributes       |400-                 |
|GET   |/profile/reset               |Profile    |.reset               |              |                             |400-                 |
|GET   |/profile/homework            |Profile    |.getUserHomeworks    |              |                             |400-                 |
|GET   |/profile/:id                 |Profile    |.view                |              |200-Profile                  |400-                 |
|ShopController
|GET   |/shop/avatar                 |Shop       |.avatarList          |              |200-ShopItem[]               |400-                 |
|GET   |/shop/belt                   |Shop       |.beltList            |              |200-ShopItem[]               |400-                 |
|GET   |/shop/:id                    |Shop       |.buy                 |              |200-                         |400-                 |
|HighScoreController
|GET   |/highscore/points            |HighScore  |.byPoints            |              |200-HighscoreList            |400-                 |
|GET   |/highscore/time              |HighScore  |.byTime              |              |200-HighscoreList            |400-                 |
|GET   |/highscore/runs              |HighScore  |.byRuns              |              |200-HighscoreList            |400-                 |
|GET   |/highscore/sql               |HighScore  |.bySQL               |              |200-HighscoreList            |400-                 |
|GET   |/highscore/rate              |HighScore  |.byRate              |              |200-HighscoreList            |400-                 |
|GET   |/highscore/coins             |HighScore  |.byCoins             |              |200-HighscoreList            |400-                 |



#HomeWorkController
GET         /homework                           controllers.HomeWorkController.getAll()
POST        /homework                           controllers.HomeWorkController.create()
GET         /homework/tasks                     controllers.HomeWorkController.getCurrentHomeWorkForCurrentSession()
GET         /homework/students                  controllers.HomeWorkController.getAllStudents()
GET         /homework/delete/:id                controllers.HomeWorkController.delete(id: Long)
POST        /homework/result                    controllers.HomeWorkController.getSubmitsForHomeworkTaskSet()
GET         /homework/result/:sid/:hid          controllers.HomeWorkController.getForTask(sid: Long, hid: Long)
