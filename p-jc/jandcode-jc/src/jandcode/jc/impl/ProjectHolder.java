package jandcode.jc.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.jc.*;

import java.io.*;
import java.text.*;
import java.util.*;

/**
 * Хранилище проектов
 */
public class ProjectHolder implements IProjects {

    private Ctx ctx;

    // реестр проектов
    private LinkedHashMap<String, Project> items = new LinkedHashMap<>();

    // проекты, которые в данный момент загружаются
    private Set<Project> currentLoadProjects = new HashSet<>();

    // beforeLoad, которые уже были выполнены
    private Set<String> executedBeforeLoad = new HashSet<>();

    // кеш корневых проектов по путям. ключ - для какого пути, значение - какой проект
    // определен как корневой
    private Map<String, Project> rootProjects = new HashMap<>();

    public ProjectHolder(Ctx ctx) {
        this.ctx = ctx;
    }

    public Project load(String projectPath) {
        String pf = resolveProjectFile(UtFile.getWorkdir(), projectPath);
        if (pf == null) {
            throw new XError("Не найден файл проекта {0}", projectPath);
        }
        Project p = items.get(pf);
        if (p != null) {
            if (currentLoadProjects.contains(p)) {
                throw new XError("Циклическая загрузка проекта {0}", pf);
            }
            return p;
        }

        // существует?
        boolean existPF = UtFile.exists(pf);
        // имеет имя project.jc?
        boolean isProjectJc = UtFile.filename(pf).equals(JcConsts.PROJECT_FILE);

        // .jc-root
        if (existPF && isProjectJc && !executedBeforeLoad.contains(pf)) {
            String markerFile = UtFile.findFileUp(JcConsts.JC_ROOT_FILE, UtFile.path(pf));
            if (markerFile != null && !executedBeforeLoad.contains(markerFile)) {
                executedBeforeLoad.add(markerFile);
                ctx.getLog().debug(MessageFormat.format(JcConsts.JC_ROOT_FILE + " found in  [{0}]", UtFile.path(markerFile)));
                String pfRoot = UtFile.join(UtFile.path(markerFile), JcConsts.PROJECT_FILE);
                if (UtFile.exists(pfRoot)) {
                    ctx.getLog().debug(MessageFormat.format(JcConsts.PROJECT_FILE + " as rootProject found in  [{0}]", UtFile.path(markerFile)));
                    load(pfRoot);
                }
            }
        }

        // создаем новый проект
        Project newProject = new ProjectImpl(ctx, pf);

        // выполняем beforeLoad, если он есть и не выполнялся
        if (existPF && !executedBeforeLoad.contains(pf)) {
            executedBeforeLoad.add(pf);
            String scriptBeforeLoad = ctx.getBeforeLoadProjectScript(pf);
            if (scriptBeforeLoad != null) {

                // в рамках нового проекта выполняем beforeLoad
                ctx.getLog().debug(MessageFormat.format("before load project [{0}]", pf));
                try {
                    IBeforeLoadScript b = (IBeforeLoadScript) newProject.create(scriptBeforeLoad);
                    b.executeBeforeLoad();
                    ctx.getLog().debug(MessageFormat.format("end before load project [{0}]", pf));
                } catch (Exception e) {
                    throw new XErrorMark(e, "file: " + pf);
                }
            }
        }

        // снова проверяем реестр на наличие проекта
        p = items.get(pf);
        if (p != null) {
            if (currentLoadProjects.contains(p)) {
                throw new XError("Циклическая загрузка проекта после beforeLoad {0}", pf);
            }
            return p;
        }

        ctx.getLog().debug(MessageFormat.format("load project [{0}]", pf));
        try {
            // не существует проект, используем тот, для кого уже beforeLoad был выполнен
            // там мог быть classpath() и про него проект хочет знать
            p = newProject;

            // кешируем
            items.put(pf, p);

            currentLoadProjects.add(p);
            try {
                //проект уже в списке, но еще не загружен. Уведомляем
                ctx.fireEvent(new JcConsts.Event_ProjectAdded(p));

                // инициализация
                if (existPF) {
                    p.include(pf);
                }

            } finally {
                currentLoadProjects.remove(p);
            }

            //выполняем afterLoad
            p.fireEvent(new JcConsts.Event_ProjectAfterLoad(p));

            //выполняем afterLoadAll
            p.fireEvent(new JcConsts.Event_ProjectAfterLoadAll(p));

            ctx.getLog().debug(MessageFormat.format("end load project [{0}]", pf));
        } catch (Exception e) {
            throw new XErrorMark(e, "file: " + pf);
        }
        // готово
        return p;
    }

    public Collection<Project> getProjects() {
        return items.values();
    }

    public String resolveProjectFile(String basePath, String projectPath) {
        String path = UtFile.abs(basePath);
        if (!UtString.empty(projectPath)) {
            if (UtFile.isAbsolute(projectPath)) {
                path = projectPath;
            } else {
                path = UtFile.join(path, projectPath);
            }
        }
        //

        path = UtFile.absCanonical(path);
        File f;

        // если не указано расширение, то подставим '.jc' и проверим файл
        String ext = UtFile.ext(path);
        if (!"jc".equals(ext)) {
            f = new File(path + ".jc");
            if (f.exists() && f.isFile()) {
                return f.getAbsolutePath();
            }
        }

        f = new File(path);

        if (!f.exists()) {
            return null;
        }

        if (f.isDirectory()) {
            f = new File(f, JcConsts.PROJECT_FILE);
        }
        if (f.exists() && f.isFile()) {
            return f.getAbsolutePath();
        }
        return null;
    }

    public Project getRootProject(String path) {
        path = UtFile.abs(path);
        Project p = this.rootProjects.get(path);
        if (p != null) {
            return p;
        }
        String markerFile = UtFile.findFileUp(JcConsts.JC_ROOT_FILE, path);
        if (markerFile == null) {
            return null;
        }
        p = load(UtFile.path(markerFile));
        this.rootProjects.put(path, p);
        return p;
    }

}
