<%@ page import="jandcode.jsa.utils.*; jandcode.commons.*; jandcode.core.*; jandcode.core.web.*; jandcode.core.web.gsp.*;" %>
<%
  BaseGsp th = this

  SvgIconGenerator g = th.create(SvgIconGenerator);
  g.add("jandcode/jsa/core/_tst/pages/images/icons/**/*.svg");
%>
let jsaBase = require('jandcode.jsa.base')
module.exports = ${g.generate()}
jsaBase.svgicon.registerIcons(module.exports)
