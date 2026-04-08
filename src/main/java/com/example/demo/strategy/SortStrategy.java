package com.example.demo.strategy;

import com.example.demo.model.Application;
import java.util.List;

public interface SortStrategy {
    List<Application> sort(List<Application> apps);
}