package jandcode.core.std;

import jandcode.core.*;

/**
 * Сервис каталогов с даннымы.
 * <p>
 * Для каждого имени, запрашиваемого через getDataDir можно настроить путь в conf.
 */
public interface DataDirService extends Comp {

    /**
     * По имени возвращает физический путь до каталога.
     *
     * @param name имя пути
     * @return физический путь. Каталог автоматически будет создан, если не существует
     */
    String getPath(String name);

    /**
     * Возвращает каталог localPath относительно пути с именем name
     */
    String getPath(String name, String localPath);

}
