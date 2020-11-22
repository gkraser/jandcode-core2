/**
 * Базовый предок для декораторов приложения и фреймов.
 */
export default {

    props: {
        /**
         * Ссылка на оформляемый компонент
         */
        own: {
            default: function() {
                return this.$parent;
            }
        }
    },

    computed: {

        /**
         * Есть ли иконка
         */
        hasIcon() {
            return this.own.icon && !this.own.icon.startsWith(' ')
        },

        /**
         * Есть ли заголовок
         */
        hasTitle() {
            return !!this.own.title
        }

    }

}
