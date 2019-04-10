<template>
    <div :class="[cn(),{'mod-click-action':!!$listeners.click}]" v-on="$listeners">
        <template v-if="text2">
            <div :class="cn('text1')">{{text}}</div>
            <div :class="cn('text2')">{{text2}}</div>
        </template>
        <div v-else :class="cn('text')">{{text}}</div>
    </div>
</template>

<script>
    export default {
        name: 'jc-toolbar-title',
        props: {
            text: String,
            text2: String,
        },
    }
</script>
