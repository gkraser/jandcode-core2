<template>
    <Page>

        <template #toolbar>
            <AppToolbarDemoSet :toolbarSet="toolbarSet"/>
        </template>

        <jc-btn label="Кнопка на пока"/>
    </Page>
</template>

<script>
/* Фрейм для тестирования декоратора приложения
----------------------------------------------------------------------------- */

import {apex} from '../../vendor'
import AppToolbarDemoSet from '../_components/AppToolbarDemoSet'

export default {
    extends: apex.JcFrame,
    components: {
        AppToolbarDemoSet
    },
    props: {},
    created() {
        this.title = 'Заголовок фрейма'
    },
    initFrame() {
    },
    data() {
        return {
            toolbarSet: null,
        }
    },
    methods: {},
}
</script>
