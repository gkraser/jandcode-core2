/* Базовая реализация action
----------------------------------------------------------------------------- */

import {jsaBase} from '../vendor'
import JcIcon from '../icon/JcIcon'
import {findCompUp, isComp} from '../../utils/base'

let popupManager = jsaBase.popupManager

export let config = {
    icons: {
        empty: 'empty',
        caretDown: 'caret-down',
        caretRight: 'caret-right',
    },

    delayShowSubMenu: 400,
}

function isMenu(a) {
    return isComp(a, 'menu')
}

/**
 * Ищет самый верхний popup с меню
 * @param from откуда начинать
 */
function findJcPopupTopMenu(from) {
    let cur = from;
    let res = null;
    while (cur) {
        cur = findCompUp(findCompUp(cur, 'menu'), 'popup')
        if (cur) {
            res = cur;
        }
    }
    return res;
}

/**
 * Взято из vue-router: vue-router\src\components\link.js
 */
function guardEvent(e) {
    // don't redirect with control keys
    if (e.metaKey || e.altKey || e.ctrlKey || e.shiftKey) return
    // don't redirect when preventDefault called
    if (e.defaultPrevented) return
    // don't redirect on right click
    if (e.button !== undefined && e.button !== 0) return
    // don't redirect if `target="_blank"`
    if (e.currentTarget && e.currentTarget.getAttribute) {
        const target = e.currentTarget.getAttribute('target')
        if (/\b_blank\b/i.test(target)) return
    }
    // this may be a Weex event which doesn't have this method
    if (e.preventDefault) {
        e.preventDefault()
    }
    return true
}

//////

export default {

    props: {
        icon: {
            type: String,
        },

        text: {
            type: String,
        },

        caretIcon: {
            type: String
        },

        href: {
            type: String
        },

        // route-link: to
        to: {
            type: [String, Object]
        },

        // route-link: replace
        replace: {
            type: Boolean
        },

        /**
         * Произвольные данные привязанные к компоненту
         */
        metaInfo: Object
    },

    data() {
        return {
            submenuShowed: false
        }
    },

    methods: {

        doRenderAction(h, cssAdd) {

            // является ли элементом меню
            let isMenuItem = isMenu(this.$parent)
            // есть ли подменю
            let hasSubMenu = !!this.$slots.default

            let icon = this.icon
            let caretIcon = this.caretIcon
            if (isMenuItem) {
                if (!icon) {
                    icon = config.icons.empty
                }
                if (hasSubMenu) {
                    caretIcon = config.icons.caretRight
                } else {
                    caretIcon = config.icons.empty
                }
            } else {
                if (!caretIcon) {
                    if (hasSubMenu) {
                        caretIcon = config.icons.caretDown
                    }
                }
            }


            let childs = []

            //
            if (icon) {
                let tag = h(JcIcon, {
                    props: {icon: icon}, 'class': [this.cn('icon')]
                })
                childs.push(tag)
            }

            //
            if (this.text) {
                let tag = h('div', {'class': this.cn('text')}, [
                    this.text
                ])
                childs.push(tag)
            }

            if (caretIcon) {
                let tag = h(JcIcon, {
                    props: {icon: caretIcon}, 'class': [this.cn('caret-icon')]
                })
                childs.push(tag)
            }

            if (this.$slots.default) {
                // подменю
                let popupType = isMenuItem ? 'submenu' : 'dropdown';

                let isChildAction = true;
                let t0 = this.$slots.default[0];
                if (this.$slots.default.length === 1) {
                    // только один элемент
                    isChildAction = isComp(t0, 'action')
                }

                let tag

                if (isChildAction) {
                    tag = h('jc-popup', {
                        ref: 'submenu', props: {popupType: popupType},
                        on: {showPopup: this.onShowPopup, closePopup: this.onClosePopup}
                    }, [
                        h('jc-menu', this.$slots.default)
                    ])
                } else {
                    tag = h('jc-popup', {
                        ref: 'submenu', props: {popupType: popupType},
                        on: {showPopup: this.onShowPopup, closePopup: this.onClosePopup}
                    }, [
                        t0
                    ])
                }
                childs.push(tag)
            }


            let onHandlers = {
                click: this.onClick
            }
            if (isMenuItem) {
                onHandlers.mouseenter = this.onMouseEnter;
                onHandlers.mouseleave = this.onMouseLeave;
            }
            //
            let css = [this.cn({submenuShowed: this.submenuShowed})]
            if (cssAdd) {
                css.push(cssAdd)
            }
            //
            let attrs = {}
            if (this.href) {
                attrs.href = this.href
            }
            if (this.to && this.$router) {
                let r = this.$router.resolve(this.to, this.$route, false)
                attrs.href = r.href
            }

            //
            return h('a', {
                'class': css,
                attrs: attrs,
                on: onHandlers
            }, childs)
        },

        onClick(ev) {
            let submenu = this.$refs.submenu;
            if (submenu) {
                if (isMenu(this.$parent)) {
                    submenu.showPopup()
                } else {
                    if (submenu.isShowedPopup()) {
                        submenu.closePopup()
                    } else {
                        submenu.showPopup()
                    }
                }
            } else {
                let p = findJcPopupTopMenu(this)
                if (p) {
                    p.closePopup()
                }

                if (this.to && this.$router && guardEvent(ev)) {
                    let r = this.$router.resolve(this.to, this.$route, false)
                    if (this.replace) {
                        this.$router.replace(r.location)
                    } else {
                        this.$router.push(r.location)
                    }
                }
                this.$emit('click', this)
            }
        },

        onShowPopup() {
            this.submenuShowed = true;
        },

        onClosePopup() {
            this.submenuShowed = false;
        },

        onMouseEnter(ev) {
            this.__mouseEnter = true
            setTimeout(() => {
                this.doShowSubMenuAuto(ev)
            }, config.delayShowSubMenu)
        },

        onMouseLeave(ev) {
            this.__mouseEnter = false
        },

        doShowSubMenuAuto(ev) {
            if (!this.__mouseEnter || this.submenuShowed) {
                return;
            }
            // имитация нажатия мышки на этом элементе!
            popupManager.onMouseDown(ev)
            //
            let submenu = this.$refs.submenu;
            if (submenu) {
                submenu.showPopup()
            }
        }

    }
}

