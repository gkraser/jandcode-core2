import {tst, mod, test} from '../vendor'


function dataset1() {
    let data = []
    let ut = new test.RndUtils()

    let idx = 0
    for (let dt of ut.days(30)) {
        let rec = {
            dt: dt,
            p1: ut.rnd.integer({min: 100, max: 200}),
            p2: ut.rnd.integer({min: 200, max: 300}),
        }
        data.push(rec)
        idx++
    }

    return data
}

export class Chart1 extends mod.ChartBuilder {

    onBuild() {
        super.onBuild();
        //
        let opt = this.getOptions()
        //
        opt.animation = false
        //
        this.add('dataset', {
            id: 'main',
            source: dataset1()
        })
        //
        this.addGrid({id: 'main'}, {
            x: {type: 'time'},
            y: {}
        })
        //
        this.add('series', {
            type: 'line',
            encode: {
                x: 'dt',
                y: 'p1'
            }
        })
        //
        this.add('series', {
            type: 'bar',
            encode: {
                x: 'dt',
                y: 'p2'
            }
        })
    }

}
