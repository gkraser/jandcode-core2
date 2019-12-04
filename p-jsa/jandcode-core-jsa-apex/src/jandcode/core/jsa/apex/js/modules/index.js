export * from './dao'
export * from './cfg-store'
export * from './app'
export * from './msgbox'

// инициализируем приложение
import {app} from './app'

import {CfgStoreService} from './app/app-service-cfg-store'
import {ErrorHandlersService} from './app/app-service-error-handlers'

app.registerService(ErrorHandlersService)
app.registerService(CfgStoreService)
