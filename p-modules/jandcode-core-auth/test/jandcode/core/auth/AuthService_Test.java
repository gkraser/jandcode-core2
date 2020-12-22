package jandcode.core.auth;

import jandcode.core.auth.std.*;
import jandcode.core.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class AuthService_Test extends App_Test {

    @Test
    public void dummy_admin() throws Exception {
        AuthService authSvc = app.bean(AuthService.class);
        //
        AuthUser u = authSvc.login(new DefaultUserPasswdAuthToken("admin", "111"));
        //
        assertEquals(u.getAttrs().getString("username"), "admin");
        //
    }

    @Test
    public void dummy_error_auth() throws Exception {
        AuthService authSvc = app.bean(AuthService.class);
        //
        AuthUser u = null;
        try {
            u = authSvc.login(new DefaultUserPasswdAuthToken("admin", "111_"));
            fail();
        } catch (Exception e) {
            utils.showError(e);
        }
        //
    }


}
