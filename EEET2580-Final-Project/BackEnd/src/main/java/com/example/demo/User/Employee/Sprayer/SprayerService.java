package com.example.demo.User.Employee.Sprayer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.demo.SprayOrder.SprayOrderDTO.FarmerOrderResponseDTO;
import com.example.demo.SprayOrder.SprayOrderDTO.SprayerOrderResponseDTO;
import com.example.demo.SprayOrder.enumType.PaymentType;
import com.example.demo.User.Customer.Farmer.Farmer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.Authentication.AuthDTO.AuthRequest;
import com.example.demo.Authentication.AuthDTO.AuthResponse;
import com.example.demo.Authentication.AuthService;
import com.example.demo.Security.Utils.PasswordUtil;
import com.example.demo.SprayOrder.SprayOrder;
import com.example.demo.SprayOrder.*;
import com.example.demo.SprayOrder.SprayOrderService;
import com.example.demo.User.Employee.Sprayer.SprayerDTO.SprayerRequest.SprayerCreateRequest;
import com.example.demo.User.Employee.Sprayer.SprayerDTO.SprayerResponse.SprayerConfirmResponse;
import com.example.demo.User.Employee.Sprayer.SprayerDTO.SprayerResponse.SprayerResponse;
import com.example.demo.User.Employee.Sprayer.SprayerValidator.SprayerIdValidator;
import com.example.demo.User.enumType.UserType;

import jakarta.persistence.EntityNotFoundException;

@Service
public class SprayerService {
    @Autowired
    private SprayerRepository sprayerRepository;

    @Autowired
    private SprayerIdValidator sprayerIdValidator;

    @Autowired
    private SprayOrderRepository sprayOrderRepository;

    @Autowired
    private SprayOrderService sprayOrderService;

    public Optional<List<Sprayer>> getAll(){
        return Optional.of(sprayerRepository.findAll());
    }

    public SprayerResponse createSprayer(SprayerCreateRequest sprayerCreateRequest){
        if (sprayerRepository.findSprayerByPhoneNumber(sprayerCreateRequest.getPhoneNumber()).isPresent()) {
            throw new IllegalArgumentException("Phone number already used");
        }

        if (sprayerRepository.findSprayerByEmail(sprayerCreateRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already used");
        }

        Sprayer sprayer = new Sprayer();

        String sId = sprayerIdValidator.generateId();

        sprayer.setsId(sId);
        sprayer.setFirstName(sprayerCreateRequest.getFirstName());
        sprayer.setLastName(sprayerCreateRequest.getLastName());

        String hashedPassword = PasswordUtil.hashPassword(sprayerCreateRequest.getPassword());
        sprayer.setPassword(hashedPassword);

        sprayer.setPhoneNumber(sprayerCreateRequest.getPhoneNumber());
        sprayer.setEmail(sprayerCreateRequest.getEmail());
        sprayer.setAddress(sprayerCreateRequest.getAddress());
        sprayer.setUserType(UserType.SPRAYER);
        sprayer.setSprayerExpertise(sprayerCreateRequest.getSprayerExpertise());

        System.out.println(sprayerCreateRequest.getSprayerExpertise());
        sprayerRepository.save(sprayer);
        // Save the Farmer to the database
        return new SprayerResponse(sprayer);
    }

    public Optional<Sprayer> findSprayerById(String sId) {
        return sprayerRepository.findSprayerById(sId);
    }

    public AuthResponse sprayerLogin(AuthRequest authRequest) {
        Optional<Sprayer> sprayer;

        // Check if the input is an email or a phone number
        if (authRequest.getEmailOrPhoneNumber().contains("@")) {
            // It's an email
            sprayer = sprayerRepository.findSprayerByEmail(authRequest.getEmailOrPhoneNumber());
        } else {
            // It's a phone number
            sprayer = sprayerRepository.findSprayerByPhoneNumber(authRequest.getEmailOrPhoneNumber());
        }

        // Check if farmer exists and password is correct
        if (sprayer.isEmpty() || !PasswordUtil.checkPassword(authRequest.getPassword(), sprayer.get().getPassword())) {
            throw new IllegalArgumentException("Invalid email/phone number or password");
        }

        // Generate the token
        String token = AuthService.generateToken(sprayer.get().getEmail(), sprayer.get().getUserType()); // Use email as the subject

        // Prepare the response
        AuthResponse authResponse = new AuthResponse();
        authResponse.setAuth(true);
        authResponse.setToken(token);

        return authResponse;
    }

    public SprayerConfirmResponse confirmPaymentReceived(Long sprayOrderId, String sprayerEmail, Double receivedAmount) {
        SprayOrder order = sprayOrderRepository.findById(sprayOrderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        boolean isAssignedSprayer = order.getSprayers().stream()
                .anyMatch(sprayer -> sprayer.getEmail().equals(sprayerEmail));
        if (!isAssignedSprayer) {
            throw new IllegalArgumentException("This sprayer is not assigned to the order.");
        }

        double totalCost = order.getTotalCost();
        double change = 0.0;

        if (order.getPaymentType().equals(PaymentType.CASH)) {
            if (receivedAmount == null) {
                throw new IllegalArgumentException("Received amount must be provided for CASH payments.");
            }

            // Calculate change
            change = receivedAmount - totalCost;

            // Mark payment confirmation
            order.setReceivedAmount(receivedAmount);
            order.setChange(change);
        } else if (order.getPaymentType().equals(PaymentType.CARD)) {
            // For CARD payments, assume the amount was processed through Stripe
            order.setReceivedAmount(totalCost); // Just to indicate full amount paid, but no real amount needed here
            order.setChange(0.0); // No change needed
        } else {
            throw new IllegalArgumentException("Unknown payment type.");
        }

        // Mark the order as confirmed
        order.setSprayerConfirmation(true);

        // Save the order
        sprayOrderRepository.save(order);

        System.out.println("Total: " + totalCost + " Received: " + (order.getPaymentType().equals(PaymentType.CASH) ? receivedAmount : totalCost) + " Change: " + change);

        // Check if both confirmations are done and mark the order as completed
        sprayOrderService.markOrderAsCompletedIfBothConfirmed(order);

        return new SprayerConfirmResponse(order);
    }


    public Sprayer findSprayerByEmail(String email) {
        return sprayerRepository.findSprayerByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Sprayer with email " + email + " not found."));
    }


    //SPRAYER SUGGESTION IN PAGINATION


    public boolean hasAcceptedRequestsInSameWeek(Sprayer sprayer, int week) {
        List<SprayOrder> acceptedOrders = sprayOrderRepository.findAcceptedOrdersBySprayerAndWeek(sprayer, week);
        return !acceptedOrders.isEmpty();
    }

    public Page<SprayerOrderResponseDTO> getSprayOrdersBySprayerEmail(String email, Pageable pageable){
        // Validate the farmer's existence
        sprayerRepository.findSprayerByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Sprayer with email " + email + " not found."));

        // Fetch the paginated spray orders
        Page<SprayOrder> sprayOrders = sprayOrderRepository.findBySprayersEmail(email, pageable);

        // Map the SprayOrder entities to FarmerOrderResponseDTO using the DTO constructor
        return sprayOrders.map(SprayerOrderResponseDTO::new);
    }

    public Page<SprayerResponse> getPrioritizedAndPaginatedSprayers(Pageable pageable, int week) {
        // Fetch all sprayers
        List<Sprayer> allSprayers = sprayerRepository.findAll();

        // Prioritize sprayers who have not accepted any request in the same week
        List<Sprayer> prioritizedSprayers = allSprayers.stream()
                .sorted((s1, s2) -> {
                    // Check acceptance status
                    boolean s1HasAccepted = hasAcceptedRequestsInSameWeek(s1, week);
                    boolean s2HasAccepted = hasAcceptedRequestsInSameWeek(s2, week);

                    // Prioritize non-accepted requests
                    int acceptanceComparison = Boolean.compare(s1HasAccepted, s2HasAccepted);

                    // If both have the same acceptance status, prioritize apprentices
                    if (acceptanceComparison == 0) {
                        return s1.getSprayerExpertise().compareTo(s2.getSprayerExpertise());
                    }

                    return acceptanceComparison;
                })
                .collect(Collectors.toList());

        // Convert Sprayer entities to SprayerResponse DTOs
        List<SprayerResponse> sprayerResponses = prioritizedSprayers.stream()
                .map(SprayerResponse::new)
                .collect(Collectors.toList());

        // Paginate the sorted list
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), sprayerResponses.size());

        if (start >= sprayerResponses.size()) {
            return new PageImpl<>(Collections.emptyList(), pageable, sprayerResponses.size()); // Return empty page if page number is out of range
        }

        List<SprayerResponse> pageContent = sprayerResponses.subList(start, end);
        return new PageImpl<>(pageContent, pageable, sprayerResponses.size());
    }
    public Page<Sprayer> findAllSprayers(Pageable pageable) {
        return sprayerRepository.findAll(pageable);
    }
}
