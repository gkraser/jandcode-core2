package jandcode.commons.cli;

import jandcode.commons.ansifer.*;

/**
 * Помощь командной строки с подсветкой опций
 */
public class CliHelpAnsifer extends CliHelp {

    private String optNameStyle = "opt-name";
    private String optHelpStyle = "opt-help";

    public CliHelpAnsifer() {
    }

    public CliHelpAnsifer(String optNameStyle, String optHelpStyle) {
        this.optNameStyle = optNameStyle;
        this.optHelpStyle = optHelpStyle;
    }

    public CliHelpAnsifer(String optNameStyle) {
        this.optNameStyle = optNameStyle;
    }

    /**
     * Стиль для опций
     */
    public String getOptNameStyle() {
        return optNameStyle;
    }

    public void setOptNameStyle(String optNameStyle) {
        this.optNameStyle = optNameStyle;
    }

    /**
     * Стиль для помощи
     */
    public String getOptHelpStyle() {
        return optHelpStyle;
    }

    public void setOptHelpStyle(String optHelpStyle) {
        this.optHelpStyle = optHelpStyle;
    }

    protected String prepareOptionName(String text) {
        return Ansifer.getInst().style(getOptNameStyle(), super.prepareOptionName(text));
    }

    protected String prepareOptionHelp(String text) {
        return Ansifer.getInst().style(getOptHelpStyle(), super.prepareOptionHelp(text));
    }

}
