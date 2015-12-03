'use strict'


module.exports = function (grunt) {
  var globalConfig = {
    adminTool:'admin-tool',
    frontEnd:'front-end'
  };

  grunt.initConfig({
    globalConfig: globalConfig,

    connect: {
      options: {
        port: 9000,
        // Change this to '0.0.0.0' to access the server from outside.
        hostname: 'localhost',
        base: 'front-end/front-end',
        keepalive:true
      },
      proxies: [
        {
          context: '/api', // the context of the data service
          host: 'localhost', // wherever the data service is running
          port: 9090, // the port that the data service is running on
          changeOrigin: true
        }
      ],
    }
  });

  grunt.registerTask('grunt-frontEnd', function () {
      var done = this.async();
      grunt.util.spawn(
        {
          grunt: true,
          args: ['default', '--force'],
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
  grunt.loadNpmTasks('grunt-connect-proxy');

  grunt.registerTask('default', ['grunt-frontEnd', 'connect']);
}
