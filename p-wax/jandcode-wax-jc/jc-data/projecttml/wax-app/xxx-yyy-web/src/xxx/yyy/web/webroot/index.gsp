<%@ page import="jandcode.wax.web.*; jandcode.web.*; jandcode.commons.*; jandcode.web.gsp.*" %>
<!doctype html>
<%
  BaseGsp th = this
  //
  def ctx = th.inst(WaxIndexGspContext)
  //
  ctx.title = "XxxYyy"
  ctx.main = "xxx.yyy.web"
%>
<html>
<head>
  <meta charset="UTF-8">
  <title>${ctx.title}</title>
  <link rel="icon" href="data:,">
  <jc:linkModule module="${ctx.modules}"/>
</head>
<body>
<div id="jc-app"></div>
<script>
    Jc.cfg.set(${ctx.cfgJson})
    Jc.ready(function() {
        require('${ctx.main}').run()
    })
</script>
</body>
</html>
