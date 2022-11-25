package com.drlang.aot.javapoet;

import org.junit.jupiter.api.Test;
import org.springframework.data.relational.core.sql.In;
import org.springframework.javapoet.JavaFile;
import org.springframework.javapoet.MethodSpec;
import org.springframework.javapoet.TypeSpec;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Stream;

class JavaPoetTest {
    private TypeSpec typeSpec(Class<?> target) {
        Assert.state(!java.lang.reflect.Modifier.isFinal(target.getModifiers()), "the class can't be final");
        var methods = Stream.of(ReflectionUtils.getUniqueDeclaredMethods(target)).
                filter(method -> !Modifier.isPrivate(method.getModifiers()) && !ReflectionUtils.isObjectMethod(method)).
                map(this::methodSpec).toList();
        var newType = TypeSpec.classBuilder(target.getSimpleName() + "_Subclass")
                        .addModifiers(javax.lang.model.element.Modifier.PUBLIC);
        Stream.of(target.getInterfaces()).forEach(newType::addSuperinterface);
        newType.superclass(target);
        methods.forEach(newType::addMethod);
        return newType.build();
    }

    private MethodSpec methodSpec(Method method) {
        var newMethodDefnition = MethodSpec.methodBuilder(method.getName()).
                addModifiers(javax.lang.model.element.Modifier.PUBLIC).
                returns(method.getReturnType()).addAnnotation(Override.class);
        List<String> paramNames = Stream.of(method.getParameters()).map(pa -> {
            var parName = pa.getName();
            newMethodDefnition.addParameter(pa.getType(), parName);
            return parName;
        }).toList();
        String returnStatement = String.format("%s super.$L($L))", method.getReturnType().equals(Void.class) ? "" : "return ");
        newMethodDefnition.addStatement(returnStatement,method.getName(),
                StringUtils.collectionToDelimitedString(paramNames,";"));
        return newMethodDefnition.build();
    }

    @Test
    void subClass() throws Exception {
        TypeSpec typeSpec = typeSpec(CustomerService.class);
        var javaFile = JavaFile.builder(Customer.class.getPackageName(), typeSpec).build();
        String code = javaFile.toString();
        System.out.println(code);
    }
}

class CustomerService {

    Customer save(String name) {
        return null;
    }

    Customer byId(Integer id) {
        return null;
    }
}

record Customer(Integer id, String name) {

}
