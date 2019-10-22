<%@ page import="jandcode.core.jsa.tst.test.utils.TstRunContext; jandcode.core.jsa.tst.test.utils.*; jandcode.core.web.*; jandcode.core.jsa.tst.test.*; jandcode.commons.*; jandcode.core.web.gsp.*" %>
<!doctype html>
<%
  /*
    Общий шаблон для запускалок тестовых файлов js

   */
  BaseGsp th = this

  def runCtx = th.inst(jandcode.core.jsa.tst.test.utils.TstRunContext)

  //
  def cfgJson = UtJson.toJson(runCtx.cfg)
%>
<html>
<head>
  <meta charset="UTF-8">
  <title>${runCtx.title}</title>
  <link rel="icon" href="data:,">
  <jc:linkModule module="jandcode.core.jsa.base"/>
  <jc:linkModule module="${runCtx.theme}"/>
  <jc:linkModule module="${runCtx.env}"/>
  <jc:linkModule module="${runCtx.main}"/>
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