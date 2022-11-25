package com.drlang.aot.factorybeans;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FactoryBeansConfiguration {
    @Bean
    public AnimalFactoryBean animalFactoryBean() {
        return new AnimalFactoryBean(true, false);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> factoryBeanListener(Animal animal) {
        return event -> animal.speak();
    }

}

interface Animal {
    void speak();
}

class AnimalFactoryBean implements FactoryBean<Animal> {
    private final boolean likeYarn, likeFrisbees;

    public AnimalFactoryBean(boolean likeYarn, boolean likeFrisbees) {
        this.likeYarn = likeYarn;
        this.likeFrisbees = likeFrisbees;
    }

    @Override
    public Animal getObject() throws Exception {
        return (this.likeYarn && !likeFrisbees) ? new Cat() : new Dog();
    }

    @Override
    public Class<?> getObjectType() {
        return Animal.class;
    }
}

class Dog implements Animal {
    @Override
    public void speak() {
        System.out.println("woof");
    }
}

class Cat implements Animal {

    @Override
    public void speak() {
        System.out.println("meow");
    }
}
