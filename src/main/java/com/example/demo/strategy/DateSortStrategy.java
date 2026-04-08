package com.example.demo.strategy;


import com.example.demo.model.Application;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DateSortStrategy implements SortStrategy {
    @Override
    public List<Application> sort(List<Application> apps) {
        return apps.stream()
                .sorted(Comparator.comparing(Application::getApplicationDate).reversed())
                .collect(Collectors.toList());
    }
}