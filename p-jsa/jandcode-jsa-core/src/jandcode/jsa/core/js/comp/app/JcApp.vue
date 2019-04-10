<template>
    <div :class="cn()">

        <div :class="cn('head')">
            <jc-toolbar>
                <slot name="tb-main"></slot>
            </jc-toolbar>
        </div>

        <div :class="cn('main')">
            <div :class="cn('tools')" v-if="!!$slots['tools']">
                <slot name="tools"></slot>
            </div>
            <div :class="cn('content')">
                <slot name="content"></slot>
            </div>
        </div>

        <div :class="cn('foot')" v-if="!!$slots['tb-foot']">
            <jc-toolbar>
                <slot name="tb-foot"></slot>
            </jc-toolbar>
        </div>

    </div>
</template>

<script>
    export default {
        name: 'jc-app',
        props: {},
        data() {
            return {}
        }
    }
</script>
