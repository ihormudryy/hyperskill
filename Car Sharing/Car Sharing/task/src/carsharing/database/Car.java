package carsharing.database;

public class Car {

    private Integer id;
    private String name;
    private Integer companyId;

    public Car(String name, Integer companyId) {
        this.name = name;
        this.companyId = companyId;
    }

    public Car(Integer id, String name, Integer companyId) {
        this.id = id;
        this.name = name;
        this.companyId = companyId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
