<%@ page import="jandcode.core.jsa.tst.*; jandcode.core.jsa.gsp.*; jandcode.core.web.*; jandcode.commons.*; jandcode.core.web.gsp.*" %>
<!doctype html>
<%
  BaseGsp th = this
  //
  def ctx = th.inst(JsaIndexGspContext)
  def tstCtx = th.inst(TstIndexGspContext)
  //
  String path = th.context.rootGsp.args.path
  String theme = th.request.params.getString('theme')
  //
  ctx.main = path
  ctx.theme = ctx.resolveTheme(theme, "base")
  ctx.env = "jandcode.core.jsa.tst/tst-runner.js"
  ctx.addModule(tstCtx.envTstJs)
%>
<html>
<head>
  <meta charset="UTF-8">
  <title>${tstCtx.title}</title>
  <link rel="icon" href="data:,">
  <meta name="viewport" content="width=device-width, initial-scale=1">
</head>

<body>
<div id="jc-app"></div>

<% ctx.outLinkModules() %>

<script>
    Jc.cfg.set(${ctx.cfgJson})
    Jc.cfg.set({tst: ${tstCtx.cfgJson}})
    Jc.ready(function() {
        require('${ctx.env}').runModule('${ctx.main}')
    })
</script>
</body>
</html>
