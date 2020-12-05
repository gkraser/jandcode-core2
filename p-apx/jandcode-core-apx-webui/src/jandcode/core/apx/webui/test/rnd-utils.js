import {Quasar} from '../js/index'
import Chance from 'chance'

/**
 * Утилиты для генерации данных
 */
export class RndUtils {

    constructor() {
        // генератор случайностей
        this.rnd = new Chance(12331)
        // утилиты для дат от Quasar
        this.q_date = Quasar.date
    }

    /**
     * Дату в строку ISO
     * @param dt
     * @return {string}
     */
    dateToStr(dt) {
        return this.q_date.formatDate(dt, 'YYYY-MM-DD')
    }

    /**
     * Возвращает массив дат длинной days-дней.
     * Даты в формате timestamp (число msec).
     * Последняя дата в списке - 'сегодня'.
     * Первая - 'сегодня - days'
     * @param days количество дней
     */
    days(days) {
        if (days < 1) {
            days = 1
        }
        let startDate = this.q_date.subtractFromDate(new Date(), {days: days - 1})
        let res = []
        for (let i = 0; i < days; i++) {
            let dt = this.dateToStr(this.q_date.addToDate(startDate, {days: i}))
            res.push(dt)
        }
        return res
    }

}
