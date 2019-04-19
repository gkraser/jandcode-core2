import * as test from 'jandcode.jsa.base/test'
import * as m from 'jandcode.jsa.base/js/svgicon'

let assert = test.assert

describe("utils/svgicon", function() {

    let svg1 = '<svg viewBox="0 0 48 48"></svg>'
    let svg2 = `
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 400 500">
            <path d="M318.75 444"/>
        </svg>
    `

    it("empty", function() {
        let nms = m.getIconNames()
        assert.ok(nms.length > 0)
        assert.ok(nms[0] === 'empty')
    })

    it("register real", function() {
        m.registerIcon('q2', svg2)
        let a = m.getPlaceHolder()
        let b = document.getElementById(m.iconId('q2'))
        assert.equal(b.getAttribute('viewBox'), '0 0 400 500');
        assert.equal(b.innerHTML.trim(), '<path d="M318.75 444"></path>');
    })

})
