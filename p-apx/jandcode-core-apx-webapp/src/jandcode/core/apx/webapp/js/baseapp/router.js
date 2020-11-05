/* simple routing

use: https://github.com/pillarjs/path-to-regexp

----------------------------------------------------------------------------- */

import {jsaBase} from '../vendor'
import {match} from 'path-to-regexp';

export class FrameRouter {

    constructor() {
        this._routes = []
    }

    /**
     * Найти фрейм, соотвествующий uri
     * @param uri
     */
    resolveFrame(uri) {
        return null //todo
    }

    /**
     * @param uri {String}
     * @return {null|*|RegExpMatchArray|Promise<Response | undefined>}
     */
    resolve(uri) {
        if (uri == null) {
            return null
        }

        let queryStr = ''
        let queryParams = {}
        let a = uri.indexOf('?')
        if (a !== -1) {
            queryStr = decodeURIComponent(uri.substring(a + 1))
            uri = uri.substring(0, a)
            let sp = new URLSearchParams(queryStr)
            for (let key of sp.keys()) {
                queryParams[key] = sp.get(key)
            }
        }
        uri = decodeURI(uri)

        for (let r of this._routes) {
            let tmp = r.match(uri)
            if (tmp === false) {
                continue
            }
            // найдено!
            let res = {
                path: tmp.path,
                frame: r.frame,
                params: {}
            }
            jsaBase.extend(res.params, tmp.params);
            jsaBase.extend(res.params, queryParams);
            //
            return res
        }
        return null
    }

    /**
     * Добавить routes
     * @param routes {Array} список объектов-описаний route
     */
    addRoutes(routes) {
        if (!routes) {
            return
        }
        for (let r of routes) {
            let rd = new RouteDef(r)
            this._routes.push(rd)
        }
    }
}


/**
 * Определение одного пути router
 */
export class RouteDef {

    constructor(options) {
        /**
         * Путь. Используется синтаксис path-to-regexp
         * @type {null}
         */
        this.path = null

        /**
         * Определение фрейма, соответсвующее пути
         * @type {null}
         */
        this.frame = null

        //
        this.path = options.path
        this.frame = options.frame

        if (this.path == null) {
            throw new Error('Parametr path not defined')
        }
        if (this.frame == null) {
            throw new Error('Parametr frame not defined')
        }

        //
        this._match = match(this.path, {
            encode: encodeURI,
            decode: decodeURIComponent
        });
        // this._toPath = compile(this.path, {
        //     encode: encodeURI
        // });
        //
    }

    match(uri) {
        return this._match(uri)
    }

}