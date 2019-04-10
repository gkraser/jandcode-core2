const gulp = require('gulp')

function nm_taskFactory(g, taskName, module, taskParams) {
    let globs = g.makeGlobs(g.nodeModulesPath, taskParams)
    
    gulp.task(taskName, function() {
        let lastRun = gulp.lastRun(taskName)

        return gulp.src(globs, {base: g.nodeModulesPath})
            .pipe(gulp.dest(g.buildPathNodeModules))
    })
}

///
module.exports = function(g) {
    g.registerTaskFactory("nm", nm_taskFactory)
}
