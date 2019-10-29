/*  Модульная система jsa.
----------------------------------------------------------------------------- */

(function() {
    'use strict';

    // глобальное пространство имен Jc
    window.Jc = window.Jc || {}
    let Jc = window.Jc

    // все модули
    let modules = {}

    // модули в порядке объявления
    let modulesByOrder = []

    // все модули по id
    let modulesById = {}

    // для css
    let cssIdx = 0

    /**
     * Определение модуля.
     * Если первым параметр объект, то его формат:
     * {name: имя модуля, id:id, text: текст модуля, requireMap:map}
     * @param name имя модуля. Полное имя vfs-пути модуля, включая расширение
     * @param id id модуля
     * @param requireMap соответствие между именем используемым в require и реальным модулем,
     *                   например: {'./m1':'my/module/js/m1.js'}
     * @param modFunc функция тела модуля c параметрами:
     *                (exports, require, module, __filename, __dirname).
     *                Если параметр строка, то преобразуется в функцию.
     * @return {*} ссылка на модуль
     */
    function moduleDef(name, id, requireMap, modFunc) {
        let _name = name
        let _requireMap = requireMap
        let _modFunc = modFunc
        let _id = id

        if (typeof _name !== 'string') {
            _requireMap = _name.requireMap
            _modFunc = _name.text
            _name = _name.name
            _id = _name.id
        }

        let module = modules[_name]
        if (module) {
            return module
        }

        if (typeof _modFunc === 'string') {
            let text = _modFunc + "\n//# sourceURL=jc-jsa:///" + _name;
            try {
                _modFunc = new Function('exports,require,module,__filename,__dirname', text)
            } catch(e) {
                console.error("Error in module", "jc-jsa:///" + _name);
                throw e;
            }
        }

        // создаем новый модуль
        modules[_name] = module = {
            name: _name,
            id: _id,
            requireMap: _requireMap || {},
            modFunc: _modFunc,
            exports: {}
        }

        modulesByOrder.push(module)

        if (_id) {
            modulesById[_id] = module
        }

        return module
    }

    /**
     * Функция require
     * @param name имя модуля или id модуля
     * @return exports модуля
     */
    function require(name) {

        let module = modulesById[name]
        if (!module) {
            module = modules[name]
            if (!module) {
                throw new Error('module [' + name + '] not defined')
            }
        }
        if (module.loaded) {
            // уже был выполнен require для этого модуля
            return module.exports
        }

        // эта функция используется для require внутри модуля
        // она обеспечивает поддержку requireMap
        let internalRequire = function(name) {
            let newName = module.requireMap[name] || name
            if (Array.isArray(newName)) {
                let last = {};
                for (let m of newName) {
                    last = require(m)
                }
                return last
            }
            return require(newName)
        }

        // дабы избежать возможных циклических ссылок
        module.loaded = true

        // вызываем функцию модуля
        module.modFunc.call(module.exports, module.exports, internalRequire, module, module.name, module.name)

        internalRequire = null

        return module.exports
    }

    /**
     * Возвращает все зарегистрированные модули
     */
    function getModules() {
        return modules;
    }

    /**
     * Делает require для всех модулей, для которых это еще не было выполнено
     */
    function requireAll() {
        for (let i = modulesByOrder.length - 1; i >= 0; i--) {
            let module = modulesByOrder[i]
            if (!module.loaded) {
                require(module.name)
            }
        }
    }

    function appendCssTag(css, filename) {
        let styleTag = document.createElement("style");
        styleTag.rel = 'stylesheet'
        styleTag.type = 'text/css'
        cssIdx++;
        if (filename) {
            css = css + "\n/*# sourceURL=jc-jsa:///inline-styles/[" + cssIdx + "]/" + filename + "*/";
        } else {
            css = css + "\n/*# sourceURL=jc-jsa:///inline-styles/style-" + cssIdx + ".css*/";
        }
        styleTag.innerHTML = css;
        document.head.appendChild(styleTag);
    }

    /**
     * Подключение css
     * @param css если строка - считается именем модуля css, если нет '{',
     * иначе - текстом css. Если объект, то это содержимое модуля css
     * в формате {text:cssText, requires:[], css: true}
     */
    function requireCss(css) {
        let _css = css
        if (typeof _css === 'string') {
            if (_css.indexOf('{') === -1) {
                // это модуль
                _css = require(_css)
            } else {
                // это текст css
                _css = {text: css, requires: [], css: true}
            }
        }

        if (!(_css.css && _css.text)) {
            return // это не css
        }

        // уже было использовано
        if (_css._used) {
            return
        }

        _css._used = true

        // сначала все зависимые
        if (_css.requires) {
            for (let req of _css.requires) {
                requireCss(req)
            }
        }

        appendCssTag(_css.text, _css.filename)
    }

    /**
     * Поиск подуля по имени или id
     * @param name
     * @return {*} модуль или null
     */
    function findModule(name) {
        let module = modulesById[name]
        if (!module) {
            module = modules[name]
        }
        return module
    }

    // глобализируем
    Jc.require = require
    Jc.requireAll = requireAll
    Jc.requireCss = requireCss
    Jc.moduleDef = moduleDef
    Jc.getModules = getModules
    Jc.findModule = findModule
    Jc.baseUrl = '/';
    window.require = require
})();
