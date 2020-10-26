<template>
    <Page>

        <template #toolbar>
            <jc-action label="Сохранить"/>
        </template>

        <jc-btn label="Кнопка на пока"/>
    </Page>
</template>

<script>
/* Фрейм для тестирования декоратора приложения
----------------------------------------------------------------------------- */

import {apex} from '../../vendor'

export default {
    extends: apex.JcFrame,
    props: {},
    created() {
        this.title = 'Заголовок фрейма'
    },
    initFrame() {
    },
    data() {
        return {}
    },
    methods: {},
}
</script>
