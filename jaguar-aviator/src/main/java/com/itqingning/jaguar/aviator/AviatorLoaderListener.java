package com.itqingning.jaguar.aviator;

import com.googlecode.aviator.AviatorEvaluator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Created by lvws on 2019/4/16.
 */
@Slf4j
public class AviatorLoaderListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("aviator SpringContextFunctionLoader loading...");
        AviatorEvaluator.addFunctionLoader(new SpringContextFunctionLoader());
        log.info("aviator SpringContextFunctionLoader loaded");
    }
}
