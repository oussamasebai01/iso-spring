package com.example.coconsult.entities;


import java.time.LocalDate;

public class Salary {
    private Double baseSalary;
    private Double bonus;
    private Double totalSalary;
    private LocalDate calculationDate;
    private String period; // MONTHLY, QUARTERLY, YEARLY

    // Constructors
    public Salary() {
        this.calculationDate = LocalDate.now();
    }

    public Salary(Double baseSalary, Double bonus, String period) {
        this();
        this.baseSalary = baseSalary;
        this.bonus = bonus;
        this.totalSalary = baseSalary + bonus;
        this.period = period;
    }

    // Getters and Setters
    public Double getBaseSalary() { return baseSalary; }
    public void setBaseSalary(Double baseSalary) {
        this.baseSalary = baseSalary;
        calculateTotal();
    }

    public Double getBonus() { return bonus; }
    public void setBonus(Double bonus) {
        this.bonus = bonus;
        calculateTotal();
    }

    public Double getTotalSalary() { return totalSalary; }

    public LocalDate getCalculationDate() { return calculationDate; }
    public void setCalculationDate(LocalDate calculationDate) { this.calculationDate = calculationDate; }

    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }

    private void calculateTotal() {
        this.totalSalary = (baseSalary != null ? baseSalary : 0) + (bonus != null ? bonus : 0);
    }

    @Override
    public String toString() {
        return "Salary{" +
                "baseSalary=" + baseSalary +
                ", bonus=" + bonus +
                ", totalSalary=" + totalSalary +
                ", calculationDate=" + calculationDate +
                ", period='" + period + '\'' +
                '}';
    }
}
