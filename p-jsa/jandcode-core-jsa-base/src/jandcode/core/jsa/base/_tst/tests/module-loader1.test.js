import {test, jsaBase} from './vendor'

// import * as m from '../../js'

describe(__filename, function() {

    it("error-load-1", function() {
        Jc.loadModule('xxx')
        throw 'Не отработало'
    })

})

