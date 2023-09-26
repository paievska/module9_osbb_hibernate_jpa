package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.example.data.OsbbMembers;
import org.example.data.OwnershipRights;
import org.example.data.RCResidents;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OsbbCrud {

    private final EntityManager entityManager;

    public OsbbCrud(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<OwnerInfo> getOwnersWithoutCarPermissionAndLessThanTwoApartments() {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<OwnershipRights> query = cb.createQuery(OwnershipRights.class);
        Root<OwnershipRights> ownershipRightsRoot = query.from(OwnershipRights.class);
        Join<RCResidents, OwnershipRights> residentsJoin = ownershipRightsRoot.join("memberId");

        Predicate carPermissionPredicate = cb.equal(residentsJoin.get("carPermission"), 0);
        Subquery<Long> subquery = query.subquery(Long.class);
        Root<OwnershipRights> subqueryRoot = subquery.from(OwnershipRights.class);
        subquery.select(cb.count(subqueryRoot.get("id")));
        subquery.where(cb.equal(subqueryRoot.get("memberId").get("id"), residentsJoin.get("memberId").get("id")));
        Predicate lessThanTwoApartmentsPredicate = cb.lt(subquery, 2L);

        query.where(cb.and(carPermissionPredicate, lessThanTwoApartmentsPredicate));

        List<OwnershipRights> ownershipRights = entityManager.createQuery(query).getResultList();
        List<OwnerInfo> ownerInfos = new ArrayList<>();

        for (OwnershipRights ownershipRight : ownershipRights) {
            RCResidents residents = ownershipRight.getMemberId();
            OsbbMembers osbbMembers = ownershipRight.getMemberId().getMemberId();
            OwnerInfo ownerInfo = new OwnerInfo(
                    residents.getName(),
                    residents.getSurname(),
                    osbbMembers.getEmail(),
                    ownershipRight.getApartmentId().getBuildingId().getBuildingNumber(),
                    ownershipRight.getApartmentId().getApartmentNumber(),
                    ownershipRight.getApartmentId().getArea(),
                    ownershipRight.getApartmentId().getBuildingId().getAddress()
            );
            ownerInfos.add(ownerInfo);
        }
        return ownerInfos;
    }

    public void saveResultToFile(List<OwnerInfo> owners, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (OwnerInfo owner : owners) {
                String line = String.format(
                        "Ім'я: %s, Прізвище: %s, Email: %s, Номер будинку: %s, Номер квартири: %s, Площа квартири: %.2f, Адреса будинку: %s%n",
                        owner.name(),
                        owner.surname(),
                        owner.email(),
                        owner.buildingNumber(),
                        owner.apartmentNumber(),
                        owner.area(),
                        owner.address()
                );
                writer.write(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

