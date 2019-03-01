<%@ page import="jandcode.jc.*" %>
<%
  GspScript th = this

  // каталог в котором лежит скрипт
  def scrdir = th.scriptDir

  // копируем все ресурсы
  th.ant.copy(todir: th.outDir) {
    fileset(dir: srcdir) {
      include(name: '**/*.js')
      include(name: '**/*.css')
      include(name: '**/*.jpg')
    }
  }

  // генерируем html
%>
<!doctype html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Document</title>
</head>
<body>
text
</body>
</html>
