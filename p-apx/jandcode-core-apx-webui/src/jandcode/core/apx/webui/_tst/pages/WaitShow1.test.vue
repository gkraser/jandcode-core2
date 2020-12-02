<template>
    <tst-apx-panel class="showwait1-test-c1445eb9">
        <template #tools>
        </template>

        <div class="q-mb-md">
            <jc-btn label="request1" @click="request1"/>
            собственный вложенный waitShow, ошибка в процессе
        </div>

        <div class="q-mb-md">
            <jc-btn label="request2" @click="request2"/>
            2 запроса подряд, без ошибки
        </div>

        <div class="q-mb-md">
            <jc-btn label="request3" @click="request3"/>
            2 запроса подряд, второй с ошибкой
        </div>

    </tst-apx-panel>
</template>

<script>
import {apx} from '../vendor'

let act = 'jandcode/core/apx/webui/_tst/pages/_actions/action-json1.json'

export default {
    extends: Vue.component('tst-apx-page'),
    components: {},
    created() {
        this.cfgStore.applyDefault({})
    },
    data() {
        return {}
    },
    methods: {
        applyCfg() {
            let cfg = this.cfg
        },

        async request1() {

            apx.jsaBase.waitShow()
            try {
                let res1 = await apx.jsaBase.ajax.request({
                    url: act,
                    params: {
                        cnt: 2,
                        pause: 1000,
                        error: false
                    }
                })

                apx.jsaBase.waitShow()
                try {
                    await apx.jsaBase.ajax.request({
                        url: act,
                        params: {
                            cnt: 2,
                            pause: 1000,
                            error: true
                        }
                    })
                } finally {
                    apx.jsaBase.waitHide()
                }
                console.info("res1", res1);

            } finally {
                apx.jsaBase.waitHide()
            }
        },

        async request2() {

            let res1 = await apx.jsaBase.ajax.request({
                url: act,
                params: {
                    cnt: 2,
                    pause: 2000,
                    error: false
                }
            })

            await apx.jsaBase.ajax.request({
                url: act,
                params: {
                    cnt: 2,
                    pause: 2000,
                    // error: true
                }
            })

            console.info("res1", res1);

        },

        async request3() {

            let res1 = await apx.jsaBase.ajax.request({
                url: act,
                params: {
                    cnt: 2,
                    pause: 2000,
                    error: false
                }
            })

            await apx.jsaBase.ajax.request({
                url: act,
                params: {
                    cnt: 2,
                    pause: 2000,
                    error: true
                }
            })

            console.info("res1", res1);

        }
    }
}
</script>

<style lang="less">

.showwait1-test-c1445eb9 {


}

</style>
