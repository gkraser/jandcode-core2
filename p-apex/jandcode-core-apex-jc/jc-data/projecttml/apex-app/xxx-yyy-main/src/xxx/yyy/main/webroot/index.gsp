<%@ page import="jandcode.core.jsa.gsp.*; jandcode.core.web.*; jandcode.commons.*; jandcode.core.web.gsp.*" %>
<!doctype html>
<%
  BaseGsp th = this
  //
  def ctx = th.inst(JsaIndexGspContext)
  //
  ctx.title = "XxxYyy"
  ctx.main = "xxx.yyy.main/js/main.js"
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
    Jc.ready(function() {
        require('${ctx.main}').run()
    })
</script>
</body>
</html>
