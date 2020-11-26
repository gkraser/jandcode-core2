<template>
    <div class="jc-datagrid">
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
        this.gridInst = this.createGridInst()
        //
        this.rsw = apx.utils.resizeWatch(this.$el, (ev) => {
            // let st = window.getComputedStyle(this.$el)
            // console.info("--------------");
            // console.info("st.height", st.height);
            // console.info("st.width", st.width);
            //todo и как размер узнать? this.gridInst.setHeight(ev.height)
        })
    },
    beforeDestory() {
        this.rsw.destroy()
        this.rsw = null
        this.gridInst.destroy()
        this.gridInst = null
    },
    methods: {
        createGridInst() {
            return new Tabulator(this.$el, this.options)
        }
    },
}
</script>
