<%@ page import="jandcode.core.web.cachecontrol.*; jandcode.core.std.*; jandcode.commons.named.*; jandcode.core.web.gsp.*; jandcode.core.web.*; jandcode.commons.*" %>
<jc:page template="page/tst-main">
  <%
    //
    BaseGsp th = this
    //
    def domainSvc = th.webService
    def appInfo = th.app.bean(AppInfo)
    def cacheControlSvc = th.app.bean(CacheControlService)
  %>
  <!-- ===================================================================== -->
  <h2 class="first">App</h2>
  <table class="tst-table">
    <%
      def fullUrl = UtWeb.getServerAddr(th.request) + th.ref("/")
    %>
    <tr>
      <td>appdir</td>
      <td>${th.app.appdir}</td>
    </tr>
    <tr>
      <td>workdir</td>
      <td>${th.app.workdir}</td>
    </tr>
    <tr>
      <td>env.dev</td>
      <td>${th.app.env.dev}</td>
    </tr>
    <tr>
      <td>env.source</td>
      <td>${th.app.env.source}</td>
    </tr>
    <tr>
      <td>env.test</td>
      <td>${th.app.env.test}</td>
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
    <tr>
      <td>main module</td>
      <td>${appInfo.mainModule}</td>
    </tr>
    <tr>
      <td>app title</td>
      <td>${appInfo.title}</td>
    </tr>
    <tr>
      <td>java</td>
      <td>${System.getProperty('java.version')} (${System.getProperty('java.vendor')} ${System.getProperty('java.vm.version')}) home: ${System.getProperty('java.home')}</td>
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
      <th>web/mount-module</th>
      <th>path</th>
    </tr>
    <% for (r in th.app.modules) { %>
    <tr>
      <td>${r.name}</td>
      <td>${UtWeb.isMountModule(r) ? 'yes' : ''}</td>
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
  <h2>Action factorys</h2>
  <table class="tst-table">
    <tr>
      <th>nm</th>
      <th>class</th>
    </tr>
    <%
      for (r in domainSvc.actionFactorys) {
        String nm = ""
        if (r instanceof INamed) {
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
          if (UtString.empty(cls)) {
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

  <!-- ===================================================================== -->
  <h2>Cache-Controls</h2>
  <table class="tst-table">
    <tr>
      <th>name</th>
      <th>mask</th>
      <th>value</th>
    </tr>
    <%
      for (r in cacheControlSvc.cacheControls) {
    %>
    <tr>
      <td>${r.name}</td>
      <td>${r.mask}</td>
      <td>${r.cacheControl}</td>
    </tr>
    <% } %>
  </table>

</jc:page>