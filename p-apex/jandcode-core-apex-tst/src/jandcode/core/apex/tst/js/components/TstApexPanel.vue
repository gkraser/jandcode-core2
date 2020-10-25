<template>
    <div class="tst-apex-panel">
        <div class="tst-apex-panel--head">
            <template v-if="cfg.path">
                <div class="tst-apex-panel--value-big">{{ cfg.fileName }}</div>
                <span class="tst-apex-panel--divider"></span>
                <div class="tst-apex-panel--value-small"><a
                        :href="'?path='+cfg.filePath">{{ cfg.filePath }}/</a>
                </div>
                <span class="tst-apex-panel--divider" :style="{flex:1}"></span>
                <div class="tst-apex-panel--value-small">Theme:</div>
                <select v-model="curTheme">
                    <option v-for="theme in cfg.themes">{{ theme.path }}</option>
                </select>
                <button @click="resetTheme">Reset theme</button>
                <button @click="baseTheme">&#8594;
                    {{ isThemeStd() ? 'apex-base' : 'apex-std' }} theme
                </button>
            </template>
        </div>

        <template v-if="(isCfg && $slots['tools']) || (isCfg && debugBg)">
            <div class="tst-apex-panel--head">
                <tst-btn @click="resetCfg" label="resetCfg"/>
                <tst-checkbox label="debugBg" v-model="own.cfg.debugBg" v-if="debugBg"/>
                <slot name="tools"/>
            </div>
        </template>

        <div class="tst-apex-panel--head" v-if="$slots['tools-1']">
            <slot name="tools-1"/>
        </div>

        <div class="tst-apex-panel--head" v-if="$slots['tools-2']">
            <slot name="tools-2"/>
        </div>

        <div class="tst-apex-panel--body"
             :class="[noPadding?'tst-apex-panel--no-padding':'']">
            <slot></slot>
        </div>
    </div>
</template>

<script>
import {jsaBase} from '../vendor'

export default {
    name: 'tst-apex-panel',
    props: {
        debugBg: {
            type: Boolean,
            default: null
        },
        noPadding: {
            type: Boolean,
            default: null
        }
    },
    data() {
        return {
            curTheme: Jc.cfg.tst.theme,
        }
    },
    created() {
        if (this.isCfg || this.debugBg) {
            this.own.cfgStore.applyDefault({
                debugBg: false,
            })
            this.own.$watch('cfg', () => {
                document.body.classList.toggle("debug-bg", this.own.cfg.debugBg)
            }, {deep: true, immediate: true})
        }
    },
    watch: {
        curTheme: function(v) {
            let prms = jsaBase.url.getPageParams()
            prms.theme = v
            window.location.search = jsaBase.url.params(prms)
        }
    },
    computed: {
        cfg() {
            return Jc.cfg.tst || {}
        },

        // какой компонент-страница использует этот в качестве шаблона
        own() {
            return this.$parent
        },

        // есть ли на странице конфигурация
        isCfg() {
            return this.own && this.own.cfg && this.own.cfgStore
        },

    },
    methods: {
        resetTheme() {
            let prms = jsaBase.url.getPageParams()
            delete prms.theme
            window.location.search = jsaBase.url.params(prms)
        },
        baseTheme() {
            let prms = jsaBase.url.getPageParams()
            prms.theme = this.isThemeStd() ? 'apex-base' : 'apex-std'
            window.location.search = jsaBase.url.params(prms)
        },
        isThemeStd() {
            return this.curTheme.indexOf('apex-std') !== -1
        },
        resetCfg() {
            return this.own.cfgStore.reset();
        }
    }
}
</script>

<style>

.tst-apex-panel {
  min-height: 100vh;
}

.tst-apex-panel--head {
  font-family: Arial, Helvetica, sans-serif;
  font-size: 13px;
  border-bottom: 1px solid silver;
  background-color: #f5f5f5;
  padding-left: 10px;
  padding-right: 10px;
  display: flex;
  height: 34px;
  align-items: center;
}

.tst-apex-panel--head > *:not(:first-child) {
  margin-left: 8px;
}

.tst-apex-panel--head input[type="range"] {
  color: red;
  width: 60px;
}

.tst-apex-panel--head .tst-apex-panel--value-big {
  color: navy;
  font-size: 12px;
  font-family: "Lucida Console", Monaco, monospace;
}

.tst-apex-panel--head .tst-apex-panel--value-small {
  font-size: 11px;
  color: gray;
  font-family: "Lucida Console", Monaco, monospace;
}

.tst-apex-panel--head select {
  color: navy;
  font-size: 12px;
  font-family: "Lucida Console", Monaco, monospace;
  padding: 2px;
  background-color: #f5f5f5;
}

.tst-apex-panel--head a:hover {
  text-decoration: underline;
}

.tst-apex-panel--body {
  padding: 20px 20px;
}

.tst-apex-panel--body.tst-apex-panel--no-padding {
  padding: 0;
}

.tst-apex-panel--size-label {
  min-width: 30px;
}

.tst-apex-panel--divider {
  width: 4px;
}

</style>