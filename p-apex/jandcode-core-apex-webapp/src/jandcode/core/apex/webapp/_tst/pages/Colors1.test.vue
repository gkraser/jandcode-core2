<template>
    <tst-apex-panel class="colors1-test-5473d81a">
        <template #tools>
        </template>

        <table class="table">
            <tr>
                <td></td>
                <td v-for="tone in 9">{{ toneNm(tone) }}</td>
                <td class="delim"></td>
                <td v-for="a in samples">{{ a.text + '00/' + a.bg + '00' }}</td>
            </tr>
            <tr v-for="color in colors">
                <td>{{ color }}</td>
                <td v-for="tone in 9">
                    <div :class="[bgCls(color,tone),'sample','sample-color']">
                    </div>
                </td>
                <td class="delim"></td>
                <td v-for="a in samples">
                    <div :class="[bgCls(color,a.bg),textCls(color,a.text),'sample','sample-text']">
                        {{ a.text + '00/' + a.bg + '00' }}
                    </div>
                </td>
            </tr>
        </table>

    </tst-apex-panel>
</template>

<script>

let colors = ['primary', 'secondary', 'accent', 'positive', 'negative', 'info', 'warning']

export default {
    extends: Vue.component('tst-apex-page'),
    components: {},
    created() {
        this.cfgStore.applyDefault({})
    },
    data() {
        return {
            colors: colors,
            samples: [
                {bg: 5, text: 1},
                {bg: 1, text: 7},
                {bg: 3, text: 9},
                {bg: 1, text: 7},
            ]
        }
    },
    methods: {
        applyCfg() {
            let cfg = this.cfg
        },
        colorNm(color, toneNum) {
            return color + '-' + toneNum + '00';
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
