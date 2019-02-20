package jandcode.commons.conf;

/**
 * Раскрывальщик иерархий Conf.
 * <p>
 * Тип - это свойство объекта root. Должен быть Conf. Все его свойства -
 * объекты этого типа. Например:
 * <pre>{@code
 * field {            // тип field
 *     string {}      // объект string типа field
 *     memo {}        // объект memo типа field
 * }
 * domain {           // тип domain
 *     sys {}         // объект sys типа domain
 *     Abonent {}     // объект Abonent типа domain
 * }
 * }</pre>
 * <p>
 * Предок указывается в свойстве объекта parent. Означает, что для раскрытия объекта,
 * нужно взять все свойства объекта parent и наложить на них свойства самого  объекта.
 * <p>
 * Объекты определенного типа могут содержать свойства, которые являются контейнерами
 * объектов определенного типа.
 * Это правило задается методом:
 * {@link ConfExpander#addRuleContainer(java.lang.String, java.lang.String, java.lang.String)}
 * Например:
 * <pre>{@code
 * domain {           // тип domain
 *     Abonent {      // объект Abonent типа domain
 *          a: 1      // произвольное свойство
 *          field {   // свойство является контейнером объектов типа field
 *              f1 {} // объект f1 типа field
 *          }
 *     }
 * }
 * Объекты определенного типа могут содержать свойства, которые являются объектами
 * определенного типа.
 * Это правило задается методом:
 * {@link ConfExpander#addRuleObject(java.lang.String, java.lang.String, java.lang.String)}
 * Например:
 * <pre>{@code
 * domain {           // тип domain
 *     Abonent {      // объект Abonent типа domain
 *          a: 1      // произвольное свойство
 *          ref {}    // свойство является объектом типа field
 *     }
 * }
 * }</pre>
 * <p>
 * Часть свойств могут быть помечены как "ненаследуемые". Такие свойства удаляются
 * при раскрытии из всех предком и имеют значения только из раскрываемого объекта.
 * Это правило задается методами:
 * {@link ConfExpander#addRuleNotInherited(java.lang.String, java.lang.String)}
 * и {@link ConfExpander#addRuleNotInherited(java.lang.String)}.
 * <p>
 * Раскрытие свойств объектов производится только по явно указанным правилам.
 */
public interface ConfExpander {

    /**
     * Контейнер, в котором лежат данные для раскрытия
     */
    Conf getRoot();

    /**
     * Получить раскрытую Conf для объекта типа type с именем name.
     */
    Conf expand(String type, String name);

    /**
     * Получить раскрытую Conf для объектов типа type
     */
    Conf expand(String type);

    /**
     * Определить правило раскрытия.
     * Объект типа type имеет свойство prop, которое является контейнером
     * объектов типа elementType.
     */
    void addRuleContainer(String type, String prop, String elementType);

    /**
     * Определить правило раскрытия.
     * Объект типа type имеет свойство prop, которое является объектом
     * типа objectType.
     */
    void addRuleObject(String type, String prop, String objectType);

    /**
     * Определить правило раскрытия.
     * В объекте типа type свойство prop не наследуется.
     * Свойство можно указать с маской * в конце (например tag.*) для всех свойств,
     * которые так начинаются.
     */
    void addRuleNotInherited(String type, String prop);

    /**
     * Определить правило раскрытия.
     * В объектах всех типов свойство prop не наследуется.
     * Свойство можно указать с маской * в конце (например tag.*) для всех свойств,
     * которые так начинаются.
     */
    void addRuleNotInherited(String prop);

    /**
     * Определить правило раскрытия.
     * В объекте типа type атрибут parent обрабатывается особым образом
     */
    void addRuleParent(String type, ConfParentResolver parentResolver);
}
