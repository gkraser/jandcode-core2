<%@ page import="jandcode.jsa.tst.test.utils.*; jandcode.web.*; jandcode.jsa.tst.test.*; jandcode.commons.*; jandcode.web.gsp.*" %>
<!doctype html>
<%
  /*
    Общий шаблон для запускалок тестовых файлов js

   */
  BaseGsp th = this

  def runCtx = th.inst(TstRunContext)

  //
  def cfgJson = UtJson.toJson(runCtx.cfg)
%>
<html>
<head>
  <meta charset="UTF-8">
  <title>${runCtx.title}</title>
  <link rel="icon" href="data:,">
  <jc:linkModule path="jandcode.jsa.base"/>
  <jc:linkModule path="${runCtx.theme}"/>
  <jc:linkModule path="${runCtx.env}"/>
  <jc:linkModule path="${runCtx.main}"/>
  <jc:pagePartOut name="head"/>
</head>
<body>
<script>
    // конфигурация
    Jc.cfg.set({tst: ${cfgJson}})
</script>
<div id="jc-app"></div>
<jc:pagePartOut name="body"/>
</body>
</html>