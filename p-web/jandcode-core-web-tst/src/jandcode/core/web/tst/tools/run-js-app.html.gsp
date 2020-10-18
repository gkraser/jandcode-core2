<%@ page import="jandcode.core.web.gsp.*; jandcode.core.*; jandcode.core.web.*; jandcode.commons.*" %>
<jc:page template="page/tst-main">
  <%
    BaseGsp th = this
    //
    List<ModuleInst> mods = UtWeb.getMountModules(th.app)

    def apps = { ModuleInst mod ->
      return UtWeb.expandPath(th.app, mod.getVPath() + "/js/**/*.gsp")
    }


  %>
  <h1>Run Js App</h1>

  <p>Все файлы [MODULE-PATH]/js/**/*.gsp считаем точками входа в приложение и просто
  их рендерим.</p>
  <ul>
    <%
      for (m in mods) {
        def lst = apps(m)
        if (lst.size() == 0) {
          continue
        }
    %>
    <li><b>${m.name}</b>
      <ul>
        <% for (itm in lst) { %>
        <li><a target="_blank"
               href="${th.ref('_tst/render/' + UtFile.removeExt(itm))}">${itm}</a>
        </li>
        <% } %>
      </ul>
    </li>
    <%
      }
    %>

  </ul>

</jc:page>