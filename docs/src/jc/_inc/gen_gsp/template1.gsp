<%@ page import="jandcode.jc.*" %>
<%
  // содержимое gsp - это тело метода jandcode.jc.GspScript#onGenerate
  // поэтому можно получить типизированную ссылку
  GspScript th = this

  def s = "Текст1"
%>
Текст
Переменная ${s}
Аргументы ${th.args['a1']}, ${th.args.a2}
<% out("Текст") %>
<% out(s) %>
