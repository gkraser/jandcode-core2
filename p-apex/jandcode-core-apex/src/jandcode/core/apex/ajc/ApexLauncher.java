package jandcode.core.apex.ajc;

public class ApexLauncher {

    public static void launch(String[] args, Class mainScript) {
        ApexJcMain m = new ApexJcMain();
        m.run(args, mainScript.getName());
    }

}
