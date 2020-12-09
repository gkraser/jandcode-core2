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
        if (!this.options) {
            throw new Error("chart options not assigned")
        }

        let defaultHeight = 200
        let defaultWidth = 300
        //
        this.chartInst = this.createChartInst()
        //
        this.$nextTick(() => {
            // приходится.. Если сразу el=(0,0), то потом никто ему размер не меняет
            let bcr = this.$el.getBoundingClientRect()
            if (bcr.height === 0 && bcr.width === 0) {
                this.$el.style.width = '' + defaultWidth + 'px'
                this.$el.style.height = '' + defaultHeight + 'px'
            }
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
        this.rsw.destroy()
        this.rsw = null
        this.chartInst.dispose()
        this.chartInst = null
    },
    methods: {
        createChartInst() {
            let theme = this.theme || Jc.cfg.echarts.theme
            let chartInst = echarts.init(this.$el, theme)
            chartInst.setOption(this.options)
            return chartInst
        }
    },
}
</script>
