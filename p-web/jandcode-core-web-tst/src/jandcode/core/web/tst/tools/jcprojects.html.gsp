<%@ page import="jandcode.core.web.gsp.*; jandcode.commons.moduledef.*; jandcode.core.jc.*; jandcode.core.web.virtfile.impl.virtfs.*; jandcode.core.*; jandcode.core.web.*; jandcode.commons.*" %>
<jc:page template="page/tst-main">
  <%
    BaseGsp th = this
    //
    List<Module> srcModules = []
    for (m in th.app.modules) {
      if (m.sourceInfo.isSource()) {
        srcModules.add(m)
      }
    }
  %>
  <!-- ===================================================================== -->
  <h2 class="first">Modules</h2>
  <table class="tst-table">
    <tr>
      <th>module</th>
      <th>project dir</th>
    </tr>
    <%
      for (p in srcModules) {
    %>
    <tr>
      <td>${p.name}</td>
      <td>${p.sourceInfo.projectPath}</td>
    </tr>
    <%
      }
    %>
  </table>

</jc:page>