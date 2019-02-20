package jandcode.jc.impl.just

import jandcode.jc.*
import org.junit.jupiter.api.*

class Log_Test extends CustomProjectTestCase {

    @Test
    public void test_Log1() throws Exception {
        ctx.log.verbose = true
        Project p = load("workdir1")
        //
        script(p).with {
            log.info(wd.join("temp"))
            log.info("info1")
            log.debug("debug1")
            log.call("call1")
            log("log1")
            ant.echo(message: "hello log from ant")
            log "log1 no parenst"
            log.info "info1 no parenst"
        }

    }

}
