package com.davromalc.shared.payments.acceptance;

import com.davromalc.cucumber.micronaut.MicronautObjectFactory;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.CucumberOptions.SnippetType;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class) //Cucumber does not support JUnit 5 :( https://cucumber.io/docs/cucumber/api/#junit
@CucumberOptions(
    plugin = {"pretty", "html:target/features"},
    glue = {"com.davromalc.shared.payments.acceptance"},
    objectFactory = MicronautObjectFactory.class,
    features = "classpath:features", snippets = SnippetType.CAMELCASE)
public class CucumberRunnerITCase {

}
