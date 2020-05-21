/*

Поддержка js

----------------------------------------------------------------------------- */

const Vinyl = require('vinyl');
const Terser = require("terser");

/**
 * Минификация js-файла.
 * @param g JsaGulpBuilder
 * @param file какой файл
 * @param context gulp-контекст (this из места вызова)
 * @param suf суффикс, если не задан - '-min'
 */
function minifyJs(g, file, context, suf) {
    if (!suf) {
        suf = '-min'
    }
    let src = file.contents.toString()
    let res = Terser.minify(src, g.terserConfig)
    if (res.code) {
        let rfile = new Vinyl({
            cwd: file.cwd,
            base: file.base,
            path: file.path + suf,
            contents: Buffer.from(res.code)
        });
        context.push(rfile)
    }
    if (res.error) {
        console.error("ERROR minify js:" + file.path + ": " + res.error.message)
    }

}

module.exports = {
    minifyJs
}
