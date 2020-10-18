<template>
    <div class="tst-big-content">
        <button @click="toggle">toggle</button>
        <div v-for="i in curRows">Row #{{ i }}</div>
        <button @click="toggle">toggle</button>
    </div>
</template>

<script>
export default {
    name: 'tst-big-content',
    props: {
        maxRows: {
            default: 200
        },
        minRows: {
            default: 2
        },
        rows: {
            default: null
        }
    },
    data() {
        return {
            curRows: 0
        }
    },
    created() {
        this.curRows = this.rows ? this.rows : this.minRows
    },
    methods: {
        toggle() {
            this.curRows = this.curRows === this.minRows ? this.maxRows : this.minRows
        }
    }
}
</script>
