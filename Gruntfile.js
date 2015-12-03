'use strict'


module.exports = function (grunt) {
  var globalConfig = {
    adminTool:'admin-tool',
    frontEnd:'front-end'
  };

  grunt.initConfig({
    globalConfig: globalConfig,



    connect: {
      server: {
        options: {
          port: 8000,
          base: 'front-end/front-end',
          hostname: 'localhost',

          middleware: function (connect, options, defaultMiddleware) {
             var proxy = require('grunt-connect-proxy/lib/utils').proxyRequest;
             return [
                // Include the proxy first
                proxy
             ].concat(defaultMiddleware);
          }
        },
        proxies: [{
          context: '/API', // the context of the data service
          host: 'localhost', // wherever the data service is running
          port: 9000, // the port that the data service is running on
          changeOrigin: true
        }]
      }
    },
    watch: {
      files: ['front-end/src/**/*'],
      tasks: ['grunt-frontEnd']
    }
  });

  grunt.registerTask('grunt-frontEnd', function () {
      var done = this.async();
      grunt.util.spawn(
        {
          grunt: true,
          args: ['default'],
          opts: {
              cwd: 'front-end'
          }
        },
        function (err, result, code) {
          console.log(result.stdout);
          done();
        }
      );
  });

  grunt.loadNpmTasks('grunt-contrib-connect');
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-connect-proxy');

  grunt.registerTask('default', ['grunt-frontEnd', 'configureProxies:server', 'connect:server', 'watch']);
}
