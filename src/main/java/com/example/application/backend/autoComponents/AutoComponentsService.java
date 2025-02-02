package com.example.application.backend.autoComponents;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class AutoComponentsService {
    @Autowired
    private AutoComponentsRepository componentsRepository;

    public List<AutoComponentsEntity> findAllComponents () {
        return componentsRepository.findAllByOrderByComponentNameAsc();
    }
}
