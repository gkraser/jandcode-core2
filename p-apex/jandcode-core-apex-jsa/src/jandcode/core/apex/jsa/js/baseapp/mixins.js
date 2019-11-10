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
         * Владелец. Компонент-приложение.
         */
        own() {
            let own = this.$parent
            if (!own) {
                throw new Error("baseapp не может быть без родителя")
            }
            return own
        }
    },

    methods: {

        /**
         * Выполнить метод cmd с аргументами args
         * у владельца компонента, если у него есть такой метод.
         * Если у него нету а есть у меня - выполнить мой.
         *
         * @param cmd
         * @param args
         * @return {*}
         */
        callOwn(cmd, ...args) {
            if (this.own[cmd]) {
                return this.own[cmd](...args)
            }
            if (this[cmd]) {
                return this[cmd](...args)
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

