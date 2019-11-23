/*

Загрузчик динамических модулей

----------------------------------------------------------------------------- */


import * as cnv from './cnv'
import * as error from './error'
import * as url from './url'
import {axios} from './vendor'

// модули, которые уже были загружены
let _usedModules = {}

/**
 * Был ли использован данный модуль
 * @param module
 */
function isUsed(module) {
    let m = _usedModules[module] || Jc.findModule(module)
    return !!m;
}

function use(module) {
    if (isUsed(module)) {
        return
    }
    _usedModules[module] = {name: module, used: true}
}

/**
 * Загрузка указанных модулей
 * @param modules список asset
 * @return {Promise}
 */
export function loadModule(modules) {

    return new Promise(function(resolve, reject) {

        // список не использованных модулей из modules
        // их будем запрашивать
        let notUsed = []

        function doCallback() {
            // если сюда попали, то явно все ок

            // метим все запрашиваемые как использованные
            for (let a of notUsed) {
                use(a)
            }

            resolve()  //todo все модули возвращать?
        }

        // если модули не указаны, ничего и не делаем
        if (!modules) {
            doCallback();
            return;
        }

        // делаем массив запрашиваемых модулей
        if (cnv.isString(modules)) {
            modules = [modules];
        }
        if (!cnv.isArray(modules)) {
            throw new Error("modules not array")
        }

        // проверяем, какие нужно получить
        for (let a of modules) {
            if (!isUsed(a)) {
                notUsed.push(a)
            }
        }

        if (notUsed.length == 0) {
            // нечего получать
            doCallback();
            return;
        }

        // при ошибке
        function onError(err) {
            if (err.response) {
                let e = error.errorCreate(err.response.data)
                console.error("Jc.loadModule", e.getMessage(), notUsed, err);
                reject(e)
            } else {
                let e = error.errorCreate(err)
                reject(e)
            }
        }

        // обработка requires
        function onRequires(resp) {
            // получили массив модулей
            let data = resp.data
            if (data.length == 0) {
                // он пуст
                doCallback();
                return;
            }
            let needIds = ''
            for (let a of data) {
                if (!isUsed(a)) {
                    needIds += a;
                }
            }
            if (!needIds) {
                // нечего получать
                doCallback();
                return;
            }

            // нужно получить texts
            axios.request({
                url: url.ref('jsa/t'),
                responseType: 'text',
                headers: {'X-Requested-With': 'XMLHttpRequest'},
                params: {
                    p: needIds
                }
            }).then(onTexts)
                .catch(onError)

        }

        // обработка text
        function onTexts(resp) {
            // регистрируем все модули, которые загрузили
            eval(resp.data)
            // for (let a of resp) {
            //     Jc.moduleDef(a);
            // }
            //
            doCallback();
        }

        // нужно получить requires
        axios.request({
            url: url.ref('jsa/r'),
            responseType: 'text',
            headers: {'X-Requested-With': 'XMLHttpRequest'},
            params: {
                p: notUsed.join(",")
            }
        }).then(onRequires)
            .catch(onError)


    })

}
