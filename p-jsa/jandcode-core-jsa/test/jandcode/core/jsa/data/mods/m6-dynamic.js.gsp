<%@ page import="jandcode.core.jsa.jsmodule.JsModuleBuilder; jandcode.core.web.gsp.*; jandcode.commons.*; jandcode.core.jsa.jsmodule.*; jandcode.core.*; jandcode.core.web.*; jandcode.core.web.gsp.*;" %>
<%
  BaseGsp th = this
  jandcode.core.jsa.jsmodule.JsModuleBuilder b = th.inst(JsModuleBuilder)
  b.dynamic = true
  def a = [:]
  for (i in 1..10) {
    a.put('i' + i, i * 100)
  }
%>
<script>
    let dt = '${UtDateTime.now()}';
</script>
