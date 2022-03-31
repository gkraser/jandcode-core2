package jandcode.core.dbm.verdb;

public interface IVerdbVersionLink extends Comparable<IVerdbVersionLink> {

    /**
     * Версия каталога. NNN.0.0
     */
    VerdbVersion getVersion();

}
