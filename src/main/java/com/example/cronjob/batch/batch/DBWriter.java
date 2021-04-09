package com.example.cronjob.batch.batch;

import com.example.cronjob.batch.entity.User;
import com.example.cronjob.batch.repository.UserRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

// Creating the writer
@Component
public class DBWriter implements ItemWriter<User> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void write(List<? extends User> users) throws Exception {
        System.out.println("Data saved for the users "+ users);
        userRepository.saveAll(users);
    }
}
