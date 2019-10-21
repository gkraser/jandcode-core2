package jandcode.core.apex.ajc;

public class ApexLauncher {

    public static void launch(String[] args, Class mainScript) {
        ApexJc m = new ApexJc();
        m.run(args, mainScript.getName());
    }

}
