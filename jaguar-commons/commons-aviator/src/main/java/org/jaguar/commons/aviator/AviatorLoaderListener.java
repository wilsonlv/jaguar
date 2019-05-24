package org.jaguar.commons.aviator;

import com.googlecode.aviator.AviatorEvaluator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Created by lvws on 2019/4/16.
 */
@Slf4j
@Component
public class AviatorLoaderListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("aviator SpringContextFunctionLoader loading...");
        AviatorEvaluator.addFunctionLoader(new SpringContextFunctionLoader(contextRefreshedEvent.getApplicationContext()));
        log.info("aviator SpringContextFunctionLoader loaded");
    }
}
