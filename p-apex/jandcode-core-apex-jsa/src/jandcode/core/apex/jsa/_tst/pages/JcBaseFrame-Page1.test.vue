<template>
    <div class="main">
        <div class="f1">
            <FramePage1 :params="frame1"/>
        </div>
        <div class="f1">
            <FramePage1 :params="frame2"/>
        </div>
        <div class="f1">
            <FramePage1 :params="frame3"/>
        </div>
        <div class="f1">
            <FramePage1 :params="frame4"/>
        </div>
    </div>
</template>

<script>
    import FramePage1 from './FramePage1'

    export default {
        components: {
            FramePage1,
        },
        props: {},
        data() {
            return {
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
    .main {
        padding: 20px;
    }

    .f1 {
        border: 1px solid green;
        margin-bottom: 10px;
    }
</style>