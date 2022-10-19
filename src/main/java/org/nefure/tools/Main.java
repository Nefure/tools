package org.nefure.tools;

import org.nefure.fxscaffold.container.ApplicationContext;
import org.nefure.tools.view.MainMenu;

/**
 * @author 9528
 */
public class Main{

    public static void main(String[] args) {
        ApplicationContext.run(MainMenu.class, args);
    }

}