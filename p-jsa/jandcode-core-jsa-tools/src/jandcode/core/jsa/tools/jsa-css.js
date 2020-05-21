/*

Поддержка css

----------------------------------------------------------------------------- */

const Vinyl = require('vinyl');
var CleanCSS = require('clean-css');

/**
 * Минификация css-файла.
 * @param g JsaGulpBuilder
 * @param file какой файл
 * @param context gulp-контекст (this из места вызова)
 * @param suf суффикс, если не задан - '-min'
 */
function minifyCss(g, file, context, suf) {
    if (!suf) {
        suf = '-min'
    }
    let src = file.contents.toString()
    let opts = {
    }
    let res = new CleanCSS(opts).minify(src);
    if (res.styles) {
        let rfile = new Vinyl({
            cwd: file.cwd,
            base: file.base,
            path: file.path + suf,
            contents: Buffer.from(res.styles)
        });
        context.push(rfile)
    }
    if (res.errors && res.errors.length>0) {
        console.error("ERROR minify css:" + file.path + ": ")
        console.error(res.errors)
    }
    if (res.warnings && res.warnings.length>0) {
        console.error("WARNING minify css:" + file.path + ": ")
        console.error(res.warnings)
    }

}

module.exports = {
    minifyCss
}
