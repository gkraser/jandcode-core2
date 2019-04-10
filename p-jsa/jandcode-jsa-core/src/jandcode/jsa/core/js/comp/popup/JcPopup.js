import {jsaBase, Vue} from '../vendor'
import JcPopupContainer from './JcPopupContainer'

let popupManager = jsaBase.popupManager


export default {
    name: 'jc-popup',

    props: {
        popupType: {
            type: String,
            default: 'dropdown'
        },
    },

    data() {
        return {
            contentRendered: false
        }
    },

    render(h) {

        // проверки
        if (!this.$slots.default) {
            throw new Error('popup: слот по умолчанию не определен')
        }

        if (this.$slots.default.length !== 1) {
            throw new Error('popup: только один компонент допустим в слоте по умолчанию')
        }

        let slotDefault = this.$slots.default[0]
        if (!slotDefault.tag) {
            throw new Error('popup: не указан компонент в слоте по умолчанию')
        }

        // div-обертка
        if (this.contentRendered) {
            return h('div', {
                'class': this.cn(),
                style: {display: 'none'},
            }, this.$slots.default);
        } else {
            return h('div', {
                'class': this.cn(),
                style: {display: 'none'},
            });
        }
    },

    beforeDestroy() {
        this.closePopup();
        if (this.popupVm) {
            this.popupVm.$destroy();
        }
        if (this.popupObject) {
            this.popupObject.ownerEl = null
            this.popupObject.popupEl = null
            this.popupObject.onShow = null
            this.popupObject.onClose = null
            this.popupObject = null
        }
        this.popupOwnerEl = null
    },

    methods: {

        /**
         * Возвращает описание контейнера
         */
        getPopupContainerDesc() {
            return {
                component: JcPopupContainer,
                props: {}
            }
        },

        getPopupOwnerEl() {
            if (this.popupOwnerEl) {
                return this.popupOwnerEl;
            }

            // не задано, определяем

            if (this.$el) {
                let p = this.$el.parentElement
                if (p) {
                    return p;
                }
            }

            return null;
        },

        /**
         * Возвращает экземпляр IPopup
         */
        __getPopupObject() {
            if (this.popupObject) {
                return this.popupObject;
            }

            // создаем контейнер
            let cont = this.getPopupContainerDesc()
            let vm = this.popupVm = new Vue({
                render(h) {
                    return h(cont.component, {props: cont.props, ref: 'cont'})
                }
            })
            vm.$mount()

            // забираем то, что будем показывать
            // именно элемент, потому-что div - это не компонент, а его возможно показать
            let popupContentEl = this.$el.firstChild;
            this.$el.removeChild(popupContentEl);

            // монтируем то, что будем показывать
            vm.$refs.cont.popupMount(popupContentEl)

            //
            let p = this.popupObject = popupManager.createPopup()
            p.params.type = this.popupType
            p.popupEl = vm.$refs.cont.$el
            p.ownerEl = this.getPopupOwnerEl()
            p.onShow = () => {
                this.$emit('showPopup', this);
            }
            p.onClose = () => {
                this.$emit('closePopup', this);
            }

            //
            return this.popupObject
        },

        showPopup() {
            if (!this.contentRendered) {
                this.contentRendered = true
            }
            this.$nextTick(() => {
                let p = this.__getPopupObject()
                popupManager.showPopup(p)
            })
        },

        closePopup() {
            let p = this.popupObject;
            if (!p) {
                return
            }
            popupManager.closePopup(p)
        },

        /**
         * Показан ли popup сейчас
         */
        isShowedPopup() {
            if (!this.popupObject) {
                return false;
            }
            return popupManager.isShowed(this.popupObject);
        }

    }
}
