/**
 * Взято из QIcon 1.3.xxx (next)
 * Поддержка iconMapFn (preview)
 */
function QIcon_getter__type() {
    let cls
    let icon = this.name

    if (!icon) {
        return {
            cls: void 0,
            content: void 0
        }
    }

    const commonCls = 'q-icon' +
        (this.left === true ? ' on-left' : '') +
        (this.right === true ? ' on-right' : '') +
        (this.color !== void 0 ? ` text-${this.color}` : '')

    if (this.$q.iconMapFn !== void 0) {
        const res = this.$q.iconMapFn(icon)
        if (res !== void 0) {
            if (res.icon !== void 0) {
                icon = res.icon
            } else {
                return {
                    cls: res.cls + ' ' + commonCls,
                    content: res.content !== void 0
                        ? res.content
                        : ' '
                }
            }
        }
    }

    if (icon.startsWith('img:') === true) {
        return {
            img: true,
            cls: commonCls,
            src: icon.substring(4)
        }
    }

    let content = ' '

    if (/^fa[s|r|l|b|d]{0,1} /.test(icon) || icon.startsWith('icon-') === true) {
        cls = icon
    } else if (icon.startsWith('bt-') === true) {
        cls = `bt ${icon}`
    } else if (icon.startsWith('eva-') === true) {
        cls = `eva ${icon}`
    } else if (/^ion-(md|ios|logo)/.test(icon) === true) {
        cls = `ionicons ${icon}`
    } else if (icon.startsWith('ion-') === true) {
        cls = `ionicons ion-${this.$q.platform.is.ios === true ? 'ios' : 'md'}${icon.substr(3)}`
    } else if (icon.startsWith('mdi-') === true) {
        cls = `mdi ${icon}`
    } else if (icon.startsWith('iconfont ') === true) {
        cls = `${icon}`
    } else if (icon.startsWith('ti-') === true) {
        cls = `themify-icon ${icon}`
    } else {
        cls = 'material-icons'

        if (icon.startsWith('o_') === true) {
            icon = icon.substring(2)
            cls += '-outlined'
        } else if (icon.startsWith('r_') === true) {
            icon = icon.substring(2)
            cls += '-round'
        } else if (icon.startsWith('s_') === true) {
            icon = icon.substring(2)
            cls += '-sharp'
        }

        content = icon
    }

    return {
        cls: cls + ' ' + commonCls,
        content
    }
}


////// fix

let quasar = require('quasar')

Object.defineProperty(quasar.QIcon.prototype, "type", {
    get: QIcon_getter__type
});
