import JcDecor from './JcDecor'

import JcDecorApp from './JcDecorApp'
import JcDecorAppStd from './JcDecorAppStd'

import JcDecorFrame from './JcDecorFrame'
import JcDecorFramePage from './JcDecorFramePage'
import JcDecorFrameDialog from './JcDecorFrameDialog'

import JcApp from './JcApp'
import JcFrame from './JcFrame'
import * as frame from './frame'

frame.componentHolder.set('app.App', JcDecorAppStd)
frame.componentHolder.set('frame.Page', JcDecorFramePage)
frame.componentHolder.set('frame.Dialog', JcDecorFrameDialog)

export {
    JcDecor,
    JcDecorApp,
    JcDecorAppStd,
    JcDecorFrame,
    JcDecorFramePage,
    JcDecorFrameDialog,
    JcApp,
    JcFrame,
    frame,
}

// на верхний уровень
export {
    showDialog
} from './frame'
