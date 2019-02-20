package jandcode.core.std;

/**
 * Интерфейс для сервисов, которые владеют ресурсами и могут проверять, не изменились ли
 * они.
 */
public interface ICheckChangedResource {

    /**
     * Проверить измененные ресурсы и перегрузить их при необходимости.
     *
     * @param info для сбора информации об измененных ресурсах
     */
    void checkChangedResource(CheckChangedResourceInfo info) throws Exception;

}
