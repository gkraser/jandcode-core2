package jandcode.xcore.std;

import jandcode.xcore.*;

/**
 * Сервис для отслеживания измененных ресурсов и перегрузки их.
 * Вызывать по мере необходимости! Желательно только в отладочном режиме.
 */
public interface CheckChangedResourceService extends Comp {

    /**
     * Проверить измененные ресурсы.
     * Метод дергает приложение и все сервисы,
     * которые реализуют интерфейс ICheckChangedResource.
     *
     * @return собранная информация об результатах проверки измененных ресурсов
     */
    CheckChangedResourceInfo checkChangedResource() throws Exception;

}
