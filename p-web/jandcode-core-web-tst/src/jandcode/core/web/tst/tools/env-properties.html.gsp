<%@ page import="jandcode.core.web.gsp.*; jandcode.commons.moduledef.*; jandcode.core.jc.*; jandcode.core.web.virtfile.impl.virtfs.*; jandcode.core.*; jandcode.core.web.*; jandcode.commons.*" %>
<jc:page template="page/tst-main">
  <%
    BaseGsp th = this
    //
    List items = []
    for (m in th.app.env.properties) {
      items.add([key: m.key, value: m.value])
    }
    items.sort { a, b -> a.key <=> b.key }
  %>
  <!-- ===================================================================== -->
  <h2 class="first">App.getEnv().getProperties()</h2>
  <table class="tst-table">
    <tr>
      <th>property name</th>
      <th>value</th>
    </tr>
    <%
      for (p in items) {
    %>
    <tr>
      <td>${p.key}</td>
      <td>${th.escapeHtml(p.value)}</td>
    </tr>
    <%
      }
    %>
  </table>

</jc:page>