
Конфигурирование
================

Утилита `jc` конфигурируется в файле `$USERHOME/.config/jandcode/jc-cfg.cfx`
формата [Conf](../commons/conf) и в переменной среды `JC_PATH`.

JC_PATH
-------

Переменная содержит список проектов, разделенных ';' (или ':' для Linux),
которые будет загружены до загрузки основного проекта.


jc-cfg.cfx
---------

### Настройки по умолчанию

По умолчанию настройки выглядят так:

@@code file=_utils/ResFile#path 
    src=res:jandcode/jc/jc-cfg-default.cfx


### Структура файла

#### color-style

Цветовые схемы для ansi-консоли

```xml
<root>
    <color-style name="STYLE-NAME" color="COLOR" background="COLOR"/>
</root>
```

где:      
      
* `name` - имя стиля. Имена доступных стилей можно посмотреть в настройках по умолчанию
* `color` - цвет символов
* `background` - цвет фона

Если цвет не задан (пустая строка), то он принимается равным значению по умолчанию
для консоли.

Для цветов используются константы:

| Обычные | Яркие   |
| ---     | ---     |
|black    |black_h  |
|red      |red_h    |
|green    |green_h  |
|yellow   |yellow_h |
|blue     |blue_h   |
|magenta  |magenta_h|
|cyan     |cyan_h   |
|white    |white_h  |

