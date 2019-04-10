/* Утилиты для функций
----------------------------------------------------------------------------- */

/**
 * Последовательное выполнение набора функций, возможно асинхронных
 */
export class FuncsSeries {

    constructor() {
        this.list = []
    }

    /**
     * Добавить функцию в список исполняемых.
     * Первым параметром функция может иметь callback, в этом случае его нужно вызвать
     * для индикации окончания работы функции.
     */
    add(fn) {
        this.list.push(fn)
    }

    /**
     * Запустить все. После выполнения вызовется callback
     * @param callback
     */
    run(callback) {
        let list = this.list

        function step(idx) {
            if (idx >= list.length) {
                if (callback) {
                    callback()
                }
            } else {
                let fn = list[idx]
                let flagCb

                let cbStep = function() {
                    if (flagCb) {
                        throw new Error("callback вызван дважды")
                    }
                    flagCb = true
                    step(idx + 1)
                }

                if (fn.length === 0) {
                    // синхронная функция
                    fn()
                    step(idx + 1)
                } else {
                    fn(cbStep)
                }
            }
        }

        step(0)
    }
}

/**
 * Последовательное выполнение набора функций, возможно асинхронных
 * @param fnArray массив функций. Первым параметром функция может иметь callback,
 * в этом случае его нужно вызвать для индикации окончания работы функции.
 * @param callback После выполнения вызовется callback
 */
export function series(fnArray, callback) {
    let a = new FuncsSeries()
    for (let fn of fnArray) {
        a.add(fn);
    }
    a.run(callback)
}
