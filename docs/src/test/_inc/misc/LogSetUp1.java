package test._inc.misc;

import jandcode.commons.test.*;

public class LogSetUp1 extends Utils_Test {

    {
        // включить логгирование
        utils.logSetUp = true;

        // установить конфигурацию логгирования из каталога с тестом
        // можно не устанавливать, тогда будет использоватся конфигурация по умолчанию
        utils.logSetUpCfg = utils.getTestFile("_logback.xml");
    }

}