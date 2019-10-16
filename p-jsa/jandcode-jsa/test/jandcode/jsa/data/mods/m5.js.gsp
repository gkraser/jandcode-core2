<%@ page import="jandcode.core.web.gsp.*; jandcode.commons.*; jandcode.jsa.jsmodule.*; jandcode.core.*; jandcode.core.web.*; jandcode.core.web.gsp.*;" %>
<%
  BaseGsp th = this
  JsModuleBuilder b = th.inst(JsModuleBuilder)
  def a = [:]
  for (i in 1..10) {
    a.put('i' + i, i * 100)
  }
%>
<script>
    // module=${b.module.name}
    module.exports = ${UtJson.toJson(a)};
</script>
