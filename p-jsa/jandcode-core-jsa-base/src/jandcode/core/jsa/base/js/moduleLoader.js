/*

Загрузчик динамических модулей

----------------------------------------------------------------------------- */


import * as cnv from './cnv'
import * as ajax from './ajax'
import * as error from './error'

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
 * @param callback
 */
export function loadModule(modules, callback) {

    // список не использованных модулей из modules
    // их будем запрашивать
    let notUsed = []

    function doCallback() {
        // если сюда попали, то явно все ок

        // метим все запрашиваемые как использованные
        for (let a of notUsed) {
            use(a)
        }

        if (cnv.isFunction(callback)) {
            Jc.ready(function() {
                callback();
            })
        }
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
        let e = error.errorCreate(err)
        console.error("Jc.loadModule", e.getMessage(), notUsed);
        throw err
    }

    // обработка requires
    function onRequires(resp) {
        // получили массив модулей
        if (resp.length == 0) {
            // он пуст
            doCallback();
            return;
        }
        let needIds = ''
        for (let a of resp) {
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
        ajax.request({
            url: 'jsa/t',
            //dataType: 'json',
            params: {
                p: needIds
            }
        })
            .done(onTexts)
            .catch(onError)

    }

    // обработка text
    function onTexts(resp) {
        // регистрируем все модули, которые загрузили
        //console.info("resp",resp);
        eval(resp)
        // for (let a of resp) {
        //     Jc.moduleDef(a);
        // }
        //
        doCallback();
    }

    // нужно получить requires
    ajax.request({
        url: 'jsa/r',
        dataType: 'json',
        params: {
            p: notUsed.join(",")
        }
    })
        .done(onRequires)
        .catch(onError)

}
