package com.franco.gestao_financeira.bdd;


import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import io.cucumber.junit.platform.engine.Constants;

@Suite
@IncludeEngines("cucumber") 
@ConfigurationParameter(key = Constants.FEATURES_PROPERTY_NAME, value = "src/test/resources/features")
@ConfigurationParameter(
    key = Constants.GLUE_PROPERTY_NAME, 
    value = "com.franco.gestao_financeira.bdd.steps" 
)
@ConfigurationParameter(key = Constants.FILTER_TAGS_PROPERTY_NAME, value = "@social or @planning")
@ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME, value = "pretty, html:target/cucumber-reports/relatorio-bdd.html")
public class CucumberRunnerTest {
    // Essa classe fica vazia mesmo, as anotações fazem tudo.
}