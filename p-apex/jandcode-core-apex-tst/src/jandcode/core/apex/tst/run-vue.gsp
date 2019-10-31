<%@ page import="jandcode.core.apex.tst.*; jandcode.core.jsa.gsp.*; jandcode.core.web.*; jandcode.commons.*; jandcode.core.web.gsp.*" %>
<!doctype html>
<%
  BaseGsp th = this
  //
  def ctx = th.inst(JsaIndexGspContext)
  def tstCtx = th.inst(ApexTstIndexGspContext)
  //
  ctx.title = th.args.path
  ctx.env = "jandcode.core.apex.tst/tst-runner"
  ctx.main = th.args.path
  ctx.theme = "apex"
%>
<html>
<head>
  <meta charset="UTF-8">
  <title>${ctx.title}</title>
  <link rel="icon" href="data:,">
  <meta name="viewport" content="width=device-width, initial-scale=1">
</head>

<body>
<jc:linkModule module="${ctx.modules}"/>
<div id="jc-app"></div>
<script>
    Jc.cfg.set(${ctx.cfgJson})
    Jc.cfg.set({tst: ${tstCtx.cfgJson}})
    Jc.ready(function() {
        require('${ctx.env}').runVue('${ctx.main}')
    })
</script>
</body>
</html>