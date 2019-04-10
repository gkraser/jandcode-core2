<%@ page import="jandcode.commons.conf.*; jandcode.commons.error.*; jandcode.commons.named.*; jandcode.commons.variant.*; jandcode.mdoc.source.*; jandcode.mdoc.gsp.*; jandcode.commons.*" %>
<%

  /*
    Дерево, имитирующее файловую систему

    file = имя файла conf, в котором описана структура каталогов.
           Каждый объект в ней - это каталог или файл.
           Если есть дочерние - каталог. Если нет - файл.
           Если нужен каталог без файлов - указать свойство folder=true.
           В свойстве mark - текст который буде показан после имени сереньким

    root = путь conf до объекта, который показывать. Если пусто - то весь загруженный

    join = список путей, которые нужно объеденить и полученный результат показать.
           Если содержит '@', то все что до '@' - что объединять, все что после - куда.

  */

  // контекст генерации
  GspTemplateContext th = context

  // атрибуты, переданые команде
  IVariantMap attrs = th.args.attrs

  String file = attrs.getString("file")
  String root = attrs.getString("root")
  List<String> join = UtCnv.toList(attrs.getString("join"))

  def ref = th.builder.refResolver.resolveRefInc(file, th.outFile)
  if (ref == null) {
    throw new XError("Файл не найден: ${file}")
  }
  def confRoot = UtConf.create()
  UtConf.load(confRoot).fromString(ref.sourceFile.text)
  if (root != "") {
    confRoot = confRoot.findConf(root, true)
  }
  if (join.size() > 0) {
    Conf tmp = UtConf.create()
    for (String join1 : join) {
      int a = join1.indexOf('@')
      if (a == -1) {
        tmp.join(confRoot.findConf(join1))
      } else {
        tmp.findConf(join1.substring(a + 1), true).join(confRoot.findConf(join1.substring(0, a)))
      }
    }
    confRoot = tmp
  }


  def isFolder = { Conf conf ->
    return conf.getConfs().size() > 0 || conf.getBoolean("folder")
  }

  //
  def outFolder, outIt
  outFolder = { Conf folder, int level = 0 ->
    if (level > 0) {
      out("<li class='folder'>")
      outIt(folder, level, true)
    }
    List<Conf> childs = folder.getConfs()
    if (childs.size() > 0) {
      out("<ul>")
      for (Conf child : childs) {
        if (isFolder(child)) {
          outFolder(child, level + 1)
        }
      }
      for (Conf child : childs) {
        if (!isFolder(child)) {
          out("<li class='file'>")
          outIt(child, level, false)
          out("</li>")
        }
      }
      out("</ul>")
    }
    if (level > 0) {
      out("</li>")
    }
  }

  //
  outIt = { Conf item, int level, boolean folder ->
    def itemCls = folder ? "folder" : "file"
    out("<span class='item ${itemCls}'>${item.getName()}</span>")
    String mark = item.getString("mark")
    if (mark != "") {
      out("<span class='mark'>${mark}</span>")
    }
  }

%>
<div class="cm-tree-fs">
  <%
    outFolder(confRoot)
  %>
</div>