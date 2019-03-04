<%@ page import="jandcode.jc.*" %>
<%
  GspScript th = this

  // текущий файл для вывода: temp/gendir/result1.txt
  def curFile = th.currentFile

  // базовый каталог для вывода: temp/gendir
  def outDir = th.outDir

  // вывод в файл temp/gendir/result1.txt
  th.out('1')

  // меняем файл на temp/gendir/f2.txt, указывая имя относительно outDir
  th.changeFile("f2.txt")

  // вывод в файл temp/gendir/f2.txt
  th.out('2')

  // меняем файл на ранее используемый temp/gendir/result1.txt
  // и указывая параметр append=true, открываем его в режиме добавления
  th.changeFile("result1.txt", true)

  // добавляем данные в файл temp/gendir/result1.txt
  th.out('3')

  // меняем файл на temp/gendir/f3.txt, предыдущий temp/gendir/result1.txt
  // остается открытым
  th.pushFile("f3.txt")

  // выводим в файл temp/gendir/f3.txt
  th.out('4')

  // возвращаемся к файлу temp/gendir/result1.txt
  th.popFile()

  // выводим в файл temp/gendir/result1.txt
  th.out('5')

%>
