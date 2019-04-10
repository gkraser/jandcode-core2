package jandcode.core;

import jandcode.commons.conf.*;
import jandcode.commons.named.*;

/**
 * Конфигурация бина.
 * Экземпляр этого объекта используется при конфигурировании бина.
 */
public interface BeanConfig extends IConfLink, INamed {

    /**
     * Рекомендуемое имя бина.
     * Если создаваемый бин может иметь имя, то это рекомендация ему,
     * какое имя взять.
     * <p>
     * Имя из {@link IConfLink#getConf()} лучше не использовать, т.к. вероятность того,
     * что имя узла будет суррогатным очень велико.
     */
    String getName();

    /**
     * При значении false конфигурация не имеет имени.
     */
    boolean hasName();

    /**
     * Ссылка на конфигурацию.
     * Всегда существует.
     * <p>
     * Велика вероятность, что это ссылка на разделяемую конфигурацию,
     * если конечно явно не обозначено для определенных случаев.
     * Поэтому изменения в нее лучше не вносить.
     */
    Conf getConf();

    /**
     * Имеется ли явно определенная conf.
     * true - да, false - conf была создана автоматически.
     */
    boolean hasConf();

    /**
     * Имя класса из атрибута class в conf.
     * Если нету, возвращается пустая строка.
     */
    String getConfClassName();

}