/**
 * gsp support
 */
Prism.languages.gsp = Prism.languages.extend('markup', {
    'gsp code': {
        pattern: /(\<\%)[\w\W]*?(\%\>)/i,
        inside: Prism.languages.groovy
    }
});

Prism.languages.insertBefore('gsp', 'comment', {
    'gsp comment': /\%\{--[\w\W]*?--\}\%/
});

Prism.languages.cfx = Prism.languages.extend('xml', {
});

Prism.languages.vue = Prism.languages.extend('html', {
});
