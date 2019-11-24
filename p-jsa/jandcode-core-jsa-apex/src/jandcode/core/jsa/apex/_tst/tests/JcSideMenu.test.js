import {apex, test} from './vendor'

describe("JcSideMenu.test.js", function() {

    test.initUi()
    test.pauseAfterEach()

    it("1", function() {
        let vm = test.vueMount({
            template: `<jc-side-menu-item ref="r1" label="z1" icon="bus"/>`
        })
        test.assert.equal(vm.$refs.r1.label, 'z1')
    })


    it("opened-2", async function() {
        let vm = test.vueMount({
            template: `<div>
    <q-btn label="out" @click="onOut"/>
    <jc-side-menu-item ref="r1" label="op1" icon="bus" :opened="op1" @input="op1=$event">
        <jc-side-menu-item label="OPEN"/>
    </jc-side-menu-item>
    <jc-side-menu-item ref="r2" label="op2" icon="bus" :opened="op2">
        <jc-side-menu-item label="OPEN"/>
    </jc-side-menu-item>
    <jc-side-menu-item ref="r2" label="op2" icon="bus">
        <jc-side-menu-item label="OPEN"/>
    </jc-side-menu-item>
</div>`,
            data() {
                return {
                    op1: true,
                    op2: true,
                }
            },
            methods: {
                onOut() {
                    console.info('op1', vm.op1, 'op2', vm.op2);

                },
                test1(a, b) {
                    console.info(a, b);
                }
            }
        })

        let r1 = vm.$refs.r1

        await vm.$nextTick()
        await test.pause(1)

        vm.op1 = false
        await vm.$nextTick()
        await test.pause(1)


    })

})

