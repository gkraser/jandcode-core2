<template>
    <div class="tst-icon-box column inline items-center q-pa-md" :data-icon="iconName">

        <q-badge transparent class="tst-icon-box--icon-type self-end"
                 :label="iconType"
                 :text-color="iconTypeColor"/>

        <q-tooltip>
            <span class="text-yellow">{{ iconName }}</span>
            :
            {{ iconValue }}
        </q-tooltip>

        <q-icon :name="iconName" class="col q-mb-md tst-icon-box--icon"
                :style="{fontSize: iconSize, color:iconColor}"
                :class="{'tst-icon-box--icon-border':iconBorder}"
        />


        <div class="tst-icon-box--icon-name col text-size-sm text-gray-700">
            {{ iconName }}
        </div>

    </div>
</template>

<script>
import {apex} from '../vendor'

export let colors = {
    'font': 'red',
    'svg': 'accent',
    'img:svg': 'green',
    'img:png': 'blue',
    'other': 'orange'
}

export default {
    name: 'tst-icon-box',
    props: {
        icon: {
            default: 'bus'
        },
        iconSize: {
            default: '2rem'
        },
        iconColor: {
            default: '#616161'
        },
        iconBorder: {
            default: false
        }
    },
    data() {
        return {}
    },
    computed: {
        iconName() {
            return apex.utils.getIcon(this.icon) ? this.icon : 'empty'
        },
        iconValue() {
            return apex.utils.getIcon(this.icon) || 'empty'
        },
        iconType() {
            let v = this.iconValue
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
            return type
        },
        iconTypeColor() {
            return colors[this.iconType] || colors['other']
        }
    },
    methods: {},
}
</script>

<style lang="less">

.tst-icon-box {
  width: 12rem;

  &--icon-name {
    max-width: 10rem;
    text-overflow: ellipsis;
    overflow: hidden;
    white-space: nowrap;
  }

  &--icon-type {
    position: relative;
    right: -0.8rem;
    top: -0.8rem;
    background: transparent;
  }

  &--icon {
    border: 1px solid transparent;
  }

  &--icon-border {
    border: 1px solid gray;
  }

}

</style>
