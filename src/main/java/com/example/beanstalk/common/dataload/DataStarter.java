package com.example.beanstalk.common.dataload;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataStarter implements CommandLineRunner {

    private final DataLoader dataLoader;

    @Override
    public void run(String... args) throws Exception {
        dataLoader.loadData();
    }
}