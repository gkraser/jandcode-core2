package jandcode.jc.std.idea

import jandcode.commons.simxml.*
import jandcode.jc.*
import jandcode.jc.std.*

/**
 * Базовый класс для ipr,iml,iws представлений xml
 */
abstract class BaseXml {

    SimXml root
    Project project

    BaseXml(Project project, SimXml root) {
        this.root = root
        this.project = project
    }

    public Ctx getCtx() {
        return project.getCtx()
    }

    /**
     * Получить имя проекта для idea
     */
    String getIdeaProjectName(Project p) {
        GenIdea gi = p.getIncluded(GenIdea)
        if (gi == null) {
            return p.name
        }
        return gi.ideaProjectName
    }

    /**
     * Получить имя текущего проекта для idea
     */
    String getIdeaProjectName() {
        return getIdeaProjectName(project)
    }

}
