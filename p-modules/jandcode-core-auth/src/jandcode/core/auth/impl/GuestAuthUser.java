package jandcode.core.auth.impl;

/**
 * Гость
 */
public class GuestAuthUser extends CustomAuthUser {

    public boolean isGuest() {
        return true;
    }

}
