<template>
    <tst-panel class="app1-test-50b9dc93" :class="{bg:isBaseTheme()}">

        <template slot="tools">
            <button @click="$parent.$el.classList.toggle('bg')">bg</button>
            <button @click="logoTmlChange">logo</button>
        </template>

        <jc-app>
            <template #tb-main>
                <template v-if="logoTml==0">
                    <jc-toolbar-logo
                            url="jandcode/core/jsa/vue/_tst/pages/images/icons/logo3.svg"
                    />
                    <jc-toolbar-title text="Sanqbox" text2="это просто sandbox"
                    />
                </template>

                <template v-if="logoTml==1">
                    <jc-toolbar-logo
                            url="jandcode/core/jsa/vue/_tst/pages/images/icons/logo3.svg"
                    />
                    <jc-toolbar-title text="Sanqbox"
                    />
                </template>

                <template v-if="logoTml==2">
                    <jc-toolbar-logo
                            url="jandcode/core/jsa/vue/_tst/pages/images/icons/logo3.svg"
                    />
                </template>

                <template v-if="logoTml==3">
                    <jc-toolbar-title text="Sanqbox" text2="это просто sandbox"
                    />
                </template>

                <template v-if="logoTml==4">
                    <jc-toolbar-title text="Sanqbox"
                    />
                </template>

                <jc-separator/>
                <jc-action icon="menu"/>
                <jc-separator/>
                <jc-action text="Help"/>
                <jc-action text="Help2"/>
                <jc-action text="Help">
                    <jc-action icon="bus" text="01 Hello"/>
                    <jc-action text="02 Hello Hello Hello ">
                        <jc-action text="03 Hello"/>
                        <jc-action text="04 Hello Hello Hello ">
                            <jc-action text="05 Hello"/>
                            <jc-action text="06 Hello Hello Hello ">
                                <jc-action text="07 Hello"/>
                                <jc-action text="08 Hello Hello Hello "/>
                                <jc-action text="09 Hello Hello Hello "/>
                            </jc-action>
                            <jc-action text="10 Hello Hello Hello "/>
                            <jc-action text="11 Hello Hello Hello "/>
                        </jc-action>
                        <jc-action text="12 Hello Hello Hello "/>
                        <jc-action text="13 Hello Hello Hello "/>
                    </jc-action>
                    <jc-action text="16 One sub item">
                        <jc-action text="14 Hello"/>
                    </jc-action>
                    <jc-action text="17 Custom popup">
                        <div style="padding: 50px;background-color: #e8eaf6">
                            CUSTOM POPUP
                        </div>
                    </jc-action>
                    <jc-separator/>
                    <jc-action icon="bus" text="15 Hello Hello Hello "/>
                </jc-action>

                <!--<jc-separator :style="{flex: 1}" :class="['aaa']"/>-->
                <div :style="{flex: 1}" :class="['aaa']"/>

                <jc-action icon="bus"/>
                <jc-action icon="logo1"/>
                <jc-action icon="logo2"/>
                <jc-action icon="logo3"/>
                <jc-action icon="logo4"/>
                <jc-action icon="logo5"/>
                <jc-action icon="logo6"/>

            </template>

            <template #content>
                content
                <tst-big-content/>
            </template>

            <template #tools>
                tools
                <tst-big-content/>
            </template>

            <template #tb-foot>
                <div :style="{flex: 1}" :class="['aaa']"/>
                <jc-action icon="menu"/>
                <jc-separator/>
                <jc-action text="Help"/>
                <jc-action text="Help2"/>
                <jc-action text="Help">
                    <jc-action icon="bus" text="01 Hello"/>
                    <jc-action text="02 Hello Hello Hello "/>
                    <jc-action text="17 Custom popup">
                        <div style="padding: 50px;background-color: #e8eaf6">
                            CUSTOM POPUP
                        </div>
                    </jc-action>
                    <jc-separator/>
                    <jc-action icon="bus" text="15 Hello Hello Hello "/>
                </jc-action>
            </template>

        </jc-app>

    </tst-panel>
</template>

<script>
    export default {
        props: {},
        data() {
            return {
                logoTml: 0
            }
        },
        methods: {
            isBaseTheme() {
                return Jc.cfg.theme.name == 'base';
            },
            logoTmlChange() {
                this.logoTml = this.logoTml + 1
                if (this.logoTml > 5) {
                    this.logoTml = 0
                }
            }
        }
    }
</script>

<style lang="less">
    @import (reference) "vendor";

    .app1-test-50b9dc93 {

        &.bg {
            .jc-action {
                background-color: @color-light-green-100;
            }
            .jc-action > * {
                background-color: @color-light-green-200;
            }
            .jc-icon {
                background-color: @color-light-green-600;
            }

            .jc-app--head {
                background-color: @color-blue-grey-300;
                .jc-toolbar {
                    background-color: @color-blue-grey-500;
                }
            }
            .jc-app--tools {
                background-color: @color-blue-grey-200;
            }
            .jc-app--main {
                background-color: @color-blue-grey-100;
            }
            .jc-app--foot {
                background-color: @color-blue-grey-500;
            }
        }

        .jc-app {
            height: 70vh;
        }

    }

</style>
