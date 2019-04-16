<%@ page import="jandcode.jsa.cfg.*; jandcode.web.*; jandcode.commons.*; jandcode.web.gsp.*" %>
<!doctype html>
<%
  BaseGsp th = this
  //
  String title = "XxxYyy"
  //
  String env = "jandcode/jsa/core/index.js"
  String theme = "jandcode/jsa/core/css/theme-std.js"
  String main = "xxx/yyy/index.js"

  // cfg
  def cfgSvc = th.app.bean(JsaClientCfgService)
  def cfgJson = UtJson.toJson(cfgSvc.grabClientCfg())
%>
<html>
<head>
  <meta charset="UTF-8">
  <title>${title}</title>
  <link rel="icon" href="data:,">
  <jc:linkModule path="${env}"/>
  <jc:linkModule path="${theme}"/>
  <jc:linkModule path="${main}"/>
  <script>
      Jc.cfg.set(${cfgJson})
  </script>
</head>
<body>
<div id="jc-app"></div>
<script>
    Jc.ready(function() {
        require('${main}').run()
    })
</script>
</body>
</html>
