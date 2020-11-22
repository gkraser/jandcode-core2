<template>
    <tst-apx-panel class="modulegrid1-test-0d2967b7" fontsize>
        <template #tools>
        </template>

        <template v-for="n in [].concat(fontSizesStd, fontSizesVar)">
            <div class="q-mb-md txt-wrap row">
                <div>
                    <div class="mark">.{{ fontSize(n) }}</div>
                    <div style="font-size:0.8em" class="text-gray-800 q-ml-sm">
                        {{ realSize[n] }}
                    </div>
                </div>
                <div class="txt col" :class="[fontSize(n)]" :ref="n">
                    {{ lorem(130) }}
                </div>
            </div>
            <div v-if="n==='xl'" class="q-mb-xl"></div>
        </template>


    </tst-apx-panel>
</template>

<script>
import * as tst from 'jandcode.core.apx.tst'

let fontSizesStd = [
    'xs', 'sm', 'md', 'lg', 'xl'
]

let fontSizesVar = [
    'small-3', 'small-2', 'small-1',
    'norm',
    'large-1', 'large-2', 'large-3', 'large-4', 'large-5',
    'large-6', 'large-7',
]

export default {
    extends: Vue.component('tst-apx-page'),
    components: {},
    mixins: [tst.mixins.lorem],

    created() {
        this.cfgStore.applyDefault({})
    },
    mounted() {
        this.$nextTick(() => {
            let rs = {}
            for (let n of [].concat(this.fontSizesStd, this.fontSizesVar)) {
                let el = this.$refs[n]
                let st = null
                if (el) {
                    st = window.getComputedStyle(el[0])
                }
                if (st) {
                    rs[n] = st.fontSize
                } else {
                    rs[n] = '???'
                }
            }
            this.realSize = rs
        })
    },
    data() {
        return {
            fontSizesStd: fontSizesStd,
            fontSizesVar: fontSizesVar,
            realSize: {}
        }
    },
    methods: {
        applyCfg() {
            let cfg = this.cfg
        },
        fontSize(n) {
            return 'text-size-' + n
        }
    }
}
</script>

<style lang="less">

.modulegrid1-test-0d2967b7 {

  .txt-wrap {
    padding: 10px;
    border: 1px solid silver;
    width: 40rem;
  }

  .txt {
  }

  .mark {
    width: 10rem;
  }
}

</style>
