import * as test from 'jandcode.core.jsa.base/test'
import * as m from 'jandcode.core.jsa.base'

let assert = test.assert

describe("index", function() {

    it("test 1", function() {
        assert.ok('base' in m);
        assert.ok('nextId' in m.base);
    })

})
    