<%@ page import="jandcode.core.jsa.utils.SvgIconGenerator; jandcode.core.jsa.utils.*; jandcode.commons.*; jandcode.core.*; jandcode.core.web.*; jandcode.core.web.gsp.*;" %>
<%
  BaseGsp th = this

  jandcode.core.jsa.utils.SvgIconGenerator g = th.create(SvgIconGenerator);
  g.add("jandcode/core/jsa/vue/_tst/pages/images/icons/**/*.svg");
%>
let jsaBase = require('jandcode.core.jsa.base')
module.exports = ${g.generate()}
jsaBase.svgicon.registerIcons(module.exports)
