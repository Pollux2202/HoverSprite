package com.example.demo.User.Employee.Receptionist;



import com.example.demo.User.Employee.Receptionist.Annotations.IdValidation.ReceptionistIdValidation;
import com.example.demo.User.User;
import com.example.demo.User.enumType.UserType;
import jakarta.persistence.*;

@Entity
@Table(name = "receptionist")
public class Receptionists extends User {

    @ReceptionistIdValidation
    @Id
    @Column(name = "rid")
    private String rId;

    public Receptionists(){
    }


    public Receptionists(String firstName, String lastName, String password, String phoneNumber, String email, String address, UserType userType, String rId) {
        super(firstName, lastName, password, phoneNumber, email, address, userType);
        this.rId = rId;
    }


    public String getrId() {
        return rId;
    }

    public void setrId(String rId) {
        this.rId = rId;
    }
}
