<%@ page import="jandcode.core.web.gsp.*; jandcode.core.jsa.jsmodule.*; jandcode.commons.*; jandcode.core.web.*;" %>
<%
  BaseGsp th = this
  def svc = th.app.bean(JsModuleService)
  def bh = svc.getModule("jandcode/core/jsa/base/js/boot/babelHelpers.js")
  def mc = svc.getModule("jandcode/core/jsa/base/js/boot/module-core.js")
%>
${bh.text}
${mc.text}


