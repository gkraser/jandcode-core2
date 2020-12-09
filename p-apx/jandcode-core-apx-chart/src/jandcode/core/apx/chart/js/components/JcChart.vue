<template>
    <div class="jc-chart">
    </div>
</template>

<script>
import {apx, echarts} from '../vendor'

/**
 * Простой vue-binding для echarts.
 *
 * После mount имеется экземпляр chartInst, делайте с ним что хотите.
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

    },
    mounted() {
        let defaultHeight = 200
        let defaultWidth = 300
        //
        this.chartInst = this.__createChartInst()
        //
        this.$nextTick(() => {
            // приходится.. Если сразу el=(0,0), то потом никто ему размер не меняет
            let bcr = this.$el.getBoundingClientRect()
            if (bcr.height === 0 && bcr.width === 0) {
                this.$el.style.width = '' + defaultWidth + 'px'
                this.$el.style.height = '' + defaultHeight + 'px'
            }
            this.__setChartInst(this.chartInst)
        })

        this.rsw = apx.utils.resizeWatch(this.$el, (ev) => {
            let bcr = this.$el.getBoundingClientRect()
            let newHeight = null
            let newWidth = null
            if (bcr.height === 0) {
                newHeight = defaultHeight
            }
            if (bcr.width === 0) {
                newWidth = defaultWidth
            }
            this.chartInst.resize({height: newHeight, width: newWidth})
        })
    },

    beforeDestroy() {
        this.__destroyChartInst(this.chartInst)
        this.rsw.destroy()
        this.rsw = null
        this.chartInst.dispose()
        this.chartInst = null
    },

    methods: {

        __createChartInst() {
            let theme = this.theme || Jc.cfg.echarts.theme
            let chartInst = echarts.init(this.$el, theme)
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
            }
        },

        /**
         * Уведомить объект options, что экземпляр уничтожается, если он это поддерживает
         */
        __destroyChartInst(chartInst) {
            if (apx.jsaBase.isFunction(this.options.destroyChartInst)) {
                this.options.destroyChartInst(chartInst, this)
            }
        },

    },
}
</script>
