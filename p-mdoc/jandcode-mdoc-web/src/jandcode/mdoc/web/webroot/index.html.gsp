<%@ page import="jandcode.mdoc.web.*; jandcode.commons.*; jandcode.core.*; jandcode.core.web.*; jandcode.core.web.gsp.*;" %>
<%
  BaseGsp th = this
  def apps = th.app.bean(WebMDocService).getRegisterApps()
  if (apps.size() == 0) {
    th.request.redirect("index.html")
  }
%>

<!doctype html>
<html>
<head>
  <meta charset="UTF-8">
  <title>mdoc serve</title>
</head>
<body>
<h1>MDoc Serve</h1>
<h2>Доступные документы</h2>
<ul>
  <% for (a in apps) { %>
  <li>
    <a href="${th.ref(a)}">${a}</a>
  </li>
  <% } %>
</ul>
</body>
</html>
