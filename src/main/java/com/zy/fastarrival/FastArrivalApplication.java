package com.zy.fastarrival;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
public class FastArrivalApplication {

    public static void main(String[] args) {
        SpringApplication.run(FastArrivalApplication.class, args);
    }

}
