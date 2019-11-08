<template>
    <div class="icon-test-5f658e00">

        <div class="column q-pa-lg q-gutter-y-md">

            <div class="col page-title">
                Все иконки
            </div>

            <div class="col row q-gutter-x-md">
                <q-input clearable dense outlined v-model="filterText"
                         label="Include" class="col-2"/>
                <q-input clearable dense outlined v-model="filterTextExclude"
                         label="Exclude" class="col-2"/>
                <div class="col row q-gutter-x-md">
                    <q-btn label="Цвет" class="col-1">
                        <q-popup-proxy transition-show="scale" transition-hide="scale">
                            <q-color v-model="colorIcon"/>
                        </q-popup-proxy>
                    </q-btn>
                    <q-btn-toggle
                            v-model="iconSize"
                            toggle-color="primary"
                            class="col-3"
                            :flat="true"
                            :options="[
                                {label: '16', value: 16},
                                {label: '24', value: 24},
                                {label: '32', value: 32},
                                {label: '48', value: 48},
                                {label: '64', value: 64},
                                {label: '128', value: 128}
                            ]"
                    />
                    <q-checkbox v-model="iconBorder" label="Рамка" class="col-1"/>
                </div>
            </div>

            <div v-if="selectedName" class="col icon-selected-info">
                <span class="label">Имя:</span>
                <span class="value">{{selectedName}}</span>
                <span class="label">Иконка:</span>
                <span class="value"> {{selectedIcon}}</span>
            </div>

            <div class="col icon-wrap row q-gutter-md">
                <q-card v-for="icon in allIcons" class="icon-box" :key="icon.name"
                        @click="clickIcon(icon.name, icon.value)">

                    <q-tooltip content-style="font-size: 14px">
                        <span class="text-yellow">{{icon.name}}</span>
                        :
                        {{icon.value}}
                    </q-tooltip>

                    <q-badge floating transparent class="q-mt-sm q-mr-sm"
                             :label="icon.type"
                             :text-color="icon.typeColor"
                             color="white"/>

                    <q-card-section class="column items-center q-pt-lg">
                        <q-icon :name="icon.name" :size="''+iconSize+'px'"
                                :style="{color:colorIcon}"
                                :class="{'icon-border':iconBorder}"/>

                        <div class="text-grey-7 q-mt-md icon-name">
                            {{icon.name}}
                        </div>
                    </q-card-section>
                </q-card>
            </div>

        </div>
    </div>
</template>

<script>
    import * as apex from 'jandcode.core.apex.jsa'

    // регистрируем собственные иконки
    apex.utils.icons.registerIcons({
        'my-svg-icon1': 'img:' + __dirname + '/images/icon1.svg',
        'my-png-icon2': 'img:' + __dirname + '/images/icon2.png',
        'my-icon': 'videocam',
        'my-menu': 'menu'
    })

    let colors = {
        'font': 'red',
        'svg': 'accent',
        'img:svg': 'green',
        'img:png': 'blue',
        'other': 'orange'
    }

    export default {
        props: {},
        data() {
            return {
                filterText: '',
                filterTextExclude: '',
                selectedName: '',
                selectedIcon: '',
                colorIcon: '#616161',
                iconBorder: false,
                iconSize: 64,
            }
        },
        computed: {
            allIcons() {
                let all = apex.utils.icons.getIcons()
                let keys = Object.keys(all)
                if (this.filterTextExclude) {
                    keys = keys.filter(s => s.indexOf(this.filterTextExclude) === -1)
                }
                if (this.filterText) {
                    keys = keys.filter(s => s.indexOf(this.filterText) !== -1)
                }
                keys.sort()
                return keys.map((name) => {
                    let v = all[name]
                    let type = 'font'
                    if (v.startsWith('img:')) {
                        type = 'img'
                        let a = v.lastIndexOf('.')
                        if (a !== -1) {
                            type = type + ':' + v.substring(a + 1)
                        }
                    } else if (v.startsWith('svg:')) {
                        type = 'svg'
                    }
                    return {
                        name: name,
                        value: v,
                        type: type,
                        typeColor: colors[type] || colors['other']
                    }
                })
            }
        },
        methods: {
            clickIcon(n, v) {
                this.selectedName = n
                this.selectedIcon = v
            }
        },
    }
</script>

<style lang="scss">
    .icon-test-5f658e00 {

        .page-title {
            font-size: 24px;
            font-weight: bold;
        }

        .icon-box {
            display: inline-block;
            width: 150px;
        }

        .icon-border {
            border: 1px solid gray;
        }

        .icon-name {
            font-size: 0.9em;
            max-width: 120px;
            text-overflow: ellipsis;
            overflow: hidden;
            white-space: nowrap;
            flex: 1;
        }

        .icon-selected-info {

            & .label {
                color: gray;
            }

            & .value {
                color: brown;
            }
        }

        .q-btn-group {
            .q-btn{
               padding-left: 4px;
               padding-right: 4px;
            }
        }

    }
</style>