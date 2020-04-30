package org.screamingsandals.simpleinventories.groovy;

public class CantObtainGroovyException extends Exception {
    public CantObtainGroovyException() {
        super("Can't download groovy needed by GroovyLoader!");
    }
}
