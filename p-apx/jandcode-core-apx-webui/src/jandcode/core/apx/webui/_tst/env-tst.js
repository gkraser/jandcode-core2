import './vendor'
import {apx} from './vendor'

apx.icons.registerIcons({
    'svg1': 'img:' + __dirname + '/images/cpu.svg',
    'png1': 'img:' + __dirname + '/images/calc.png',
    'folder1': 'img:' + __dirname + '/images/folder.svg',
    'file1': 'img:' + __dirname + '/images/file.svg',
})

//
import * as vendor from './vendor'

Jc._TST_VENDOR = vendor
Jc._TST_app = vendor.jsaBase.app
//
