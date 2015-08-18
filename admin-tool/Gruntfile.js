'use strict';

module.exports = function(grunt) {

    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),

        concat: {
            dist: {
                src: ["adminapp/scripts/app.js", "adminapp/scripts/services/*.js", "adminapp/scripts/controllers/*.js"],
                dest: "build/adminapp/scripts/app.js"
            }
        },
        copy: {
            dist: {
                files: [{
                    src: 'adminapp/css/**/*',
                    dest: 'build/',
                    expand: true
                },
                {
                    src: "adminapp/views/**/*",
                    dest: "build/",
                    expand: true
                },
                {
                    src: 'adminapp/templates/**/*',
                    dest: 'build/',
                    expand: true
                },
                {
                    src: 'index.html',
                    dest: 'build/index.html'
                },
                {
                    src: 'adminapp/scripts/libraries/**/*',
                    dest: 'build/',
                    expand: true
                }]
            },
            move: {
                files: [{
                    cwd: 'build/',
                    src: '**/*',
                    dest: '../back-end/public/admintool/',
                    expand: true
                }]
            }
        },
        uglify: {
            options: {
                report: 'min',
                preserveComments: 'some'
            },
            dist: {
                files: {
                    'build/adminapp/scripts/app.min.js': [
                        'build/adminapp/scripts/app.js'
                    ]
                }
            }
        },
        connect: {
            server: {
                options: {
                    port: 9001,
                    keepalive: true
                }
            }
        },
        processhtml: {
            dist: {
                options: {
                    process: true,
                    data: {
                        title: '<%= pkg.name %>'
                    }
                },
                files: {
                    'build/index.html': ['index.html']
                }
            },
        },
    });

    grunt.loadNpmTasks('grunt-processhtml');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-contrib-connect');
    grunt.loadNpmTasks('grunt-contrib-uglify');

    grunt.registerTask('default', ['concat:dist', 'copy:dist', 'processhtml:dist', 'uglify:dist']);
    grunt.registerTask('serve', ['connect:server']);
    grunt.registerTask('move', ['default', 'copy:move']);

};
