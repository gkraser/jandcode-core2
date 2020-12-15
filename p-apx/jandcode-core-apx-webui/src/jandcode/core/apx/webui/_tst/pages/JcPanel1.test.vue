<template>
    <tst-apx-panel class="jcpanel1-test-70b5b71e" :class="classes">
        <template #tools>
            <tst-checkbox label="mark" v-model="cfg.mark"/>
            <tst-checkbox label="height300" v-model="cfg.height300"/>
        </template>

        <div class="row q-gutter-x-md q-mb-md">

            <jc-panel class="col" title="Это панель 1">
                тело в секции
            </jc-panel>

            <jc-panel class="col" title="Это панель 2">
                <template #toolbar>
                    <jc-action icon="more-v"/>
                    <jc-action icon="bus" label="Привет"/>
                </template>

                тело в секции

            </jc-panel>

            <jc-panel class="col">
                тело в секции
            </jc-panel>

            <jc-panel class="col">
                <template #body>
                    тело БЕЗ секции
                </template>
            </jc-panel>

            <jc-panel class="col">
                <template #toolbar>
                    <jc-action icon="more-v"/>
                </template>
                без заголовка, тело в секции
            </jc-panel>
<!--            <q-card>-->
<!--                <q-card-section>-->
<!--                    hello-->
<!--                </q-card-section>-->
<!--            </q-card>-->
        </div>

        <div class="row q-gutter-x-md q-mb-md">

            <jc-panel class="col" title="Это панель 1">
                тело в секции<br>
                тело в секции<br>
                тело в секции<br>
                тело в секции<br>
                тело в секции<br>
                тело в секции<br>
                тело в секции<br>
                тело в секции<br>
                <template #footer>
                    Это footer
                </template>
            </jc-panel>

            <jc-panel class="col" title="Это панель 2" :style="panelStyle">
                <template #toolbar>
                    <jc-action icon="more-v"/>
                    <jc-action icon="bus" label="Привет"/>
                </template>

                тело в секции

                <template #footer>
                    Это footer
                </template>
            </jc-panel>

            <jc-panel class="col">
                тело в секции
                <template #footer>
                    Это footer
                </template>
            </jc-panel>

            <jc-panel class="col">
                <template #body>
                    тело БЕЗ секции
                </template>
                <template #footer>
                    Это footer
                </template>
            </jc-panel>

            <jc-panel class="col">
                <template #toolbar>
                    <jc-action icon="more-v"/>
                </template>
                без заголовка, тело в секции
                <template #footer>
                    Это footer
                </template>
            </jc-panel>
        </div>

        <div class="row q-gutter-x-md q-mb-md">

            <jc-panel class="col" title="Это панель 2" :style="panelStyle" body-fit>
                <template #toolbar>
                    <jc-action icon="more-v"/>
                    <jc-action icon="bus" label="Привет"/>
                </template>

                <div class="content">CONTENT</div>

                <template #footer>
                    Это footer
                </template>
            </jc-panel>

            <jc-panel class="col">
                тело в секции<br>
                тело в секции<br>
                тело в секции<br>
                тело в секции<br>
                тело в секции<br>
                <template #footer>
                    Это footer
                </template>
            </jc-panel>

        </div>


    </tst-apx-panel>
</template>

<script>
import {tst} from '../vendor'

export default {
    mixins: [tst.mixins.cfgStore],
    components: {},
    created() {
        this.cfgStore.applyDefault({
            mark: false,
            height300: false,
        })
    },
    data() {
        return {}
    },
    computed: {
        classes() {
            let res = {
                'debug-mark': this.cfg.mark
            }
            return res
        },
        panelStyle() {
            let res = {}
            if (this.cfg.height300) {
                res.height = '300px'
            }
            return res
        }
    },
    methods: {
        applyCfg() {
            let cfg = this.cfg
        },
    }
}
</script>

<style lang="less">

.debug-mark {

  .jc-panel__body, .q-card__section {
    background-color: #a5d6a7;
  }

  .jc-panel__header {
    background-color: #bcced6;
  }

  .jc-panel__footer {
    background-color: #efcfe2;
  }

  .content {
    background-color: #f3f3d7;
  }

}

.jcpanel1-test-70b5b71e {


}

</style>
