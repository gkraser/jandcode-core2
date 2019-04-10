<%@ page import="jandcode.commons.error.*; jandcode.commons.conf.*; jandcode.web.*; jandcode.commons.*; jandcode.web.gsp.*; jandcode.web.tst.*" %>
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
  <h2 class="first">Other tools</h2>

  <p>Дополнительные инструменты</p>

  <ul>

    <%
      //menu
      for (mi in th.app.conf.getConfs("web/tst/menu/tools/item")) {
        def attrs = ['class': 'item menuitem', href: th.ref(mi.getString('href'))]
        def s = mi.getString('target')
        if (!UtString.empty(s)) {
          attrs['target'] = s
        }
    %>
    <li>
      <a ${th.htmlAttrs(attrs)}>${mi.getString('title')}</a>
    </li>
    <%
      }
    %>

  </ul>

</jc:page>