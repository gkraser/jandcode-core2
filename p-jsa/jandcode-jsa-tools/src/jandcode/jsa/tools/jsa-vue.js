/*

    Поддержка vue

 */

const compiler = require('vue-template-compiler')
const babel = require("@babel/core");
const transpile = require('vue-template-es2015-compiler')
const path = require("path");
const fs = require("fs");


/**
 * Компиляция текста vue-файла в тело модуля
 * @param text текст vue-файла
 * @param babelConfig конфигурация babel
 * @param styleCompilers компиляторы стилей вида {less:fn,...}
 */
function compileVue(text, babelConfig, styleCompilers) {
    let res = ''
    let parts = compiler.parseComponent(text)

    if (parts.script) {
        let content = parts.script.content
        if (content) {
            let r = babelTransform(content, babelConfig)
            res += r.code + '\n'
        }
    }

    res += 'var __OPT__ = module.exports.default;\n'

    if (parts.template) {
        let content = parts.template.content
        if (content) {
            let r = compiler.compile(content)

            if (r.errors.length) {
                r.errors.forEach(function(msg) {
                    console.error('\n' + msg + '\n')
                })
                throw new Error('Vue template compilation failed')
            }

            res +=
                '__OPT__.render = ' + toFunction(r.render) + '\n';

            if (r.staticRenderFns.length) {
                res +=
                    '__OPT__.staticRenderFns = ' + '[' + r.staticRenderFns.map(toFunction).join(',') + ']' + '\n'
            }

        }
    }

    if (parts.styles) {
        let stres = ''
        for (let style of parts.styles) {
            let lang = style.lang || 'css'
            if (lang === 'css') {
                stres += style.content + '\n';
            } else {
                let scomp = styleCompilers[lang]
                if (!scomp) {
                    throw new Error('vue style lang [' + lang + '] not supported')
                }
                let stext = scomp(style.content)
                stres += stext + "\n";
            }
        }
        stres = stres.trim()
        if (stres) {
            res +=
                'var __CSS__ = {css: true, filename: __filename, text: ' + JSON.stringify(stres) + '}\n' +
                'var __BFC__ = __OPT__.beforeCreate;\n' +
                '__OPT__.beforeCreate = function() {Jc.requireCss(__CSS__);' +
                'if (__BFC__) __BFC__.call(this);}\n'
        }
    }

    return res
}

function toFunction(code) {
    return transpile('function render() {' + code + '}')
}

function babelTransform(text, babelConfig) {
    return babel.transform(text, babelConfig)
}


module.exports = {
    compileVue
}
