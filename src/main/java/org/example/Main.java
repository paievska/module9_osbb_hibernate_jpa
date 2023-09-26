package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("osbb");
        EntityManager em = emf.createEntityManager();
        OsbbCrud osbbCrud = new OsbbCrud(em);
        List<OwnerInfo> residents = osbbCrud.getOwnersWithoutCarPermissionAndLessThanTwoApartments();
        System.out.println(residents);
        osbbCrud.saveResultToFile(residents, "result.txt");
        em.close();
        emf.close();
    }
}