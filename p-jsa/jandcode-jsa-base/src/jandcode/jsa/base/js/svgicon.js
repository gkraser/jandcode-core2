/* svg-иконки
----------------------------------------------------------------------------- */

import * as base from './base'

// private vars

// dom узел, где будут храниться иконкт
let _placeHolder = null

// зарегистрированные иконки
let _icons = {}

// были ли изменения в иконках
let _iconsChanged = false

// прификс для id
const ID_PREFIX = 'jc-svgicon-'

// пустая иконка
const EMPTY_ICON = 'empty'

//////

/**
 * Регистрация svg-иконки
 * @param name имя иконки
 * @param svgtext svg-текст иконки
 */
export function registerIcon(name, svgtext) {
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
export function registerIcons(cfg) {
    if (!cfg) {
        return
    }
    for (let key in cfg) {
        registerIcon(key, cfg[key])
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
 * Если иконки нет - возвращается пустая иконка.
 */
export function iconId(iconName) {
    _initIcons();
    let ic = _icons[iconName];
    if (!ic) {
        ic = _icons[EMPTY_ICON];
        if (!ic) {
            return '';
        }
    }
    return ic.id;
}

/**
 * Текст html для иконки
 * @param iconName имя иконки
 * @return {string}
 */
export function iconHtml(iconName) {
    let icId = iconId(iconName);
    return '<svg><use xlink:href="#' + icId + '"></use></svg>';
}

/**
 * Возвращает список имен зарегистрированных иконок
 * @return {Array}
 */
export function getIconNames() {
    let res = [];
    for (let n in _icons) {
        res.push(n);
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

/**
 * Возвращает dom узел с иконками
 * @return {Element}
 */
export function getPlaceHolder() {
    _initIcons()
    return _placeHolder
}

// регистрируем пустую иконку
registerIcon(EMPTY_ICON, '<svg viewBox="0 0 48 48"></svg>')

