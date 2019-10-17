<%@ page import="jandcode.commons.*; jandcode.mdoc.source.*; jandcode.mdoc.topic.*; jandcode.mdoc.builder.*; jandcode.commons.simxml.*; jandcode.mdoc.gsp.*" %>
<%

  //  Все файлы в проекте

  // контекст генерации
  GspTemplateContext th = context

  //
  def request = th.doc.app.bean("jandcode.core.web.WebService").request

  // строим дерево файлов
  TreeNodeSourceFile root = new TreeNodeSourceFile(th.builder.doc.sourceFiles.items)

  def outNode
  outNode = { TreeNodeSourceFile t, int level ->
    int pad = level * 14 + 10
    out("<tr>")

    def outFile
    def topic
    if (!t.folder) {
      topic = th.builder.doc.topics.find(t.sourceFile.path)
      if (topic != null) {
        outFile = th.builder.outFiles.findByTopicId(topic.id)
      }
    }

    //
    String s = t.name
    String s1
    if (t.folder) {
      s = t.name + "/"
    } else if (outFile != null) {
      s = "<a href='${request.ref(outFile.path)}'>${t.name}</a>"
    }
    out("<td class='toc-name' style='padding-left:${pad}px;'>${s}</td>\n")

    //
    s = ""
    if (!t.folder) {
      s = "=&gt;"
    }
    out("<td>${s}</td>")

    //
    s = ""
    s1 = ""
    if (!t.folder) {
      s = t.sourceFile.path
      if (outFile != null) {
        s = "<a href='${request.ref(outFile.path)}'>${topic.sourceFile.path}</a>"
      }
      s1 = t.sourceFile.realPath
      if (s1 == null) {
        s1 = ""
      }
      if (s1.startsWith("file:")) {
        s1 = UtFile.vfsPathToLocalPath(s1)
      }
    }
    out("<td>${s}<div class=\"toc-realfile\">${s1}</div></td>")

    out("</tr>")

    for (t1 in t.childs) {
      outNode(t1, level + 1)
    }

  }
%>
<div class="topic-header">
  <h1>Все файлы</h1>
</div>
<% th.out(th.generate("_theme/debug/topic-panel.gsp")) %>
<div class="topic-body">
  <br>
  <table class="debug-table-toc">
    <%
      for (t1 in root.childs) {
        outNode(t1, 0)
      }
    %>
  </table>
</div>
