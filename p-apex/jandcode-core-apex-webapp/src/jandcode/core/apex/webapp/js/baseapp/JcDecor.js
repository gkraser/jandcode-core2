/**
 * Базовый предок для декораторов приложения и фреймов.
 */
export default {

    props: {
        own: {
            default: function() {
                return this.$parent;
            }
        }
    },

    computed: {

        /**
         * Ссылка на оформляемый компонент
         */
        own1() {
            return this.$parent
        },

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
