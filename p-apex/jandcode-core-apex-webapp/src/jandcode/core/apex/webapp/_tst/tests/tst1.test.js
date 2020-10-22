import {test} from '../vendor'

/*
    Демонстрация поддержки тестирования 
 */

describe('tst1.test.js', function() {

    test.pauseAfterEach()

    it("out html", function() {
        let body = test.getBody()
        body.innerHTML = '<div>Hello<b>bold</b>!</div>'
    })


    it("append el", function() {
        let body = test.getBody()
        let z = document.createElement('div')
        z.innerHTML = '<div>Hello2<b>bold</b>!</div>'
        body.appendChild(z)
    })


    it("render vue comp from comp", function() {
        let Comp = {
            template: `<div>
    <q-btn label="Button1" :color="colorData"/>
    <q-btn :label="'Button - '+colorProp" :color="colorProp"/>
</div>`,
            props: ['colorProp'],
            data() {
                return {
                    colorData: 'secondary'
                }
            }
        }

        let colors = [
            'primary',
            'secondary',
            'accent',
            'dark',
            'positive',
            'negative',
            'info',
            'warning',
        ]
        for (let c of colors) {
            let vm = test.vueMount(Comp, {
                propsData: {
                    colorProp: c
                }
            })
        }
    })


    it("render vue comp from template", function() {
        let vm = test.vueMount(`<div>
    <q-btn label="Button1" color="negative"/>
    <q-btn label="Button2" color="accent"/>
</div>`)
    })


    it("render big", function() {
        let vm = test.vueMount(`<div>
    <div v-for="n in 20" class="no-wrap" style="min-width:1500px" :key="n">
        <q-btn v-for="m in 12" :label="'Button-'+n+':'+m" :key="m"/>
    </div>
</div>`)
    })


    it("check values", async function() {
        let Comp = {
            template: `<div>{{value1}}-{{value2}}</div>`,
            props: ['p1'],
            created() {
                this.value1 = this.value1 + this.p1
            },
            data() {
                return {
                    value1: '1'
                }
            },
            computed: {
                value2() {
                    return this.value1 + this.p1 + '!'
                }
            }

        }
        let vm = test.vueMount(Comp, {
            propsData: {
                p1: '2'
            }
        })
        test.assert.equal(vm.value1, '12')
        test.assert.equal(vm.value2, '122!')
        //
        await vm.setPropsData({p1: '333'})
        //
        test.assert.equal(vm.value2, '12333!')

    })

})

