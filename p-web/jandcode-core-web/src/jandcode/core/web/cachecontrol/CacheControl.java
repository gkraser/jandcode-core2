package jandcode.core.web.cachecontrol;

import jandcode.core.*;

/**
 * Описание заголовка Cache-Control для определенных типов файлов.
 */
public interface CacheControl extends Comp {

    /**
     * Маска фпйлов, для которой применяется это правило
     */
    String getMask();

    /**
     * Значение для заголовка ответа Cache-Control.
     * Возможно специальное значение 'etag'.
     * В этом случае кеширование через etag.
     */
    String getCacheControl();

}
