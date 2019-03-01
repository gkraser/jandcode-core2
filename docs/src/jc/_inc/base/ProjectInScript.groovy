package jc._inc.base

import jandcode.jc.*

class ProjectInScript extends ProjectScript {

    void onInclude() {
        // текущий проект
        Project p1 = getProject()

        // это groovy, можно писать и так:
        Project p2 = project

        // получение имени проекта
        String s1 = project.getName()
        String s2 = project.name

        // скрипт делегирует методы интерфейса
        // jandcode.jc.Project проекту,
        // поэтому можно писать и так:
        String s3 = getName()
        String s4 = name

        // а это получение пути относительно
        // рабочего  каталог проекта (workdir, wd):
        String wd1 = project.wd("temp")
        String wd2 = wd("temp")
    }

}
