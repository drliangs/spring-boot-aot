package com.drlang.aot.bpp.code;


import org.springframework.beans.factory.aot.BeanRegistrationAotContribution;
import org.springframework.beans.factory.aot.BeanRegistrationAotProcessor;
import org.springframework.beans.factory.support.RegisteredBean;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.javapoet.CodeBlock;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class CompilationEndpointConfiguration {
    @Bean
    CompilationEndpoint compilationEndpoint() {
        return new CompilationEndpoint();
    }

    @Bean
    public CompilationEndpointBeanRegistrationAopProcessor compilationEndpointBeanRegistrationAopProcessor() {
        return new CompilationEndpointBeanRegistrationAopProcessor();
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> compilationEndpointListener(CompilationEndpoint compilationEndpoint) {
        return event -> {
            Map<String, Object> map = compilationEndpoint.compilation();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }
        };
    }
}

class CompilationEndpointBeanRegistrationAopProcessor implements BeanRegistrationAotProcessor {

    @Override
    public BeanRegistrationAotContribution processAheadOfTime(RegisteredBean registeredBean) {
        if (!CompilationEndpoint.class.isAssignableFrom(registeredBean.getBeanClass())) {
            return null;
        }
        return (ctx, code) -> {
            var generatedClasses = ctx.getGeneratedClasses();

            var generatedClass = generatedClasses.
                    getOrAddForFeatureComponent(CompilationEndpoint.class.getSimpleName() + "Feature"
                            , CompilationEndpoint.class, b -> b.addModifiers(Modifier.PUBLIC));

            var generatedMethod = generatedClass.getMethods().add("postProcessCompilationEndpoint",
                    builder -> {
                        var outputBeanVariableName = "outputBean";
                        builder.addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                                .addParameter(RegisteredBean.class, "registeredBean")
                                .addParameter(CompilationEndpoint.class, "inputBean")
                                .returns(CompilationEndpoint.class)
                                .addCode(CodeBlock.builder()
                                        .addStatement("$T $L = new $T( $T.ofEpochMilli($L), new $T($S))",
                                                CompilationEndpoint.class, outputBeanVariableName, CompilationEndpoint.class,
                                                Instant.class, System.currentTimeMillis() + "L",
                                                File.class,
                                                new File(".").getAbsolutePath()).
                                        addStatement("return $L", outputBeanVariableName).
                                        build());
                    });
            var methodReference = generatedMethod.toMethodReference();
            code.addInstancePostProcessor(methodReference);
        };

    }
}

@Endpoint(id = "compilation")
class CompilationEndpoint {
    private final Map<String, Object> map = new ConcurrentHashMap<>();

    public CompilationEndpoint() {
    }

    public CompilationEndpoint(Instant instant, File directory) {
        map.putAll(Map.of("instant", instant, "directory", directory));
    }

    @ReadOperation
    public Map<String, Object> compilation() {
        return Map.of("compilation", this.map, "now", Instant.now());
    }

}

