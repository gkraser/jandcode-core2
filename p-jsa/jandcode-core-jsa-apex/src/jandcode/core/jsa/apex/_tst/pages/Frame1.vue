<template>
    <component :is="interior">
        <template #toolbar>
            <q-btn v-for="b in tb" :icon="b.icon" dense flat/>
        </template>

        <q-card>
            <q-card-section>
                Контент
            </q-card-section>
        </q-card>
    </component>
</template>

<script>
    import * as baseapp from '../../js/baseapp'

    export default {
        extends: baseapp.JcFrame,
        props: {
            interior: {
                default: 'Page'
            },
            params: {
                type: Object,
                default: function() {
                    return {}
                }
            },
        },
        created() {
            this.title = this.params.title
            if (this.params.title2) {
                this.title2 = this.params.title2
            }
            this.icon = this.params.icon
        },
        data() {
            return {
                tb: [
                    {icon: 'bus'},
                    {icon: 'setup'},
                    {icon: 'user'},
                ]
            }
        }
    }
</script>
