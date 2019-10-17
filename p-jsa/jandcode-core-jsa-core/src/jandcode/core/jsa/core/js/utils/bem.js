/*

bem
Генератор классов и др.

----------------------------------------------------------------------------- */

const DELIM_ELEMENT = '--'
const DELIM_MOD = '__'
const DELIM_MOD_VALUE = '_'

/**
 * Формирование списка классов для bem.
 * Основа взята отсюда: https://github.com/GREENpoint/bem-cn-fast
 * @param componentName имя компонента
 * @param elementOrMods имя элемениа
 * @param mods модификаторы
 * @return {*}
 */
export function bem(componentName, elementOrMods, mods) {
    if (!elementOrMods) {
        return componentName;
    }

    let element

    if (typeof elementOrMods === 'string') {
        element = elementOrMods;
    } else {
        mods = elementOrMods;
    }

    let base = componentName
    if (element) {
        base += DELIM_ELEMENT + element;
    }

    let res = base;
    for (let name in mods) {
        let value = mods[name]
        if (value) {
            res += ' ' + (
                typeof value === 'boolean'
                    ? (base + DELIM_MOD + name)
                    : (base + DELIM_MOD + name + DELIM_MOD_VALUE + value)
            );
        }
    }
    return res;

}