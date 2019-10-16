<%@ page import="jandcode.core.web.gsp.*; jandcode.core.web.*; jandcode.commons.error.*; jandcode.commons.*" %>
<jc:page template="page/tst-main">
  <%
    //
    BaseGsp th = this
    //
    boolean exc = th.request.params.getBoolean("exc")
    if (exc) {
      throw new XError("Error")
    }
  %>
  <!-- ===================================================================== -->
  <h2 class="first">Showerror</h2>

  <p>Проверка отображения ошибок</p>

  <ul>
    <li>
      <a href="${th.ref('/NOT-EXIST-ACTION')}">404 - not found</a>
    </li>
    <li>
      <a href="?exc=true">throw Exception</a>
    </li>
  </ul>

</jc:page>