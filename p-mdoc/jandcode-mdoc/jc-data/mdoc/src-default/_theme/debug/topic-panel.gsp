<%@ page import="jandcode.mdoc.*; jandcode.mdoc.gsp.*; jandcode.commons.*" %>
<%

  //  Это шаблон отладочной панели для статьи

  // контекст генерации
  GspTemplateContext th = context

  //
  def request = th.doc.app.bean("jandcode.core.web.WebService").request
  def hasEditor = (th.doc.app.beanFactory.findBean("jandcode.mdoc.web.CmdRunner") != null) &&
      !UtString.empty(th.topic.sourceFile.realPath)
  def time = System.currentTimeMillis()
  def autoReload = th.builder.mode.serveDebug && th.doc.cfg.props.getBoolean(MDocConsts.DOC_PROP_SERVE_AUTORELOAD, true)
%>
<link rel="stylesheet" href="${th.ref('_theme/debug/style.css')}"/>
<script src="${th.ref('_theme/lib/jquery/jquery.min.js')}"></script>
<script src="${th.ref('_theme/debug/utils.js')}"></script>
<script>
    mdoc.baseUrl = '${request.ref('/')}';
    mdoc.sourceFile = '${th.topic.sourceFile.path}';
</script>
<div id="edit-marker" class="edit-marker"
     title="Нажмите 'E' or 'e' для редактирования этого места или сделайте двойной щелчок"><img
    src="${th.ref('_theme/debug/edit-marker.png')}"></div>
<div class="debug-panel debug-panel-topic">
  <div class="toolbar">
    <div class="type">
      :topic:
    </div>
    <div class="grow info sourceFile">
      ${th.topic.sourceFile.path}
    </div>
    <button
        onclick="location.href = mdoc.baseUrl + '-debug/page-toc-files.html'">TOC</button>
    <button
        onclick="location.href = mdoc.baseUrl + '-debug/page-allfiles.html'">AllFiles</button>
    <button onclick="location.href = '?prod';">View as prod</button>
    <button onclick="this.innerHTML = 'Wait...';
    this.disabled = true;
    mdoc.rebuild()">Rebuild</button>
    <% if (hasEditor) { %>
    <button onclick="mdoc.edit('${th.topic.sourceFile.path}')">Edit</button>
    <% } %>
  </div>
</div>
<% if (autoReload) { %>
<script>
    mdoc.start_checkChanged('${th.outFile.path}', '${time}')
</script>
<% } %>
