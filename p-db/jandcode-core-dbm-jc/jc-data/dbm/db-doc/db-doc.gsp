<%@ page import="jandcode.core.dbm.*; jandcode.core.dbm.dbstruct.*; jandcode.jc.*;jandcode.commons.*;jandcode.core.dbm.domain.*" %>
<%
  GspScript th = this

  DomainDbUtils dbUtils = this.args.dbUtils

  GspScript utils = th.create("${th.scriptDir}/_utils.gsp")

  // копируем все ресурсы
  th.ant.copy(todir: th.outDir) {
    fileset(dir: th.scriptDir) {
      include(name: '**/*.js')
      include(name: '**/*.css')
    }
  }

  th.changeFile("index.html")
%>
<!doctype html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Документация по баз данных</title>
  <link rel="stylesheet" href="style.css"/>
</head>

<body>

<h1><a id="_top"></a>Документация по базе данных</h1>

<h2>Содержание</h2>
<ul>
  <li><a href="#__toc_diagram">Диаграммы</a></li>
  <li><a href="#__toc_tab">Таблицы</a></li>
  <li><a href="#__toc_tab_not_in_diagram">Таблицы, не попавшие на диаграммы</a></li>
</ul>

%{--
==================================================================================
 список таблиц
==================================================================================
--}%
<h2><a name="__toc_tab"></a>Таблицы</h2>
<ul>
  <%
    for (d in dbUtils.domains) {
  %>
  <li>
    <a href="#${d.dbTableName}">${d.dbTableName}</a>
    <span class="small-title">${d.title}</span>
  </li>
  <%
    }
  %>
</ul>

%{--
==================================================================================
 описание конкретных таблиц
==================================================================================
--}%
<%
  for (d in dbUtils.domains) {
%>
<h2 class="table_desc"><a id="${d.dbTableName}"></a>${d.dbTableName}</h2>
<% utils.vars.out_title(d) %>
<% utils.vars.out_comment(d) %>

<h3>Поля</h3>
<table class="table-data">
  <tr>
    <th>Поле</th>
    <th>Тип</th>
    <th>Описание</th>
  </tr>
  <% for (f in d.fields) { %>
  <tr>
    <td>
      ${f.name}
    </td>
    <td>
      <% utils.vars.out_field_type(f) %>
    </td>
    <td>
      <% utils.vars.out_title(f) %>
      <% utils.vars.out_comment(f) %>
    </td>
  </tr>
  <% } %>
</table>

<h3>Диаграмма ссылок</h3>
<div class="diagram-wrapper">
  <img src="images/refs-diag--${d.dbTableName}.svg"/>
</div>

<%
  }
%>

</body>
</html>