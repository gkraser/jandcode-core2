<template>
    <div class="jc-shower-main-breadcrumbs">
        <q-breadcrumbs>
            <template v-for="it in items">
                <q-breadcrumbs-el v-if="!it.last" :label="it.title"
                                  class="jc-shower-main-breadcrumbs--link"
                                  @click="clickItem(it)"/>
                <q-breadcrumbs-el v-else :label="it.title"/>
            </template>
        </q-breadcrumbs>
    </div>
</template>

<script>
import {jsaBase} from '../vendor'

/**
 * Компонент для отображения текущего списка фреймов в main.
 */
export default {
    name: 'jc-shower-main-breadcrumbs',
    props: {},
    created() {
        let th = this
        //
        this.shower = null
        this.handlerChangeFrames = function() {
            // до возникновения события shower не известен!
            th.shower = jsaBase.app.frameManager.getShower('main')
            th.updateItems()
        }
        jsaBase.app.eventBus.$on("shower.main.change-frames", this.handlerChangeFrames)
    },
    beforeDestroy() {
        jsaBase.app.eventBus.$off("shower.main.change-frames", this.handlerChangeFrames)
    },
    data() {
        return {
            items: [],
        }
    },
    methods: {
        updateItems() {
            let lst = []
            this.frames = this.shower.getFrames()
            let index = 0
            for (let fw of this.frames) {
                let item = {
                    title: fw.getTitleShort(),
                    id: fw.getId(),
                    last: index === this.frames.length - 1,
                    index: index,
                }
                index++
                lst.push(item)
            }
            this.items = lst
        },

        clickItem(item) {
            this.shower.activateFrameWrapper(this.frames[item.index])
        }

    },
}
</script>
