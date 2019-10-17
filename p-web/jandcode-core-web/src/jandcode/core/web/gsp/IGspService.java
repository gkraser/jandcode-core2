package jandcode.core.web.gsp;

import jandcode.commons.groovy.*;
import jandcode.commons.named.*;
import org.apache.commons.vfs2.*;

/**
 * Интерфейс сервиса для gsp
 */
public interface IGspService extends IGspFactory {

    /**
     * Зарегистрированные gsp
     */
    NamedList<GspDef> getGsps();

    /**
     * Виртуальный путь до файла шаблона в формате ресурсов.
     * Может быть пустым, если шаблон не определен в файле (например это просто класс)
     * или лежит вне виртуального web-каталога.
     */
    String getGspPath(String gspName);

    /**
     * Возвращает исходный текст gsp, если доступен.
     * Если недоступен - пустую строку.
     */
    String getGspSourceText(String gspName);

    /**
     * Возвращает исходный текст groovy-класса gsp, если доступен.
     * Если недоступен - пустую строку.
     */
    String getGspClassText(String gspName);

    /**
     * Компилировать gsp-файл. Для внутреннего использования.
     */
    GroovyClazz compileGsp(FileObject gspFile) throws Exception;

    /**
     * Создать новый контекст для рендеринга gsp
     */
    GspContext createGspContext();

}
