<template>
    <Dialog>
        <div class="row q-gutter-x-md q-mb-md" style="width:500px">
            <jc-panel title="Pane1" class="col">
                <tst-big-content/>

            </jc-panel>
            <jc-panel title="Pane2" class="col-8">
                <tst-big-content/>

            </jc-panel>
        </div>
        <div class="row">
            <jc-panel title="Pane3" class="col">
                <tst-big-content/>

            </jc-panel>
        </div>
    </Dialog>
</template>

<script>
import {apx} from '../vendor'

export default {
    extends: apx.JcFrame,
    created() {
        this.title = 'Panels'
    },
    frameInit() {

    },
    data() {
        return {

        }
    },
    methods: {},
}
</script>
