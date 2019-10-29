<template>
    <div :class="cn()">
        <div :class="cn('content')">
            <slot name="content"></slot>
            <div v-if="hasDummyToggleIcon" :class="cn('right')">
                <jc-icon icon="empty"/>
            </div>
            <div v-if="hasNested" :class="cn('right')" @click="onClickToggle">
                <jc-icon :icon="iconToggle"/>
            </div>
        </div>
        <div v-if="isNestedOpened" :class="cn('nested')">
            <slot name="nested"></slot>
        </div>
    </div>
</template>

<script>
    import {findCompUp} from '../../utils/base'

    export let config = {
        icons: {
            nestedOpened: 'expand-less',
            nestedClosed: 'expand-more',
        },

    }

    export default {
        name: 'jc-list-item',

        props: {},

        data() {
            return {
                nestedOpened: false
            }
        },

        computed: {

            /**
             * Есть ли вложенный
             */
            hasNested() {
                return !!this.$slots['nested'];
            },

            iconToggle() {
                return this.isNestedOpened ? config.icons.nestedOpened : config.icons.nestedClosed;
            },

            hasDummyToggleIcon() {
                if (this.hasNested) {
                    return false
                }
                let z = findCompUp(this, 'list')
                if (!z) {
                    return true
                }
                return !z.hideToggle
            },

            isNestedOpened() {
                return this.hasNested && this.nestedOpened;
            }

        },

        methods: {
            toggleNested() {
                this.nestedOpened = !this.nestedOpened
            },

            onClickToggle(ev) {
                this.toggleNested()
            },

            onClick(ev) {
                console.info("click", ev);
                this.showedChild = !this.showedChild;
            }
        }
    }
</script>


