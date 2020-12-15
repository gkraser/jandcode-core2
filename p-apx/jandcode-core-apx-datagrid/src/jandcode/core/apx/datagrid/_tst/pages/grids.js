import {apx} from '../vendor'

let tabledata = [
    {id: 1, name: "Oli Bob", age: "12", col: "red", dob: ""},
    {id: 2, name: "Mary May", age: "1", col: "blue", dob: "14/05/1982"},
    {id: 3, name: "Christine Lobowski", age: "42", col: "green", dob: "22/05/1982"},
    {id: 4, name: "Brendon Philips", age: "125", col: "orange", dob: "01/08/1980"},
    {id: 5, name: "Margret Marmajuke", age: "16", col: "yellow", dob: "31/01/1999"},
];

export function grid1(opt) {
    opt = apx.jsaBase.extend(opt)
    let count = opt.count || 400

    let data = tabledata.slice()

    for (let i = 100; i < 100 + count; i++) {
        data.push({
            id: i,
            name: 'user-' + i
        })
    }

    let res = {
        data: data,
        layout: "fitColumns",
        selectable: 1,
        columns: [
            {title: "Name", field: "name", width: 120},
            {title: "Age", field: "age", hozAlign: "left", formatter: "progress"},
            {title: "Favourite Color", field: "col", widthGrow: 1},
            {
                title: "Date Of Birth", field: "dob", sorter: "date",
                hozAlign: "center"
            },
        ],
    }

    return res
}

