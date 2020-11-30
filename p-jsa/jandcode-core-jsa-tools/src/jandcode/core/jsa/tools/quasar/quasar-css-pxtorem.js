/*

Извлечение px-переменных из quasar.css и генерация на их основе
less-файла для темы.

Как пользоватся
---------------

В gulpfile.js в проекте:

    g.init(function(g) {
        require('jandcode.core.jsa.tools/quasar/quasar-css-pxtorem')()
    })

Появится gulp-task: quasar-pxtorem.

Результат записывается в temp:
* q-fix-px.less - less файл, с заменой px -> @VAR
* quasar.css - измененный quasar.css, с заменой px->rem

----------------------------------------------------------------------------- */

const gulp = require('gulp')
const through2 = require('through2').obj;
const Vinyl = require('vinyl');
const pxRegex = require("postcss-pxtorem/lib/pixel-unit-regex");
const postcss = require('gulp-postcss')
const postcss_pxtorem = require('./lib/postcss-pxtorem-fix')
const path = require('path')


let _replace_pixels = []

/**
 * Замена XXpx на переменную @rXXXpx
 * @param m
 * @param $1
 * @return {string|string|*}
 */
function pxReplace(m, $1) {
    if (!$1) return m;
    const pixels = parseFloat($1);
    if (pixels < 0) return m;
    _replace_pixels.push(pixels)
    return pixels === 0 ? "0" : '@r' + pixels + "px";
}

function toFixed(number, precision) {
    const multiplier = Math.pow(10, precision + 1),
        wholeNumber = Math.floor(number * multiplier);
    return (Math.round(wholeNumber / 10) * 10) / multiplier;
}

function initGulpTask() {

    gulp.task('quasar-pxtorem', () => {

        let lessFiles = {}

        function handler(css, decl, i, value, origValue) {
            const filePath = css.source.input.file;
            let z1 = lessFiles[filePath]
            if (!z1) {
                z1 = {
                    rules: [
                        {
                            atrule: '',
                            selector: '',
                            props: [],
                            dummy: true,
                        }
                    ],
                }
                lessFiles[filePath] = z1
            }
            let selector = decl.parent.selector
            let atruleNode = decl.parent.parent
            let atrule = ''
            if (atruleNode.type === 'atrule') {
                atrule = atruleNode.name + ' ' + atruleNode.params
            }
            let cur = z1.rules[z1.rules.length - 1]
            if (cur.atrule !== atrule || cur.selector !== selector) {
                cur = {
                    atrule: atrule,
                    selector: selector,
                    props: []
                }
                z1.rules.push(cur)
            }
            //
            if (origValue.indexOf('0.1px') !== -1) {
                console.warn('bad value 0.1px, skip', selector, '{', decl.prop, ':', origValue, '}')
                return
            }
            if (origValue.indexOf('7.15') !== -1) {
                console.warn('bad value 7.15 replace to 7', selector, '{', decl.prop, ':', origValue, '}')
                origValue = origValue.replace(/7\.15/g, "7")
                console.info("--", origValue);
            }

            //
            let nv = origValue.replace(pxRegex, pxReplace)
            cur.props.push(decl.prop + ': ' + nv + ';')
        }

        // черный список селекторов
        let sz = ['none', 'xs', 'sm', 'md', 'lg', 'xl']
        let bn = ['t', 'r', 'b', 'l', 'a', 'x', 'y']
        let black1 = ['-gutter-']
        for (let s of sz) {
            for (let b of bn) {
                black1.push('.q-m' + b + '-' + s)
                black1.push('.q-p' + b + '-' + s)
            }
        }

        let rootValue = 16
        return gulp.src('node_modules/quasar/dist/quasar.css')
            .pipe(postcss([
                postcss_pxtorem({
                    replace: false,
                    rootValue: rootValue,
                    propList: ['font', 'font-size', 'line-height', 'letter-spacing',
                        'padding*', 'margin*', 'min-height'],
                    selectorBlackList: [/^body$/].concat(black1),
                    onHandle: handler,
                })
            ]))
            .pipe(through2(function(file, enc, callback) {
                let less = lessFiles[file.path]
                if (less) {

                    let s = ''
                    for (let rule of less.rules) {
                        if (rule.dummy) {
                            continue;
                        }
                        let pad = ''
                        if (rule.atrule !== '') {
                            s += '@' + rule.atrule + ' {\n'
                            pad = '  '
                        }

                        s += pad + rule.selector + '{\n'
                        for (let p of rule.props) {
                            s += pad + '  ' + p + "\n"
                        }
                        s += pad + '}\n'

                        if (rule.atrule !== '') {
                            s += '}\n'
                        }
                    }

                    var rfile = new Vinyl({
                        cwd: file.cwd,
                        base: file.base,
                        path: path.resolve(file.base, 'q-fix-px.less'),
                        contents: Buffer.from(s)
                    });
                    this.push(rfile)

                    //
                    let vars = [...new Set(_replace_pixels)]
                    _replace_pixels = []
                    vars.sort((a, b) => a - b)
                    let s_vars = ''
                    for (let v of vars) {
                        let v1 = toFixed(v / rootValue, 5);
                        s_vars += '@r' + v + 'px: ' + v1 + 'rem;\n';
                    }

                    rfile = new Vinyl({
                        cwd: file.cwd,
                        base: file.base,
                        path: path.resolve(file.base, 'q-fix-px--vars.less'),
                        contents: Buffer.from(s_vars)
                    });
                    this.push(rfile)
                } else {
                    console.info("NOT FOUND", file.path);
                }
                callback(null, file)
            }))
            .pipe(gulp.dest('temp/'))
    })

}

//
module.exports = initGulpTask

