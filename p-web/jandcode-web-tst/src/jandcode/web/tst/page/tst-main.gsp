<%@ page import="jandcode.commons.conf.*; jandcode.web.*; jandcode.commons.*; jandcode.web.gsp.*; jandcode.web.tst.*" %>
<!doctype html>
<html>
<%
  BaseGsp th = this
%>
<head>
  <meta charset="UTF-8">
  <link rel="icon" href="data:,">

  <title>tst</title>

  <jc:link path="./normalize.css"/>
  <jc:link path="./tst-main.css"/>

  <jc:pagePartOut name="head"/>

</head>

<body>

<div class="tst-page-header">
  <%
    //menu
    for (mi in th.app.conf.getConfs("web/tst/menu/main/item")) {
      def attrs = ['class': 'item menuitem', href: th.ref(mi.getString('href'))]
      def s = mi.getString('target')
      if (!UtString.empty(s)) {
        attrs['target'] = s
      }
  %>
  <a ${th.htmlAttrs(attrs)}>${mi.getString('title')}</a>
  <%
    }
  %>
</div>

<div class="tst-page-content">
  <jc:pagePartOut name="body"/>
</div>

</body>
</html>