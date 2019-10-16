<%@ page import="jandcode.core.web.gsp.*; jandcode.core.web.*; jandcode.commons.*; jandcode.core.web.tst.*" %>
<jc:page template="page/tst-main">
  <%
    BaseGsp th = this
    String path = th.args.path
    def runFileFilter = th.app.create(TstRunFile)
    TstRes root = TstRes.createRoot(th.app, path, true, runFileFilter)

    int cols = 3

    def outA0 = { TstRes r, boolean fullpath = false ->
      String rf, text
      if (fullpath) {
        text = r.path
      } else {
        text = r.name
      }
      if (UtString.empty(text)) {
        text = "/"
      }
      String cls
      if (r.folder) {
        if (UtString.empty(r.path)) {
          rf = th.ref("_tst/run", [path: "ROOT"])
        } else {
          rf = th.ref("_tst/run", [path: r.path])
        }
        cls = 'ref-folder'
      } else {
        rf = th.ref("_tst/run", [path: "/${r.path}"])
        cls = 'ref-file'
        if (r.isGsp()) {
          cls = 'ref-file-gsp'
        }
      }
      if (r.path == root.path && !root.tstRoot) {
        if (text != "/") {
          def a = text.split("/")
          text = ""
          def cp = ""
          for (b in a) {
            if (text != "") {
              text += "/"
              cp += "/"
            }
            cp += b
            def rf1 = th.ref("/_tst/run", [path: cp])
            text += "<a href='${rf1}'>${b}</a>"
          }
        }
        th.out("<span class='ref ${cls}' href='${rf}'>${text}</span>")
      } else {
        th.out("<a class='ref ${cls}' href='${rf}'>${text}</a>")
      }
    }

    def outCell1 = { TstRes r, boolean fullpath = false ->
      int rows = r.childRows(cols)
      th.out("<tr><td class='cell1' rowspan='${rows}'>")
      outA0(r, fullpath)
      th.out("</td>")
    }

    def outCell2 = { TstRes r ->
      int rows = r.childRows(cols)
      for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
          String cls = ""
          if (i == rows - 1) {
            cls += " cell2 bottom"
          }
          TstRes r1 = r.childCell(i, j, cols)
          if (r1 != null) {
            cls += " filled"
          }
          th.out("<td class='${cls}'>")
          if (r1 != null) {
            outA0(r1)
          }
          th.out("</td>")
        }
        th.out("</tr>")
        if (i < rows - 1) {
          th.out("<tr>")
        }
      }
    }

  %>
  <h2 class="first">_tst/run</h2>
  <table class="tst-showpath-table">
    <% if (!root.tstRoot) { %>
    <tbody class="row">
    <% outCell1(root, true) %>
    <% outCell2(root) %>
    </tbody>
    <% } %>

    <% for (TstRes item in root.childs) { %>
    <tbody class="row">
    <% outCell1(item, root.tstRoot) %>
    <% outCell2(item) %>
    </tbody>
    <%
      }
    %>
  </table>

</jc:page>