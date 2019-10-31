<template>
    <q-layout view="hHh lpR fFf">

        <q-header elevated class="bg-primary text-white">
            <q-toolbar>
                <q-btn dense flat round icon="menu" @click="left = !left"/>

                <q-toolbar-title>
                    XxxYyy
                </q-toolbar-title>
            </q-toolbar>
        </q-header>

        <q-drawer show-if-above v-model="left"
                  side="left" bordered content-class="bg-grey-2">
        </q-drawer>

        <q-page-container>
            <router-view></router-view>
        </q-page-container>

    </q-layout>
</template>

<script>
    export default {
        components: {},
        props: {},
        data() {
            return {
                left: false,
            }
        },
    }
</script>
