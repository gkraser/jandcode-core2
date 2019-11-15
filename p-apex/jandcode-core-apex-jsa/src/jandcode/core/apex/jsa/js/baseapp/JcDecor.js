/**
 * Базовый предок для декораторов приложения и фреймов.
 */
export default {

    computed: {

        /**
         * Ссылка на оформляемый компонент
         */
        own() {
            return this.$parent
        },

        /**
         * Есть ли иконка
         */
        hasIcon() {
            return this.own.icon && !this.own.icon.startsWith(' ')
        }

    }

}
