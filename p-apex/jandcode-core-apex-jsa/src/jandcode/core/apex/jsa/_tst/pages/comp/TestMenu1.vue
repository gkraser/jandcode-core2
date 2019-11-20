<template>
    <jc-side-menu :bordered="bordered" class="col">
        <template v-for="item in itemsLevel(1)">
            <jc-side-menu-item v-bind="item" @click="onClick"/>
            <template v-if="levels>=2">
                <jc-side-menu-item v-bind="item" :label="item.label+' (exp)'">
                    <template v-for="item in itemsLevel(2)">
                        <jc-side-menu-item v-bind="item" @click="onClick"/>
                        <template v-if="levels>=3">
                            <jc-side-menu-item v-bind="item" :label="item.label+' (exp)'">
                                <template v-for="item in itemsLevel(3)">
                                    <jc-side-menu-item v-bind="item" @click="onClick"/>
                                    <template v-if="levels>=4">
                                        <jc-side-menu-item v-bind="item"
                                                           :label="item.label+' (exp)'">
                                            <template v-for="item in itemsLevel(4)">
                                                <jc-side-menu-item v-bind="item"
                                                                   @click="onClick"/>
                                            </template>
                                        </jc-side-menu-item>
                                    </template>
                                </template>
                            </jc-side-menu-item>
                        </template>
                    </template>
                </jc-side-menu-item>
            </template>
        </template>
    </jc-side-menu>
</template>

<script>
    let itemsDefault = [
        {label: 'Элемент с font', icon: 'font1'},
        {label: 'Элемент без иконки', icon: '', defaultOpened: true},
        {label: 'Элемент с svg', icon: 'svg1'},
        {label: 'Элемент с png', icon: 'png1'},
    ]

    export default {
        props: {
            items: Array,
            levels: {
                type: Number,
                default: 3
            },
            bordered: {
                type: Boolean,
                default: false
            }
        },
        data() {
            return {}
        },
        methods: {
            itemsLevel(level) {
                let res = []
                for (let item of !!this.items ? this.items : itemsDefault) {
                    let n = Object.assign({}, item)
                    n.label = '(' + level + ') ' + n.label
                    if (n.defaultOpened) {
                        if (level != 1) {
                            n.defaultOpened = false
                        }
                    }
                    res.push(n)
                }
                return res
            },

            onClick(ev, it) {
                this.$emit('click', ev, it)
            }

        }

    }
</script>
