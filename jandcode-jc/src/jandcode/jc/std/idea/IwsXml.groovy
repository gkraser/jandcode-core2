package jandcode.jc.std.idea

import jandcode.commons.*
import jandcode.commons.simxml.*
import jandcode.jc.*

/**
 * Класс для iws представления xml
 */
class IwsXml extends BaseXml {
    IwsXml(Project project, SimXml root) {
        super(project, root)
    }

    /**
     * Добавить конфигурацию запуска по умолчанию.
     * name и factory специфична для каждого случая, нужно смотреть в iws после
     * ручной модификации.
     *
     * @param type тип
     * @param factoryName фабрика
     * @return добавленная или переиспользуемая конфигурация, можно настраивать далее
     */
    SimXml addDefaultRunConfig(String type, String factoryName) {
        String key1 = "component@name=RunManager/configuration@type=${type}"
        def x1 = root.findChild(key1, true)
        x1['default'] = true
        if (!UtString.empty(factoryName)) {
            x1['factoryName'] = factoryName
        }
        return x1
    }

}
