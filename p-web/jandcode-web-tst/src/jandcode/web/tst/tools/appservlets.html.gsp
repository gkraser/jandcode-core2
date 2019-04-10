<%@ page import="jandcode.web.gsp.BaseGsp; jandcode.web.gsp.*; jandcode.core.*; jandcode.web.*; jandcode.commons.*" %>
<jc:page template="page/tst-main">
  <%
    jandcode.web.gsp.BaseGsp th = this
    //
    def ctx = th.request.httpServlet.getServletContext()
    def r = ctx.getServletRegistrations();
    def lst = []
    for (z in r) {
      def cn = z.value.className
      if (cn != "AppServlet") {
        continue
      }

      def x = [
          name: z.key,
      ]

      for (m in z.value.mappings) {
        def s = m.replace("/*", "/")
        x.uri = s
        break
      }

      def p = th.request.httpRequest.servletContext.contextPath + x.uri
      x.root = p

      lst.add(x)
    }

  %>

  <h1>Running jandcode.web.AppServlet</h1>
  <table class="tst-table">
    <tr>
      <th>Servlet</th>
      <th>Home</th>
      <th>Tst</th>
    </tr>
    <% for (itm in lst) { %>
    <tr>
      <td>
        ${itm.name}
      </td>
      <td>
        <a href="${itm.root}">${itm.root}</a>
      </td>
      <td>
        <a href="${itm.root}tst">${itm.root}tst</a>
      </td>
    </tr>
    <% } %>
  </table>
</jc:page>