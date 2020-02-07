/* svg-иконки
----------------------------------------------------------------------------- */

import * as base from './base'

// dom узел, где будут храниться иконки
let _placeHolder = null

// зарегистрированные иконки
let _icons = {}

// были ли изменения в иконках
let _iconsChanged = false

// прификс для id
const ID_PREFIX = 'jc-svgicon-'

//////

/**
 * Регистрация svg-иконки
 * @param name имя иконки
 * @param svgtext svg-текст иконки
 */
export function registerSvgIcon(name, svgtext) {
    _iconsChanged = true;
    let id = base.nextId(ID_PREFIX + name + '-');
    _icons[name] = {
        text: svg2symbol(svgtext, id),
        id: id,
        used: false
    };
}

/**
 * Регистрация svg-иконок
 * @param cfg ключ - имя, значение - svg-текст
 */
export function registerSvgIcons(cfg) {
    if (!cfg) {
        return
    }
    for (let key in cfg) {
        registerSvgIcon(key, cfg[key])
    }
}

/**
 * Инициализация иконок
 */
function _initIcons() {
    if (!_iconsChanged) {
        return; // не было изменений в иконках
    }
    if (!_placeHolder) {
        _placeHolder = document.createElement('div');
        _placeHolder.id = base.nextId(ID_PREFIX);
        _placeHolder.style.display = 'none';
        document.body.appendChild(_placeHolder);
    }
    let s = '';
    for (let n in _icons) {
        let ic = _icons[n];
        if (ic.used) {
            continue;
        }
        s += ic.text;
        ic.text = ''; // не нужен более
        ic.used = true;
    }
    if (s) {
        s = '<svg><defs>' + s + '</defs></svg>';
        _placeHolder.insertAdjacentHTML("beforeend", s);
    }
}

/**
 * Возвращает id указанной иконки.
 * Если иконки нет - возвращается null.
 */
export function getSvgIconId(iconName) {
    _initIcons();
    let ic = _icons[iconName];
    if (!ic) {
        return null
    }
    return ic.id
}

/**
 * Возвращает список имен зарегистрированных иконок
 * @return {Array}
 */
export function getSvgIconNames() {
    let res = []
    for (let n in _icons) {
        res.push(n)
    }
    return res;
}

/**
 * Конвертация текста svg в текст svg-symbol
 * @param svgtext текст svg
 * @param id id символа
 */
function svg2symbol(svgtext, id) {
    if (!svgtext) {
        return "";
    }

    // разбиваем на запчасти
    let r = /\<svg([^]*?)\>([^]*)\<\/svg\>/m;
    let m = svgtext.match(r);
    if (!m) {
        return svgtext;
    }

    let head = m[1];
    let body = m[2];
    let viewBox = null;

    // извлекаем viewBox
    r = /viewBox\s*=\s*\"(.*?)\"/;
    m = head.match(r);
    if (m) {
        viewBox = m[1];
    }

    // делаем результат
    let s = '<symbol ';
    if (viewBox) {
        s += 'viewBox="' + viewBox + '" ';
    }
    s += 'id="' + id + '">'
    s += body;
    s += '</symbol>';

    return s;
}

