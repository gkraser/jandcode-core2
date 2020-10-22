export * from './cfg-store'

import {jsaBase} from '../vendor'
import {CfgStoreService} from './app/app-service-cfg-store'

// инициализируем приложение

jsaBase.app.registerService(CfgStoreService)
