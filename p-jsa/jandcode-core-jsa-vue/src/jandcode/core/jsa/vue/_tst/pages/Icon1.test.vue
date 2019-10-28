<template>
    <tst-panel class="icon1-59f8b2d7">

        <div>
            <h3>В тексте</h3>
            <div>
                <span>Это текст</span>
                <jc-icon icon="bus"></jc-icon>
                <span>Это снова текст</span>
            </div>
            <div class="bg">
                <span>Это текст</span>
                <jc-icon icon="bus"></jc-icon>
                <span>Это снова текст-(рйqbQЙ)</span>
            </div>
        </div>

        <div>
            <h3>By url</h3>
            <div class="bg">
                <span>Это png</span>
                <jc-icon url="jandcode/core/jsa/vue/_tst/pages/images/logo1.png"></jc-icon>
                <span>Это svg</span>
                <jc-icon url="jandcode/core/jsa/vue/_tst/pages/images/logo3.svg"></jc-icon>
                <template v-for="sz in iconSizesPx()">
                    <span>{{sz}}px</span>
                    <jc-icon url="jandcode/core/jsa/vue/_tst/pages/images/logo1.png"
                             :style="{width:''+sz+'px',height:''+sz+'px'}" :class="'qaz3'"
                    ></jc-icon>
                    <jc-icon url="jandcode/core/jsa/vue/_tst/pages/images/logo3.svg"
                             :style="{width:''+sz+'px',height:''+sz+'px'}" :class="'qaz2'"
                    ></jc-icon>
                </template>
            </div>
        </div>

        <div class="all-icons">
            <h4>All icons ({{allIconNames().length}})</h4>
            <div>
                <template v-for="icon in allIconNames()">
                    <span class="icon-place">
                        <jc-icon :icon="icon" :key="icon" :title="icon"></jc-icon>
                    </span>
                </template>
            </div>
        </div>

    </tst-panel>

</template>

<script>
    import {jsaBase} from '../../js/vendor'
    import {JcIcon} from '../../js/comp/index'

    export default {
        tst: {},
        components: {
            JcIcon
        },
        props: {},
        data() {
            return {}
        },
        methods: {
            allIconNames() {
                return jsaBase.svgicon.getIconNames();
            },
            iconSizesPx() {
                return [16, 24, 32, 48, 80, 96]
            }
        }

    }
</script>

<style lang="less">

    @import 'vendor';

    .icon1-59f8b2d7 {
        .bg {
            background-color: @color-light-green-300;
        }

        .bg > span {
            background-color: @color-light-green-500;
        }

        .icon-place {
            padding: 4px;
        }
    }

</style>
