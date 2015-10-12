|Action|URL                |Controller |Method           |Send          |HTTP - Response Success |Http - Response Error   |
|------|-------------------|-----------|-----------------|--------------|------------------------|------------------------|
|||Application|
|GET   |/                  |Application|.index           |              |200-                    |                        |
|GET   |/admin             |Application|.admin           |              |200-                    |                        |
|||Session|
|POST  |/login             |Session    |.create          |Login.Form    |200- redirect(/profile) |401-Login.Error         |
|GET   |/logout            |Session    |.delete          |              |200-                    |                        |
|||User|
|POST  |/signup                     |User       |.create               |Signup.Form   |200- redirect(/login)   |400-Signup.Error        |
|POST  |/users                      |User       |.edit                 |User.Form     |200-                    |                        |
|DELETE|/users                      |User       |.delete               |              |200-                    |                        |
|GET   |/users/verify/:code         |User       |.verifyEmail          |              |200-                    |                        |
|POST  |/users/password             |User       |.sendResetPasswordMail|              |200-                    |                        |
|POST  |/users/dopasswordreset/:code|User       |.doResetPassword      |              |200-                    |                        |
|GET   |/users/student              |User       |.checkStudent         |              |200-                    |                        |
|POST  |/users/:id                  |User       |.promote              |              |200-                    |                        |
|||Item|
|GET   |/profile/inventory |Item       |.inventory       |              |200- Inventory          |                        |
|GET   |/profile/scrolls   |Item       |.scrollCollection|              |200- Scroll[]           |                        |
|GET   |/profile/belt      |Item       |.belt            |              |200- Belt[]             |                        |
|POST  |/profile/belt      |Item       |.edit            |Belt.Form     |200-                    |                        |
|GET   |/profile/used/:id  |Item       |.used            |              |200-                    |                        |
|POST  |/profile/collected |Item       |.collected       |Collected.Form|200-                    |                        |
|||Settings|
|GET   |/profile/settings  |Settings   |.index           |              |200- Setting            |                        |
|POST  |/profile/settings  |Settings   |.edit            |Settings.Form |200-                    |                        |
|||Challenge|
|GET   |/challenge/story   |Challenge  |.story           |              |200-                    |                        |
|GET   |/challenge/skip    |Challenge  |.skip            |              |200-                    |                        |
|GET   |/challenge/reset   |Challenge  |.reset           |              |200-                    |                        |
#TaskFileController
GET     /taskFile           | .index            |                | ok   - JSON.TaskFiles       |                       | X
POST    /taskFile           | .create           | JSON.TaskFile  | ok   - JSON.TaskFile        |                       | X
GET     /taskFile/:id       | .view             |                | ok   - JSON.TaskFile        |                       | X
POST    /taskFile/:id       | .edit             |                | ok   - JSON.TaskFile        |                       |
POST    /taskFile/:id/rate  | .rate             | JSON.Rating    | ok   -                      |                       | X
POST    /taskFile/:id/comm  | .comment          | JSON.Text      | ok   -                      |

#SubTaskController
GET     /task               | .index            |                | ok   - JSON.tasks           |                       | X
GET     /task/:id           | .view             |                | ok   - JSON.task            |                       |
POST    /task/:id/rat       | .rate             | JSON.rating    | ok   -                      |                       |
POST    /task/:id/comment   | .comment          | JSON.comment   | ok   -                      |                       |
GET     /task/story/:id     | .story            |                | ok   - JSON.taskExercise    |                       | X
POST    /task/story/:id     | .storySolve       | JSON.task.solve| ok   - JSON.task.result     | 400(JSON.task.result) |
GET     /task/trivia        | .trivia           |                | ok   - JSON.taskExercise    |                       | X
POST    /task/trivia/:id    | .triviaSolve      | JSON.task.solve| ok   - JSON.task.result     | 400(JSON.task.result) |
GET     /task/homework      | .homework         |                | ok   - JSON.taskExercise    |                       | X
POST    /task/homework/:id  | .homeworkSolve    | JSON.task.solve| ok   - JSON.task.result     | 400(JSON.task.result) |

#ProfileController
GET     /profile            | .index            |                | ok   - JSON.playerstate     |                       | X
GET     /profile/:id        | .view             |                | ok   - JSON.profile         |                       | X
GET     /profile/character  | .character        |                | ok   - JSON.characterState  |                       | X
POST    /profile/avatar/:id | .avatar           |                | ok   - JSON.attributes      |                       | X

#ShopController
GET     /shop               | .avatarList       |                | ok   - JSON.shopitems       |                       | X
GET     /shop/avatar        | .avatarList       |                | ok   - JSON.shopitems       |                       | X
GET     /shop/belt          | .beltList         |                | ok   - JSON.shopitems       |                       | X
POST    /shop/:id           | .buy              |                | ok   -                      | 400                   | X

#HighScoreController
GET     /highscore/points   | .byPoints         |                | ok   - JSON.highscores      |                       | X
GET     /highscore/time     | .byTime           |                | ok   - JSON.highscores      |                       | X
GET     /highscore/runs     | .byRuns           |                | ok   - JSON.highscores      |                       | X
GET     /highscore/sql      | .bySQL            |                | ok   - JSON.highscores      |                       | X
GET     /highscore/rate     | .byRate           |                | ok   - JSON.highscores      |                       | X
GET     /highscore/coins    | .byCoins          |                | ok   - JSON.highscores      |                       | X
