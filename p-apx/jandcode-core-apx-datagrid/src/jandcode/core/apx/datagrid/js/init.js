import {Tabulator} from './vendor'
import css from 'tabulator-tables/dist/css/tabulator_simple.min.css'

//todo пока сразу ставим, потом сделать видимо ленивым
Jc.requireCss(css, "before-theme")

// глобализация
window.Tabulator = Tabulator

export {
    Tabulator
}
