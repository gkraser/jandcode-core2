/* Настройка builder для конкретного разработчика
----------------------------------------------------------------------------- */
let webpack = require('webpack')

let WpbApxPlugin = require('@jandcode/apx/wpb-plugin')

module.exports = function(builder) {
    let apxPlugin = builder.findPlugin(WpbApxPlugin)
    if (apxPlugin) {
        apxPlugin.options.tstModules.push(
            '@jandcode/apx',
            '@jandcode/apx-ui',
            '@jandcode/base',
            '@jandcode/apx-map',
            '@jandcode/apx-chart',
            '@jandcode/apx-datagrid',
        )
        apxPlugin.options.themes.push(
            '*', // все темы
        )
    }

    if (!builder.isProd) {
        builder.merge({
            //devtool: 'eval',
            plugins: [
                new webpack.ProgressPlugin(),
            ]
        })
    }
}
