<template>
    <tst-apex-panel class="colors1-test-5473d81a">
        <template #tools>
        </template>

        <table class="table">
            <tr>
                <td></td>
                <td v-for="tone in tones">{{ tone }}</td>
                <td class="delim"></td>
                <td v-for="a in samples">{{ a.text + '/' + a.bg }}</td>
            </tr>
            <tr v-for="color in colors">
                <td>{{ color }}</td>
                <td v-for="tone in tones">
                    <div :class="[`bg-${color}-${tone}`,'sample','sample-color']">
                    </div>
                </td>
                <td class="delim"></td>
                <td v-for="a in samples">
                    <div :class="[`bg-${color}-${a.bg}`,`text-${color}-${a.text}`,'sample','sample-text']">
                        {{ a.text + '/' + a.bg }}
                    </div>
                </td>
            </tr>
        </table>

    </tst-apex-panel>
</template>

<script>

let colors = ['gray', 'primary', 'secondary', 'accent', 'positive', 'negative', 'info', 'warning']
let tones = [100, 200, 300, 400, 500, 600, 700, 800, 900]

export default {
    extends: Vue.component('tst-apex-page'),
    components: {},
    created() {
        this.cfgStore.applyDefault({})
    },
    data() {
        return {
            colors: colors,
            tones: tones,
            samples: [
                {bg: 500, text: 100},
                {bg: 100, text: 700},
                {bg: 300, text: 900},
                {bg: 100, text: 700},
            ]
        }
    },
    methods: {
        applyCfg() {
            let cfg = this.cfg
        },
        colorNm(color, tone) {
            return color + '-' + tone;
        },
        toneNm(toneNum) {
            return '' + toneNum + '00';
        },
        textCls(color, toneNum) {
            return 'text-' + this.colorNm(color, toneNum);
        },
        bgCls(color, toneNum) {
            return 'bg-' + this.colorNm(color, toneNum);
        },
    }
}
</script>

<style lang="less">

.colors1-test-5473d81a {

  .table {

    //border: 1px solid gray;
    border-collapse: collapse;

    td {
      border: 1px solid silver;
      padding: 6px;
      text-align: center;
      vertical-align: middle;
    }

    .delim {
      border: none;
      width: 20px;
    }

    .sample {
      height: 30px;
      min-width: 80px;
      line-height: 30px;
    }

    .sample-color {
      min-width: 50px;
    }

    .sample-text {
      min-width: 70px;
    }
  }


}

</style>
