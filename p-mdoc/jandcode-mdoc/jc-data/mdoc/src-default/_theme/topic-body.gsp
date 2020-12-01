<%@ page import="jandcode.mdoc.topic.*; jandcode.mdoc.gsp.*" %>
<%

  //  Тело статьи

  // контекст генерации
  GspTemplateContext th = context
  // утилиты
  TocGspUtils tocUtils = new TocGspUtils(th)

  // элемент общего содержания, в котором статья
  Toc tocInRoot = th.builder.toc.findByTopic(th.topic.id)
%>
<div class="topic-breadcrumb">
  ${tocUtils.makeBreadcrumb(th.builder.toc, th.topic)}
</div>

<div class="topic-header">
  <h1>${th.topic.title}</h1>
</div>
<%
  if (th.builder.mode.serveDebug) {
    th.out(th.generate("_theme/debug/topic-panel.gsp"))
  }
%>
<% if (th.topic.toc.childs.size() > 0) { %>
<div class="topic-toc">
  <div class="topic-toc-title">Содержание</div>

  <div class="topic-toc-content">
    ${tocUtils.makeToc(th.topic.toc)}
  </div>
</div>
<% } %>
<div class="topic-body">
  ${th.topicBody}
</div>
<% if (th.topic.toc.childs.size() == 0 && tocInRoot != null && tocInRoot.childs.size() > 0) { %>
<div class="topic-toc">
  <div class="topic-toc-title">Содержание</div>

  <div class="topic-toc-content">
    ${tocUtils.makeToc(tocInRoot)}
  </div>
</div>
<% } %>
