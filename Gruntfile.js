module.exports = function (grunt) {
  var globalConfig = {
    adminTool:'admin-tool',
    frontEnd:'front-end'
  };

  grunt.initConfig({
    globalConfig: globalConfig,

    copy: {
      frontEnd: {
        cwd: 'front-end/front-end',
        src: '**/*',
        dest: 'data',
        expand:true
      },
      adminTool: {
        cwd: 'admin-tool/admin-tool',
        src: '**/*',
        dest: 'data/admin',
        expand: true
      }
    },

    connect: {
      server: {
        options: {
          port: 8000,
          base: 'data',
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
      frontEnd: {
        files: ['front-end/src/**/*'],
        tasks: ['grunt-frontEnd', 'copy:frontEnd']
      },
      adminTool: {
        files: ['admin-tool/src/**/*'],
        tasks: ['grunt-adminTool', 'copy:adminTool']
      }
    },
    clean: {
      app: ['data']
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
          done();
        }
      );
  });

  grunt.registerTask('grunt-adminTool', function () {
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
          done();
        }
      );
  });

  grunt.loadNpmTasks('grunt-contrib-copy');
  grunt.loadNpmTasks('grunt-contrib-connect');
  grunt.loadNpmTasks('grunt-contrib-copy');
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-connect-proxy');
  grunt.loadNpmTasks('grunt-contrib-clean');

  grunt.registerTask('default', ['grunt-frontEnd', 'grunt-adminTool', 'copy', 'configureProxies:server', 'connect:server', 'watch']);
};
