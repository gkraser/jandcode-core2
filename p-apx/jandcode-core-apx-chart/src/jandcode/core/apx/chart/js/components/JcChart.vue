<template>
    <div class="jc-chart" :style="style">
    </div>
</template>

<script>
import {apx, echarts} from '../vendor'
import * as chartHolder from '../chart-holder'

function getLocale() {
    return 'RU' // todo locale нужно как то по правильному определять...
}

/**
 * Простой vue-binding для echarts.
 *
 * После mount имеется экземпляр chartInst, делайте с ним что хотите.
 *
 * @event setChartInst см. {@link ChartBuilder.setChartInst}
 * @event destroyChartInst см. {@link ChartBuilder.destroyChartInst}
 */
export default {
    name: 'jc-chart',
    props: {

        /**
         * Опции для echart.
         * Это параметры метода echarts.setOption
         * Не нужно передавать сюда реактивные данные,
         * реактивность тут не поддерживается.
         *
         * В качестве значения можно передать:
         *
         * 1) простой объект с параметрами диаграммы, как прописано в документации echarts
         *
         * 2) объект с методом getOptions(). Тогда в качестве опцию будет братся то, что
         * вернет этот метод.
         *
         * Следует иметь ввиду, что экземпляр диаграммы может рендерится столько раз,
         * сколько захочет vue. Так что, getOptions может вызыватся много раз,
         * и для разных экземпляров vue.
         *
         * Если объект имеет метод setChartInst(chartInst, compInst),
         * то метод будет вызван при создании экземпляра echarts.
         */
        options: {
            type: Object,
            default: null,
        },

        /**
         * Тема echarts для этого графика.
         * Если не указано, берется из Jc.cfg.echarts.theme
         */
        theme: {
            type: String,
            default: null
        },

        /**
         * Высота диаграммы
         */
        height: {
            default: 200,
        }

    },
    mounted() {
        let defaultHeight = this.defaultHeight = 200
        let defaultWidth = this.defaultWidth = 300
        //
        let chartInst = this.__createChartInst()
        //
        this.$nextTick(() => {
            // приходится.. Если сразу el=(0,0), то потом никто ему размер не меняет
            let bcr = this.$el.getBoundingClientRect()
            if (bcr.height === 0 && bcr.width === 0) {
                this.$el.style.width = '' + defaultWidth + 'px'
                this.$el.style.height = '' + defaultHeight + 'px'
            }
            this.__setChartInst(chartInst)
        })

        this.chartInst = chartInst

        this.rsw = apx.jsaBase.dom.resizeWatch(this.$el, (ev) => {
            this.syncSize()
        })

        chartHolder.registerChart(this)
    },

    beforeDestroy() {
        chartHolder.unregisterChart(this)
        if (this.chartInst != null) {
            this.__destroyChartInst(this.chartInst)
        }
        this.rsw.destroy()
        this.rsw = null
        if (this.chartInst != null) {
            this.chartInst.dispose()
            this.chartInst = null
        }
    },

    computed: {
        style() {
            return {
                height: apx.jsaBase.dom.toStyleSize(this.height)
            }
        },

    },

    methods: {

        syncSize() {
            let bcr = this.$el.getBoundingClientRect()
            let newHeight = null
            let newWidth = null
            if (bcr.height === 0) {
                newHeight = this.defaultHeight
            }
            if (bcr.width === 0) {
                newWidth = this.defaultWidth
            }
            this.chartInst.resize({height: newHeight, width: newWidth})
        },

        __createChartInst() {
            let theme = this.theme || Jc.cfg.echarts.theme
            let locale = getLocale()
            let chartInst = echarts.init(this.$el, theme, {
                locale: locale,
            })
            chartInst.setOption(this.__getOptions())
            return chartInst
        },

        /**
         * Опции для диаграммы
         */
        __getOptions() {
            if (!this.options) {
                throw new Error("chart options not assigned")
            }
            if (apx.jsaBase.isFunction(this.options.getOptions)) {
                return this.options.getOptions()
            }
            return this.options
        },

        /**
         * Установить chartInst в объекте options, если он это поддерживает
         */
        __setChartInst(chartInst) {
            if (apx.jsaBase.isFunction(this.options.setChartInst)) {
                this.options.setChartInst(chartInst, this)
                this.$emit('setChartInst', chartInst, this)
            }
        },

        /**
         * Уведомить объект options, что экземпляр уничтожается, если он это поддерживает
         */
        __destroyChartInst(chartInst) {
            if (apx.jsaBase.isFunction(this.options.destroyChartInst)) {
                this.options.destroyChartInst(chartInst, this)
                this.$emit('destroyChartInst', chartInst, this)
            }
        },

        /**
         * Экспортировать данные из диаграммы
         */
        __exportData() {
            if (apx.jsaBase.isFunction(this.options.exportData)) {
                return this.options.exportData()
            } else {
                throw new Error("Экспорт данных не поддерживается")
            }
        }

    },
}
</script>
