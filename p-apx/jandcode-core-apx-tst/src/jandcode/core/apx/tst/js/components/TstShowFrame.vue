<template>
    <div class="tst-show-frame">
        <App container style="height: calc(100vh - 72px);" :own="this">
        </App>
    </div>
</template>

<script>
import * as mixins from '../mixins'
import {apx} from '../vendor'

export default {
    name: 'tst-show-frame',
    mixins: [mixins.cfgStore, apx.JcApp],

    props: {
        frame: {
            required: true
        },
        params: {
            default: function() {
                return {}
            }
        },
        shower: {
            default: null,
        }
    },

    created() {
        this.title = 'Имитация приложения'
        this.icon = null
        if (Jc.cfg.tst && Jc.cfg.tst.fileName) {
            this.title = Jc.cfg.tst.fileName
        }
    },

    data() {
        return {}
    },

    mounted() {
        let body = this.$parent.$refs.body
        if (body) {
            body.classList.add('tst-apx-panel--no-padding')
        }
        let options = {
            frame: this.frame,
            params: this.params
        }
        if (this.shower != null) {
            options.shower = this.shower
        }
        apx.showFrame(options)
    },

    methods: {},
}
</script>
