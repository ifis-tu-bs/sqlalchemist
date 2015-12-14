- extract the admin tool and front end from the back end deployment circle

- rewrite the session & login system
    - disallow login from multiple places

- implement an customizable role system
    - customized votes by role
    - the student role
    - predefined roles
        - admin
        - hiwi
        - creator
        - user
    - function:
        - customize roles
        - promote & degrade user
        - canCreate & edit own task/sets
        - edit all taskSets
        - enable homework mode
        - see admin tool & browse through the tasks
        - see homework submits

- merge profile & user models

- develop an logging system 
    - fully integrate the logging system to the new login system

- redesign the API Urls to begin with an /api/ prefix

- rewrite the profile & user controller to match the combinded class
    - uniqe and cleared controllers for inventroy / scrollcollection

- implement the adaptive diffculty system for tasks

- write an detailed ER-Scheme
