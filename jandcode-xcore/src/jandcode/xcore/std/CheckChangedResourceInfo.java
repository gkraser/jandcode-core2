package jandcode.xcore.std;

/**
 * Параметр для CheckChangedResourceService
 */
public interface CheckChangedResourceInfo {

    /**
     * Возвращает true, если при проверке измененных ресурсов принято
     * решение "приложение лучше перезагрузить, серьезные изменения"
     */
    boolean isNeedRestartApp();

    /**
     * Установка значения true для {@link CheckChangedResourceInfo#isNeedRestartApp()}.
     * Вызывается в сервисах, которые проверяют ресурсы.
     */
    void needRestartApp();


}
