<template>
    <div class="jc-frame-dialog">

        <div class="jc-frame-page--header">
            HEADER
        </div>

        <div class="jc-frame-page--body">
            <slot name="default">
            </slot>
        </div>

        <div class="jc-frame-page--footer">
            footer
        </div>

    </div>
</template>

<script>
    import {apex} from "./vendor"

    export default {
        extends: apex.JcDecorFrame,
    }
</script>
