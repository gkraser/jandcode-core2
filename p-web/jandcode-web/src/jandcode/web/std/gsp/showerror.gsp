<%@ page import="jandcode.web.std.action.*; jandcode.commons.*; jandcode.web.gsp.*; jandcode.web.*;" %>
<!doctype html>
<html>
<%
  BaseGsp th = this
  ShowerrorInfo info = th.args.info
%>
<head>
  <meta charset="UTF-8">
  <title>Error</title>
  <link rel="icon" href="data:,">
  <style type="text/css">
  body {
    font-family: Arial, Helvetica, sans-serif;
    margin: 0;
    padding: 20px;
    color: #000;
    background-color: #fff;
    font-size: 8pt;
  }

  h1 {
    color: darkred;
  }

  .error {
    border: red solid 2px;
    color: black;
    background: #ffe0e0;
    padding: 9px;
    font-size: 9pt;
    text-align: left;
  }

  .stack {
    border: gray solid 1px;
    color: black;
    background: #eee;
    padding: 8px;
    font-size: 9pt;
    text-align: left;
  }

  .script {
    border: green solid 1px;
    color: black;
    background: #d5ebd5;
    padding: 8px;
    font-size: 9pt;
    text-align: left;
  }
  </style>
</head>

<body>

<h1>Ошибка ${info.code}</h1>

<%
  if (info.text) {
%>
<div>
  <pre class="error"><%=th.escapeHtml(info.text)%></pre>
</div>
<%
  }
%>

<%
  if (info.scriptText) {
%>
<h2>Скрипты</h2>
<pre class="script">${th.escapeHtml(info.scriptText)}</pre>
<%
  }
%>

<%
  if (info.stackText) {
%>
<h2>Стек</h2>
<pre class="stack">${th.escapeHtml(info.stackText)}</pre>
<%
  }
%>

<%
  if (info.stackFullText) {
%>
<h2>Полный стек</h2>
<pre class="stack">${th.escapeHtml(info.stackFullText)}</pre>
<%
  }
%>

</body>
</html>