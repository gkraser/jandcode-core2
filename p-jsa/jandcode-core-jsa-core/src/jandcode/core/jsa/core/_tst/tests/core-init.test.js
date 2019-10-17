import * as test from 'jandcode.core.jsa.base/test'
import 'jandcode.core.jsa.core'

let assert = test.assert

describe("core-init.test.js", function() {

    it("ready", function() {
        console.info("test start");
        Jc.ready(function() {
            console.info("ready1");
        })
        Jc.ready(function() {
            console.info("ready2");
        })
    })


})
    