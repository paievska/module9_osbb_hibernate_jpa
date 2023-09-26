package org.example;

public record OwnerInfo(String name, String surname, String email, String buildingNumber, int apartmentNumber,
                        float area, String address) {

    @Override
    public String name() {
        return name;
    }

    @Override
    public String surname() {
        return surname;
    }

    @Override
    public String email() {
        return email;
    }

    @Override
    public String buildingNumber() {
        return buildingNumber;
    }

    @Override
    public int apartmentNumber() {
        return apartmentNumber;
    }

    @Override
    public float area() {
        return area;
    }

    @Override
    public String address() {
        return address;
    }

    @Override
    public String toString() {
        return "ownerName= " + name +
                ", surname= " + surname +
                ", ownerEmail= " + email +
                ", buildingNumber= " + buildingNumber +
                ", apartmentNumber= " + apartmentNumber +
                ", apartmentArea= " + area +
                ", buildingAddress= " + address + ". \n";
    }
}
