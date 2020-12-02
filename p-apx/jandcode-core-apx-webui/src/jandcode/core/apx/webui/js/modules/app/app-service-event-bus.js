/* Общая шина событий для приложения
----------------------------------------------------------------------------- */

import {Vue, jsaBase} from '../../vendor'

export class EventBusService extends jsaBase.AppService {

    onCreate() {
        this.eventBus = new Vue()

        /**
         * Глобальный Event Bus.
         * Просто vue-компонент.
         *
         * @member {Vue} App#eventBus
         */
        this.app.eventBus = this.eventBus
    }

    onStop() {
        this.eventBus.$destroy()
    }

}


