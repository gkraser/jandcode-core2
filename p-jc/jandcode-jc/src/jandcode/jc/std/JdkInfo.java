package jandcode.jc.std;

/**
 * Информация о jdk для gen-idea
 */
public class JdkInfo {

    /**
     * Версия jdk в виде числа (например: 14).
     */
    public String getJdkVersion() {
        return System.getProperty("java.specification.version");
    }

    /**
     * Возвращает languageVersion для настройки проекта idea для
     * указанной версии jdk
     */
    public String getIdeaLanguageLevelForJdkVersion(String jdkVersion) {
        String v;
        if ("8".equals(jdkVersion)) {
            v = "1_8";
        } else if ("9".equals(jdkVersion)) {
            v = "1_9";
        } else {
            v = jdkVersion;
        }
        return "JDK_" + v;
    }

}
