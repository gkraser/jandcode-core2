/**
 * Базовый предок для декораторов приложения
 */
export let JcBaseDecorApp = {

    computed: {

        /**
         * Ссылка на оформляемое приложение
         */
        app() {
            return this.$parent
        }
    }

}

/**
 * Базовый предок для декораторов фреймов
 */
export let JcBaseDecorFrame = {

    computed: {

        /**
         * Ссылка на оформляемый фрейм
         */
        frame() {
            return this.$parent
        }
    }

}

/**
 * Базовый предок для приложений
 */
export let JcBaseApp = {

    data() {
        return {
            title: 'Без заголовка',
            icon: 'app-logo',
        }
    },

    methods: {
        home() {
            if (this.$router) {
                this.$router.push("/", () => {})
            }
        }
    }

}

/**
 * Базовый предок для фреймов
 */
export let JcBaseFrame = {

    data() {
        return {
            title: 'Без заголовка',
            icon: 'frame',
        }
    },

    methods: {}

}