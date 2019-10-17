<template>
    <tst-panel>
        <div>
            Icons
            {{ic('bus')}}
        </div>
        <span v-html="ic('bus')"></span>
        <span v-html="ic('true')"></span>
        <span v-html="ic('false')"></span>
    </tst-panel>
</template>

<script>
    import {jsaBase} from './vendor'

    export default {
        props: {},
        data() {
            return {}
        },
        methods: {
            ic(name) {
                return jsaBase.svgicon.iconHtml(name);
            }
        }
    }
</script>
