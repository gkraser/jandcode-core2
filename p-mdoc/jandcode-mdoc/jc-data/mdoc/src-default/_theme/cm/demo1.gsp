<%@ page import="jandcode.commons.variant.*; jandcode.mdoc.source.*; jandcode.mdoc.gsp.*; jandcode.commons.*" %>
<%

  //  Это пример обработчика команды

  // контекст генерации
  GspTemplateContext th = context

  // атрибуты, переденые команде
  IVariantMap attrs = th.args.attrs

%>
<div>
  Вывод команды <b>demo1</b> с атрибутами
  <code>${UtString.xmlEscape(attrs.toString())}</code>
</div>

