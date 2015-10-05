//User States, die einzelnen States die nicht von melonjs vorgegeben werden
var STATE_SHEET = me.state.USER + 0;
var STATE_TASK = me.state.USER + 1;
var STATE_TRIVIA = me.state.USER + 2;
var STATE_BELT = me.state.USER + 3;
var STATE_SHOP = me.state.USER + 4;
var STATE_START = me.state.USER + 5;
var STATE_COLLECTOR = me.state.USER + 6;
var STATE_RESULT = me.state.USER + 7;
var STATE_PROFILE = me.state.USER + 8;
var STATE_LOGIN = me.state.USER + 9;
var STATE_SIGNUP = me.state.USER + 10;
var STATE_HOMEWORK = me.state.USER + 11;
var STATE_ALERT = me.state.USER + 12;
var STATE_BUY = me.state.USER + 13;
var STATE_FORGOTPASSWORD = me.state.USER + 14;
var STATE_TEXT = me.state.USER + 15;
var STATE_LEGEND = me.state.USER + 16;

//set game fps for better performance
me.sys.fps = 45;
/* game namespace */
var game = {
    /**
     * an object where to store game global data
     */
    data : {
        playerStat : 0,
        gotSession: false,
        score : 0,
        lofiCoins : 0,
        runs: 0,
        //music and sound
        recentTitle: "",
        sound: false,
        music: false,
        soundVolume: 1,
        musicVolume: 0.5,
        playing: false,
        sprite: "",
        shop : 0,
        beltShop: 0,
        spriteId: 0,
        shopId: 0,
        scollLimit: 0,
        collectedScrolls: 0,
        scrolls: [],
        //Tutorial
        wait: false,
        audio: "",
        text : 0,
        nextText: true,
        cheat: false,
        count: 0,
        profile: 0
    },
    //informations for the Task
    task : {
        kind : 0,
        difficulty : 0,
        potionId : 0,
        name : null,
        startTime : 0,
        finishTime : 0,
        gainScore : 0,
        gainCoins: 0
    },
    //Spielerattribute
    stats : {
        health : 9,
        hp: 0,
        addhp: 0,
        speed : 9,
        addspeed: 0,
        jump : 25,
        addjump: 0,
        defense : 4,
        adddefense: 0,
        beltslotamount : 0
    },
    //Ingame Memory
    persistent : {
        currentLevel: [7,7,7],
        depth : 0,
        maxDepth: 5,
        jumpOn: false,
        speedOn: false,
        defenseOn: false,
        damage: 0,
        boss: false
    },
    //Alert Messages
    alert: {
        toState : null,
        isAlert : false,
        alertText : ""
    },
    level: {
        scrolls :  [
            [0,10,20,30,11,16],[1,11,21,31,1,6],[2,12,22,32,7,12],[3,13,23,33,2,17],[4,14,24,34,3,8],
            [5,15,25,35,13,18],[6,16,26,36,9,19],[7,17,27,37,4,14],[8,18,28,38,5,15],[9,19,29,39,10,20]
        ]
    },




    // Run on page load.
    "onload" : function () {
        // Initialize the video.
        if (!me.video.init("screen", me.video.CANVAS, 1320, 768, true, 'auto')) {
            alert("Your browser does not support HTML5 canvas.");
            return;
        }

        // add "#debug" to the URL to enable the debug Panel
        if (document.location.hash === "#debug") {
            window.onReady(function () {
                me.plugin.register.defer(this, me.debug.Panel, "debug", me.input.KEY.V);
            });
        }

        // Initialize the audio.
        me.audio.init("mp3");

        // Set a callback to run when loading is complete.
        me.loader.onload = this.loaded.bind(this);

        // Load the resources
        me.loader.preload(game.resources);




        // Initialize melonJS and display a loading screen.
        me.state.change(me.state.LOADING);
    },

    // Run on game resources loaded.
    "loaded" : function () {

        // set the "Play/Ingame" Screen Object

        me.state.set(me.state.MENU, new game.TitleScreen());


        me.state.set(STATE_TRIVIA, new game.TriviaScreen());
        me.state.set(STATE_SHEET, new game.SheetScreen());
        me.state.set(STATE_TASK, new game.TaskScreen());
        me.state.set(STATE_START, new game.StartScreen());
        me.state.set(STATE_SHOP, new game.ShopScreen());
        me.state.set(STATE_BELT, new game.BeltScreen());
        me.state.set(STATE_COLLECTOR, new game.CollectorScreen());
        me.state.set(STATE_RESULT, new game.ResultScreen());
        me.state.set(STATE_PROFILE, new game.ProfileScreen());
        me.state.set(STATE_LOGIN, new game.LoginScreen());
        me.state.set(STATE_SIGNUP, new game.SignUpScreen());
        me.state.set(STATE_HOMEWORK, new game.HomeworkScreen());
        me.state.set(STATE_ALERT, new game.AlertScreen());
        me.state.set(STATE_BUY, new game.BuyScreen());
        me.state.set(STATE_FORGOTPASSWORD, new game.ForgotPasswordScreen());
        me.state.set(STATE_TEXT, new game.TextScreen());
        me.state.set(STATE_LEGEND, new game.LegendScreen());



        // set the "Play/Ingame" Screen Object
        me.state.set(me.state.PLAY, new game.PlayScreen());
        me.state.set(me.state.SETTINGS, new game.SettingsScreen());
        me.state.set(me.state.GAMEOVER, new game.GameOverScreen());
        me.state.set(me.state.HIGHSCORE, new game.HighscoreScreen());
        me.state.set(me.state.READY, new game.ReadyScreen());

        // set a global fading transition for the screen
        me.state.transition("fade", "#000000", 100);

        // add our player entity in the entity pool
        me.pool.register("mainPlayer", game.PlayerEntity);
        me.pool.register("CoinEntity", game.CoinEntity);
        me.pool.register("ScrollEntity", game.ScrollEntity);
        me.pool.register("EnemyEntity", game.EnemyEntity);
        me.pool.register("SpikeEntity", game.SpikeEntity);
        me.pool.register("FireEntity", game.FireEntity);



        // Start the game.
        me.state.change(STATE_START);
    }
};

