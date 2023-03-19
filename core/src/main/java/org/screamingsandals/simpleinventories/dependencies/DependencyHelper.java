package org.screamingsandals.simpleinventories.dependencies;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DependencyHelper {
    GROOVY("groovy.util.GroovyScriptEngine", "Groovy", "3.0.16"),
    UNIVOCITY("com.univocity.parsers.csv.CsvParser", "Univocity", "2.8.3");

    private final String checkClass;
    private final String dependencyName;
    private final String dependencyVersion;

    public void load() {
        try {
            Class.forName(checkClass);
        } catch (ClassNotFoundException exception) {
            new DependencyLoader(checkClass, dependencyName, dependencyVersion).load();
        }
    }
}
