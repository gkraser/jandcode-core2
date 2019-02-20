package jandcode.commons.simxml;

/**
 * Константы для SimXml.
 */
public class SimXmlConsts {

    /**
     * Имя атрибута, через которое осуществляется доступ к тексту узла.
     * Например:
     * <pre>{@code
     * <node>TEXT</text>
     * }</pre>
     * Доступ к значению:
     * <pre>{@code
     * node.getValue("#text")  // TEXT
     * node.getText()          // TEXT
     * }</pre>
     */
    public static final String ATTR_TEXT = "#text";

    /**
     * Имя узла, в котором сохраняется текст узла.
     * Например:
     * <pre>{@code
     * <node>
     *     TEXT
     *     <node1>TEXT1</node1>
     *     TEXT2
     *     </text>
     * }</pre>
     * Доступ к значению:
     * <pre>{@code
     * node.getValue("#text")   // TEXT
     * node.getText()           // TEXT
     * for (SimNode x: node.getChilds()) {
     *    x.getName()           // node1, #text
     *    x.getText()           // TEXT1, TEXT2
     *    x.getValue("#text")   // TEXT1, TEXT2
     * }
     * }</pre>
     */
    public static final String NODE_TEXT = "#text";

    /**
     * Имя узла, в котором сохраняется коментарий узла.
     * Например:
     * <pre>{@code
     * <node>
     *     <!-- TEXT1 -->
     *     <!-- TEXT2 -->
     *     </text>
     * }</pre>
     * Доступ к значению:
     * <pre>{@code
     * for (SimNode x: node.getChilds()) {
     *    x.getName()           // #comment, #comment
     *    x.getValue("#text")   // TEXT1, TEXT2
     *    x.getText()           // TEXT1, TEXT2
     * }
     * }</pre>
     */
    public static final String NODE_COMMENT = "#comment";

    /**
     * Имя ноды по умолчанию.
     * Используется, если имя ноды явно не назначено, но нужно.
     * Например при записи в файл.
     */
    public static final String NODE_DEFAULT = "node";

    /**
     * Имя корневой ноды по умолчанию.
     * Используется, если имя ноды явно не назначено, но нужно.
     * Например при записи в файл.
     */
    public static final String NODE_DEFAULT_ROOT = "root";


}
