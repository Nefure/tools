package org.nefure.tools.view;

import javafx.stage.Stage;
import org.nefure.fxscaffold.Entrance;
import org.nefure.fxscaffold.container.ApplicationContext;
import org.nefure.tools.config.StageConfig;

/**
 * @author nefure
 * @date 2022/10/12 21:06
 */
public class MainMenu extends Entrance {

    @Override
    public void onStart(Stage stage,ApplicationContext context) {
        StageConfig config = context.getBean(StageConfig.class);
        context.setMainCss(context.loadCss(config.getMainCss()));
        stage.setTitle(config.getTitle());
        stage.setResizable(Boolean.TRUE.equals(config.getResizable()));
        stage.getIcons().add(context.loadImage(config.getIcon()));
        stage.setScene(context.getScene(config.getRootFxml()));
        stage.show();
    }
}
