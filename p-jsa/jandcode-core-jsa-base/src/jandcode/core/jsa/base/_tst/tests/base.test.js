import * as test from 'jandcode.core.jsa.base/test'
import * as m from 'jandcode.core.jsa.base/js/base'

let assert = test.assert


describe("utils/base", function() {

    it("nextId", function() {
        assert.ok(m.nextId().match(/jc-\d+$/))
        assert.ok(m.nextId() !== m.nextId())
    })

    it('nextId prefix', function() {
        assert.ok(m.nextId('aa').match(/aa\d+$/))
        assert.ok(m.nextId('aa-').match(/aa-\d+$/))
    })

})
    