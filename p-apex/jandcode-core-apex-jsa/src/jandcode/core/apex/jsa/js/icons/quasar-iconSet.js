/* Quasar iconSet
----------------------------------------------------------------------------- */

import {jsaBase, Quasar} from '../vendor'

export let quasarIconSet = {}
export let quasarIcons = {}

function cnv(toApex, toQuasar, from, prefix) {
    for (let key in from) {
        let value = from[key]
        let newKey = prefix + '.' + key
        if (jsaBase.isObject(value)) {
            toQuasar[key] = {}
            cnv(toApex, toQuasar[key], value, newKey)
        } else {
            toApex[newKey] = value
            toQuasar[key] = newKey
        }
    }
}

function build() {
    let miq = JSON.parse(JSON.stringify(Quasar.iconSet.def))
    cnv(quasarIcons, quasarIconSet, miq, 'quasar')
    delete quasarIcons['quasar.name']
}

build()

