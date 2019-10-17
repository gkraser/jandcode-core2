import * as test from 'jandcode.core.jsa.base/test'

let assert = test.assert

// асинхронные тесты

describe("utils/async1", function() {

    it("nextId", function(done) {
        console.info("this", this);
        setTimeout(function() {
            console.info("timeout ok");
            done()
        }, 200)
    })

})
    