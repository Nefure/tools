package org.nefure.tools.config;

import org.nefure.fxscaffold.annotion.Config;

/**
 * @author nefure
 * @date 2022/10/10 15:53
 */
@Config("stage")
public class StageConfig {

    private String title;

    private String rootFxml;

    private String icon;

    private String mainCss;

    private Boolean resizable;

    public String getTitle() {
        return title;
    }

    public String getRootFxml() {
        return rootFxml;
    }

    public String getIcon() {
        return icon;
    }

    public String getMainCss() {
        return mainCss;
    }

    public Boolean getResizable() {
        return resizable;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRootFxml(String rootFxml) {
        this.rootFxml = rootFxml;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setMainCss(String mainCss) {
        this.mainCss = mainCss;
    }

    public void setResizable(Boolean resizable) {
        this.resizable = resizable;
    }

}
