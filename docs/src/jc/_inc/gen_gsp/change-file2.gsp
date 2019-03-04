<%@ page import="jandcode.jc.*" %>
<%
  GspScript th = this

  // текущий файл для вывода temp/gendir/result1.txt, однако он еще не создан,
  // так как вывода еще не было
  def curFile = th.currentFile

  // выводим в набор файлов
  for (i in 1..3) {
    th.changeFile("f-${i}.txt")
    th.out(i)
  }

  // будут созданы файлы:
  // temp/gendir/f-1.txt
  // temp/gendir/f-2.txt
  // temp/gendir/f-3.txt
%>
