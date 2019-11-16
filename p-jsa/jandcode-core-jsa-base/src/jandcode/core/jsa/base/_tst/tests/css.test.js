import * as test from 'jandcode.core.jsa.base/test'

let assert = test.assert

describe(__filename, function() {

    it("change theme", function() {
        let notheme1 = {css: true, filename: 'notheme1.css', text: '.t{color:brown}'};
        let theme1 = {css: true, filename: 'theme1.css', text: '.t{color:red}'};
        let theme2 = {css: true, filename: 'theme2.css', text: '.t{color:green}'};
        Jc.requireCss(notheme1)
        Jc.requireCss(theme1, 'theme')
        Jc.requireCss(theme2, 'theme')
    })

    it("after theme", function() {
        let theme1 = {css: true, filename: 'theme1.css', text: '.t{color:red}'};
        let css2 = {css: true, filename: 'css2.css', text: '.t{color:green}'};
        let css3 = {css: true, filename: 'css3.css', text: '.t{color:blue}'};
        let css4 = {css: true, filename: 'css4.css', text: '.t{color:black}'};
        Jc.requireCss(theme1, 'theme')
        Jc.requireCss(css2, 'before-theme')
        Jc.requireCss(css3, 'before-theme')
        Jc.requireCss(css4)
    })

})
    