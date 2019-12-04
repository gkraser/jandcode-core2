import {test, apex} from './vendor'

// import * as m from '../../js'

describe('component-holder1.test.js', function() {

    let holder = apex.frame.componentHolder

    it("1", function() {
        let a = holder.get('frame')
        test.assert.ok(apex.isObject(a))
        test.assert.ok(Object.keys(a).length > 1)
        test.assert.ok(a['Dialog'] != null)
    })

    it("comp-type-1", function() {
        let a = holder.get('frame.Dialog')
        test.assert.notOk(apex.isObject(a))
        test.assert.ok(apex.isFunction(a))
    })

})

