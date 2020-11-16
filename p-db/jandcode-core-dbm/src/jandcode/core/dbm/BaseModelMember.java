package jandcode.core.dbm;

import jandcode.commons.error.*;
import jandcode.core.*;

/**
 * Предок для компонентов с информацией о модели
 */
public abstract class BaseModelMember extends BaseComp implements IModelMember {

    private Model model;

    public Model getModel() {
        if (model == null) {
            throw new XError("Экземпляр компонента создан вне контекста модели: {0}", getClass());
        }
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public App getApp() {
        return getModel().getApp();
    }

}
