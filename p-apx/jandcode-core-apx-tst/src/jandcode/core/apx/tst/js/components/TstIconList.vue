<template>
    <div>
        <div class="row q-mb-md q-gutter-x-md items-center">

            <q-input clearable dense outlined v-model="filterText"
                     label="Include"/>
            <q-input clearable dense outlined v-model="filterTextExclude"
                     label="Exclude"/>

            <q-btn label="Цвет">
                <q-popup-proxy transition-show="scale" transition-hide="scale">
                    <q-color v-model="iconColor"/>
                </q-popup-proxy>
            </q-btn>

            <div class="row items-center">
                <label class="q-pr-sm">Размер (rem):</label>
                <div class="col row q-gutter-x-md">
                    <q-btn-toggle
                            v-model="iconSize"
                            toggle-color="primary"
                            :flat="true"
                            :options="[
                                {label:'1', value: '1rem'},
                                {label:'1.5', value: '1.5rem'},
                                {label:'2', value: '2rem'},
                                {label:'5', value: '5rem'},
                                {label:'7', value: '7rem'},
                                {label:'9', value: '9rem'},
                            ]"
                    />
                    <q-checkbox v-model="iconBorder" label="Рамка" class="col-1"/>
                </div>
            </div>
            <div v-if="selectedName">
                <label>Имя:</label>
                <span><b>{{ selectedName }}</b></span>
                <label>Иконка:</label>
                <span><b>{{ selectedIcon }}</b></span>
            </div>
        </div>

        <div @click="onClick">
            <template v-for="icon in iconsList">
                <tst-icon-box :icon="icon" class="q-mr-md q-mb-md shadow-2"
                              :iconSize="iconSize" :iconColor="iconColor"
                              :iconBorder="iconBorder"/>
            </template>
        </div>
    </div>
</template>

<script>
import {apx, jsaBase} from '../vendor'

export default {
    name: 'tst-icon-list',
    props: {
        icons: {
            default: null
        }
    },
    data() {
        return {
            filterText: '',
            filterTextExclude: '',
            selectedName: '',
            selectedIcon: '',
            iconColor: '#616161',
            iconBorder: false,  //todo
            iconSize: '7rem',
        }
    },
    computed: {
        iconsList() {
            let keys = []
            if (!this.icons) {
                keys = Object.keys(apx.icons.getIcons())
            } else if (jsaBase.isObject(this.icons)) {
                keys = Object.keys(this.icons)
            } else if (jsaBase.isArray(this.icons)) {
                keys = this.icons
            }
            //
            if (this.filterTextExclude) {
                keys = keys.filter(s => s.indexOf(this.filterTextExclude) === -1)
            }
            if (this.filterText) {
                keys = keys.filter(s => s.indexOf(this.filterText) !== -1)
            }
            keys.sort()
            return keys
        },
    },
    methods: {
        onClick(ev) {
            let a = ev.target.closest('.tst-icon-box')
            if (a) {
                this.selectedName = a.dataset.icon
                this.selectedIcon = apx.icons.getIcon(this.selectedName)
            } else {
                this.selectedName = ''
                this.selectedIcon = ''
            }
        }
    },
}
</script>
