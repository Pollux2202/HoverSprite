package com.example.demo.User.Customer.Farmer;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.demo.SprayOrder.SprayOrderDTO.FarmerOrderResponseDTO;
import com.example.demo.User.Employee.Receptionist.ReceptionistDTO.Response.ReceptionistContactDTO;
import com.example.demo.User.Employee.Receptionist.ReceptionistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.Authentication.AuthDTO.AuthRequest;
import com.example.demo.Authentication.AuthDTO.AuthResponse;
import com.example.demo.Authentication.AuthService;
import com.example.demo.Security.Utils.PasswordUtil;
import com.example.demo.SprayOrder.SprayOrder;
import com.example.demo.SprayOrder.SprayOrderRepository;
import com.example.demo.SprayOrder.SprayOrderService;
import com.example.demo.User.Customer.Farmer.FarmerDTO.Request.FarmerRegisterRequest;
import com.example.demo.User.Customer.Farmer.FarmerDTO.Response.FarmerResponse;
import com.example.demo.User.Customer.Farmer.FarmerValidator.FarmerIdValidator;
import com.example.demo.User.enumType.UserType;

import jakarta.persistence.EntityNotFoundException;

@Service
public class FarmerService {

    @Autowired
    private final FarmerRepository farmerRepository;

    @Autowired
    private final FarmerIdValidator farmerIdValidator;

    @Autowired
    private SprayOrderRepository sprayOrderRepository;

    @Autowired
    private SprayOrderService sprayOrderService;

    @Autowired
    private ReceptionistRepository receptionistRepository;

    public FarmerService(FarmerRepository farmerRepository, FarmerIdValidator farmerIdValidator) {
        this.farmerRepository = farmerRepository;
        this.farmerIdValidator = farmerIdValidator;
    }


    public FarmerResponse createFarmer(FarmerRegisterRequest farmerRequest) {
        // Check if the phone number already exists
        if (farmerRepository.findFarmerByPhoneNumber(farmerRequest.getPhoneNumber()).isPresent()) {
            throw new IllegalArgumentException("Phone number already used");
        }

        // Check if the email already exists
        if (farmerRepository.findFarmerByEmail(farmerRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already used");
        }

        // Generate custom fId
        String fId = farmerIdValidator.generateId();

        // Create a new Farmer object
        Farmer farmer = new Farmer();
        farmer.setfId(fId);
        farmer.setFirstName(farmerRequest.getFirstName());
        farmer.setLastName(farmerRequest.getLastName());

        // Hash the password after validation
        String hashedPassword = PasswordUtil.hashPassword(farmerRequest.getPassword());
        farmer.setPassword(hashedPassword);

        farmer.setPhoneNumber(farmerRequest.getPhoneNumber());
        farmer.setEmail(farmerRequest.getEmail());
        farmer.setAddress(farmerRequest.getAddress());
        farmer.setUserType(UserType.FARMER);

        // Save the Farmer to the database
        farmerRepository.save(farmer);

        return new FarmerResponse(farmer);
    }

    public FarmerResponse findFarmerById(String fId) {
        try {
            return farmerRepository.findFarmerByFId(fId);
        } catch (Exception e) {
            throw e;
        }
    }

    public AuthResponse farmerLogin(AuthRequest authRequest) {
        Optional<Farmer> farmerOptional;

        // Check if the input is an email or a phone number
        if (authRequest.getEmailOrPhoneNumber().contains("@")) {
            // It's an email
            farmerOptional = farmerRepository.findFarmerByEmail(authRequest.getEmailOrPhoneNumber());
        } else {
            // It's a phone number
            farmerOptional = farmerRepository.findFarmerByPhoneNumber(authRequest.getEmailOrPhoneNumber());
        }

        System.out.println(System.currentTimeMillis());
        // Check if farmer exists and password is correct
        if (farmerOptional.isEmpty() || !PasswordUtil.checkPassword(authRequest.getPassword(), farmerOptional.get().getPassword())) {
            throw new IllegalArgumentException("Invalid email/phone number or password");
        }

        // Generate the token
        String token = AuthService.generateToken(farmerOptional.get().getEmail(), farmerOptional.get().getUserType()); // Use email as the subject

        // Decode the token (for demonstration purposes)


        // Prepare the response
        AuthResponse authResponse = new AuthResponse();
        authResponse.setAuth(true);
        authResponse.setToken(token);

        System.out.println("Login Successfully");
        return authResponse;
    }


    public void confirmSprayCompleted(Long sprayOrderId, String farmerEmail) {
        SprayOrder order = sprayOrderRepository.findById(sprayOrderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (!order.getFarmer().getEmail().equals(farmerEmail)) {
            throw new IllegalArgumentException("This order does not belong to the farmer.");
        }

        // Farmer confirms that the spray is completed
        order.setFarmerConfirmation(true);
        sprayOrderRepository.save(order);

        // Check if both confirmations are done and mark the order as completed
        sprayOrderService.markOrderAsCompletedIfBothConfirmed(order);
    }
    
    public Farmer findFarmerByEmail(String email) {
        return farmerRepository.findFarmerByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Farmer with email " + email + " not found."));
    }

    public Page<FarmerOrderResponseDTO> getSprayOrdersByFarmerEmail(String email, Pageable pageable) {
        // Validate the farmer's existence
        farmerRepository.findFarmerByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Farmer with email " + email + " not found."));
        // Fetch the paginated spray orders
        Page<SprayOrder> sprayOrders = sprayOrderRepository.findByFarmerEmail(email, pageable);

        // Map the SprayOrder entities to FarmerOrderResponseDTO using the DTO constructor
        return sprayOrders.map(FarmerOrderResponseDTO::new);
    }

    public Page<Farmer> findAllFarmers(Pageable pageable) {
        // Fetch all farmers using pagination
        return farmerRepository.findAll(pageable);
    }

    public List<ReceptionistContactDTO> getAllReceptionistContact() {
        return receptionistRepository.findAll()
                .stream()
                .map(ReceptionistContactDTO::new) // Convert each Receptionist to ReceptionistContactDTO
                .collect(Collectors.toList());
    }
}
