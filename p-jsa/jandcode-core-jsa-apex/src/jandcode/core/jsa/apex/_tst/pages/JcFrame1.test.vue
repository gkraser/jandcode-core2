<template>
    <div class="main q-pa-md">

        <div class="q-mb-md">
            <q-btn-toggle
                    v-model="interior"
                    toggle-color="primary"
                    class="col-3"
                    :flat="true"
                    :options="[
                    {value: 'Page', label:'Page'},
                    {value: 'Dialog', label:'Dialog'},
                ]"
            />
        </div>

        <div class="row q-gutter-x-md q-mb-md">
            <div class="f1 col">
                <Frame1 :params="frame1" :interior="interior"/>
            </div>
            <div class="f1 col">
                <Frame1 :params="frame2" :interior="interior"/>
            </div>
        </div>
        <div class="row q-gutter-x-md">
            <div class="f1 col">
                <Frame1 :params="frame3" :interior="interior"/>
            </div>
            <div class="f1 col">
                <Frame1 :params="frame4" :interior="interior"/>
            </div>
        </div>
    </div>
</template>

<script>
    import Frame1 from './Frame1'

    export default {
        components: {
            Frame1,
        },
        props: {},
        data() {
            return {
                interior: 'Page',
                frame1: {
                    title: 'Заголовок 1',
                    title2: 'Заголовок 2',
                    icon: 'frame',
                },
                frame2: {
                    title: 'Заголовок 11',
                    title2: 'Заголовок 12',
                    icon: '',
                },
                frame3: {
                    title: 'Заголовок 11',
                    title2: '',
                    icon: 'frame',
                },
                frame4: {
                    title: 'Заголовок 11',
                    title2: '',
                    icon: '',
                }
            }
        }
    }
</script>


<style lang="scss">
    .f1 {
        border: 1px solid green;
    }
</style>