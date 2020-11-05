import {apx} from '../vendor'

import * as m from '../../js/baseapp/router'

describe(__filename, function() {

    it("1", function() {
        
        let r = new m.RouteDef({
            path: '/usr/:id',
            frame: 'f1'
        })

        let a = r.match("/usr/234")
        console.info(a);

        //
    })

})

