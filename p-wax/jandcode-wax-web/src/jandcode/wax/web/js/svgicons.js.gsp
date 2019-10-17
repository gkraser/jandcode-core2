<%@ page import="jandcode.core.jsa.utils.*; jandcode.commons.*; jandcode.core.*; jandcode.core.web.*; jandcode.core.web.gsp.*;" %>
<%
  /*
    Сборник из всех svg-иконок всех модулей.
    Подключается автоматически.
   */
  BaseGsp th = this

  SvgIconGenerator g = th.create(SvgIconGenerator);
  g.add("[*]/images/svgicons/**/*.svg");
%>
let jsaBase = require('jandcode.core.jsa.base')
module.exports = ${g.generate()}
jsaBase.svgicon.registerIcons(module.exports)
