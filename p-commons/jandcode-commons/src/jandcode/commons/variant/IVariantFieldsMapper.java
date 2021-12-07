package jandcode.commons.variant;

/**
 * Маппинг полей.
 * Настраивает соотвествие между именами полей.
 * <pre>{@code
 * // например имеем владельца с полями f1, f2
 * // тогда можно так:
 * String mapField(String fieldName, IVariantFieldsOwner fieldsOwner) {
 *    if (fieldName.endsWith("1")) return "f1"
 *    if (fieldName.endsWith("2")) return "f2"
 *    return null;
 * }
 * }</pre>
 */
public interface IVariantFieldsMapper {

    /**
     * Для поля fieldName, которое запрашивается из fieldsOwner,
     * возвращает имя поля, которое имеется ввиду.
     *
     * @param fieldName   запрашиваемое имя поля
     * @param fieldsOwner для какого владельца производится маппинг
     * @return null, если не известно, иначе - имя поля во владельце.
     */
    String mapField(String fieldName, IVariantFieldsOwner fieldsOwner);


}
