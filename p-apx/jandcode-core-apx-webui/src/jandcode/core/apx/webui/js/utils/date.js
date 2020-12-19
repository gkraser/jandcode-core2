/* Утилиты для дат

Особенность: рассматривает даты как строки в формате ISO.
Принимает в качестве параметров даты в виде: Date, msec, strISO.
Возвращает даты всегда в виде строки ISO.

Связано с тем, что данные с сервера приходят в виде дат ISO.

Используются утилиты для дат от Quasar.

----------------------------------------------------------------------------- */

import {jsaBase, Quasar} from '../vendor'

/**
 * Утилиты для дат от quasar
 */
export let q_date = Quasar.date

// все форматы по требованиям quasar

export let ISO_DATE = 'YYYY-MM-DD'

// настройки echarts по умолчанию
jsaBase.cfg.setDefault({
    date: {
        // формат даты для отображения
        displayFormat: 'DD.MM.YYYY'
    }
})

/**
 * Дату в строку указанного формата
 * @param dt дата
 * @param fmt формат
 */
export function format(dt, fmt) {
    return q_date.formatDate(dt, fmt)
}

/**
 * Строку в дату указанного формата
 * @param dt дата
 * @param fmt формат. Если не указан, используется iso
 * @return null, если дата не правильная
 */
export function parse(dt, fmt) {
    fmt = fmt || ISO_DATE
    let dtTmp = q_date.extractDate(dt, fmt)
    let sf = format(dtTmp, fmt)
    if (sf.length !== fmt.length || dt !== sf) {
        return null
    }
    return toStr(dtTmp)
}

/**
 * Дату в строку в iso-формате
 * @param dt дата
 */
export function toStr(dt) {
    return format(dt, ISO_DATE)
}

/**
 * Дату в строку для отображения
 * @param dt дата
 */
export function toDisplayStr(dt) {
    return format(dt, jsaBase.cfg.date.displayFormat)
}

/**
 * Добавить дни
 * @param dt дата
 * @param days сколько дней
 */
export function addDays(dt, days) {
    let res = q_date.addToDate(dt, {days: days})
    return toStr(res)
}

/**
 * Удалить дни
 * @param dt дата
 * @param days сколько дней
 */
export function subDays(dt, days) {
    let res = q_date.subtractFromDate(dt, {days: days})
    return toStr(res)
}

/**
 * Сегодня
 */
export function today() {
    return toStr(new Date())
}

