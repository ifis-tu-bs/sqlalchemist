module.exports = function(grunt) {
  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),
    concat: {
      dist: {
        src: ['assets/lib/melonJS-2.0.2.js', 'assets/lib/plugins/*.js', 'assets/js/game.js', 'assets/js/DOMManipulation/*.js','build/assets/js/resources.js','assets/js/DataTypes/*.js', 'assets/js/ajax/ajax.js', 'assets/js/screens/*.js', 'assets/js/entities/*.js'],
        dest: 'build/assets/js/app.js'
      }
    },
    copy: {
      dist: {
        files: [{
          src: 'assets/index.css',
          dest: 'build/assets/index.css'
        },{
          src: 'assets/main.js',
          dest: 'build/assets/main.js'
        },{
          src: 'manifest.json',
          dest: 'build/manifest.json'
        },{
          src: 'package.json',
          dest: 'build/package.json'
        },{
          src: 'assets/data/**/*',
          dest: 'build/',
          expand: true
        },{
          src: 'assets/icons/**/*',
          dest: 'build/',
          expand: true
        },{
          src: 'assets/css/*.css',
          dest: 'build/',
          expand: true
        }, {
          src: 'assets/lib/sql.js',
          dest: 'build/assets/lib/sql.js'
        },
        {
          src: 'assets/lib/codemirror.js',
          dest: 'build/assets/lib/codemirror.js'
        },
        {
          src: 'assets/lib/matchbrackets.js',
          dest: 'build/assets/lib/matchbrackets.js'
        },
        {
          src: 'assets/lib/mode-sql.js',
          dest: 'build/assets/lib/mode-sql.js'
        },
        {
          src: 'assets/lib/ace.js',
          dest: 'build/assets/lib/ace.js'
        },
        {
          src: 'assets/lib/theme-twilight.js',
          dest: 'build/assets/lib/theme-twilight.js'
        },
        {
          src: 'assets/lib/theme-ambiance.js',
          dest: 'build/assets/lib/theme-ambiance.js'
        }
        ]
      },
      move: {
        files: [{
          cwd: 'build/',
          src: '**/*',
          dest: '../back-end/public/game/',
          expand: true
        }]
      }
    },
    clean: {
      app: ['build/assets/js/app.js'],
      dist: ['build/assets/','bin/'], ///////////bin?
    },
    processhtml: {
      dist: {
        options: {
          process: true,
          data: {
            title: '<%= pkg.name %>',
          }
        },
        files: {
          'build/index.html': ['index.html']
        }
      }
    },
    uglify: {
      options: {
        report: 'min',
        preserveComments: 'some'
      },
      dist: {
        files: {
          'build/assets/js/app.min.js': [
            'build/assets/js/app.js'
          ]
        }
      }
    },
    connect: {
      server: {
        options: {
          port: 8000,
          keepalive: true
        }
      }
    },
    'download-electron': {
      version: '0.24.0',
      outputDir: 'bin'
    },
    asar: {
      dist: {
        cwd: 'build',
        src: ['**/*', '!assets/js/app.js'],
        expand: true,
        dest: 'bin/' + (
          process.platform === 'darwin'
            ? 'Electron.app/Contents/Resources/'
            : 'resources/'
        ) + 'app.asar'
      },
    },
    resources: {
      dist: {
        options: {
          dest: 'build/assets/js/resources.js',
          varname: 'game.resources',
        },
        files: [{
          src: ['assets/data/img/**/*.png'],
          type: 'image'
        },{
          src: ['assets/data/bgm/**/*', 'assets/data/sfx/**/*'],
          type: 'audio'
        },{
          src: ['assets/data/img/**/*.json'],
          type: 'json'
        },{
          src: ['assets/data/map/**/*.tmx', 'assets/data/map/**/*.json'],
          type: 'tmx'
        },{
          src: ['assets/data/map/**/*.tsx'],
          type: 'tsx'
        }]
      }
    },
    watch: {
      resources: {
        files: ['data/**/*'],
        tasks: ['resources'],
        options: {
          spawn: false,
        },
      },
    },
  });

  grunt.loadNpmTasks('grunt-contrib-concat');
  grunt.loadNpmTasks('grunt-contrib-uglify');
  grunt.loadNpmTasks('grunt-contrib-copy');
  grunt.loadNpmTasks('grunt-contrib-clean');
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-processhtml');
  grunt.loadNpmTasks('grunt-contrib-connect');
  grunt.loadNpmTasks('grunt-download-electron');
  grunt.loadNpmTasks('grunt-asar');

  // Custom Tasks
  grunt.loadTasks('tasks');

  grunt.registerTask('default', ['resources', 'concat', 'uglify', 'copy:dist', 'processhtml', 'clean:app']);
  grunt.registerTask('dist', ['default', 'download-electron', 'asar']);
  grunt.registerTask('serve', ['resources', 'connect', 'watch']);
  grunt.registerTask('move', ['default', 'copy']);

}
