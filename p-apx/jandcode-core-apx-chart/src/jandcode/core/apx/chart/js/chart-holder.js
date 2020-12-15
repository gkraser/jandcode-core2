/* Хранилище всех jc-chart, которые работают сейчас.
----------------------------------------------------------------------------- */

let _chartHolder = {}

export function registerChart(jcChart) {
    if (!jcChart) {
        return
    }
    let uid = jcChart._uid
    if (!uid) {
        return
    }
    _chartHolder[uid] = jcChart
}

export function unregisterChart(jcChart) {
    if (!jcChart) {
        return
    }
    let uid = jcChart._uid
    if (!uid) {
        return
    }
    delete _chartHolder[uid]
}

export function getAllRregistredChart() {
    return _chartHolder
}

/**
 * Скрыть все tooltip
 */
export function hideAllTooltip() {
    for (let ch of Object.values(_chartHolder)) {
        let chartInst = ch.chartInst
        if (!chartInst) {
            continue
        }
        chartInst.dispatchAction({
            type: 'hideTip',
        })
    }
}