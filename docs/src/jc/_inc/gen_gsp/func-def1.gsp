<%@ page import="jandcode.jc.*" %>
<%
  GspScript th = this

  // функции объявляем как Closure и записываем в vars

  th.vars.func1 = { a ->
    out("A=${a}")
  }

%>

Весь вывод вне функций будет проигнорирован!

<% th.vars.func2 = { b -> %>
B=${b}
<% } %>
