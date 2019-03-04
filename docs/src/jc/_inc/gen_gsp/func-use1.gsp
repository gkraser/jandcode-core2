<%@ page import="jandcode.jc.*" %>
<%
  GspScript th = this

  // создаем экземпляр скрипта, содержащего функции
  GspScript lib = th.create('lib/lib1.gsp')

  // выполняем функции
  lib.vars.func1(1)
  lib.vars.func2(2)
%>
