import * as test from 'jandcode.core.jsa.base/test'
import * as m from 'jandcode.core.jsa.base/js/cnv'

let assert = test.assert

describe("utils/cnv", function() {

    it("isObject", function() {
        assert.isOk(m.isObject({}));
        assert.isOk(m.isObject({a: 1}));
        assert.isOk(!m.isObject(new Date()));

        class Z1 {}

        let z1 = new Z1()
        assert.notOk(m.isObject(z1));
    })

    it("isNumChar", function() {
        assert.ok(m.isNumChar('4'));
        assert.ok(m.isNumChar('0'));
        assert.ok(m.isNumChar('9'));
        assert.ok(!m.isNumChar('a'));
        assert.ok(!m.isNumChar(''));
        assert.ok(!m.isNumChar(3));
    })

    it("toInt", function() {
        assert.ok(m.toInt('4') === 4);
        assert.ok(m.toInt('4px') === 4);  // !!!
        assert.ok(m.toInt('') === 0);
        assert.ok(m.toInt('', 5) === 5);
        assert.ok(m.toInt() === 0);
        assert.ok(m.toInt(null) === 0);
    })

    it("isEmpty", function() {
        assert.ok(m.isEmpty(0));
        assert.ok(m.isEmpty(''));
        assert.ok(m.isEmpty([]));
        assert.ok(m.isEmpty({}));
        assert.ok(m.isEmpty(null));
        assert.ok(m.isEmpty(undefined));

        assert.ok(!m.isEmpty(1));
        assert.ok(!m.isEmpty('1'));
        assert.ok(!m.isEmpty([1]));
        assert.ok(!m.isEmpty({a: 1}));
    })

    it("toBoolean", function() {
        assert.ok(m.toBoolean('4') === false);
        assert.ok(m.toBoolean('') === false);
        assert.ok(m.toBoolean(undefined) === false);
        assert.ok(m.toBoolean(null) === false);
        assert.ok(m.toBoolean(null, true) === true);
        assert.ok(m.toBoolean(true) === true);
        assert.ok(m.toBoolean(false) === false);
        assert.ok(m.toBoolean('1') === true);
        assert.ok(m.toBoolean('1', false) === true);
        assert.ok(m.toBoolean('yes', false) === true);
        assert.ok(m.toBoolean('true', false) === true);
    })

})
    