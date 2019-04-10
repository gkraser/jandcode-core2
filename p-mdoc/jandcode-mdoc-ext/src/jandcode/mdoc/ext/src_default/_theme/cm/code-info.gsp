<%@ page import="jandcode.commons.named.*; jandcode.commons.variant.*; jandcode.mdoc.source.*; jandcode.mdoc.gsp.*; jandcode.commons.*" %>
<%

  /*
    Информация о коде, который описывается в статье

    lib = имя библиотеки
    module = имя модуля jandcode
    pak = имя пакета
    class = имя класса
    text = текст html
    ref = ссылка. Формат 'ref|title|дополнитлеьный текст

    Атрибуты могут дублироватся!

  */

  // контекст генерации
  GspTemplateContext th = context

  // атрибуты, переденые команде
  List<NamedValue> attrsList = th.args.attrs['$list']

  Map data = [
      lib    : [],
      module : [],
      pak    : [],
      'class': [],
      text   : [],
      ref    : [],
  ]

  for (a in attrsList) {
    def b = data[a.name]
    if (b != null) {
      b.add(a.value)
    }
  }

%>
<fieldset class="cm-code-info">
  <legend>Связи</legend>
  <ul>

    <% for (itm in data.lib) { %>
    <li>
      <span class="cm-code-info--code">${itm}</span> <span
        class="cm-code-info--marker">(lib)</span>
    </li>
    <% } %>

    <% for (itm in data.module) { %>
    <li>
      <span class="cm-code-info--code">${itm}</span> <span
        class="cm-code-info--marker">(module)</span>
    </li>
    <% } %>

    <% for (itm in data.pak) { %>
    <li>
      <span class="cm-code-info--code">${itm}</span> <span
        class="cm-code-info--marker">(package)</span>
    </li>
    <% } %>

    <% for (itm in data['class']) { %>
    <li>
      <span class="cm-code-info--code">${itm}</span> <span
        class="cm-code-info--marker">(class)</span>
    </li>
    <% } %>

    <% for (itm in data.text) { %>
    <li>
      <span class="cm-code-info--text">${itm}</span>
    </li>
    <% } %>

    <%
      for (itm in data.ref) {
        String[] ar = itm.split("\\|+")
        String t = "none"
        if (ar.length > 2) {
          t = "<a href=\"${ar[0]}\">${ar[1]}</a> ${ar[2]}"
        } else if (ar.length > 1) {
          t = "<a href=\"${ar[0]}\">${ar[1]}</a>"
        } else if (ar.length > 0) {
          t = "<a href=\"${ar[0]}\"></a>"
        }
    %>
    <li>
      <span class="cm-code-info--text">${t}</span>
    </li>
    <%
      }
    %>

  </ul>
</fieldset>