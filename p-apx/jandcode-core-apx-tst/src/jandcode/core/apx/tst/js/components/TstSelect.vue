<template>
    <label class="tst-select">
        <span>{{ label }}</span>
        <select :value="value" @change="onChange">
            <option v-for="opt in optionsData" :value="opt.value">{{ opt.label }}</option>
        </select>
    </label>
</template>

<script>
import {jsaBase} from '../vendor'

export default {
    name: 'tst-select',
    props: {
        label: {
            default: null
        },
        options: {
            default: null
        },
        value: null
    },
    computed: {
        optionsData() {
            let res = []
            if (!this.options) {
                return res
            }
            for (let it of this.options) {
                if (jsaBase.isObject(it)) {
                    res.push({value: it.value, label: it.label})
                } else {
                    res.push({value: it, label: it})
                }
            }
            return res
        }
    },
    methods: {
        onChange: function(ev) {
            this.$emit('input', ev.target.value);
        }
    }
}
</script>

<style>
.tst-select {
  display: flex;
  align-items: center;
}

.tst-select > span {
  margin-right: 4px;
}
</style>