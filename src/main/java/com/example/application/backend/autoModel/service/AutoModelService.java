package com.example.application.backend.autoModel.service;

import com.example.application.backend.autoModel.domain.AutoModelEntity;
import com.example.application.backend.autoModel.repository.AutoModelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AutoModelService {
    private final AutoModelRepository autoModelRepository;

    public void verificarDuplicidade() {
        List<AutoModelEntity> modelos = autoModelRepository.findAll();
        Map<Long, List<AutoModelEntity>> modelosPorAutoMaker = modelos.stream()
                .collect(Collectors.groupingBy(AutoModelEntity::getAutoMaker));

        modelosPorAutoMaker.forEach((autoMaker, modelosDoMaker) -> {

            Set<String> modelosUnicos = new HashSet<>();
            List<String> modelosDuplicados = modelosDoMaker.stream()
                    .map(AutoModelEntity::getAutoModel)
                    .filter(modelo -> !modelosUnicos.add(modelo))
                    .toList();

            if (!modelosDuplicados.isEmpty()) {
                System.out.println("AutoMaker: " + autoMaker);
                System.out.println(" - Modelos duplicados: " + modelosDuplicados);
            }
        });
    }
}
