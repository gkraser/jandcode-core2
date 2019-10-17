<template>
    <tst-panel class="menu1-59f92af6">

        <template slot="tools">
            <button @click="$parent.$el.classList.toggle('bg')">bg</button>
        </template>

        <div class="b-wrap">
            <span>A</span>
            <jc-menu>
                <jc-action icon="bus" text="Hello"/>
                <jc-action text="Hello Hello Hello "/>
                <jc-separator/>
                <jc-action icon="bus" text="Hello Hello Hello "/>
            </jc-menu>
        </div>

        <div class="b-wrap">
            <span>B</span>
            <jc-menu>
                <jc-action text="Hello"/>
                <jc-action text="Hello Hello Hello "/>
                <jc-action text="Hello Hello Hello "/>
            </jc-menu>
        </div>

        <div v-for="n in 2" class="b-wrap">
            <span>C-{{n}}</span>
            <jc-menu>
                <jc-action icon="bus" text="Hello">
                    <jc-action icon="bus" text="Hello (click this)" @click="onClick1"/>
                    <jc-action text="Hello Hello Hello ">
                        <jc-action icon="bus" text="Hello (click this)"
                                   @click="onClick1"/>
                        <jc-action text="Hello Hello Hello "/>
                        <jc-separator/>
                        <jc-action icon="bus" text="Hello Hello Hello "/>
                    </jc-action>
                    <jc-separator/>
                    <jc-action icon="bus" text="Hello Hello Hello "/>
                </jc-action>
                <jc-action text="Hello Hello Hello ">
                    <jc-action icon="bus" text="Hello"/>
                    <jc-action text="Hello Hello Hello "/>
                    <jc-separator/>
                    <jc-action icon="bus" text="Hello Hello Hello "/>
                </jc-action>
                <jc-separator/>
                <jc-action icon="bus" text="Hello Hello Hello "/>
            </jc-menu>
        </div>

        <div class="b-wrap">
            <span>D</span>
            <jc-menu>
                <jc-action text="Action1" icon="frame"/>
                <jc-action text="Icon1" icon="frame"/>
                <jc-action text="Menu1" icon="frame"/>
            </jc-menu>
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
            onClick1(z) {
                console.info("click", z);
            }
        }
    }
</script>


<style lang="less">

    @import 'vendor';

    .menu1-59f92af6 {

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
        }

        //background-color: #f0f4c3;

        .b-wrap {
            margin-bottom: 10px;
            display: inline-flex;
        }
        .b-wrap > * {
            margin-left: 5px;
        }

        .jc-action--text {
        }

        .jc-action--icon {
        }

        svg {
        }

        .jc-menu {
            display: inline-flex;
        }

    }

</style>
