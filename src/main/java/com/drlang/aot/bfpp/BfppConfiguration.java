package com.drlang.aot.bfpp;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.aot.BeanFactoryInitializationAotContribution;
import org.springframework.beans.factory.aot.BeanFactoryInitializationAotProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.UUID;

import static com.drlang.aot.bfpp.BfppConfiguration.BEAN_NAME;

@Configuration
public class BfppConfiguration {
    static String BEAN_NAME = "myBfppListener";

    @Bean
    ListenerBeanFactoryPostProcess listenerBeanFactoryPostProcess() {
        return new ListenerBeanFactoryPostProcess();
    }

    @Bean
    static ListenerBeanFactoryInitializationAotProcessor factoryInitializationAotProcessor() {
        return new ListenerBeanFactoryInitializationAotProcessor();
    }

}

class ListenerBeanFactoryInitializationAotProcessor implements BeanFactoryInitializationAotProcessor {

    @Override
    public BeanFactoryInitializationAotContribution processAheadOfTime
            (ConfigurableListableBeanFactory beanFactory) {
        if (beanFactory.containsBeanDefinition(BEAN_NAME)) {
            return (ctx, code) -> {
                RuntimeHints runtimeHints = ctx.getRuntimeHints();
                runtimeHints.reflection().registerType(Product.class, MemberCategory.values());
            };
        }
        return null;
    }
}

class ListenerBeanFactoryPostProcess implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory bf) throws BeansException {
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry bf) throws BeansException {
//        for (String beanName : bf.getBeanDefinitionNames()) {
//            var beanDefinition  = bf.getBeanDefinition(beanName);
//        }
        if (!bf.containsBeanDefinition(BEAN_NAME)) {
            bf.registerBeanDefinition(BEAN_NAME, BeanDefinitionBuilder.rootBeanDefinition("com.drlang.aot.bfpp.Listener").getBeanDefinition());
        }
    }
}

class Listener implements ApplicationListener<ApplicationReadyEvent> {
    private final ObjectMapper objectMapper;

    public Listener(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    @SneakyThrows
    public void onApplicationEvent(ApplicationReadyEvent event) {
        var products = List.of(new Product(UUID.randomUUID().toString()),
                new Product(UUID.randomUUID().toString()));
        for (Product product : products) {
            System.out.println(objectMapper.writeValueAsString(product));
        }
    }
}

record Product(String sku) {

}