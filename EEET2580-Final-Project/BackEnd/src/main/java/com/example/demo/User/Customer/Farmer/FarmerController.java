package com.example.demo.User.Customer.Farmer;


import com.example.demo.SprayOrder.SprayOrder;
import com.example.demo.SprayOrder.SprayOrderDTO.FarmerOrderResponseDTO;
import com.example.demo.User.Employee.Receptionist.ReceptionistDTO.Response.ReceptionistContactDTO;
import com.example.demo.User.Employee.Receptionist.ReceptionistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Authentication.AuthDTO.AuthRequest;
import com.example.demo.Authentication.AuthDTO.AuthResponse;
import com.example.demo.Security.Filter.*;
import com.example.demo.Security.Utils.CookieUtil;
import com.example.demo.User.Customer.Farmer.FarmerDTO.Request.FarmerRegisterRequest;
import com.example.demo.User.Customer.Farmer.FarmerDTO.Response.FarmerResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/farmer")
public class FarmerController {

    private static final Logger logger = LoggerFactory.getLogger(FarmerController.class);

    @Autowired
    private FarmerService farmerService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;



    // Method to create a new Farmer (register)
    @PostMapping("/register")
    public ResponseEntity<FarmerResponse> registerFarmer(@RequestBody @Valid FarmerRegisterRequest farmerRequest) {
        FarmerResponse farmer = farmerService.createFarmer(farmerRequest);
        logger.info("Farmer registered successfully: {}", farmer);
        return ResponseEntity.ok(farmer);
    }



    @GetMapping("/{fId}")
    @PreAuthorize("hasRole('RECEPTIONIST')")
    public ResponseEntity<FarmerResponse> getFarmerById(@PathVariable String fId) {
        FarmerResponse farmer = farmerService.findFarmerById(fId);
        if (farmer != null) {
            return ResponseEntity.ok(farmer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<Farmer> getFarmerByEmail(HttpServletRequest request) {
        String email = jwtAuthenticationFilter.extractEmailFromToken(request);
        Farmer farmer = farmerService.findFarmerByEmail(email);
        return ResponseEntity.ok(farmer);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> farmerLogin(@RequestBody @Valid AuthRequest authRequest, HttpServletResponse response) {
        AuthResponse farmerAuthResponse = farmerService.farmerLogin(authRequest);
        String token = farmerAuthResponse.getToken();

        // Use CookieUtil to add the HttpOnly cookie
        CookieUtil.addHttpOnlyCookie(response, "jwtToken", token, 360000);

        return ResponseEntity.ok(farmerAuthResponse);
    }

    @PutMapping("/confirm/{sprayId}")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<String> farmerConfirmOrder(@PathVariable @Valid Long sprayId, HttpServletRequest request){
        String farmerEmail = jwtAuthenticationFilter.extractEmailFromToken(request);

        try {
            farmerService.confirmSprayCompleted(sprayId, farmerEmail);
            return ResponseEntity.ok("Spray order confirmed by farmer.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/spray-orders")
    @PreAuthorize("hasRole('FARMER')")
    public Page<FarmerOrderResponseDTO> getSprayOrdersByFarmerEmail(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest request) {

// Create a Pageable instance with the requested page number and page size
        Pageable pageable = PageRequest.of(page, size);
        // Retrieve the authenticated user's roles and log them
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authorities: " + authentication.getAuthorities()); // Log roles here

        // Extract the email from the JWT token in the request
        String email = jwtAuthenticationFilter.extractEmailFromToken(request);

        // Fetch and return the spray orders mapped to the DTO
        return farmerService.getSprayOrdersByFarmerEmail(email, pageable);
    }

    @GetMapping("/receptionist_contacts")
    @PreAuthorize("hasRole('FARMER')")
    public List<ReceptionistContactDTO> getAllReceptionistContacts() {
        return farmerService.getAllReceptionistContact();
    }

}





