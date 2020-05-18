const gulp = require('gulp')
const findRequires = require('find-requires')
const through2 = require('through2').obj;
const Vinyl = require('vinyl');
const fs = require('fs');
const jsaSupport = require('./jsa-support');

function nm_taskFactory(g, taskName, module, taskParams) {

    let copyGlobs = []
    let pfx = g.nodeModulesPath + '/'
    for (let lib of jsaSupport.nodeJsLibs) {
        copyGlobs.push(pfx + lib.name + '/package.json')
        if (lib.includeClient.length == 0) {
            copyGlobs.push(pfx + lib.name + '/**/*')
        } else {
            for (let z of lib.includeClient) {
                copyGlobs.push(pfx + lib.name + '/' + z)
            }
        }
        for (let z of lib.excludeClient) {
            copyGlobs.push('!' + pfx + lib.name + '/' + z)
        }
    }

    gulp.task(taskName, function() {
        let lastRun = gulp.lastRun(taskName)

        return gulp.src(copyGlobs, {base: g.nodeModulesPath})
            .pipe(gulp.dest(g.buildPathNodeModules))
    })
}

function nmExtractRequire_taskFactory(g, taskName, module, taskParams) {

    gulp.task(taskName, function() {
        let lastRun = gulp.lastRun(taskName)

        let globsBundle = ['**/*.js']
        for (let lib of jsaSupport.nodeJsLibs) {
            for (let z of lib.excludeRequire) {
                globsBundle.push('!' + lib.name + '/' + z)
            }
        }
        let globs = g.makeGlobs(g.buildPathNodeModules, {globs: globsBundle})

        return gulp.src(globs, {base: g.buildPathNodeModules, nodir: true})
            .pipe(through2(function(file, enc, callback) {
                let src = file.contents.toString()
                let a = src.lastIndexOf('require(')
                if (a === -1) {
                    callback()  // точно нет require
                    return
                }
                let b = src.indexOf(')', a)
                if (b === -1) {
                    callback()  // точно нет require
                    return
                }
                src = src.substring(0, b + 1) // уменьшаем размер для анализа! быстрее станет...
                let r = findRequires(src)
                let rfile = new Vinyl({
                    cwd: file.cwd,
                    base: file.base,
                    path: file.path + '--compiled-req',
                    contents: Buffer.from(JSON.stringify(r))
                });
                if (r.length === 0) {
                    callback()
                } else {
                    callback(null, rfile)
                }
            }))
            .pipe(gulp.dest(g.buildPathCompiledNodeModules))
    })
}

function nmModuleMapping_taskFactory(g, taskName, module, taskParams) {
    gulp.task(taskName, function(callback) {
        fs.mkdirSync(g.buildPathCompiledNodeModules, {recursive: true})

        for (let lib of jsaSupport.nodeJsLibs) {
            for (let m in lib.moduleMapping) {
                let mMap = lib.moduleMapping[m]
                let fileText = "module.exports = require('" + mMap + "')"
                let reqText = '["' + mMap + '"]'
                fs.writeFileSync(g.buildPathNodeModules + '/' + m + '.js', fileText)
                fs.writeFileSync(g.buildPathCompiledNodeModules + '/' + m + '.js--compiled-req', reqText)
            }
        }

        callback()
    })
}

///
module.exports = function(g) {
    g.registerTaskFactory("nm2", nm_taskFactory)
    g.registerTaskFactory("nm2-extract-require", nmExtractRequire_taskFactory)
    g.registerTaskFactory("nm2-module-mapping", nmModuleMapping_taskFactory)
}