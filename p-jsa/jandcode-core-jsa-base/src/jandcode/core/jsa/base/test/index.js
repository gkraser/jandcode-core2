/*

    Поддержка unit-тестирования

 */

import 'mocha/mocha'
import chai from 'chai/chai'
import mochaCss from 'mocha/mocha.css'
import mochaCssFix from './mocha-fix.css'

// css
Jc.requireCss(mochaCss)
Jc.requireCss(mochaCssFix)

//
let mocha = window.mocha
let assert = chai.assert

// инициализация
mocha.setup('bdd')

export {
    mocha,
    chai,
    assert,
}

