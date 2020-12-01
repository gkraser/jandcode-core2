<%@ page import="jandcode.mdoc.gsp.*; jandcode.commons.*" %>
<!doctype html>
<html>
<%

  //  Это шаблон страницы.
  //  Все html генерятся по этому шаблону

  // контекст генерации
  GspTemplateContext th = context
  // утилиты
  TocGspUtils tocUtils = new TocGspUtils(th)

%>
<head>
  <meta charset="UTF-8">
  <title>${th.pageTitle}</title>

  <link rel="stylesheet" href="${th.ref('_theme/lib/normalize/normalize.css')}"/>
  <link rel="stylesheet" href="${th.ref('_theme/css/prism.css')}"/>
  <link rel="stylesheet" href="${th.ref('_theme/css/admonition.css')}"/>
  <link rel="stylesheet" href="${th.ref('_theme/css/style.css')}"/>
  <% for (f in th.doc.sourceFiles.findFiles('_theme/css/style-*.css')) { %>
  <link rel="stylesheet" href="${th.ref(f.path)}"/>
  <% } %>
  <script src="${th.ref('_theme/lib/prism/prism.js')}"></script>
  <script src="${th.ref('_theme/js/prism-ext.js')}"></script>
  <script>
      window.mdoc = window.mdoc || {};
      mdoc.indexRef = '${th.ref(th.builder.toc.topic.id)}';
      mdoc.topicFile = '${th.outFile.path}';
      mdoc.topicId = '${th.topic.id}';
  </script>
  <% if (th.topic.toc.childs.size() > 0) { %>
  <script>
      <%
      out(new TocJsonUtils(th.builder).makeTocJsStr("mdoc.tocTopic", th.topic.toc))
    %>
  </script>
  <% } %>
  <script src="${th.ref('toc.js')}"></script>
</head>

<body>
<div class="page-wrap">
  <div class="page-header">
    <div class="page-header-item document-title">
      <a href="${th.ref(th.builder.toc.topic.id)}">${th.docTitle}</a>
    </div>

    <div class="page-header-item split">
    </div>

    <div class="page-header-item menu-item">
    </div>
  </div>

  <div class="page-body">

    <div class="page-tools">
      <div class="tools-block">
        <div class="tools-block-title">
          Содержание
        </div>

        <div class="tools-block-body">
          <div class="doc-toc">
            ${tocUtils.makeToc(th.builder.toc, th.topic)}
          </div>
        </div>
      </div>
    </div>

    <div class="page-content">
      <% th.out(th.generate(th.contentTemplate)) %>
    </div>

  </div>

  <div class="page-footer">

  </div>

</div>

</body>
</html>
