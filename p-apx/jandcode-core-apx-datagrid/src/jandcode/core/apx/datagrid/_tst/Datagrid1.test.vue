<template>
    <tst-apx-panel class="datagrid1-test-69e5515a">
        <template #tools>
        </template>

        <div style="row">
            <jc-datagrid :options="opt1" style="height:400px"/>
        </div>

    </tst-apx-panel>
</template>

<script>
import {apx, tst} from './vendor'

//define some sample data
let tabledata = [
    {id: 1, name: "Oli Bob", age: "12", col: "red", dob: ""},
    {id: 2, name: "Mary May", age: "1", col: "blue", dob: "14/05/1982"},
    {id: 3, name: "Christine Lobowski", age: "42", col: "green", dob: "22/05/1982"},
    {id: 4, name: "Brendon Philips", age: "125", col: "orange", dob: "01/08/1980"},
    {id: 5, name: "Margret Marmajuke", age: "16", col: "yellow", dob: "31/01/1999"},
];

export default {
    mixins: [tst.mixins.cfgStore],
    components: {},
    created() {
        this.cfgStore.applyDefault({})
        //
        for (let i = 100; i < 505; i++) {
            tabledata.push({
                id: i,
                name: 'user-' + i
            })
        }

        this.opt1 = {
            data: tabledata, //assign data to table
            layout: "fitColumns", //fit columns to width of table (optional)
            selectable:1,
            columns: [ //Define Table Columns
                {title: "Name", field: "name", width: 150},
                {title: "Age", field: "age", hozAlign: "left", formatter: "progress"},
                {title: "Favourite Color", field: "col"},
                {
                    title: "Date Of Birth", field: "dob", sorter: "date",
                    hozAlign: "center"
                },
            ],
            // rowClick: function(e, row) { //trigger an alert message when the row is clicked
            //     alert("Row " + row.getData().id + " Clicked!!!!");
            // },
        }

    },
    data() {
        return {}
    },
    methods: {
        applyCfg() {
            let cfg = this.cfg
        },
    }
}
</script>

<style lang="less">

    .datagrid1-test-69e5515a {



    }

</style>
