import {jsaBase} from '../vendor'
import lodashGet from 'lodash/get'


class DaoContext {

    constructor(data) {
        this.daoService = null
        this.params = null
        this.resolve = null
        this.reject = null
        this.result = null
    }

}

class DaoService {

    constructor() {
        this.__fns = {}
    }

    register(obj) {
        jsaBase.extend(true, this.__fns, obj)
    }

    createContext() {
        return new DaoContext()
    }

    invoke(name, params) {
        let th = this

        let p = params || {}

        let fn = name
        if (jsaBase.isString(fn)) {
            let pak = fn.replace('/', '.')
            fn = lodashGet(this.__fns, pak)
            if (!fn) {
                throw new Error('not found:' + name)
            }
        }


        let promise = new Promise(function(resolve, reject) {
            let ctx = th.createContext()
            ctx.daoService = th
            ctx.params = p
            ctx.resolve = function(result) {
                if (result != null) {
                    if (jsaBase.isArray(result)) {
                        ctx.result = {data: result}
                    } else if (jsaBase.isObject(result)) {
                        ctx.result = result
                    } else {
                        ctx.result = {value: result}
                    }
                } else if (ctx.result == null) {
                    ctx.result = {}
                }
                resolve(ctx)
            }
            ctx.reject = reject
            fn(ctx)
        })

        return promise
    }

}


export {
    DaoContext,
    DaoService,
}