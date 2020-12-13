//
import {apx, echarts} from './vendor'

/**
 * api объекта, который можно передать в качестве options в jc-chart
 */
let API_Chart_Options = {
    getOptions: function() {},
    setChartInst: function(chartInst, compInst) {},
    destroyChartInst: function(chartInst, compInst) {},
}

/**
 * Построитель диаграмм.
 * Экземпляры таких классов можно передавать в параметр options
 * компонента jc-chart. Т.е. - это фактически и есть диаграмма.
 */
export class ChartBuilder {

    constructor(params) {
        // копия параметров
        this.params = apx.jsaBase.extend({}, params)
        // конфигурация для echarts, доступена через getOptions()
        this.__options = null
        // цвета
        this.colors = {
            base_1: '#BF5C65',
            base_2: '#59B06B',
            base_3: '#2B92BF',
        }
        // утилиты для дат от Quasar
        this.q_date = apx.Quasar.date
    }

    /**
     * Конфигурация для echarts.
     * Строится в момент первого вызова в методе onBuild
     * @return {Object}
     */
    getOptions() {
        if (!this.__options) {
            this.__options = {}
            this.onInit()
            this.onBuild()
        }
        return this.__options
    }

    /**
     * Первоначальная инициализация.
     * Вызывается автоматически при обращении к getOptions() перед onBuild().
     * Тут обычно настраивают разные свойства самого builder.
     */
    onInit() {
    }

    /**
     * Построение конфигурации echarts.
     * В теле метода доступен объект с опциями echarts getOptions().
     * Нужно его заполнить для формирования диаграммы.
     */
    onBuild() {
    }

    /**
     * Метод будет вызван в jc-chart после создания экземпляра echarts.
     * Тут можно настроить обработчики событий.
     * Следует иметь ввиду, что метод может быть вызван несколько раз для
     * разных экземпляров echarts и vue, т.к. vue может рендерить компоненты по своему
     * усмотрению. Так что сохранять chartInst и compInst в свойствах для дальнейшего
     * использования бессмысленно.
     *
     * @param chartInst Экземпляр echarts
     * @param compInst Vue-экземпляр jc-chart
     */
    setChartInst(chartInst, compInst) {
    }

    /**
     * Метод будет вызван в jc-chart перед уничтожением экземпляра echarts.
     * Тут можно почистить за собой, если намусорили.
     * Следует иметь ввиду, что метод может быть вызван несколько раз для
     * разных экземпляров echarts и vue, т.к. vue может рендерить компоненты по своему
     * усмотрению.
     *
     * @param chartInst Экземпляр echarts
     * @param compInst Vue-экземпляр jc-chart
     */
    destroyChartInst(chartInst, compInst) {
    }

    /**
     * Обновить все или несколько параметров.
     * После вызова этого метода, getOptions() будет пересчитано заново.
     * Используется для имитации реактивности, например в тестах.
     *
     * @param params {Object} новые значения параметров
     */
    updateParams(params) {
        apx.jsaBase.extend(this.params, params)
        this.__options = null
    }

    ////// options utils

    /**
     * Добавляет элемент в свойство конфигурации propName.
     * Например: this.add('xAxis',{...}).
     * Если не существует - создается как массив.
     * Если существует как объект, то из него делается массив.
     *
     * @param propName имя свойства
     * @param config конфигурация
     * @return {*} параметр config
     */
    add(propName, config) {
        let opt = this.getOptions()
        if (!apx.jsaBase.isArray(opt[propName])) {
            if (apx.jsaBase.isObject(opt[propName])) {
                opt[propName] = [opt[propName]]
            } else {
                opt[propName] = []
            }
        }
        opt[propName].push(config)
        return config
    }

    /**
     * Добавляет grid вместе с осями. Для наглядности.
     * Для осей назначаются id: gridid-xXXX, gridid-yXXX...
     *
     * В свойстве axises - конфигурации осей. Выбираются все свойства, которые
     * начинаются на 'x' (для xAxis) или 'y' (для yAxis).
     * Порядок добавления осей (на примере x): x,x0,x1,x2,x3...x9.
     * Если какой то номер пропущен, игнорируем и переходим к следующему.
     * После этого добавляются все остальные xXXX в произвольном порядке.
     * Это нужно для предсказуемого значения xAxisIndex и yAxisIndex.
     *
     * @param gridConfig конфигурация гриды. Должен быть id
     * @param axises конфигурации осей x,y
     * @param axises.xXXX конфигурация оси x, если свойство начинается с 'x'
     * @param axises.yYYY конфигурация оси y, если свойство начинается с 'y'
     *
     * @return параметр gridConfig
     */
    addGrid(gridConfig, axises) {
        if (!gridConfig.id) {
            throw new Error('gridConfig.id missing')
        }
        let res = this.add('grid', gridConfig)

        let used = {}

        let p = (prop, optName) => {
            if (axises[prop] && !used[prop]) {
                used[prop] = true
                let cfg = apx.jsaBase.extend({}, axises[prop])
                cfg.id = gridConfig.id + '-' + prop
                cfg.gridId = gridConfig.id
                this.add(optName, cfg)
            }
        }

        if (axises) {
            p('x', 'xAxis')
            p('y', 'yAxis')
            for (let i = 0; i < 10; i++) {
                p('x' + i, 'xAxis')
                p('y' + i, 'yAxis')
            }
            for (let pn in axises) {
                if (pn.startsWith('x')) {
                    p(pn, 'xAxis')
                } else if (pn.startsWith('y')) {
                    p(pn, 'yAxis')
                }
            }
        }

        return res
    }

    /**
     * Расставляет gridIndex, xAxisIndex, yAxisIndex, datasetIndex в config,
     * если они есть в opt. Кроме того и xxxId свойства.
     * +show +id
     * @param config
     * @param opt
     * @return config
     */
    withOpt(config, opt) {
        if (apx.jsaBase.isObject(opt)) {

            function p1(prop, suff) {
                let nm = prop + suff
                if (nm in opt) {
                    config[nm] = opt[nm]
                }
            }

            function p(prop) {
                p1(prop, 'Index')
                p1(prop, 'Id')
            }

            p('grid')
            p('xAxis')
            p('yAxis')
            p('dataset')
            p1('show', '')
            p1('id', '')

        }
        return config
    }

    ////// утилиты

    /**
     * Набор цветов с префиксом.
     * Цвета берутся из colors, например для colorsPrefix('base_') -> 'base_0', 'base_1'...
     * @return {[]}
     */
    colorsPrefix(prefix) {
        let res = []
        for (let i = 0; i < 20; i++) {
            let color = this.colors[prefix + i]
            if (color) {
                res.push(color)
            }
        }
        return res
    }

    /**
     * Нормализовать null в dataset.
     * Если предполагается, что значение поля может быть, а может и нет,
     * то в случае отсутствия поля оно все равно должно быть со значением null,
     * иначе echarts начинает гнать. Например в таком случае:
     * [{a:1},{a:2,b:2},{a:3}] при построении для b начнется неопределенность.
     * Метод приводит такой dataset к виду: [{a:1,b:null},{a:2,b:2},{a:3,b:null}]
     * @param {Object[]} dataset  какой dataset обрабатывать
     * @param {String[]} addFields дополнительные поля, которые точно должны быть
     */
    datasetNormalizeNull(dataset, addFields = null) {
        let fields = {}
        if (addFields) {
            for (let f of addFields) {
                fields[f] = true
            }
        }
        // собираем все поля
        for (let rec of dataset) {
            for (let f in rec) {
                if (!(f in fields)) {
                    fields[f] = true
                }
            }
        }
        // исправляем undefined
        for (let rec of dataset) {
            for (let f in fields) {
                if (rec[f] === void 0) {
                    rec[f] = null
                }
            }
        }
    }

    //todo скорее всего это не нужно, нужно форматировать, а не округлять...
    /**
     * Округление данных в dataset
     * @param dataset данные
     * @param precision сколько цифр после запятой оставить
     * @param fields для каких полей
     */
    datasetNumberRound(dataset, precision, fields) {
        for (let rec of dataset) {
            for (let f of fields) {
                let v = rec[f]
                if (v != null) {
                    v = echarts.number.round(v, precision, false)
                    rec[f] = v
                }
            }
        }
    }

}

/**
 * Обертка вокруг ChartBuilder.
 * Используется как базовый класс для helper-классов построения
 * диаграмм. Такие классы содержат различные утилиты.
 */
export class ChartBuilderHelper {

    /**
     * Конструктор
     * @param builder {ChartBuilder} ссылка на builder
     * @param params {Object} параметры. Все ключи становятся свойствами экземпляра.
     */
    constructor(builder, params) {
        /**
         * Ссылка на ChartBuilder
         * @type ChartBuilder
         */
        this.builder = builder

        /**
         * Параметры, переданные в конструкторе
         */
        this.params = apx.jsaBase.extend({}, params)

    }

}