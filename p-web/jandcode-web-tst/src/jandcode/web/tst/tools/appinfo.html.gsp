<%@ page import="jandcode.commons.named.*; jandcode.commons.UtError; jandcode.web.gsp.BaseGsp; jandcode.commons.UtString; jandcode.web.UtWeb; jandcode.web.gsp.*; jandcode.web.*; jandcode.commons.*" %>
<jc:page template="page/tst-main">
  <%
    //
    jandcode.web.gsp.BaseGsp th = this
    //
    def domainSvc = th.webService
  %>
  <!-- ===================================================================== -->
  <h2 class="first">App</h2>
  <table class="tst-table">
    <%
      def fullUrl = jandcode.web.UtWeb.getServerAddr(th.request) + th.ref("/")
    %>
    <tr>
      <td>appdir</td>
      <td>${th.app.appdir}</td>
    </tr>
    <tr>
      <td>debug</td>
      <td>${th.app.isDebug()}</td>
    </tr>
    <tr>
      <td>servlet name</td>
      <td>${th.request.httpServlet.getServletName()}</td>
    </tr>
    <tr>
      <td>servlet class</td>
      <td>${th.request.httpServlet.class.name}</td>
    </tr>
    <tr>
      <td>home url</td>
      <td><a href="${fullUrl}">${fullUrl}</a></td>
    </tr>
  </table>

  <!-- ===================================================================== -->
  <h2>Mount</h2>
  <table class="tst-table">
    <tr>
      <th>name</th>
      <th>virtual path</th>
      <th>real path</th>
    </tr>
    <% for (r in domainSvc.mounts) { %>
    <tr>
      <td>${r.name}</td>
      <td>/${r.virtualPath}</td>
      <td>${r.realPath}</td>
    </tr>
    <% } %>
  </table>

  <!-- ===================================================================== -->
  <h2>Beans</h2>
  <table class="tst-table">
    <tr>
      <th>name</th>
      <th>class</th>
      <th>inst</th>
      <th>prototype</th>
    </tr>
    <% for (r in th.app.beanFactory.beans) { %>
    <tr>
      <td>${r.name}</td>
      <td>${r.cls.name}</td>
      <td>${r.hasInst() ? 'yes' : ''}</td>
      <td>${r.isPrototype() ? 'yes' : ''}</td>
    </tr>
    <% } %>
  </table>

  <!-- ===================================================================== -->
  <h2>Modules</h2>
  <table class="tst-table">
    <tr>
      <th>name</th>
      <th>path</th>
    </tr>
    <% for (r in th.app.modules) { %>
    <tr>
      <td>${r.name}</td>
      <td>${r.path}</td>
    </tr>
    <% } %>
  </table>

  <!-- ===================================================================== -->
  <h2>Actions</h2>
  <table class="tst-table">
    <tr>
      <th>name</th>
      <th>class</th>
    </tr>
    <%
      for (r in domainSvc.actions) {
        def cls
        try {
          def a = r.createInst()
          cls = a.class.name
        } catch (e) {
          cls = "ERROR: " + jandcode.commons.UtError.createErrorInfo(e).text
        }
    %>
    <tr>
      <td>${r.name}</td>
      <td>${cls}</td>
    </tr>
    <% } %>
  </table>

  <!-- ===================================================================== -->
  <h2>Action factorys</h2>
  <table class="tst-table">
    <tr>
      <th>nm</th>
      <th>class</th>
    </tr>
    <%
      for (r in domainSvc.actionFactorys) {
        String nm = ""
        if (r instanceof jandcode.commons.named.INamed) {
          nm = r.name
        }
    %>
    <tr>
      <td>${nm}</td>
      <td>${r.class.name}</td>
    </tr>
    <% } %>
  </table>

  <!-- ===================================================================== -->
  <h2>Gsps</h2>
  <table class="tst-table">
    <tr>
      <th>name</th>
      <th>class</th>
    </tr>
    <%
      for (r in domainSvc.gsps) {
        def cls
        try {
          cls = r.getGspPath()
          if (jandcode.commons.UtString.empty(cls)) {
            def g = r.createInst()
            cls = g.class.name
          }
        } catch (e) {
          cls = "ERROR: " + UtError.createErrorInfo(e).text
        }
    %>
    <tr>
      <td>${r.name}</td>
      <td>${cls}</td>
    </tr>
    <% } %>
  </table>

  <!-- ===================================================================== -->
  <h2>Renders</h2>
  <table class="tst-table">
    <tr>
      <th>name</th>
      <th>class</th>
    </tr>
    <%
      for (r in domainSvc.renders) {
        def cls
        try {
          def a = r.createInst()
          cls = a.class.name
        } catch (e) {
          cls = "ERROR: " + UtError.createErrorInfo(e).text
        }
    %>
    <tr>
      <td>${r.name}</td>
      <td>${cls}</td>
    </tr>
    <% } %>
  </table>

  <!-- ===================================================================== -->
  <h2>Filters</h2>
  <table class="tst-table">
    <tr>
      <th>name</th>
      <th>type</th>
      <th>enabled</th>
      <th>class</th>
    </tr>
    <%
      for (r in domainSvc.filters) {
        def types = ""
        for (t in r.types) {
          if (types.length() > 0) {
            types += "<br>"
          }
          types += t.name()
        }
    %>
    <tr>
      <td>${r.name}</td>
      <td>${types}</td>
      <td>${r.enabled}</td>
      <td>${r.inst.class.name}</td>
    </tr>
    <% } %>
  </table>

</jc:page>