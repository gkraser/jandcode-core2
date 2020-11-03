/* Quasar iconSet
----------------------------------------------------------------------------- */

import {jsaBase} from '../vendor'
import svgQuasarIconSet from 'quasar/dist/icon-set/svg-material-icons.umd.min.js'

export let quasarIconSet = {}
export let quasarIcons = {}

// формат: https://quasar.dev/vue-components/icon#Svg-icon-format
function quasarSvgToNormalSvg(icon) {
    let [def, viewBox] = icon.split('|')
    viewBox = viewBox !== void 0 ? viewBox : '0 0 24 24'

    let res = '<svg viewBox="' + viewBox + '">'

    def.split('&&').map(path => {
        let [d, style, transform] = path.split('@@')
        res += '<path d="' + d + '"'
        if (style) {
            res += ' style="' + style.replace(/"/g, "'") + '"'
        }
        if (transform) {
            res += ' transform="' + style.replace(/"/g, "'") + '"'
        }
        res += '/>'
    })

    res += '</svg>'

    return res
}

function cnv(toJsa, toQuasar, from, prefix) {
    for (let key in from) {
        let value = from[key]
        let newKey = prefix + '.' + key
        if (jsaBase.isObject(value)) {
            toQuasar[key] = {}
            cnv(toJsa, toQuasar[key], value, newKey)
        } else {
            if (value.startsWith('M')) {
                value = quasarSvgToNormalSvg(value)
            }
            toJsa[newKey] = value
            toQuasar[key] = newKey
        }
    }
}

function init() {
    let miq = JSON.parse(JSON.stringify(svgQuasarIconSet))
    cnv(quasarIcons, quasarIconSet, miq, 'quasar')
    delete quasarIcons['quasar.name']
}

init()
