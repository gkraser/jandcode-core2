/**
 * Простейший контейнер для popup.
 */
export default {

    name: 'jc-popup-container',

    render(h) {
        return h('div', {'class': this.cn()}, [
            h('div', {ref: 'popupMountPlace'})
        ])
    },

    methods: {

        /**
         * Монтирование компонента в контейнер
         * @param el элемент, который popup
         */
        popupMount(el) {
            this.$refs.popupMountPlace.appendChild(el)
        }
    }

}
