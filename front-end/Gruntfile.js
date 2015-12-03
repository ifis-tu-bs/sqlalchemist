module.exports = function(grunt) {
  var globalConfig = {
    name: 'sql-alchemist',
    src: 'src',
    tmp:'tmp',
    dist:'front-end'
  };

  grunt.initConfig({
    globalConfig: globalConfig,

    clean: {
      resources: ['<%= globalConfig.src %>/assets/js/resources.js'],
      dist: ['<%= globalConfig.dist %>'],
      tmp: ['<%= globalConfig.tmp %>']
    },
    resources: {
      default: {
        options: {
          dest: '<%= globalConfig.src %>/assets/js/resources.js',
          varname: 'game.resources',
        },
        files: [{
          cwd: '<%= globalConfig.src %>',
          src: ['assets/data/img/**/*.png'],
          type: 'image'
        },{
          cwd: '<%= globalConfig.src %>',
          src: ['assets/data/bgm/**/*', 'assets/data/sfx/**/*'],
          type: 'audio'
        },{
          cwd: '<%= globalConfig.src %>',
          src: ['assets/data/img/**/*.json'],
          type: 'json'
        },{
          cwd: '<%= globalConfig.src %>',
          src: ['assets/data/map/**/*.tmx', 'assets/data/map/**/*.json'],
          type: 'tmx'
        },{
          cwd: '<%= globalConfig.src %>',
          src: ['assets/data/map/**/*.tsx'],
          type: 'tsx'
        }]
      }
    },
    jshint: {
      beforeconcat: ['<%= globalConfig.src %>/assets/js/**/*.js']
    },
    concat: {
      distJSLib: {
        src: [
          '<%= globalConfig.src %>/assets/lib/jquery-2.1.4.js',
          '<%= globalConfig.src %>/assets/lib/ace.js',
          '<%= globalConfig.src %>/assets/lib/mode-sql.js',
          '<%= globalConfig.src %>/assets/lib/theme-twilight.js',
        ],
        dest: '<%= globalConfig.tmp %>/assets/js/lib.min.js'
      },
      distJS: {
        src: [
          '<%= globalConfig.src %>/assets/lib/melonJS-2.0.2.js',
          '<%= globalConfig.src %>/assets/js/game.js',
          '<%= globalConfig.src %>/assets/js/resources.js',
          '<%= globalConfig.src %>/assets/js/DataTypes/*.js',
          '<%= globalConfig.src %>/assets/js/DOMManipulation/*.js',
          '<%= globalConfig.src %>/assets/js/ajax/*.js',
          '<%= globalConfig.src %>/assets/js/screens/*.js',
          '<%= globalConfig.src %>/assets/js/entities/*.js',
        ],
        dest: '<%= globalConfig.tmp %>/assets/js/app.min.js'
      },
      distCSS: {
        src: [
            '<%= globalConfig.src %>/assets/reset.css',
            '<%= globalConfig.src %>/assets/index.css',
            '<%= globalConfig.src %>/assets/css/*.css'
        ],
        dest: '<%= globalConfig.tmp %>/assets/css/app.min.css'
      }
    },
    processhtml: {
      dist: {
        files: {
          '<%= globalConfig.tmp %>/index.html': ['<%= globalConfig.src %>/index.html']
        }
      }
    },
    uglify: {
      options: {
        report: 'min',
        preserveComments: 'some',
        banner: '/*! <%= grunt.template.today("yyyy-mm-dd") %> */'
      },
      distapp: {
        files: {
          '<%= globalConfig.dist %>/assets/js/app.min.js': [
            '<%= globalConfig.tmp %>/assets/js/app.min.js' ]
        }
      },
      distlib: {
        files: {
          '<%= globalConfig.dist %>/assets/js/lib.min.js': [
            '<%= globalConfig.tmp %>/assets/js/lib.min.js']
        }
      }
    },

    cssmin: {
      dist: {
        files: {
          '<%= globalConfig.dist %>/assets/css/app.min.css': ['<%= globalConfig.tmp %>/assets/css/app.min.css']
        }
      }
    },

    htmlmin: {
      dist: {
        options: {
          removeComments: true,
          collapseWhitespace: true
        },
        files: {
          '<%= globalConfig.dist %>/index.html': '<%= globalConfig.tmp %>/index.html',
        }
      }
    },
    copy: {
      dev: {
        files: [
          {
            src: '<%= globalConfig.src %>/manifest.json',
            dest: '<%= globalConfig.tmp %>/manifest.json'
          },
          {
            src: 'package.json',
            dest: '<%= globalConfig.tmp %>/package.json'
          },
          {
            cwd: '<%= globalConfig.src %>',
            src: 'assets/data/**/*',
            dest: '<%= globalConfig.tmp %>/',
            expand: true
          },
          {
            cwd: '<%= globalConfig.src %>',
            src: 'assets/icons/**/*',
            dest: '<%= globalConfig.tmp %>/',
            expand: true
          }
        ]
      },
      dist: {
        files: [
          {
            src: '<%= globalConfig.src %>/manifest.json',
            dest: '<%= globalConfig.dist %>/manifest.json'
          },
          {
            src: 'package.json',
            dest: '<%= globalConfig.dist %>/package.json'
          },
          {
            cwd: '<%= globalConfig.src %>',
            src: 'assets/data/**/*',
            dest: '<%= globalConfig.dist %>/',
            expand: true
          },
          {
            cwd: '<%= globalConfig.src %>',
            src: 'assets/icons/**/*',
            dest: '<%= globalConfig.dist %>/',
            expand: true
          }
        ]
      }
    },
    connect: {
      vanilla: {
        options: {
          port: 8020,
          base: '<%= globalConfig.src %>',
          keepalive: true,
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
      },
      development: {
        options: {
          port: 8020,
          base: '<%= globalConfig.tmp %>',
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
      },
      serve: {
        options: {
          port: 8020,
          base: '<%= globalConfig.dist %>',
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
      jslib: {
        files: ['<%= globalConfig.src %>/assets/lib/**/*.js'],
        tasks: ['concat:distJSLib']
      },
      js: {
        files: ['<%= globalConfig.src %>/assets/js/**/*.js'],
        tasks: ['jshint:beforeconcat', 'concat:distJS']
      },
      css: {
        files: ['<%= globalConfig.src %>/assets/css/**/*.js'],
        tasks: ['concat:distCSS']
      },
      html: {
        files: ['<%= globalConfig.src %>/index.html'],
        tasks: ['processhtml']
      },
      jsmin: {
        files: ['<%= globalConfig.tmp %>/assets/js/app.min.js'],
        tasks: ['uglify:distapp']
      },
      smin: {
        files: ['<%= globalConfig.tmp %>/assets/js/lib.min.js'],
        tasks: ['uglify:distlib']
      },
      cssmin: {
        files: ['<%= globalConfig.tmp %>/assets/css/*.js'],
        tasks: ['cssmin:dist']
      },
      htmlmin: {
        files: ['<%= globalConfig.tmp %>/*.html'],
        tasks: ['htmlmin:dist']
      },
    }
  });


  // resource Tasks
  grunt.loadTasks('tasks');
  grunt.loadNpmTasks('grunt-contrib-jshint');
  grunt.loadNpmTasks('grunt-contrib-concat');
  grunt.loadNpmTasks('grunt-processhtml');
  grunt.loadNpmTasks('grunt-contrib-uglify');
  grunt.loadNpmTasks('grunt-contrib-cssmin');
  grunt.loadNpmTasks('grunt-contrib-htmlmin');
  grunt.loadNpmTasks('grunt-contrib-copy');
  grunt.loadNpmTasks('grunt-contrib-clean');
  grunt.loadNpmTasks('grunt-contrib-connect');
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-connect-proxy');


  grunt.registerTask(
    'vanilla',
    [
      'resources',
      'jshint:beforeconcat',
      'configureProxies:vanilla',
      'connect:vanilla'
    ]
  );

  grunt.registerTask(
    'dev',
    [
      'clean:tmp',
      'resources',
      'jshint:beforeconcat',
      'concat:distJSLib',
      'concat:distJS',
      'concat:distCSS',
      'processhtml',
      'copy:dev',
      'configureProxies:development',
      'connect:development',
      'watch'
    ]
  );

  grunt.registerTask('default', ['dev']);

  grunt.registerTask(
    'dist',
    [
      'clean',
      'resources',
      'jshint:beforeconcat',
      'concat:distJSLib',
      'concat:distJS',
      'concat:distCSS',
      'processhtml',
      'copy:dist',
      'uglify:distapp',
      'uglify:distlib',
      'cssmin:dist',
      'htmlmin:dist',
    ]
  );

  grunt.registerTask(
    'serve',
    [
      'dist',
      'configureProxies:serve',
      'connect:serve',
      'watch'
    ]
  );
};
