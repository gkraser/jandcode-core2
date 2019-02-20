package jandcode.jc.impl.version;

import jandcode.jc.*;

/**
 * Реализация как в указанном проекте
 */
public class ProjectVersion extends BaseVersion {

    private Project project;

    public ProjectVersion(Project project) {
        this.project = project;
    }

    public String getText() {
        return this.project.getVersion().getText();
    }

}
