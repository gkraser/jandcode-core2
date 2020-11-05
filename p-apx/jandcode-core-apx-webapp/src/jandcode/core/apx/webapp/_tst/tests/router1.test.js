import '../vendor'
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

    it("2", function() {

        let router = new m.FrameRouter()
        router.addRoutes([
            {path: '', frame: '0'},
            {path: '/', frame: '1'},
            {path: '/usr/:id?', frame: '2'},
        ])

        let a

        function check(uri) {
            let a = router.resolve(uri)
            console.info('check:', uri, a);
        }

        console.info(decodeURI("aaa"));

        check("")
        check("/")
        check("/usr/123")
        check("usr/321")
        check("/usr/123?q=1&e=3")
        check("/usr/?id=3366")
        check("/usr/")

        //
    })

    it('should ', function() {
        let url = new URL("usr/123?q=1&e=3&q=44", 'http://dummy');
        console.info("url", url);
        console.info("sp", url.searchParams);
        //console.info("sp1",url.searchParams.getAll());
        for (let key of url.searchParams.keys()) {
            let value = url.searchParams.get(key)
            console.info("!", key, value);
        }
        ////
        let a = 'http://localhost:8080/jc/_tst/run?path=/jandcode/core/apx/webapp/_tst/tests/router1.test.js&p=%D0%BF%D1%80%D0%B8%D0%B2%D0%B5%D1%82'
        url = new URL(a)
        console.info("url",url);
        for (let key of url.searchParams.keys()) {
            let value = url.searchParams.get(key)
            console.info("!", key, value);
        }
        let b = url.searchParams.get('p')
        if (b=='привет'){
            console.info("ghbb!!!!");
        }

    });
})

