export * from './cfg-store'
export * from './msgbox'
export * from './dao'

import {jsaBase} from '../vendor'

import {CfgStoreService} from './app/app-service-cfg-store'
import {ErrorHandlersService} from './app/app-service-error-handlers'
import {WaitUIService} from './app/app-service-wait-ui'
import {EventBusService} from './app/app-service-event-bus'

// инициализируем приложение

jsaBase.app.registerService(ErrorHandlersService)
jsaBase.app.registerService(EventBusService)
jsaBase.app.registerService(WaitUIService)
jsaBase.app.registerService(CfgStoreService)
