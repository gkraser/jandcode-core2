import * as test from 'jandcode.core.jsa.base/test'
import * as m from 'jandcode.core.jsa.base/js/popup'

let assert = test.assert

describe("utils/popup", function() {

    it("manager inited", function() {
        assert.ok(m.popupManager instanceof m.PopupManager)
    })


})
    