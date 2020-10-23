package jandcode.core.web;

import jandcode.commons.datetime.*;
import jandcode.commons.variant.*;
import jandcode.core.*;
import jandcode.core.web.action.*;
import jandcode.core.web.render.*;

import javax.servlet.http.*;
import java.io.*;
import java.util.*;

/**
 * Обертка над http-запросом, http-ответом и http-сервлетом.
 */
public interface Request extends IAppLink {

    /**
     * Оригинальный HttpServletRequest
     */
    HttpServletRequest getHttpRequest();

    /**
     * Оригинальный HttpServletResponse
     */
    HttpServletResponse getHttpResponse();

    /**
     * Оригинальный HttpServlet
     */
    HttpServlet getHttpServlet();

    //////

    /**
     * Путь в url. Получен из javax.servlet.http.HttpServletRequest#getPathInfo().
     * Путь нормализуется: удалятся дублирующиеся слеши и слеши в начале
     * и конце. К примеру оригинальный pathInfo '/' превратится в пустую строку.
     */
    String getPathInfo();

    /**
     * Установить pathinfo. Используется, крайне редко, в сложных фильтрах для
     * модификации пути запроса. Записывается как есть, без нормализации.
     */
    void setPathInfo(String pathInfo);

    /**
     * Параметры запроса. Собираются из HttpServletRequest и более связи
     * с ним не имеют. Оригинальные параметры HttpServletRequest не трогаются.
     */
    IVariantMap getParams();

    /**
     * Атрибуты сессии. Является оберткой вокругоригинальных атрибутов сессии
     * из HttpServletRequest. Модификация приводит к модификации оригинального
     * набора атрибутов.
     */
    IVariantMap getSession();

    //////

    /**
     * Возвращает writer, куда нужно писать текст.
     */
    Writer getOutWriter();

    /**
     * Возвращает stream, куда нужно писать бинарные данные
     */
    OutputStream getOutStream();

    /**
     * Был ли уже запрошен поток вывода writer или stream
     */
    boolean isOutGet();

    /**
     * Установить объект, который будет являтся ответом на запрос.
     * Этот объект будет отрендерен.
     *
     * @param data любой объект, который можно отрисовать с использованием
     *             механизма рендеринга
     */
    void render(Object data);

    /**
     * Объект для рендеринга,
     * установленный в {@link Request#render(java.lang.Object)}
     */
    Object getRenderData();

    /**
     * Установить тип контента ответа mime.
     */
    void setContentType(String type);

    /**
     * Выполнялся ли метод setContentType
     */
    boolean isContentTypeAssigned();

    //////

    /**
     * Виртуальный root. Если установлен, то в функции ref() он будет добавлен
     * к любому виртуальному пути. Для получения ref() без этого виртуального корня
     * имеется функция ref() с явным требованием включения/исключения виртуального пути.
     */
    String getVrtualRoot();

    /**
     * Установить виртуальный root. Используется, крайне редко, в сложных фильтрах для
     * модификации пути запроса.
     */
    void setVrtualRoot(String vrtualRoot);


    ////// ref

    /**
     * Формирование url-адреса для ссылок.
     *
     * @param url         Либо часть url без contextPath, либо полный url
     *                    (с префиксом, например <code>http://</code>).
     *                    Может включать явные параметры через '?'
     * @param virtualRoot при значении true в url добавляется значение request.virtualRoot
     * @param params      параметры
     */
    String ref(String url, boolean virtualRoot, Map params);

    /**
     * Формирование url-адреса для ссылок. Автоматически добавляется request.virtualRoot,
     * если он установлен.
     *
     * @param url    Либо часть url без contextPath, либо полный url
     *               (с префиксом, например <code>http://</code>).
     *               Может включать явные параметры через '?'
     * @param params параметры
     */
    String ref(String url, Map params);

    /**
     * Формирование url-адреса для ссылок. Автоматически добавляется request.virtualRoot,
     * если он установлен.
     *
     * @param url Либо часть url без contextPath, либо полный url
     *            (с префиксом, например <code>http://</code>).
     *            Может включать явные параметры через '?'
     */
    String ref(String url);

    ////// redirect

    /**
     * redirect на указанный url.
     * Адрес формируется методом {@link Request#ref(java.lang.String, boolean, java.util.Map)}
     */
    void redirect(String url, boolean virtualRoot, Map params);

    /**
     * redirect на указанный url.
     * Адрес формируется методом {@link Request#ref(java.lang.String, java.util.Map)}
     */
    void redirect(String url, Map params);

    /**
     * {@link Request#redirect(java.lang.String, java.util.Map)}, где
     * <code>params=null</code>.
     */
    void redirect(String url);

    ////// utils

    /**
     * Проверить, что ресурс не изменился.
     *
     * @param curDate текущая дата модификации. Если она не старше, чем последняя дата
     *                в самом запросе, то генерится 304 код. Эта дата прописывается в
     *                заголовок ответа как 'Last-Modified'.
     */
    void checkModified(XDateTime curDate);

    /**
     * Проверить, что ресурс не изменился.
     *
     * @param etag ETag. Если он не отличается от того, что в запросе,
     *             то генерится 304 код. Этот etag прописывается в
     *             заголовок ответа как 'ETag'.
     */
    void checkETag(String etag);

    /**
     * Время начала выполнения запроса.
     */
    long getStartTime();

    //////

    /**
     * Какая action будет выполнена
     */
    IAction getAction();

    /**
     * Установить action.
     * Используется крайне редко для очень специфических случаев.
     */
    void setAction(IAction action);

    //////

    /**
     * Какой render будет использован для выполнения рендеринга.
     * Известен только после выполнения action.
     */
    IRender getRender();

    /**
     * Установить render.
     * Используется крайне редко для очень специфических случаев.
     */
    void setRender(IRender render);


    //////

    /**
     * Установить Exception, которая возникла в процессе обработки, но была
     * скрыта по каким то причинам. Например при ответе json возникающие
     * ошибки скрываются и кодируются в json-ответе.
     */
    void setException(Throwable exception);

    /**
     * Получить Exception, установленную методом setException
     */
    Throwable getException();

    //////

    /**
     * Установить значение заголовка ответа.
     * Заголовок сразу не устанавливается в реальный ответ, только после получения
     * потока вывода. Если поток вывода уже был запрошен, то устанавливается сразу.
     */
    void setHeader(String name, String value);

    /**
     * Получить значение заголовка запроса.
     * Если такого заголовка нет, возвращается null.
     */
    String getHeader(String name);

    //////

    /**
     * Признак ajax запроса
     */
    boolean isAjax();

    //////

    /**
     * Произвольные атрибуты запроса. Используются для передачи состояния в процессе
     * работы запроса.
     */
    IVariantMap getAttrs();

    /**
     * Возвращает следующую уникальную id для использования в качестве уникальной строки.
     * <p>
     * Полученная id сессионно уникальна - вероятность генерации дублирующего id для
     * одного и того же клиента в процеесе взаимодействия с сервером - ничтожно мала,
     * в пределах запроса - id гарантировано уникальна.
     *
     * @param prefix префикс id. На уникальность не влияет, просто для идентификации
     *               различных групп id. Если не указать, id будет без префикса.
     */
    String nextId(String prefix);

    //////

    /**
     * Установить заголовки http для запрета кеширования
     */
    void disableCache();


    ////// parts (multipart/form-data)

    /**
     * Возвращает {@link Part} из запроса.
     * см. {@link HttpServletRequest#getPart(java.lang.String)}
     */
    Part getPart(String name);

    /**
     * Возвращает коллекцию {@link Part} из запроса.
     * см. {@link HttpServletRequest#getParts()}
     */
    Collection<Part> getParts();

    //////

    /**
     * Контекст запроса
     */
    RequestContext getContext();

}