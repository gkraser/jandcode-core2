<%@ page import="jandcode.jc.*" %>
<%
  GspScript th = this

  // создаем экземпляры скриптов
  // важно, что бы они были созданы внутри шаблона, откуда будут выводится
  GspScript child1 = th.create('lib/child1.gsp')
  GspScript child2 = th.create('lib/child2.gsp')
%>
Первый шаблон:
${child1}
Второй шаблон:
<% out(child2) %>
