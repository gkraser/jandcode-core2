import * as test from './vendor'

import * as m from '../../js/utils/icons'

let assert = test.assert

describe(__filename, function() {

    it("1", function() {
        console.info(m.getIcons());
        m.registerIcons({
            'a':'b'
        })
    })

    it("2", function() {
        console.info(m.getIcons());
    })


})

