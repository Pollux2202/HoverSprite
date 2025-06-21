package com.example.demo.SprayOrder;


import com.example.demo.SprayOrder.SprayOrderDTO.AssignSprayerRequest;
import com.example.demo.SprayOrder.SprayOrderDTO.SprayOrderRequest;
import com.example.demo.SprayOrder.SprayOrderDTO.SprayOrderResponse;

import com.example.demo.Security.Filter.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping("/spray-order")
public class SprayOrderController {

    @Autowired
    private SprayOrderService sprayOrderService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    @RequestMapping(path ="",method = RequestMethod.GET)
    public ResponseEntity<Page<SprayOrder>> getSprayOrderPage(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "3") int pageSize) {


        return ResponseEntity.ok(sprayOrderService.getSprayOrderPage(PageRequest.of(pageNo,pageSize)));
    }


    //Receptionist Create Spray Order
    @PostMapping("/create")
    @PreAuthorize("hasRole('RECEPTIONIST')")
    public ResponseEntity<SprayOrderResponse> receptionistCreateSprayOrder(
            @RequestBody @Valid SprayOrderRequest sprayOrderRequest
//            , HttpServletRequest httpRequest
    )
    {
        // Call the service to create a SprayOrder and obtain the response
        SprayOrderResponse createdOrderResponse = sprayOrderService.createSprayOrder(sprayOrderRequest);
        // Return the response with HTTP status 200 OK
        return ResponseEntity.ok(createdOrderResponse);
    }

    @PostMapping("/farmer_created")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<SprayOrderResponse> farmerCreateSprayOrder(
            @RequestBody @Valid SprayOrderRequest sprayOrderRequest,
            HttpServletRequest httpRequest) {

        String farmerEmail = jwtAuthenticationFilter.extractEmailFromToken(httpRequest);
        SprayOrderResponse createdOrderResponse = sprayOrderService.farmerCreateSprayOrder(sprayOrderRequest, farmerEmail);


        return ResponseEntity.ok(createdOrderResponse);
    }

    @PutMapping ("/confirm/{sprayId}")
    @PreAuthorize("hasRole('RECEPTIONIST')")
    public ResponseEntity<SprayOrderResponse> confirmSprayOrder(@PathVariable Long sprayId,
                                                                HttpServletRequest request){
        String receptionistEmail = jwtAuthenticationFilter.extractEmailFromToken(request);
        SprayOrderResponse confirmResponse = sprayOrderService.confirmSprayOrder(sprayId, receptionistEmail);

        return ResponseEntity.ok(confirmResponse);
    }

    @PostMapping("/cancel/{sprayId}")
    @PreAuthorize("hasRole('RECEPTIONIST')")
    public ResponseEntity<SprayOrderResponse> cancelSprayOrder(@PathVariable Long sprayId,
                                                               HttpServletRequest httpServletRequest){
        String receptionistEmail = jwtAuthenticationFilter.extractEmailFromToken(httpServletRequest);
        SprayOrderResponse cancelledOrderResponse = sprayOrderService.cancelSprayOrder(sprayId, receptionistEmail);

        // Return the response with HTTP status 200 OK
        return ResponseEntity.ok(cancelledOrderResponse);
    }

    @PostMapping("/assign-sprayers")
    @PreAuthorize("hasRole('RECEPTIONIST')")
    public ResponseEntity<SprayOrderResponse> assignSprayers(@RequestBody AssignSprayerRequest assignSprayerRequest) throws Exception {
        SprayOrder sprayOrder = sprayOrderService.assignSprayers(assignSprayerRequest);

        if (sprayOrder != null) {
            SprayOrderResponse response = new SprayOrderResponse(sprayOrder);
            return ResponseEntity.ok(response);
        } else {
            // Option 1: Return a bad request without a body
            return ResponseEntity.badRequest().build();
        }
    }



    // POST method to mark the order as In Progress
    @PostMapping("/sprayer-confirmed/{sprayId}")
    @PreAuthorize("hasRole('SPRAYER')")
    public ResponseEntity<SprayOrderResponse> markOrderInProgress(
            @PathVariable Long sprayId,HttpServletRequest httpServletRequest) {
        String sprayerEmail = jwtAuthenticationFilter.extractEmailFromToken(httpServletRequest);
        SprayOrderResponse sprayOrderResponse = sprayOrderService.markOrderInProgress(sprayId, sprayerEmail);

        return ResponseEntity.ok(sprayOrderResponse);
    }



}
