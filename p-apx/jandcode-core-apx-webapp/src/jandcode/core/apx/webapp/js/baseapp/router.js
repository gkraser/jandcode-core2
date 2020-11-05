/* simple routing

use: https://github.com/pillarjs/path-to-regexp

----------------------------------------------------------------------------- */

import {match} from 'path-to-regexp';

export class FrameRouter {

    /**
     * Найти фрейм, соотвествующий uri
     * @param uri
     */
    resolveFrame(uri) {
        return null //todo
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