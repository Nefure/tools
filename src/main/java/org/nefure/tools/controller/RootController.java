package org.nefure.tools.controller;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.nefure.fxscaffold.annotion.Component;
import org.nefure.fxscaffold.annotion.Resource;
import org.nefure.fxscaffold.container.SceneFactory;

/**
 * @author nefure
 * @date 2022/10/10 14:59
 */

@Component("root")
public class RootController{

    @Resource
    private Stage stage;

    @Resource
    private SceneFactory factory;

    public void openTreeModule() {
        Scene tree = factory.getScene("tree");
        stage.setScene(tree);
    }

    public void openCrudBuilder(){
        Scene crudBuilder = factory.getScene("crudBuilder");
        stage.setScene(crudBuilder);
    }

}
