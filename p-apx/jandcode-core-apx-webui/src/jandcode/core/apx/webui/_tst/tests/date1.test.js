import {test} from '../vendor'
import * as m from '../../js/utils/date'

describe(__filename, function() {

    it("toStr", function() {
        let s

        s = m.toStr('2010-12-30')
        test.assert.equal(s, '2010-12-30')
        //
        s = m.toStr(new Date(2010, 11, 29)) // месяц с 0!!!
        test.assert.equal(s, '2010-12-29')
        //
        s = m.toStr(969642000000)
        test.assert.equal(s, '2000-09-23')
        //
    })

    it("toDisplayStr", function() {
        let s

        s = m.toDisplayStr('2010-12-30')
        test.assert.equal(s, '30.12.2010')
    })

    it("today", function() {
        let s

        s = m.today()
        console.info("today", s);
    })

})

