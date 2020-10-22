/* Подключение стилей .tst-tools
----------------------------------------------------------------------------- */

import q_css from './css/tst-tools-quasar-style.scss'
import s_css from './css/tst-tools-style.less'

export function applyTstToolsCss() {
    Jc.requireCss(q_css, 'before-theme')
    Jc.requireCss(s_css, 'before-theme')
}