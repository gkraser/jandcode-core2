package jandcode.jc;

/**
 * Сервисы ctx
 */
public interface IServices {

    /**
     * Получить сервис по классу. При первом обращении - сервис создается.
     */
    <A extends ICtxService> A service(Class<A> serviceClass);

    /**
     * Получить сервис по имени класса. При первом обращении - сервис создается.
     */
    Object service(String serviceClassName);

}
