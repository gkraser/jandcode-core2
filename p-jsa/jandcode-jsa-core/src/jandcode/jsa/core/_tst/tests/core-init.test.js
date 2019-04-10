import * as test from 'jandcode.jsa.base/test'
import 'jandcode.jsa.core'

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
    