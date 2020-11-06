import {test} from '../vendor'
import * as m from '../../js/baseapp/router'

describe(__filename, function() {

    let routes1 = [
        {path: '', frame: '0'},
        {path: '/', frame: '1'},
        {path: '/usr/:id?', frame: '2'},
    ]

    let routes2_noSlash = [
        {path: '', frame: '0'},
        {path: 'usr/:id?', frame: '2'},
    ]

    let router

    function check(uri, res) {
        let a = router.resolve(uri)
        console.info('check:', uri, a, JSON.stringify(a));
        if (a === null && res !== null) {
            test.assert.fail("expected null")
        }
        if (a != null) {
            delete a.queryParams
            delete a.urlParams
        }
        test.assert.deepEqual(a, res)
    }

    //////

    it("RouteDef.match", function() {

        let r = new m.RouteDef({
            path: '/usr/:id',
            frame: 'f1'
        })
        let a = r.match("/usr/234")
        test.assert.equal(a.path, '/usr/234')
        test.assert.equal(a.params.id, '234')
    })

    it("FrameRouter1", function() {
        router = new m.FrameRouter()
        router.addRoutes(routes1)

        check("", {
            "path": "", "frame": "0", "params": {}
        })
        check("/", {
            "path": "/", "frame": "0", "params": {}
        })
        check("/usr/123", {
            "path": "/usr/123", "frame": "2", "params": {"id": "123"}
        })
        check("usr/321", null)
        check("/usr/123?q=1&e=3", {
            "path": "/usr/123", "frame": "2", "params": {"id": "123", "q": "1", "e": "3"}
        })
        check("/usr/?id=3366", {
            "path": "/usr/", "frame": "2", "params": {"id": "3366"}
        })
        check("/usr/", {
            "path": "/usr/", "frame": "2", "params": {}
        })
        check("/usr/привет", {
            "path": "/usr/привет", "frame": "2", "params": {id: "привет"}
        })
        check("/usr/%D0%BF%D1%80%D0%B8%D0%B2%D0%B5%D1%82?z=%D0%BF%D1%80%D0%B8%D0%B2%D0%B5%D1%82", {
            "path": "/usr/привет", "frame": "2", "params": {id: "привет", z: "привет"}
        })
        //
    })

    it("FrameRouter2 noSlash", function() {
        router = new m.FrameRouter()
        router.addRoutes(routes2_noSlash)

        // для {path: '', frame: '0'}, срабатывает и '/' и ''!

        check("", {
            "path": "", "frame": "0", "params": {}
        })
        check("/", {
            "path": "/", "frame": "0", "params": {}
        })
        check("/usr/123", null)
        check("usr/123", {
            "path": "usr/123", "frame": "2", "params": {"id": "123"}
        })
        //
    })

})

