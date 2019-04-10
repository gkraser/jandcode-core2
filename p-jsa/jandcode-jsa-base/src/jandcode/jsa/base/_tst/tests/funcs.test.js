import * as test from 'jandcode.jsa.base/test'
import * as m from 'jandcode.jsa.base/js/utils/funcs'

let assert = test.assert

describe("utils/funcs", function() {

    it("series", function(done) {
        let cnt = ''
        let a = new m.FuncsSeries()
        //
        a.add(function(cb) {
            cnt = cnt + '-1'
            cb()
        })
        a.add(function() {
            cnt = cnt + '-2'
        })
        a.add(function(cb) {
            cnt = cnt + '-3'
            setTimeout(function() {
                cb()
            }, 100)
        })
        a.run(function() {
            assert(cnt === '-1-2-3')
            done()
        })
    })

    it("series2", function(done) {
        let cnt = ''
        let fnArray = [
            function(cb) {
                cnt = cnt + '-1'
                cb()
            },
            function() {
                cnt = cnt + '-2'
            },
            function(cb) {
                cnt = cnt + '-3'
                setTimeout(function() {
                    cb()
                }, 100)
            }
        ]
        m.series(fnArray, function() {
            assert(cnt === '-1-2-3')
            done()
        })
    })

})
    