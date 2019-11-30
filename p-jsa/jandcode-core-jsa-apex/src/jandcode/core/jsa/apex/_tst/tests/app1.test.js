import {test, apex} from './vendor'

// import * as m from '../../js'

describe('app1.test.js', function() {

    it("1", function() {
        test.assert.ok(!!apex.app)
        test.assert.notOk(!!apex.app.cfgStore)
    })

})

