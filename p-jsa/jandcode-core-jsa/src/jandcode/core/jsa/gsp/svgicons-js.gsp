<%@ page import="jandcode.core.jsa.jsmodule.*; jandcode.core.jsa.utils.*; jandcode.commons.*; jandcode.core.*; jandcode.core.web.*; jandcode.core.web.gsp.*;" %>
<%
  /*
      Генерация javascript определения svg-иконок.
      Используется только внутри генератора js-модули типа svgicons.js.gsp.

      Параметры:
      
        path
          список масок с путями (строка через ',' или список). Если путь относительный,
          то он рассматривается относительно корневой gsp

   */
  BaseGsp th = this

  GspArgsUtils ar = new GspArgsUtils(this)
  Object path = ar.getValue("path", true);

  List<String> lstPath = UtCnv.toList(path);

  SvgIconGenerator g = th.create(SvgIconGenerator);

  for (p in lstPath) {
    if (UtVDir.isRelPath(p)) {
      g.add(th.context.rootGsp.path(p))
    } else {
      g.add(p);
    }
  }

  String gText = g.generate()

  if (th.app.env.dev) {
    JsModuleBuilder b = th.inst(JsModuleBuilder)
    for (f in g.usingFiles) {
      b.addModifyDepend(f.path)
      out("// ${f.path}\n")
    }
  }
%>
module.exports = ${gText}
