package edu.uob.prototype;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class RotaBuild {

    public static void main(String[] args) {
        Rota rota1 = new Rota(LocalDate.parse("2020-01-01"), LocalDate.parse("2020-01-28"));
        Employee emp1 = new Employee("0001"); rota1.addEmployee(emp1);
        Employee emp2 = new Employee("0002"); rota1.addEmployee(emp2);
        Employee emp3 = new Employee("0003"); rota1.addEmployee(emp3);
        Employee emp4 = new Employee("0004"); rota1.addEmployee(emp4);
        Employee emp5 = new Employee("0005"); rota1.addEmployee(emp5);
        Employee emp6 = new Employee("0006"); rota1.addEmployee(emp6);
        Employee emp7 = new Employee("0007"); rota1.addEmployee(emp7);
        Employee emp8 = new Employee("0008"); rota1.addEmployee(emp8);

        RotaTools.createAllShifts(rota1);
        System.out.println(rota1);

        RotaTools.createRules(rota1);
        System.out.println(rota1.getCost());
    }

}
