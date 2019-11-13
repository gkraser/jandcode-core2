const gulp = require('gulp')
const findRequires = require('find-requires')
const through2 = require('through2').obj;
const Vinyl = require('vinyl');
const fs = require('fs');

function nm_taskFactory(g, taskName, module, taskParams) {
    let globs = g.makeGlobs(g.nodeModulesPath, taskParams)

    gulp.task(taskName, function() {
        let lastRun = gulp.lastRun(taskName)

        return gulp.src(globs, {base: g.nodeModulesPath})
            .pipe(gulp.dest(g.buildPathNodeModules))
    })
}

function nmExtractRequire_taskFactory(g, taskName, module, taskParams) {
    let globs = g.makeGlobs(g.buildPathNodeModules, {globs: ['**/*.js']})

    gulp.task(taskName, function() {
        let lastRun = gulp.lastRun(taskName)

        return gulp.src(globs, {base: g.buildPathNodeModules})
        // extract requires
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
        let mapping = taskParams.mapping;
        for (let m in mapping) {
            let mMap = mapping[m]
            let fileText = "module.exports = require('" + mMap + "')"
            let reqText = '["' + mMap + '"]'
            fs.writeFileSync(g.buildPathNodeModules + '/' + m + '.js', fileText)
            fs.writeFileSync(g.buildPathCompiledNodeModules + '/' + m + '.js--compiled-req', reqText)
        }
        callback()
    })
}

///
module.exports = function(g) {
    g.registerTaskFactory("nm", nm_taskFactory)
    g.registerTaskFactory("nm-extract-require", nmExtractRequire_taskFactory)
    g.registerTaskFactory("nm-module-mapping", nmModuleMapping_taskFactory)
}
