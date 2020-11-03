<%@ page import="jandcode.core.dao.*; jandcode.core.std.*; jandcode.commons.named.*; jandcode.core.web.gsp.*; jandcode.core.web.*; jandcode.commons.*" %>
<jc:page template="page/tst-main">
  <%
    //
    BaseGsp th = this
    //
    def daoSvc = th.app.bean(DaoService)
  %>
  <!-- ===================================================================== -->
  <!-- ===================================================================== -->
  <h2>Все доступные Dao</h2>

  <%
    for (dhn in daoSvc.daoHolderNames) {
      DaoHolder dh = daoSvc.getDaoHolder(dhn)
      List<DaoHolderItem> items = []
      items.addAll(dh.items)
      items.sort { a, b -> a.name <=> b.name }
  %>
  <h3>daoHolder: ${dhn}</h3>
  <table class="tst-table">
    <tr>
      <th>name</th>
      <th>class</th>
      <th>method</th>
    </tr>
    <% for (r in items) { %>
    <tr>
      <td>${r.name}</td>
      <td>${r.methodDef.cls.name}</td>
      <td>${r.methodDef.method}</td>
    </tr>
    <% } %>
  </table>
  <%
    }
  %>

</jc:page>