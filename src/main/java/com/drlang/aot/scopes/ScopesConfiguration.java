package com.drlang.aot.scopes;

import org.springframework.aop.framework.Advised;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.DecoratingProxy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Proxy;
import java.util.UUID;

@Configuration
public class ScopesConfiguration {
//    @Bean
//    public RuntimeHintsRegistrar runtimeHintsRegistrar(){
//        return new RuntimeHintsRegistrar() {
//            @Override
//            public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
//                hints.proxies().registerJdkProxy(RequestContext.class, Proxy.class,
//                        Advised.class, DecoratingProxy.class);
//
//            }
//        };
//    }
}

//@RestController



