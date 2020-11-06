<template>
    <component :is="rootComponentName" class="jc-page-404">
        <div class="column items-center">
            <div style="height:100px">
            </div>
            <div class="text-h1 text-red-7" style="font-size: 10rem">
                404
            </div>
            <div class="text-h3">
                Страница не найдена
            </div>
        </div>
    </component>
</template>

<script>
export default {
    name: 'jc-page-404',
    inject: {
        pageContainer: {
            default: false
        },
    },

    computed: {
        rootComponentName() {
            return this.pageContainer ? 'q-page' : 'div'
        }
    }
}
</script>
