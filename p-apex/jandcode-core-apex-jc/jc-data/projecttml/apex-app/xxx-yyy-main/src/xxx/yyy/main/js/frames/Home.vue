<template>
    <Page>
        <div class="row items-start q-gutter-md">
            <q-card class="col">
                <q-card-section>
                    Welcome!
                </q-card-section>
            </q-card>
        </div>
    </Page>
</template>

<script>
import {apex} from '../vendor'

export default {
    extends: apex.JcFrame,
    components: {},
    props: {},
    created() {
        this.title = 'Главная страница'
    },
    initFrame() {
    },
    data() {
        return {}
    },
    methods: {},
}
</script>
