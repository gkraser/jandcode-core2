import {test, jsaBase} from './vendor'

// import * as m from '../../js'

describe(__filename, function() {

    it("error-load-1", async function() {
        try {
            await Jc.loadModule('xxx')
            throw new Error('неожиданно')
        } catch(e) {
            // ignore
        }
    })

})

