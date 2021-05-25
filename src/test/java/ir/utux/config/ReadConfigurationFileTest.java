package ir.utux.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadConfigurationFileTest {
    private Logger LOGGER=LoggerFactory.getLogger(this.getClass().getName());
    private WebConfiguration webConfiguration;
    @BeforeEach
    void beforeRun(){
        webConfiguration=new WebConfiguration();
    }
    @Test
    void whenCreateWebConfigurationLoadProperties(){
        LOGGER.info("Start Test");
        Assertions.assertEquals(80,webConfiguration.getPort());
    }
}
