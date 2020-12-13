/* Утилиты для дат

Особенность: рассматривает даты как строки в формате ISO.
Принимает в качестве параметров даты в виде: Date, msec, strISO.
Возвращает даты всегда в виде строки ISO.

Связано с тем, что данные с сервера приходят в виде дат ISO.

----------------------------------------------------------------------------- */

import {jsaBase, Quasar} from '../vendor'

/**
 * Утилиты для дат от quasar
 */
export let q_date = Quasar.date

// все форматы по требованиям quasar

// настройки echarts по умолчанию
jsaBase.cfg.setDefault({
    date: {
        // формат даты для отображения
        displayFormat: 'DD.MM.YYYY'
    }
})

/**
 * Дату в строку в iso-формате
 * @param dt дата
 */
export function toStr(dt) {
    return q_date.formatDate(dt, 'YYYY-MM-DD')
}

/**
 * Дату в строку для отображения
 * @param dt дата
 */
export function toDisplayStr(dt) {
    return q_date.formatDate(dt, jsaBase.cfg.date.displayFormat)
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

