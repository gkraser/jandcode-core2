/* Тема base
----------------------------------------------------------------------------- */
import css from './theme-std.scss'

import themeCfg from './config-std'

Jc.cfg.set({
    theme: themeCfg
})

Jc.requireCss(css, 'theme')
