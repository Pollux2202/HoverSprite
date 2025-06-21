package com.example.demo.User.Employee.Receptionist;

import java.util.List;
import java.util.Optional;

import com.example.demo.SprayOrder.SprayOrder;
import com.example.demo.SprayOrder.SprayOrderDTO.ReceptionistOrderDTO;

import com.example.demo.SprayOrder.SprayOrderRepository;
import com.example.demo.User.Customer.Farmer.Farmer;
import com.example.demo.User.Customer.Farmer.FarmerRepository;
import com.example.demo.User.Employee.Receptionist.ReceptionistDTO.Response.ReceptionistContactDTO;
import com.example.demo.User.Employee.Sprayer.Sprayer;
import com.example.demo.User.Employee.Sprayer.SprayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.Authentication.AuthDTO.AuthRequest;
import com.example.demo.Authentication.AuthDTO.AuthResponse;
import com.example.demo.Authentication.AuthService;
import com.example.demo.Security.Utils.PasswordUtil;
import com.example.demo.User.Employee.Receptionist.ReceptionistDTO.Request.ReceptionistCreateRequest;
import com.example.demo.User.Employee.Receptionist.ReceptionistDTO.Response.ReceptionistResponse;
import com.example.demo.User.Employee.Receptionist.ReceptionistValidator.ReceptionistIdValidator;
import com.example.demo.User.enumType.UserType;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ReceptionistService {
    @Autowired
    private ReceptionistRepository receptionistRepository;

    @Autowired
    private ReceptionistIdValidator receptionistIdValidator;

    @Autowired
    private SprayOrderRepository sprayOrderRepository;

    @Autowired
    private FarmerRepository farmerRepository;

    @Autowired
    private SprayerRepository sprayerRepository;

    public Page<Farmer> findAllFarmers(Pageable pageable) {
        // Fetch all farmers using pagination
        return farmerRepository.findAll(pageable);
    }

    public Page<Sprayer> findAllSprayers(Pageable pageable) {
        // Fetch all farmers using pagination
        return sprayerRepository.findAll(pageable);
    }

    public ReceptionistResponse createReceptionist(ReceptionistCreateRequest receptionistCreateRequestRequest) {

        if (receptionistRepository.findReceptionistByPhoneNumber(receptionistCreateRequestRequest.getPhoneNumber()).isPresent()) {
            throw new IllegalArgumentException("Phone number already used");
        }

        // Check if the email already exists
        if (receptionistRepository.findReceptionistByEmail(receptionistCreateRequestRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already used");
        }

        Receptionists receptionist = new Receptionists();

        String rId = receptionistIdValidator.generateId();

        receptionist.setrId(rId);
        receptionist.setFirstName(receptionistCreateRequestRequest.getFirstName());
        receptionist.setLastName(receptionistCreateRequestRequest.getLastName());

        String hashedPassword = PasswordUtil.hashPassword(receptionistCreateRequestRequest.getPassword());
        receptionist.setPassword(hashedPassword);

        receptionist.setPhoneNumber(receptionistCreateRequestRequest.getPhoneNumber());
        receptionist.setEmail(receptionistCreateRequestRequest.getEmail());
        receptionist.setAddress(receptionistCreateRequestRequest.getAddress());
        receptionist.setUserType(UserType.RECEPTIONIST);

        receptionistRepository.save(receptionist);
        // Save the Farmer to the database
        return new ReceptionistResponse(receptionist);
    }


    public Optional<Receptionists> findReceptionistById(String rId) {
        try {
            return receptionistRepository.findReceptionistById(rId);
        } catch (Exception e) {
            throw e;
        }
    }

    public AuthResponse receptionistLogin(AuthRequest authRequest) {
        Optional<Receptionists> receptionists;

        // Check if the input is an email or a phone number
        if (authRequest.getEmailOrPhoneNumber().contains("@")) {
            // It's an email
            receptionists = receptionistRepository.findReceptionistByEmail(authRequest.getEmailOrPhoneNumber());
        } else {
            // It's a phone number
            receptionists = receptionistRepository.findReceptionistByPhoneNumber(authRequest.getEmailOrPhoneNumber());
        }

        // Check if farmer exists and password is correct
        if (receptionists.isEmpty() || !PasswordUtil.checkPassword(authRequest.getPassword(), receptionists.get().getPassword())) {
            throw new IllegalArgumentException("Invalid email/phone number or password");
        }

        // Generate the token
        String token = AuthService.generateToken(receptionists.get().getEmail(), receptionists.get().getUserType()); // Use email as the subject

        // Prepare the response
        AuthResponse authResponse = new AuthResponse();
        authResponse.setAuth(true);
        authResponse.setToken(token);

        return authResponse;
    }

        public Receptionists findReceptionistByEmail(String email) {
        return receptionistRepository.findReceptionistByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Receptionist with email " + email + " not found."));
    }

    public Page<ReceptionistOrderDTO> getAllSprayOrders(Pageable pageable, String email) {

//        if (receptionistRepository.findByEmail(email).isPresent()) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Receptionist with the provided email does not exist.");
//        }

        // Fetch the paginated spray orders without filtering by email
        Page<SprayOrder> sprayOrders = sprayOrderRepository.findAll(pageable);

        // Map the SprayOrder entities to SprayerOrderResponseDTO using the DTO constructor
        return sprayOrders.map(ReceptionistOrderDTO::new);
    }





}
