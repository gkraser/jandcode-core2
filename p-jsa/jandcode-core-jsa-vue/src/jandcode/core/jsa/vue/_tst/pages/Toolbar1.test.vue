<template>
    <tst-panel class="toolbar1-59f93114">

        <div v-for="n in 2" :class="'row row-'+n">
            <jc-toolbar>
                <jc-action text="Help" @click="actionClick"/>
                <jc-action icon="bus" @click="actionClick"/>
                <jc-action icon="bus" text="Текст" @click="actionClick"/>
                <jc-separator/>
                <jc-action icon="bus"/>
                <jc-action icon="find"/>
                <jc-action icon="frame"/>
                <jc-action icon="signal"/>
                <jc-separator/>
                <jc-action text="Пункт1"/>
                <jc-action text="Пункт второй"/>
                <jc-action text="Пункт3"/>
                <jc-separator/>
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
                <jc-action icon="bus">
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
                <jc-action icon="bus" text="Меню с иконкой">
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
                <span>Введите текст:</span>
                <input type="text"/>
            </jc-toolbar>
            <jc-toolbar>
                <jc-toolbar-logo icon="bus"
                                 @click="logoClick"/>
                <jc-action text="Action1"/>
                <jc-toolbar-logo url="jandcode/core/jsa/vue/_tst/pages/images/logo1.png"
                                 @click="logoClick"/>
                <jc-action text="Action2"/>
                <jc-toolbar-logo url="jandcode/core/jsa/vue/_tst/pages/images/logo3.svg"
                                 @click="logoClick"/>
                <jc-action text="Action3"/>
                <jc-toolbar-logo
                        url="jandcode/core/jsa/vue/_tst/pages/images/logo5-long.png"
                        @click="logoClick"/>
                <jc-action text="Action4"/>
                <jc-toolbar-logo
                        url="jandcode/core/jsa/vue/_tst/pages/images/logo6-long.svg"
                        @click="logoClick"/>
                <jc-action text="Action5"/>
            </jc-toolbar>
            <jc-toolbar>
                <jc-toolbar-logo url="jandcode/core/jsa/vue/_tst/pages/images/logo1.png"
                                 @click="logoClick"/>
                <jc-toolbar-title text="Sandbox" @click="logoClick"/>
                <jc-toolbar-logo url="jandcode/core/jsa/vue/_tst/pages/images/logo1.png"
                />
                <jc-toolbar-title text="Sanqbox" text2="это просто sandbox"
                />
                <jc-toolbar-logo icon="bus"
                />
                <jc-toolbar-title text="NoClick" text2="клик не настроен"
                />
            </jc-toolbar>
        </div>

    </tst-panel>
</template>

<script>
    export default {
        props: {},
        data() {
            return {}
        },
        methods: {
            logoClick(ev) {
                console.info("LOGO CLICK", ev);
            },
            actionClick(ev) {
                console.info("ACTION CLICK", ev);
            }
        }
    }
</script>


<style lang="less">

    @import 'vendor';

    .toolbar1-59f93114 {
        /*padding: 20px;*/
        /*min-height: 100vh;*/
        //background-color: #d5ebd5;

        .row {
            margin-bottom: 50px;

            .jc-toolbar {
                margin-bottom: 10px;
            }
        }

        .row-2 {
            .jc-toolbar {
                background-color: @color-light-green-500;
            }

            .jc-toolbar > * {
                background-color: @color-light-green-300;
            }

            .jc-toolbar > .jc-action:hover {
                background-color: @color-light-green-100;
            }

            .jc-toolbar > .jc-separator {
                border-left: 1px solid @color-green-900;
            }

        }

    }


</style>



