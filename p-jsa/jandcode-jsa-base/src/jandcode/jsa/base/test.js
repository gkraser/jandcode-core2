/*

    Поддержка unit-тестирования

 */

import 'mocha/mocha'
import chai from 'chai/chai'
import mochaCss from 'mocha/mocha.css'

// css
Jc.requireCss(mochaCss)

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

