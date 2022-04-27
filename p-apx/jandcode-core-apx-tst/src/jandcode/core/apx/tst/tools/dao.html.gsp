<%@ page import="jandcode.core.dao.*; jandcode.core.std.*; jandcode.commons.named.*; jandcode.core.web.gsp.*; jandcode.core.web.*; jandcode.commons.*" %>
<jc:page template="page/tst-main">
  <%
    //
    BaseGsp th = this
    //
    def daoSvc = th.app.bean(DaoService)

    def makeParamsInfo = { DaoMethodDef md ->
      def r = ['{']
      for (p in md.params) {
        if (p.index > 0) {
          r.add(', ')
        }
        r.add(p.name)
        r.add(': ')
        r.add("""<span class="type-name">${p.parameter.type.simpleName}</span>""")
      }
      r.add('}')
      return r.join('')
    }

  %>
  <!-- ===================================================================== -->
  <style>
  .method-name {
    font-size: 0.9em;
    color: #999;
    word-wrap: break-word;
    word-break: break-word;
  }

  .type-name {
    font-size: 0.8em;
    color: #aaa;
    word-wrap: break-word;
    word-break: break-word;
  }
  </style>
  <!-- ===================================================================== -->
  <h2>Все доступные Dao</h2>

  <%
    for (dhn in daoSvc.daoHolderNames) {
      DaoHolder dh = daoSvc.getDaoHolder(dhn)
      List<DaoHolderItem> items = []
      items.addAll(dh.items)
      items.sort { a, b -> a.name <=> b.name }
  %>
  <h3>dao/holder: ${dhn}</h3>
  <table class="tst-table">
    <tr>
      <th>method</th>
      <th>params</th>
      <th>invoker</th>
      <th>java method</th>
    </tr>
    <% for (r in items) { %>
    <tr>
      <td>${r.name}</td>
      <td>${makeParamsInfo(r.methodDef)}</td>
      <td>${dh.resolveDaoInvokerName(r)}</td>
      <td>
        <div>${r.methodDef.classDef.cls.name}
          <span class="method-name">
            ${r.methodDef.method.name}
          </span>
        </div>
      </td>
    </tr>
    <% } %>
  </table>
  <%
    }
  %>

</jc:page>