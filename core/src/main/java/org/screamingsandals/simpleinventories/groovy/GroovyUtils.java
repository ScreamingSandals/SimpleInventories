package org.screamingsandals.simpleinventories.groovy;

import groovy.lang.Closure;

public class GroovyUtils {
    public static void internalCallClosure(Closure<?> closure, Object delegate) {
        closure.setDelegate(delegate);
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.call(delegate);
    }
}
