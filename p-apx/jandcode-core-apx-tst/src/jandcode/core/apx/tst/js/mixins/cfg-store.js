/* Поддержка CfgStore в компонентах
----------------------------------------------------------------------------- */
import {getCfgStore} from '../cfg-store'

export default {

    beforeMount() {
        // за конфигурацией следим
        this.$watch('cfg', this.applyCfg, {deep: true, immediate: true})
    },

    computed: {
        cfgStore() {
            return getCfgStore()
        },
        cfg() {
            return this.cfgStore.cfg
        }
    },

    methods: {
        // этот метод вызовется, когда что-то изменится в конфигурации
        applyCfg() {
        }
    },

}
