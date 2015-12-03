module.exports = function(grunt) {
  var globalConfig = {
    name: 'sql-alchemist',
    src: 'src',
    build:'build',
    dest:'front-end'
  };

  grunt.initConfig({
    globalConfig: globalConfig,
    resources: {
      dist: {
        options: {
          dest: '<%= globalConfig.build %>/assets/js/resources.js',
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
      dist: ['<%= globalConfig.src %>/assets/js/**/*.js']
    },

    concat: {
      js: {
        src: [
          '<%= globalConfig.src %>/assets/lib/melonJS-2.0.2.js',
          '<%= globalConfig.src %>/assets/lib/codemirror.js',
          '<%= globalConfig.src %>/assets/js/game.js',
          '<%= globalConfig.build %>/assets/js/resources.js',
          '<%= globalConfig.src %>/assets/js/DataTypes/*.js',
          '<%= globalConfig.src %>/assets/js/DOMManipulation/*.js',
          '<%= globalConfig.src %>/assets/js/ajax/*.js',
          '<%= globalConfig.src %>/assets/js/screens/*.js',
          '<%= globalConfig.src %>/assets/js/entities/*.js'
        ],
        dest: '<%= globalConfig.build %>/assets/js/app.js'
      },
      css: {
        src: [
            '<%= globalConfig.src %>/assets/reset.css',
            '<%= globalConfig.src %>/assets/index.css',
            '<%= globalConfig.src %>/assets/css/*.css'
        ],
        dest: '<%= globalConfig.build %>/assets/css/app.css'
      }
    },

    processhtml: {
      dist: {
        files: {
          '<%= globalConfig.build %>/index.html': ['<%= globalConfig.src %>/index.html']
        }
      }
    },

    uglify: {
      options: {
        report: 'min',
        preserveComments: 'some',
        banner: '/*! <%= grunt.template.today("yyyy-mm-dd") %> */'
      },
      dist: {
        files: {
          '<%= globalConfig.dest %>/assets/js/app.min.js': [
            '<%= globalConfig.build %>/assets/js/app.js'
          ]
        }
      }
    },

    cssmin: {
      dist: {
        files: {
          '<%= globalConfig.dest %>/assets/css/app.min.css': ['<%= globalConfig.build %>/assets/css/app.css']
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
          '<%= globalConfig.dest %>/index.html': '<%= globalConfig.build %>/index.html',
        }
      }
    },

    copy: {
      dist: {
        files: [
          {
            src: '<%= globalConfig.src %>/manifest.json',
            dest: '<%= globalConfig.dest %>/manifest.json'
          },
          {
            src: 'package.json',
            dest: '<%= globalConfig.dest %>/package.json'
          },
          {
            cwd: '<%= globalConfig.src %>',
            src: 'assets/data/**/*',
            dest: '<%= globalConfig.dest %>/',
            expand: true
          },
          {
            cwd: '<%= globalConfig.src %>',
            src: 'assets/icons/**/*',
            dest: '<%= globalConfig.dest %>/',
            expand: true
          }
        ]
      }
    },


    clean: {
      app: ['<%= globalConfig.build %>'],
      dist: ['<%= globalConfig.dest %>']
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


  grunt.registerTask('default', ['resources', 'jshint', 'concat', 'processhtml', 'uglify', 'cssmin', 'htmlmin', 'copy', 'clean:app']);
  //grunt.registerTask('serve', ['default', 'connect', 'watch']);
}
