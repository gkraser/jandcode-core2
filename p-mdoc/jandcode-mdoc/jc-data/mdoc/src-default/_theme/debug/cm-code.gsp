<%@ page import="jandcode.mdoc.source.*; jandcode.mdoc.gsp.*; jandcode.commons.*" %>
<%

  //  Это шаблон отладочной панели для cm code

  // контекст генерации
  GspTemplateContext th = context

  Map attrs = th.args.attrs
  SourceFile sourceFile = th.args.sourceFile
  def hasEditor = th.doc.app.beanFactory.findBean("jandcode.mdoc.web.CmdRunner") != null &&
      sourceFile != null && !UtString.empty(sourceFile.realPath)

%>
<div class="debug-panel debug-panel-cm-code">
  <div class="toolbar">
    <div class="type">
      :code:
    </div>
    <div class="info sourceFile">
      ${sourceFile.path}
    </div>
    <div class="info grow">
      ${UtString.xmlEscape(attrs.toString())}
    </div>
    <% if (hasEditor) { %>
    <button onclick="mdoc.edit('${sourceFile.path}')">Edit</button>
    <% } %>
  </div>
</div>