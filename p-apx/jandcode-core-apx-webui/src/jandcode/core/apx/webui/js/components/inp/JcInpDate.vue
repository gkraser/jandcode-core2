<template>
    <q-input
            outlined
            :class="classes"
            :mask="inputMask"
            v-model="inpValue"
    >
        <q-menu v-model="showPopup"
                :transition-show="null"
                :transition-hide="null"
                anchor="bottom left"
                no-parent-event
        >
            <q-date v-model="inpValue"
                    :mask="displayFormat"
                    today-btn
                    years-in-month-view
            >
            </q-date>
        </q-menu>
        <template v-slot:append>
            <q-icon name="calendar" class="cursor-pointer" @click="showPopup=true"/>
        </template>
    </q-input>
</template>

<script>
import {date} from '../../utils'

let displayFormat = Jc.cfg.date.displayFormat

export default {
    name: 'jc-inp-date',
    props: {
        value: {}
    },
    created() {
        this.inpValue = date.format(this.value, this.displayFormat)
    },
    data() {
        return {
            inpValue: null,
            showPopup: false,
        }
    },
    watch: {
        value: function(v, old) {
            this.inpValue = date.format(v, this.displayFormat)
        },
        inpValue: function(v, old) {
            this.showPopup = false
            let s = date.parse(v, this.displayFormat)
            this.$emit('input', s)
        }
    },
    computed: {
        classes() {
            let res = ['jc-inp']
            return res
        },
        inputMask() {
            let s = this.displayFormat
            s = s.replace(/[DMY]/g, '#')
            return s
        },
        displayFormat() {
            return displayFormat
        }
    },
    methods: {}
}
</script>
