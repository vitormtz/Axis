package org.gvs.axis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AxisNucleoColaborativoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AxisNucleoColaborativoApplication.class, args);
    }

}
