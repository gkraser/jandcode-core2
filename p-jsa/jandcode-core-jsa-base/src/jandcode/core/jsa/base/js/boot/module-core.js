/*  Модульная система jsa.
----------------------------------------------------------------------------- */

(function() {
    'use strict';

    // глобальное пространство имен Jc
    window.Jc = window.Jc || {}
    window.Jc.cfg = window.Jc.cfg || {}
    let Jc = window.Jc

    // все модули
    let modules = {}

    // модули в порядке объявления
    let modulesByOrder = []

    // все модули по id
    let modulesById = {}

    // для css
    let cssIdx = 0
    let cssPlaces = {}

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
            let text = _modFunc
            if (Jc.cfg.envDev) {
                text = text + "\n//# sourceURL=jc-jsa:///" + _name
            }
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
            let hasStar = name.indexOf('*') !== -1
            let newName = module.requireMap[name] || name
            if (Array.isArray(newName)) {
                let last = {}
                let bundle = hasStar ? [] : null
                for (let m of newName) {
                    last = require(m)
                    if (hasStar) {
                        bundle.push(last)
                    }
                }
                if (hasStar) {
                    return bundle
                } else {
                    return last
                }
            }
            return require(newName)
        }

        // дабы избежать возможных циклических ссылок
        module.loaded = true

        // вызываем функцию модуля
        let dirname = module.name
        let a = dirname.lastIndexOf('/')
        if (a !== -1) {
            dirname = dirname.substring(0, a)
        }
        module.modFunc.call(module.exports, module.exports, internalRequire, module, module.name, dirname)

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

    function appendCssTag(css, filename, place, group) {
        let styleTag = document.createElement("style");
        styleTag.rel = 'stylesheet'
        cssIdx++;
        let cssIdxText = '' + cssIdx
        if (String.prototype.padStart) {
            cssIdxText = cssIdxText.padStart(2, '0')
        }
        if (filename) {
            if (Jc.cfg.envDev) {
                css = css + "\n/*# sourceURL=jc-jsa:///inline-styles/[" + cssIdxText + "]/" + filename + "*/";
                styleTag.dataset.path = filename
            }
        } else {
            if (Jc.cfg.envDev) {
                css = css + "\n/*# sourceURL=jc-jsa:///inline-styles/style-" + cssIdxText + ".css*/";
            }
        }
        if (group) {
            styleTag.dataset.group = group
        }
        styleTag.innerHTML = css;

        let tagPlace = cssPlaces[place]
        if (tagPlace) {
            tagPlace.parentNode.insertBefore(styleTag, tagPlace);
        } else {
            document.head.appendChild(styleTag);
        }
    }

    /**
     * Зарегистрировать место внедрения css-тегов
     * @param place имя
     */
    function defineCssPlace(place) {
        let tag = cssPlaces[place]
        if (tag) {
            return tag
        }
        tag = document.createElement("style");
        tag.rel = 'stylesheet'
        tag.dataset.place = place
        document.head.appendChild(tag);
        cssPlaces[place] = tag
        return tag
    }

    /**
     * Подключение css
     * @param css если строка - считается именем модуля css, если нет '{',
     * иначе - текстом css. Если объект, то это содержимое модуля css
     * в формате {text:cssText, css: true}
     * @param place место вставки
     */
    function requireCss(css, place) {
        if (place) {
            Jc.defineCssPlace(place)
        }
        let _css = css
        if (typeof _css === 'string') {
            if (_css.indexOf('{') === -1) {
                // это модуль
                _css = require(_css)
            } else {
                // это текст css
                _css = {text: css, css: true}
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

        let cssText = _css.text
        if (_css.rebaseUrl) {
            cssText = cssText.replace(new RegExp(_css.rebaseUrl, 'g'), Jc.cfg.baseUrl)
        }

        appendCssTag(cssText, _css.filename, place, _css.group)
    }

    /**
     * Поиск модуля по имени или id
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
    Jc.defineCssPlace = defineCssPlace
    Jc.moduleDef = moduleDef
    Jc.getModules = getModules
    Jc.findModule = findModule
    window.require = require
})();
