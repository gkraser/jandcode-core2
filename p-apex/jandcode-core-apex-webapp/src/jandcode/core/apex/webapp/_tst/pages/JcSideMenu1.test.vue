<template>
    <tst-apex-panel class="jcsidemenu2-test-7ced957a">
        <template #tools>
        </template>

        <div class="row q-gutter-md q-mb-md">

            <div class="col">
                <SideMenu1 :items="itemsSet.items1" :levels="3" @click="onClick"
                           bordered/>
            </div>

            <div class="col">
                <SideMenu1 :items="itemsSet.itemsFs" :levels="3" @click="onClick"
                           bordered/>
            </div>

            <div class="col">
                <SideMenu1 :items="itemsSet.itemsNoIcon" :levels="3" @click="onClick"
                           bordered/>
            </div>

        </div>

    </tst-apex-panel>
</template>

<script>
import SideMenu1 from './_components/SideMenu1'

let itemsSet = {
    items1: [
        {label: 'Элемент c иконкой', icon: 'bus'},
        {label: 'Элемент без иконки', icon: '', opened: true},
        {label: 'Элемент с svg', icon: 'svg1'},
        {label: 'Элемент с png', icon: 'png1'},
    ],

    itemsNoIcon: [
        {label: 'Элемент 1'},
        {label: 'Элемент 2'},
        {label: 'Элемент 3'},
        {label: 'Элемент 4'},
    ],

    itemsFs: [
        {label: 'Папка 1', icon: 'folder1'},
        {label: 'Папка 2', icon: 'folder1'},
        {label: 'Файл 3', icon: 'file1'},
        {label: 'Файл 4', icon: 'file1'},
    ],
}

export default {
    extends: Vue.component('tst-apex-page'),
    components: {
        SideMenu1
    },
    created() {
        this.cfgStore.applyDefault({})
    },
    data() {
        return {
            itemsSet: itemsSet
        }
    },
    methods: {
        applyCfg() {
            let cfg = this.cfg
        },

        onClick(ev, th) {
            console.info("click", arguments);
        }
    }
}
</script>

<style lang="less">

.jcsidemenu2-test-7ced957a {


}

</style>
