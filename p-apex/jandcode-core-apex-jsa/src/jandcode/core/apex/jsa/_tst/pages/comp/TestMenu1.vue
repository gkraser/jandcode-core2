<template>
    <jc-side-menu :bordered="bordered" class="col">
        <template v-for="item in itemsLevel(1)">
            <jc-side-menu-item v-bind="item"/>
            <template v-if="levels>=2">
                <jc-side-menu-item v-bind="item" :text="item.text+' (exp)'">
                    <template v-for="item in itemsLevel(2)">
                        <jc-side-menu-item v-bind="item"/>
                        <template v-if="levels>=3">
                            <jc-side-menu-item v-bind="item" :text="item.text+' (exp)'">
                                <template v-for="item in itemsLevel(3)">
                                    <jc-side-menu-item v-bind="item"/>
                                    <template v-if="levels>=4">
                                        <jc-side-menu-item v-bind="item"
                                                           :text="item.text+' (exp)'">
                                            <template v-for="item in itemsLevel(4)">
                                                <jc-side-menu-item v-bind="item"/>
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
        {text: 'Элемент с font', icon: 'font1'},
        {text: 'Элемент без иконки', icon: '', defaultOpened: true},
        {text: 'Элемент с svg', icon: 'svg1'},
        {text: 'Элемент с png', icon: 'png1'},
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
                    n.text = '(' + level + ') ' + n.text
                    if (n.defaultOpened) {
                        if (level != 1) {
                            n.defaultOpened = false
                        }
                    }
                    res.push(n)
                }
                return res
            }
        }

    }
</script>
