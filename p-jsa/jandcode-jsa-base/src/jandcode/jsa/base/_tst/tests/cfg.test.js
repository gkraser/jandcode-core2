import * as test from 'jandcode.jsa.base/test'
import * as m from 'jandcode.jsa.base/js/cfg'

let assert = test.assert

describe("utils/cfg", function() {

    it("set", function() {
        let z = new m.Cfg()
        z.set({
            a: 1,
            b: {
                c: 2
            }
        })
        z.set({
            b: {
                d: 3
            }
        })
        assert.equal(z.a, 1)
        assert.equal(z.b.c, 2)
        assert.equal(z.b.d, 3)
    })


})
    