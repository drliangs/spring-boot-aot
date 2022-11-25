package com.drlang.aot.qualifilers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

@Configuration
public class QualifiersConfiguration {
    @Bean
    public ApplicationListener<ApplicationReadyEvent> ios(@Android MobileMarketPhone mobileMarketPhone) {
        return event -> System.out.println(mobileMarketPhone.getClass().toString());
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> android(@Apple MobileMarketPhone mobileMarketPhone) {
        return event -> System.out.println(mobileMarketPhone.getClass().toString());
    }


    @Bean
    public ApplicationListener<ApplicationReadyEvent> mobileMarketplaceListener(Map<String, MobileMarketPhone> mobileMarketPhoneMap) {
        return event -> mobileMarketPhoneMap.forEach((key, value) -> {
            System.out.println(key + " = " + value.getClass().getName());
        });
    }

}

@Retention(RetentionPolicy.RUNTIME)
@Qualifier("android")
@Target(ElementType.PARAMETER)
@interface Android {
}

@Retention(RetentionPolicy.RUNTIME)
@Qualifier("ios")
@Target(ElementType.PARAMETER)
@interface Apple {

}

interface MobileMarketPhone {


}

@Service
@Qualifier("ios")
class AppStore implements MobileMarketPhone {

}

@Service
@Qualifier("android")
class PlayStore implements MobileMarketPhone {


}


