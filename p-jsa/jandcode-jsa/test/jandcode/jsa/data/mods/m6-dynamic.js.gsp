<%@ page import="jandcode.commons.UtJson; jandcode.jsa.jsmodule.JsModuleBuilder; jandcode.commons.*; jandcode.core.*; jandcode.web.*; jandcode.web.gsp.*;" %>
<%
  BaseGsp th = this
  JsModuleBuilder b = th.inst(JsModuleBuilder)
  b.dynamic = true
  def a = [:]
  for (i in 1..10) {
    a.put('i' + i, i * 100)
  }
%>
<script>
    let dt = '${jandcode.commons.UtDateTime.now()}';
</script>
