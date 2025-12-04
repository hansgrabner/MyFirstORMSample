package at.campus02.dbp.unidirectional;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

import java.util.List;

public class unidirectional_department {
    @Id
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public List<unidrectional_employees> getEmployees() {
        return employees;
    }

    public void setEmployees(List<unidrectional_employees> employees) {
        this.employees = employees;
    }

    private String departmentName;

    @OneToMany
    @JoinColumn(name = "department_id")
    private List<unidrectional_employees> employees;
}
