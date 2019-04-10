<%@ page import="jandcode.mdoc.topic.*; jandcode.mdoc.builder.*; jandcode.commons.simxml.*; jandcode.mdoc.gsp.*" %>
<%

  //  Полное содержание по файлам

  // контекст генерации
  GspTemplateContext th = context

  //
  def request = th.doc.app.bean("jandcode.web.WebService").request

  // строим содержание
  String xmlText = "" +
      "<root>" +
      "  <toc-root>" +
      "    <toc topic='**/*' type='files'/>" +
      "  </toc-root>" +
      "</root>";
  def xml = new SimXmlNode();
  xml.load().fromString(xmlText);
  def tb = new XmlTocBuilder(th.builder, xml, false);
  def toc = tb.buildToc();

  def outToc
  outToc = { Toc t, int level ->
    int pad = level * 20 + 10
    out("<tr>")

    def outFile
    if (t.topic != null) {
      outFile = th.builder.outFiles.findByTopicId(t.topic.id)
    }

    //
    String s = t.name
    String s1
    if (t.topic == null) {
      s = t.name + "/"
    } else if (outFile != null) {
      s = "<a href='${request.ref(outFile.path)}'>${t.name}</a>"
    }
    out("<td class='toc-name' style='padding-left:${pad}px;'>${s}</td>\n")

    s = ""
    if (t.topic != null) {
      s = "=&gt;"
    }
    out("<td>${s}</td>")
    //
    s = ""
    if (t.topic == null) {
      s = ""
    } else if (outFile != null) {
      s = "<a href='${request.ref(outFile.path)}'>${t.topic.sourceFile.path}</a>"
    }
    out("<td class='toc-file'>${s}</td>\n")

    //
    s = ""
    if (t.topic == null) {
      s = ""
    } else if (outFile != null) {
      s = "<a href='${request.ref(outFile.path)}'>${t.title}</a>"
    }
    out("<td class='toc-title'>${s}</td>\n")

    out("</tr>")

    for (t1 in t.childs) {
      outToc(t1, level + 1)
    }

  }
%>
<div class="topic-header">
  <h1>Полное содержание по файлам</h1>
</div>
<% th.out(th.generate("_theme/debug/topic-panel.gsp")) %>
<div class="topic-body">
  <br>
  <table class="debug-table-toc">
    <%
      for (t1 in toc.childs) {
        outToc(t1, 0)
      }
    %>
  </table>
</div>
