import * as test from 'jandcode.jsa.base/test'
import * as m from 'jandcode.jsa.base'

let assert = test.assert

describe("index", function() {

    it("test 1", function() {
        assert.ok('base' in m);
        assert.ok('nextId' in m.base);
    })

})
    