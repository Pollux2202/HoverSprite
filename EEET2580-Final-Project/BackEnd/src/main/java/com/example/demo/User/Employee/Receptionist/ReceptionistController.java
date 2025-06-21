package com.example.demo.User.Employee.Receptionist;


import java.util.Optional;

import com.example.demo.Security.Filter.JwtAuthenticationFilter;
import com.example.demo.SprayOrder.SprayOrder;
import com.example.demo.SprayOrder.SprayOrderDTO.ReceptionistOrderDTO;
import com.example.demo.User.Customer.Farmer.Farmer;
import com.example.demo.User.Employee.Sprayer.Sprayer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Authentication.AuthDTO.AuthRequest;
import com.example.demo.Authentication.AuthDTO.AuthResponse;
import com.example.demo.Security.Utils.CookieUtil;
import com.example.demo.User.Employee.Receptionist.ReceptionistDTO.Request.ReceptionistCreateRequest;
import com.example.demo.User.Employee.Receptionist.ReceptionistDTO.Response.ReceptionistResponse;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/receptionist")
public class ReceptionistController {
    @Autowired
    private ReceptionistService receptionistService;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @PostMapping("/create")
    public ResponseEntity<ReceptionistResponse> createReceptionist(@RequestBody @Valid ReceptionistCreateRequest receptionRequest) {
        ReceptionistResponse receptionist = receptionistService.createReceptionist(receptionRequest);
        return ResponseEntity.ok(receptionist);
    }

    @GetMapping("{rId}")
    public ResponseEntity<Optional<Receptionists>> getReceptionistById(@PathVariable String rId){
        Optional<Receptionists> receptionists = receptionistService.findReceptionistById(rId);
        if (receptionists != null) {
            return ResponseEntity.ok(receptionists);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> receptionistLogin(@RequestBody @Valid AuthRequest authRequest, HttpServletResponse response){

        AuthResponse authResponse = receptionistService.receptionistLogin(authRequest);
        String token = authResponse.getToken();
        CookieUtil.addHttpOnlyCookie(response, "jwtToken", token, 360000);
        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('RECEPTIONIST')")
    public ResponseEntity<Receptionists> getReceptionistByEmail(HttpServletRequest request) {
        String email = jwtAuthenticationFilter.extractEmailFromToken(request);
        Receptionists receptionist = receptionistService.findReceptionistByEmail(email);
        return ResponseEntity.ok(receptionist);
    }

    @GetMapping("/spray-orders/all")
    @PreAuthorize("hasRole('RECEPTIONIST')")
    public Page<ReceptionistOrderDTO> getAllOrdersPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest request) {

        // Extract the email from the JWT token
        String receptionistEmail = jwtAuthenticationFilter.extractEmailFromToken(request);

        // Create a Pageable instance with the requested page number and page size
        Pageable pageable = PageRequest.of(page, size);

        // Pass the Pageable and email to the service
        return receptionistService.getAllSprayOrders(pageable, receptionistEmail);
    }

    @GetMapping("/view/farmers")
    @PreAuthorize("hasRole('RECEPTIONIST')")
    public ResponseEntity<Page<Farmer>> findAllFarmers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Farmer> farmers = receptionistService.findAllFarmers(pageable);
        return ResponseEntity.ok(farmers);
    }

    @GetMapping("/view/sprayers")
    @PreAuthorize("hasRole('RECEPTIONIST')")
    public ResponseEntity<Page<Sprayer>> findAllSprayers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Sprayer> sprayers = receptionistService.findAllSprayers(pageable);
        return ResponseEntity.ok(sprayers);
    }

}
