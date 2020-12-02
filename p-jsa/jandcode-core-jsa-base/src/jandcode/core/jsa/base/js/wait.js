/*

Механизм блокировки интерфейса в процессе загрузки

----------------------------------------------------------------------------- */

import cfg from './cfg'
import {nextId} from './base'

let _level = 0
let _delayShowUI = 400
let _showedUI = false
let _timerId
let _mask
let _waitUI

/**
 * Класс для показа wait ui.
 * Подразумевается, что в методе show будет показано
 * что то, что полностью блокирует экран для пользователя.
 */
export class WaitUI {

    constructor() {
    }

    show() {
    }

    hide() {
    }

}

export function waitShow() {
    _level++
    if (_level === 1) {
        // для предотвращения моргания подряд идущих show/hide
        // допустим когда запросы один за другим выполняются
        _level++
        doShow()
    }
}

export function waitHide() {
    if (_level <= 0) {
        if (cfg.envDev) {
            console.warn("waitHide without waitShow");
        }
        // нечего делать
        return
    }
    _level--
    if (_level === 1) {
        // т.к. в waitShow 1 к уровню добавили, тут ее обрабатываем
        setTimeout(() => {
            if (_level === 1) {
                waitHide()
            }
        }, 50)
    }
    if (_level === 0) {
        doHide()
    }
}

export function waitHideForce() {
    if (_level <= 0) {
        // нечего делать
        return
    }
    _level = 0
    doHide()
}

/**
 * Установить отображатель ui для wait.
 * @param waitUI {WaitUI}
 */
export function setWaitUI(waitUI) {
    if (!waitUI) {
        waitUI = new WaitUI()
    }
    _waitUI = waitUI
}

setWaitUI(null)

//////

function doShow() {
    maskScreen()
    _timerId = setTimeout(function() {
        _showedUI = true
        try {
            doShowUI()
        } catch(e) {
            if (cfg.envDev) {
                console.error("wait doShowUI error", e);
            }
        }
    }, _delayShowUI)
}

function doHide() {
    window.clearTimeout(_timerId)
    if (_showedUI) {
        _showedUI = false
        try {
            doHideUI()
        } catch(e) {
            if (cfg.envDev) {
                console.error("wait doHideUI error", e);
            }
        }
    }
    unmaskScreen()
}

function doShowUI() {
    _waitUI.show()
}

function doHideUI() {
    _waitUI.hide()
}

function maskScreen() {
    if (!_mask) {
        _mask = document.createElement("div")
        _mask.id = nextId('jc-mask-')
        let st = _mask.style
        st.position = 'fixed'
        st.left = '0'
        st.top = '0'
        st.width = '100vw'
        st.height = '100vh'
        st.display = 'none'
        st.zIndex = '100500'
        st.backgroundColor = 'white'
        st.opacity = '0'
        document.body.appendChild(_mask)
    }
    _mask.style.display = 'block'
}

function unmaskScreen() {
    if (!_mask) {
        return
    }
    _mask.style.display = 'none'
}
