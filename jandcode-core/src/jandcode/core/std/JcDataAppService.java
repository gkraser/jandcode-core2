package jandcode.core.std;

import jandcode.commons.vdir.*;
import jandcode.core.*;

/**
 * Сервис для доступа к каталогам jc-data.
 * В виртуальный каталог входят только jc-data из модулей приложения.
 */
public interface JcDataAppService extends Comp, IVDirWrap {

    /**
     * Имя каталога с данными jc
     */
    String JC_DATA_DIR = "jc-data";

    /**
     * Путь ресурса в jar с данными jc
     */
    String JC_DATA_RESOURCE = "META-INF/" + JC_DATA_DIR;

}
