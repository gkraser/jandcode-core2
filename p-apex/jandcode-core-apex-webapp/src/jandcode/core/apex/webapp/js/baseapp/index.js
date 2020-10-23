import JcDecor from './JcDecor'
import JcDecorApp from './JcDecorApp'
import JcDecorFrame from './JcDecorFrame'
import JcApp from './JcApp'
import JcFrame from './JcFrame'
import * as frame from './frame'
//
import JcDecorAppStd from '../components/decor/JcDecorAppStd'
import JcDecorFramePage from '../components/decor/JcDecorFramePage'
import JcDecorFrameDialog from '../components/decor/JcDecorFrameDialog'

frame.componentHolder.set('app.App', JcDecorAppStd)
frame.componentHolder.set('frame.Page', JcDecorFramePage)
frame.componentHolder.set('frame.Dialog', JcDecorFrameDialog)

//
export {
    JcDecor,
    JcDecorApp,
    JcDecorFrame,
    JcApp,
    JcFrame,
    frame,
}

// на верхний уровень
export {
    showDialog
} from './frame'
