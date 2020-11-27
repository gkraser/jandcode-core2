<template>
    <div class="jc-datagrid">
        <div ref="table">
        </div>
    </div>
</template>

<script>
import {apx, Tabulator} from '../vendor'

/**
 * Простой vue-binding для tabulator.
 *
 * После mount имеется экземпляр gridInst, делайте с ним что хотите.
 */
export default {
    name: 'jc-datagrid',
    props: {

        /**
         * Опции для Tabulator.
         * Это параметры конструктора Tabulator
         * Не нужно передавать сюда реактивные данные,
         * реактивность тут не поддерживается.
         */
        options: {
            type: Object,
            default: null,
            required: true,
        }

    },
    mounted() {
        // убираем padding, ибо его ставить нельзя
        this.$el.style.padding = '0'
        this.gridInst = this.createGridInst()
        //
        this.rsw = apx.utils.resizeWatch(this.$el, (ev) => {
            let curHeight = this.$refs.table.style.height
            let newHeight = '' + this.calcGridHeight() + 'px'
            if (curHeight !== newHeight) {
                this.gridInst.setHeight(newHeight)
            }
        })
    },
    beforeDestroy() {
        this.rsw.destroy()
        this.rsw = null
        this.gridInst.destroy()
        this.gridInst = null
    },
    methods: {
        createGridInst() {
            let opts = apx.jsaBase.extend(this.options, {
                height: '' + this.calcGridHeight() + 'px',
            })
            return new Tabulator(this.$refs.table, opts)
        },

        calcGridHeight() {
            let defaultHeight = 150

            let el = this.$el
            let bcr = el.getBoundingClientRect()
            let cst = window.getComputedStyle(el)
            //
            let h = bcr.height // высота вместе с рамкой

            if (h === 0 || !cst.borderBottomWidth || !cst.borderTopWidth) {
                // нет размера
                return defaultHeight
            }

            let bt = parseFloat(cst.borderBottomWidth)
            let bb = parseFloat(cst.borderTopWidth)

            return h - bt - bb
        }
    },
}
</script>
