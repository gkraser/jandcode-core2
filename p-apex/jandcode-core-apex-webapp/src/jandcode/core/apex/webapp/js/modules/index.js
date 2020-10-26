export * from './cfg-store'
export * from './msgbox'

import {jsaBase} from '../vendor'

import {CfgStoreService} from './app/app-service-cfg-store'
import {ErrorHandlersService} from './app/app-service-error-handlers'

// инициализируем приложение

jsaBase.app.registerService(ErrorHandlersService)
jsaBase.app.registerService(CfgStoreService)
