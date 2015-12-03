(function () {

    /**
     * Create basic progressBar Obejct
     * @type {Object|void}
     */
    var ProgressBar = me.Renderable.extend({

        init: function (v, w, h) {
            me.Renderable.prototype.init.apply(this, [v.x, v.y, w, h]);

            // set flag to know if we need to refresh the display
            this.invalidate = false;

            // set progress bar height and width
            this.barHeight = 210;
            this.barWidth = 220;

            // current progress
            this.progress = 0;

        },

        // make sure the screen is refreshed every frame
        onProgressUpdate : function (progress) {
            this.progress = ~~(progress * this.barHeight);
            this.invalidate = true;
        },

        update : function () {
            if (this.invalidate === true) {
                // clear the flag
                this.invalidate = false;
                // and return true
                return true;
            }
            return false;
        },

        /**
         * Draw function to set color and draw the progress bars
         * @param renderer
         */
        draw : function (renderer) {
            renderer.setColor("#07698e");
            renderer.fillRect(280,543, this.barWidth, - this.progress);
            renderer.setColor("#07808e");
            renderer.fillRect(540,525, this.barWidth, - this.progress);
            renderer.setColor("#078e79");
            renderer.fillRect(810,548, this.barWidth, - this.progress);
        }
    });

    /**
     * Create the custom LoadingScreen
     * @memberOf me
     * @ignore
     * @constructor
     */
    me.DefaultLoadingScreen = me.ScreenObject.extend({

        onResetEvent : function () {
            me.game.reset();

            me.game.world.addChild(new me.ColorLayer("background", "#000000", 0));

            // init the progress bar
            var progressBar = new ProgressBar(
                new me.Vector2d(),
                me.video.renderer.getWidth(),
                me.video.renderer.getHeight()
            );
            this.handle = me.event.subscribe(
                me.event.LOADER_PROGRESS,
                progressBar.onProgressUpdate.bind(progressBar)
            );
            me.game.world.addChild(progressBar, 1);

            /**
             * Set HTML-Background for the custom LoadingScreen
             */
            var loadingScreen = new game.BackgroundElement('loadingScreenId', 100, 100, 0, 0, 'none');
            loadingScreen.setImage("assets/data/img/arbitraryLoadingScreen.png", "loadingscreen");
            me.game.world.addChild(loadingScreen);
            $("#loadingScreenId").fadeIn(100);
        },

        /**
         * Destroy object at the end of loading
         */
        onDestroyEvent : function () {
            // cancel the callback
            if (this.handle)  {
                me.event.unsubscribe(this.handle);
                this.handle = null;
            }
        }
    });
})();