<%@ page import="jandcode.core.jsa.jsmodule.*; jandcode.core.jsa.utils.*; jandcode.commons.*; jandcode.core.*; jandcode.core.web.*; jandcode.core.web.gsp.*;" %>
<%
  /*
    Сборник из всех svg-иконок всех модулей.
   */
  BaseGsp th = this

  SvgIconGenerator g = th.create(SvgIconGenerator);
  g.add("[*]/images/icons/**/*.svg");
  String gText = g.generate()

  if (th.app.debug) {
    JsModuleBuilder b = th.inst(JsModuleBuilder)
    for (f in g.usingFiles) {
      b.addModifyDepend(f.path)
    }
  }
%>
let jsaBase = require('jandcode.core.jsa.base')
module.exports = ${gText}
