<template>
    <div>
    </div>
</template>

<script>

import {apx} from '../vendor'

// базовый класс для страниц, которые используют tst-apx-panel как оформление

export default {
    name: 'tst-apx-page',

    props: {},

    created() {
        // создаем cfgStore, которое привязывается к текущему пути в _tst
        this.cfgStore = apx.createCfgStore(Jc.cfg.tst.path)
        this.cfgStore.autoSave = true
        this.cfg = this.cfgStore.cfg
        this.cfgStore.load()
    },

    beforeMount() {
        // за конфигурацией следим
        this.$watch('cfg', this.applyCfg, {deep: true, immediate: true})
    },

    data() {
        return {
            // это конфигурация из cfgStore
            cfg: null,
        }
    },

    methods: {
        // этот метод вызовется, когда то то изменится в конфигурации
        applyCfg() {
        }
    },
}
</script>
