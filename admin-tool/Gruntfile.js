'use strict';

module.exports = function(grunt) {
  var globalConfig = {
    name: 'sql-alchemist',
    src: 'src',
    build:'build',
    dest:'admin-tool'
  };
  grunt.initConfig({
    globalConfig: globalConfig,

    jshint: {
      dist: ['<%= globalConfig.src %>/assets/js/**/*.js']
    },


    concat: {
      js: {
        src: [
          '<%= globalConfig.src %>/assets/lib/angular.js',
          '<%= globalConfig.src %>/assets/lib/angular-animate.min.js',
          '<%= globalConfig.src %>/assets/lib/angular-cookies.min.js',
          '<%= globalConfig.src %>/assets/lib/angular-route.min.js',
          '<%= globalConfig.src %>/assets/lib/angular-sanitize.min.js',
          '<%= globalConfig.src %>/assets/lib/ace.min.js',
          '<%= globalConfig.src %>/assets/lib/ui-ace.min.js',
          '<%= globalConfig.src %>/assets/lib/mode-sql.js',
          '<%= globalConfig.src %>/assets/lib/theme-twilight.js',
          '<%= globalConfig.src %>/assets/lib/jquery-2.1.4.min.js',
          '<%= globalConfig.src %>/assets/lib/bootstrap.min.js',
          '<%= globalConfig.src %>/assets/lib/ui-bootstrap-tpls-0.14.2.min.js',
          '<%= globalConfig.src %>/assets/js/app.js',
          '<%= globalConfig.src %>/assets/js/services/*.js',
          '<%= globalConfig.src %>/assets/js/controllers/*.js',
        ],
        dest: '<%= globalConfig.build %>/assets/js/app.min.js'
      },
      css: {
        src: [
          '<%= globalConfig.src %>/assets/css/bootstrap.min.css',
          '<%= globalConfig.src %>/assets/css/app.css',
          '<%= globalConfig.src %>/assets/css/modalStyleClass.css'
        ],
        dest: '<%= globalConfig.build %>/assets/css/app.min.css'
      }
    },

    processhtml: {
      dist: {
        files: {
          '<%= globalConfig.build %>/index.html': ['<%= globalConfig.src %>/index.html']
        }
      },
    },

    uglify: {
      options: {
        report: 'none',
        preserveComments: 'none',
        banner: '/*! <%= grunt.template.today("yyyy-mm-dd") %> */'
      },
      dist: {
        files: {
          '<%= globalConfig.dest %>/assets/js/app.min.js': [
            '<%= globalConfig.build %>/assets/js/app.min.js'
          ]
        }
      }
    },
    cssmin: {
      dist: {
        files: {
          '<%= globalConfig.dest %>/assets/css/app.min.css': ['<%= globalConfig.build %>/assets/css/app.min.css']
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
            cwd: '<%= globalConfig.src %>',
            src: 'assets/fonts/**/*',
            dest: '<%= globalConfig.dest %>/',
            expand: true
          },
          {
            cwd: '<%= globalConfig.src %>',
            src: 'assets/data/**/*',
            dest: '<%= globalConfig.dest %>/',
            expand: true
          },
          {
            cwd: '<%= globalConfig.src %>',
            src: 'assets/templates/**/*',
            dest: '<%= globalConfig.dest %>/',
            expand: true
          },
          {
            cwd: '<%= globalConfig.src %>',
            src: 'assets/views/**/*',
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

  grunt.loadNpmTasks('grunt-contrib-jshint');
  grunt.loadNpmTasks('grunt-contrib-concat');
  grunt.loadNpmTasks('grunt-processhtml');
  grunt.loadNpmTasks('grunt-contrib-uglify');
  grunt.loadNpmTasks('grunt-contrib-cssmin');
  grunt.loadNpmTasks('grunt-contrib-htmlmin');
  grunt.loadNpmTasks('grunt-contrib-copy');
  grunt.loadNpmTasks('grunt-contrib-clean');

  grunt.registerTask('default', ['jshint', 'concat', 'processhtml', 'uglify', 'cssmin', 'htmlmin', 'copy', 'clean:app']);
};
