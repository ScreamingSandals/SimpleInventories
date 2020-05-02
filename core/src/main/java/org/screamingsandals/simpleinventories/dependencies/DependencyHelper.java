package org.screamingsandals.simpleinventories.dependencies;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DependencyHelper {
    GROOVY("groovy.util.GroovyScriptEngine", "Groovy", "https://repo1.maven.org/maven2/org/codehaus/groovy/groovy/3.0.3/groovy-3.0.3.jar", "3.0.3"),
    UNIVOCITY("com.univocity.parsers.csv.CsvParser", "Univocity", "https://repo1.maven.org/maven2/com/univocity/univocity-parsers/2.8.3/univocity-parsers-2.8.3.jar", "2.8.3");

    private final String checkClass;
    private final String dependencyName;
    private final String dependencyURL;
    private final String dependencyURLVersion;

    public void load() {
        try {
            Class.forName(checkClass);
        } catch (ClassNotFoundException exception) {
            new DependencyLoader(checkClass, dependencyName, dependencyURL, dependencyURLVersion).load();
        }
    }
}
