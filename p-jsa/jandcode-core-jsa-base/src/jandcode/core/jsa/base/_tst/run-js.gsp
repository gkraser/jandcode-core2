<%@ page import="jandcode.core.jsa.gsp.*; jandcode.core.web.*; jandcode.commons.*; jandcode.core.web.gsp.*" %>
<!doctype html>
<%
  BaseGsp th = this
  //
  def ctx = th.inst(JsaIndexGspContext)
  //
  ctx.title = th.args.path
  ctx.env = "jandcode.core.jsa.base"
  ctx.main = th.args.path
  ctx.theme = null
%>
<html>
<head>
  <meta charset="UTF-8">
  <title>${ctx.title}</title>
  <link rel="icon" href="data:,">
  <meta name="viewport" content="width=device-width, initial-scale=1">
</head>

<body>
<% ctx.outLinkModules() %>
<div id="jc-app"></div>
<div id="mocha"></div>
<script>
    Jc.cfg.set(${ctx.cfgJson})
    Jc.ready(function() {
        let main = require('${ctx.main}')
        if (main.run) {
            main.run()
        } else if (window.mocha) {
            mocha.run()
        } else {
            throw new Error("No run() or mocha.run()")
        }
    })
</script>
</body>
</html>
