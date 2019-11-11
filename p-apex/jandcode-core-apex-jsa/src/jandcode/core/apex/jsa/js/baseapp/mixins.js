/**
 * mixin для подключения в BaseApp
 * @type {{}}
 */
export let BaseAppMixin = {

    props: {
        title: {
            default: 'Без заголовка'
        },
        icon: {
            default: 'app-logo'
        }
    },

    computed: {

        /**
         * Компонент приложение.
         */
        app() {
            let a = this.$parent
            if (!a) {
                throw new Error("baseapp не может быть без родителя")
            }
            return a
        }
    },

    methods: {

        /**
         * Выполнить метод methodName с аргументами args
         * у приложения, если у него есть такой метод.
         * Если у него нету а есть у меня - выполнить мой.
         *
         * @param methodName
         * @param args
         * @return {*}
         */
        callApp(methodName, ...args) {
            if (this.app[methodName]) {
                return this.app[methodName](...args)
            }
            if (this[methodName]) {
                return this[methodName](...args)
            }
        }
    }

}

/**
 * mixin для подключения в App, которое использует BaseApp
 * @type {{}}
 */
export let AppMixin = {

    computed: {

        /**
         * Ссылка на компонент baseApp.
         * Он либо помечен ref='baseApp', либо просто возвращает первого ребенка.
         */
        baseApp() {
            let a = this.$refs.baseApp
            if (!a) {
                a = this.$children[0]
            }
            return a
        }

    },

}

