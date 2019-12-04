import {apex, test} from './vendor'

import * as m from '../../js/modules/cfg-store'

describe('cfg-store1.test.js', function() {

    function create(key, keepStore) {
        key = "test:" + key
        if (!keepStore) {
            window.localStorage.removeItem(key)
        }
        let z = m.createCfgStore(key)
        test.assert.ok(z.autoSave === false)
        return z
    }

    it("1", function() {
        let z = create(this.test.title)

        z.applyDefault({
            a: 1,
            b: 2,
            c: {
                d: 1,
                e: 2
            }
        })

        z.applyDefault({
            a: 11,
            c: {
                f: 12
            },
            d: {
                d1: 22
            }
        })

        test.assert.equal(z.configKey, "test:1")
        test.assert.equal(z.cfg.a, 1)
        test.assert.equal(z.cfg.b, 2)
        test.assert.equal(z.cfg.d.d1, 22)
    })

    it("load-empty", function() {
        let z = create(this.test.title)
        z.load()
        test.assert.equal(Object.keys(z.cfg).length, 0)
    })

    it("save-1", function() {
        let z = create(this.test.title)
        z.applyDefault({
            a: 1,
            b: {
                c: 2,
                d: 3
            }
        })
        test.assert.equal(Object.keys(z.cfg).length, 2)
        z.cfg.a = 11
        z.cfg.b.c = 22
        z.save()

        //
        let z2 = create(this.test.title, true)
        z2.load()
        test.assert.equal(Object.keys(z2.cfg).length, 0)
        z2.applyDefault({
            a: 1,
            b: {
                c: 2,
                d: 3
            }
        })
        test.assert.equal(Object.keys(z2.cfg).length, 2)
        test.assert.equal(z2.cfg.a, 11)

    })

    it("reset-1", function() {
        let z = create(this.test.title)
        z.applyDefault({
            a: 1,
            b: {
                c: 2,
                d: 3
            }
        })
        z.cfg.a = 11
        z.cfg.b.c = 22
        z.save()

        test.assert.equal(z.cfg.a, 11)
        //

        z.reset()
        test.assert.equal(z.cfg.a, 1)
        test.assert.equal(z.cfg.b.c, 2)
    })


    it("reset-part-1", function() {
        let z = create(this.test.title)
        z.applyDefault({
            a: 1,
            b: {
                c: 2,
                d: 3,
                e: {
                    f: 4,
                    g: 5
                }
            }
        })
        z.cfg.b.c = 222
        z.cfg.b.e.f = 444

        test.assert.equal(z.cfg.b.c, 222)
        test.assert.equal(z.cfg.b.e.f, 444)

        //
        z.reset('xx.yy') // такого пути нет
        test.assert.equal(z.cfg.b.c, 222)
        test.assert.equal(z.cfg.b.e.f, 444)

        //
        z.reset('b.e')
        test.assert.equal(z.cfg.b.c, 222)
        test.assert.equal(z.cfg.b.e.f, 4)

    })

    it("reset-value-1", function() {
        let z = create(this.test.title)
        z.applyDefault({
            a: 1,
            b: {
                c: 2,
                d: 3,
                e: {
                    f: 4,
                    g: 5
                }
            }
        })
        z.cfg.b.c = 222
        z.cfg.b.e.f = 444

        //
        test.assert.equal(z.cfg.b.e.f, 444)
        z.reset('b.e.f')
        test.assert.equal(z.cfg.b.e.f, 4)

    })

})

