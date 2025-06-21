package com.example.demo.User.Employee.Sprayer;

import com.example.demo.Security.Filter.*;
import com.example.demo.Security.Utils.CookieUtil;
import com.example.demo.Authentication.AuthDTO.AuthRequest;

import com.example.demo.Authentication.AuthDTO.AuthResponse;
import com.example.demo.SprayOrder.SprayOrderDTO.FarmerOrderResponseDTO;
import com.example.demo.SprayOrder.SprayOrderDTO.SprayerOrderResponseDTO;
import com.example.demo.User.Employee.Receptionist.Receptionists;
import com.example.demo.User.Employee.Sprayer.SprayerDTO.SprayerRequest.SprayerConfirmRequest;
import com.example.demo.User.Employee.Sprayer.SprayerDTO.SprayerRequest.SprayerCreateRequest;
import com.example.demo.User.Employee.Sprayer.SprayerDTO.SprayerResponse.SprayerConfirmResponse;
import com.example.demo.User.Employee.Sprayer.SprayerDTO.SprayerResponse.SprayerResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sprayer")
public class SprayerController {
    @Autowired
    private SprayerService sprayerService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @PostMapping("/create")
    public ResponseEntity<SprayerResponse> createSprayer(@RequestBody @Valid SprayerCreateRequest sprayerCreateRequest){
        SprayerResponse sprayerResponse = sprayerService.createSprayer(sprayerCreateRequest);
        return ResponseEntity.ok(sprayerResponse);
    }

    @GetMapping("/{sId}")
    public ResponseEntity<Optional<Sprayer>> getSprayerById(@PathVariable String sId){
        Optional<Sprayer> sprayer = sprayerService.findSprayerById(sId);
        if (sprayer != null) {
            return ResponseEntity.ok(sprayer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/login")

    public ResponseEntity<AuthResponse> sprayerLogin(@RequestBody @Valid AuthRequest authRequest, HttpServletResponse response){

        AuthResponse authResponse = sprayerService.sprayerLogin(authRequest);
        String token = authResponse.getToken();
        CookieUtil.addHttpOnlyCookie(response, "jwtToken", token, 360000);
        return ResponseEntity.ok(authResponse);
    }

    @PutMapping("/confirm/{orderId}")
    @PreAuthorize("hasRole('SPRAYER')")
    public ResponseEntity<SprayerConfirmResponse> sprayerConfirm(
            @PathVariable Long orderId,
            @RequestBody @Valid SprayerConfirmRequest sprayerConfirmRequest,
            HttpServletRequest request) {

        // Extract sprayer email from the JWT token
        String sprayerEmail = jwtAuthenticationFilter.extractEmailFromToken(request);

        // Call the service to confirm payment
        SprayerConfirmResponse response = sprayerService.confirmPaymentReceived(
                orderId,
                sprayerEmail,
                sprayerConfirmRequest.getReceivedAmount());

        // Return the response entity with HTTP status 200 (OK)
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('SPRAYER')")
    public ResponseEntity<Sprayer> getSprayerByEmail(HttpServletRequest request) {
        String email = jwtAuthenticationFilter.extractEmailFromToken(request);
        Sprayer sprayer = sprayerService.findSprayerByEmail(email);
        return ResponseEntity.ok(sprayer);
    }

    @GetMapping("/paged")
    public Page<SprayerResponse> getPaginatedSprayers(Pageable pageable, @RequestParam int week) {
        return sprayerService.getPrioritizedAndPaginatedSprayers(pageable, week);
    }

    @RequestMapping(path ="/getAll",method = RequestMethod.GET)
   public ResponseEntity<List<Sprayer>> getAllSprayer() {
        Optional<List<Sprayer>> retrieve = sprayerService.getAll();
        return retrieve.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/spray-orders")
    @PreAuthorize("hasRole('SPRAYER')")
    public Page<SprayerOrderResponseDTO> getSprayOrdersByFarmerEmail(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page, size);
        // Retrieve the authenticated user's roles and log them
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authorities: " + authentication.getAuthorities()); // Log roles here

        // Extract the email from the JWT token in the request
        String email = jwtAuthenticationFilter.extractEmailFromToken(request);

        // Fetch and return the spray orders mapped to the DTO
        return sprayerService.getSprayOrdersBySprayerEmail(email, pageable);
    }
}
