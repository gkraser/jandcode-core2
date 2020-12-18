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
import {apx} from '../vendor'

export default {
    extends: apx.JcFrame,
    components: {},
    props: {},
    created() {
        this.title = 'Главная страница'
    },
    frameInit() {
    },
    data() {
        return {}
    },
    methods: {},
}
</script>
