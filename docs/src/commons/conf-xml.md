
Формат файла xml
================

Для обычных файлов xml используется особый процесс загрузки.

Для каждого тега xml создается безымянный объект `Conf` в атрибуте 
`$name` которого сохраняется имя тега.

Пример
------

@@code file=sample1.xml

получаем:
 
@@code file=_utils/ConfUtils#confToText
    src=sample1.xml 


Текст тега
----------

Текст тега помещается в атрибут `text`

@@code file=text1.xml

получаем:
 
@@code file=_utils/ConfUtils#confToText
    src=text1.xml
         

Специальный коментарий
----------------------

Если коментарий начинается с символа `'@'`, то его текст рассматривается как 
значение атрибута `comment` объекта, внутри которого встретился коментарий:

@@code file=comment1.xml

получаем:
 
@@code file=_utils/ConfUtils#confToText
    src=comment1.xml
    
     
Функции
-------

См. [](conf-cfx.md#func).


Подстановки
-----------

См. [](conf-cfx.md#subst).





