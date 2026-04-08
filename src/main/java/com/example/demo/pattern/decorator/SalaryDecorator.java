package com.example.demo.pattern.decorator;

import com.example.demo.model.Application;

public class SalaryDecorator extends ApplicationDecorator {

    private final Double salary;

    public SalaryDecorator(Application wrapped, Double salary) {
        super(wrapped);
        this.salary = salary;
        wrapped.setSalaryDetails(salary);
    }

    @Override
    public String getDetails() {
        return "Salary: $" + salary;
    }
}
