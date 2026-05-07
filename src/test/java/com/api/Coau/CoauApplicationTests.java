    package com.api.Coau;  // Mude para minúsculo

import com.api.Coau.config.AppConfig;
    import org.junit.jupiter.api.Test;
    import org.springframework.boot.test.context.SpringBootTest;
    import org.springframework.context.annotation.Import;

    @SpringBootTest(classes = CoauApplication.class)
    @Import(AppConfig.class)
    class CoauApplicationTests {
        @Test
        void contextLoads() {
        }
    }
    