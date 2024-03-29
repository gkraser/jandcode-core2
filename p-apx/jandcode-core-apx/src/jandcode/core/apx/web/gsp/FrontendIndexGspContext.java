package jandcode.core.apx.web.gsp;

import jandcode.core.web.gsp.*;
import jandcode.core.web.std.gsp.*;

/**
 * Расширение {@link JsIndexGspContext} для работы с каталогом приложения,
 * которое собрано с помошью webpack.
 * <p>
 * Считается, что приложение собрано с помощью модуля @jandcode/tools и следует
 * определенным в нем соглашениям.
 */
public interface FrontendIndexGspContext extends IGspContextLinkSet {

    /**
     * Каталог в виртуальной файловой системе web, в котором собрано webpack
     * приложение. По умолчанию - frontend
     */
    String getSrcPath();

    /**
     * Установить каталог в виртуальной файловой системе web, в котором собрано webpack
     * приложение.
     */
    void setSrcPath(String path);

    /**
     * Путь до файла с манифестом entrypoints, сгенерерованного WebpackAssetsManifest.
     * Путь указывается относительно {@link FrontendIndexGspContext#getSrcPath()}.
     * По умолчанию - entrypoints-manifest.json
     */
    String getEntrypointsManifestPath();

    /**
     * @see FrontendIndexGspContext#getEntrypointsManifestPath()
     */
    void setEntrypointsManifestPath(String path);

    /**
     * Имя библиотеки из конфигурации webpack output.library.name.
     * По умолчанию JcEntry
     */
    String getLibraryName();

    /**
     * @see FrontendIndexGspContext#getLibraryName()
     */
    void setLibraryName(String libraryName);

    /**
     * Добавить ресурсы в {@link JsIndexGspContext#addLink(java.lang.String)}
     * для указанной entrypoint
     */
    void addLink(String entryPoint);

}
