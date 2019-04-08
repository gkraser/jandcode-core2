
Conf
====

@@code-info
    lib=jandcode-commons
    pak=jandcode.commons.conf
    class=jandcode.commons.conf.Conf
    class=jandcode.commons.UtConf

Средства для работы с конфигурациями.

Объект `jandcode.commons.conf.Conf` представляет собой структуру типа "map-of-maps".

Прежде всего, это обычный Map, точнее `jandcode.commons.variant.IVariantMap`.
Имена ключей (в дальнейшем - свойства) - регистрозависимые.

Значениями могут быть обычные простые объекты (строки, числа, даты) или
объекты Conf. Значениями не могут быть объекты `List` и `Map`.
  
Создание экземпляра
-------------------

Пустой объект:

@@code file=UseConf1#create_empty mode=bodyout

Из `Map`:

@@code file=UseConf1#create_fromMap mode=bodyout

Из `List`:

@@code file=UseConf1#create_fromList mode=bodyout


Значения свойств
----------------

Установить значение можно с помощью метода `setValue` или `put`:

@@code file=UseConf1#set_simple mode=bodyout

Прочитать значения свойств можно с помощью метода `get` или методов
интерфейса `jandcode.commons.variant.IVariantNamed` (`getString`, `getInt` и т.д.):

@@code file=UseConf1#get_simple mode=bodyout


Путь
----

Путь до свойства описывается как имена свойств, разделенных символом `'/'`.
Можно читать и писать значения по пути:

@@code file=UseConf1#path1 mode=bodyout


Conf как значение свойства
--------------------------

В качестве значения свойства может быть указан объект `Conf`:

@@code file=UseConf1#value_conf1 mode=bodyout

Для получения значения как `Conf` имееются специальные методы `getConf` и `findConf`.
Если значения по пути отсутсвует или оно не `Conf`, то `getConf` сгенерирует ошибку,
а `findConf` вернет null:

@@code file=UseConf1#value_conf2 mode=bodyout

`findConf` может автоматически создавать объекты по указанному пути. Для этого
нужно указать вторым параметром `true`: 

@@code file=UseConf1#value_conf3_find mode=bodyout


Безымянное свойство
-------------------

Для имитации списков предусмотрено специальное безымянное свойство `#`.
При присвоении значения такому свойству генерируется уникальное имя свойства:

@@code file=UseConf1#noname1 mode=bodyout


Map и List как значение свойства
--------------------------------

Типы `Map` и `List` преобразуются в тип `Conf`.
Для типа `List` создается объект `Conf` где для каждого элемента списка 
создается безымянное свойство:

@@code file=UseConf1#value_conf3_map_and_list mode=bodyout

Загрузка из файла
-----------------

Загрузка возможна из файлов формата [cfx](conf-cfx.md), [xml](conf-xml.md) или 
[json](conf-json.md).
При загрузке существующие данные в объекте не стираются. 

@@code file=UseConf1#load1 mode=bodyout

join
----

Объекты `Conf` можно объединять методом `join`. При этом в объект, в который 
объединяем, присваиваются свойства из объекта, который объединяем. Для свойств
типа `Conf` делаются копии.   

@@code file=UseConf1#join1 mode=bodyout



  