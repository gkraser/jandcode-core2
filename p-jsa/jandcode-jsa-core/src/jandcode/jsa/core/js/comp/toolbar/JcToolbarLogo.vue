<template>
    <jc-icon v-if="icon" :class="[cn(),{'mod-click-action':!!$listeners.click}]"
             :icon="icon" :url="url" v-on="$listeners"/>
    <img v-else :class="[cn(),{'mod-click-action':!!$listeners.click}]" :src="getUrl()"
         v-on="$listeners">
</template>

<script>
    import {jsaBase} from '../vendor'

    export default {
        name: 'jc-toolbar-logo',
        props: {
            icon: String,
            url: String,
        },
        methods: {
            getUrl() {
                return jsaBase.url.ref(this.url)
            }
        }
    }
</script>
