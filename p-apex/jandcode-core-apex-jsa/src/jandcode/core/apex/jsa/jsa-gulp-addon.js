const gulp = require('gulp')
const postcss = require('gulp-postcss')
const pxtorem = require('postcss-pxtorem')

/**
 * Преобразование едениц изменения px в rem для quasar.css
 */
function quasarCss_taskFactory(g, taskName, module, taskParams) {

    let srcs = [
        g.buildPathNodeModules + '/quasar/dist/*.css',
    ]

    let opts = {
        replace: false,
        propList: ['font', 'font-size', 'line-height', 'letter-spacing', 'padding*', 'marging*'],
    }

    gulp.task(taskName, function() {
        return gulp.src(srcs)
            .pipe(postcss([
                pxtorem(opts)
            ]))
            .pipe(gulp.dest(g.buildPathNodeModules + '/quasar/dist/css-rem'))
    })

}


module.exports = function(g) {
    g.registerTaskFactory("quasar-css", quasarCss_taskFactory)
}
