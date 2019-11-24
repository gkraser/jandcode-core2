import * as test from 'jandcode.core.jsa.base/test'

let assert = test.assert

describe(__filename, function() {

    function css(name) {
        return {css: true, filename: name + '.css', text: '.t{color:name}', group: 'g1'}
    }

    function clearCss() {
        let a
        a = document.querySelectorAll("style[data-group=g1]")
        a.forEach(n => n.parentNode.removeChild(n))
    }

    Jc.defineCssPlace('p1')
    Jc.defineCssPlace('p2')

    beforeEach(function() {
        clearCss()
    });

    it("no places", function() {
        Jc.requireCss(css('1'))
        Jc.requireCss(css('2'))
    })

    it("places", function() {
        Jc.requireCss(css('1'))
        Jc.requireCss(css('2'))
        Jc.requireCss(css('3'), 'p1')
        Jc.requireCss(css('4'), 'p1')
        Jc.requireCss(css('5'), 'p2')
        Jc.requireCss(css('6'), 'p2')
    })

})
    