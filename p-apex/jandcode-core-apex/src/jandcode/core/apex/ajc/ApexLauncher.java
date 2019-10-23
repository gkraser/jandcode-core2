package jandcode.core.apex.ajc;

/**
 * Запускалка jc.
 * Вызывается из main-метода приложения.
 */
public class ApexLauncher {

    public static void launch(String[] args, Class mainScript) {
        ApexJcMain m = new ApexJcMain();
        m.run(args, mainScript.getName());
    }

}
