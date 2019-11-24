import {test, apex} from './vendor'

// import * as m from '../../js'

describe('app1.test.js', function() {

    it("1", function() {
        test.assert.ok(!!apex.app)
        test.assert.ok(!!apex.app.cfgStore)
        test.assert.equal(apex.app.cfgStore.configKey, 'apex.app')
    })

})

