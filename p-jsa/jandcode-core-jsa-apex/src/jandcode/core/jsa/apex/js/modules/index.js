export * from './dao'
export * from './cfg-store'
export * from './app'

// инициализируем приложение
import {app} from './app'

import AppPluginCfgStore from './app/app-plugin-cfg-store'


app.use(AppPluginCfgStore)
