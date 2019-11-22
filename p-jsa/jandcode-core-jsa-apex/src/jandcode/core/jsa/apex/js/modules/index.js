export * from './dao'
export * from './cfg-store'
export * from './app'

// инициализируем приложение
import {app} from './app'

// плагины для приложения
import AppPluginCfgStore from './app/app-plugin-cfg-store'
import AppPluginErrorHandlers from './app/app-plugin-error-handlers'


app.use(AppPluginErrorHandlers)
app.use(AppPluginCfgStore)
