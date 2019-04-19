/* Приложение
----------------------------------------------------------------------------- */

import {getApp} from './app'

export * from './app'

/**
 * @type {App}
 */
Object.defineProperty(exports, "app", {
    enumerable: true,

    /**
     * @return {App}
     */
    get: function() {
        return getApp();
    }
});


