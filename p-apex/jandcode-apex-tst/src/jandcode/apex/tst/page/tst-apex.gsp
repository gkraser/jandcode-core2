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
  ctx.theme = ctx.resolveTheme(theme, "apex-std")
  ctx.env = "jandcode.apex.tst/tst.js"
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
<div id="tst-wrapper" class="tst-wrapper">
  <div id="tst-wrapper--mocha" class="tst-wrapper--mocha">
    <div id="mocha"></div>
  </div>

  <div id="tst-wrapper--jc-app" class="tst-wrapper--jc-app">
    <div id="jc-app"></div>
  </div>
</div>

<% ctx.outLinkModules() %>

<script>
    Jc.cfg.set(${ctx.cfgJson})
    Jc.cfg.set({tst: ${tstCtx.cfgJson}})
    Jc.ready(function() {
        Jc.tst.runModule('${ctx.main}')
    })
</script>
</body>
</html>
