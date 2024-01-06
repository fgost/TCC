package com.example.application.config.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("application")
@Component
@Getter
public class ApplicationProperties {
    private final Storage storage = new Storage();

    @Getter
    @Setter
    public static class Storage {

        private ImplType impl = ImplType.LOCAL;

        private Local local = new Local();

        @AllArgsConstructor
        @Getter
        public enum ImplType {
            LOCAL(false);

            private boolean redirect;
        }

        @Getter
        @Setter
        public static class Local {
            private String root;
        }
    }
}
