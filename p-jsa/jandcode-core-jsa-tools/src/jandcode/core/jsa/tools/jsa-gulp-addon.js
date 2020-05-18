module.exports = function(g) {
    require('./jsa-gulp-js')(g)
    require('./jsa-gulp-babelHelpers')(g)
    require('./jsa-gulp-vue')(g)
    require('./jsa-gulp-nm')(g)
    require('./jsa-gulp-nm2')(g)
    require('./jsa-gulp-less')(g)
    require('./jsa-gulp-sass')(g)
}
