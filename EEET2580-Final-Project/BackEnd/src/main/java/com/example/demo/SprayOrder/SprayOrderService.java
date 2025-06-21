package com.example.demo.SprayOrder;


import com.example.demo.Notification.NotificationController;
import com.example.demo.Notification.NotificationService;
import com.example.demo.SprayOrder.SprayOrderDTO.AssignSprayerRequest;
import com.example.demo.SprayOrder.Stripe.StripeService;
import com.example.demo.SprayOrder.enumType.OrderStatus;
import com.example.demo.SprayOrder.enumType.PaymentType;
import com.example.demo.SpraySession.enumType.CalenderType;
import com.example.demo.SpraySession.enumType.SubSessionType;
import com.example.demo.User.Customer.Farmer.Farmer;
import com.example.demo.User.Customer.Farmer.FarmerRepository;
import com.example.demo.User.Employee.Receptionist.ReceptionistRepository;
import com.example.demo.User.Employee.Receptionist.Receptionists;

import com.example.demo.User.Employee.Sprayer.Sprayer;
import com.example.demo.User.Employee.Sprayer.SprayerRepository;
import com.example.demo.SprayOrder.SprayOrderDTO.SprayOrderRequest;
import com.example.demo.SprayOrder.SprayOrderDTO.SprayOrderResponse;
import com.example.demo.SpraySession.SpraySessionsRepository;
import com.example.demo.SpraySession.SprayingSessions;
import com.example.demo.User.Employee.Sprayer.enumType.SprayerExpertise;
import com.stripe.exception.StripeException;
import jakarta.persistence.EntityNotFoundException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Comparator;

@Service
public class SprayOrderService {
    @Autowired
    private FarmerRepository farmerRepository;

    @Autowired
    private ReceptionistRepository receptionistRepository;

    @Autowired
    private SprayerRepository sprayerRepository;

    @Autowired
    private SpraySessionsRepository sprayingSessionsRepository;

    @Autowired
    private SprayOrderRepository sprayOrderRepository;

    @Autowired
    private StripeService stripeService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationController notificationController;

    public Page<SprayOrder> getSprayOrderPage(Pageable pageable) {
        return  sprayOrderRepository.findAll(pageable);
    }

    //Create by Receptionist
    public SprayOrderResponse createSprayOrder(SprayOrderRequest request) {

        Farmer farmerEntity = farmerRepository.findFarmerByEmail(request.getFarmer().getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Farmer not found"));


        SprayingSessions timeSlot = getTimeSlot(request);

        SprayOrder sprayOrder = new SprayOrder();
        sprayOrder.setFarmer(farmerEntity);

        sprayOrder.setSubSessionType(request.getSubSessionType());

        if (request.getSubSessionType() == SubSessionType.SUB_SESSION_1 && !timeSlot.isSubSession1Booked()) {
            timeSlot.setSubSession1Booked(true);
        } else if (request.getSubSessionType() == SubSessionType.SUB_SESSION_2 && !timeSlot.isSubSession2Booked()) {
            timeSlot.setSubSession2Booked(true);
        } else {
            throw new IllegalStateException("Session already booked");
        }

        if (request.getPaymentType() == PaymentType.CARD) {
            System.out.println("The farmer will have to pay by Cash if Receptionist creates their order");
            throw new IllegalStateException("CARD payment is not valid when creating by RECEPTIONIST");
        }

        sprayOrder.setCropType(request.getCropType());
        sprayOrder.setFarmArea(request.getFarmArea());
        sprayOrder.setPaymentType(request.getPaymentType());
        sprayOrder.setCreatedDateTime(LocalDateTime.now());
        sprayOrder.setSprayingSessions(timeSlot);


        sprayOrder.setCalenderType(request.getCalenderType());
        setDefaultFields(sprayOrder);

        sprayOrder.setLocation(request.getLocation());

        // Calculate total cost
        long costPerHectare = 30000L;
        long farmAreaInLong = (long) request.getFarmArea(); // Convert farm area to long
        long totalCostInVND = costPerHectare * farmAreaInLong; // Total cost in VND
        sprayOrder.setTotalCost(totalCostInVND);
        SprayOrder savedOrder = sprayOrderRepository.save(sprayOrder);


        updateOrderStatus(savedOrder.getSprayId(), OrderStatus.CONFIRMED);


        SprayOrderResponse  response= new SprayOrderResponse(savedOrder);
        System.out.println("SprayOrderResponse: " + response);
        return response;
    }


    public SprayOrderResponse cancelSprayOrder(Long orderId, String receptionistEmail) {
        SprayOrder sprayOrder = sprayOrderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Spray Order not found with id " + orderId));

//        Receptionists receptionist = receptionistRepository.findReceptionistByEmail(receptionistEmail)
//                .orElseThrow(() -> new EntityNotFoundException("Receptionist not found"));
//        sprayOrder.setReceptionist(receptionist);


        if (sprayOrder.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Order is already cancelled");
        }

        // Mark the sub-session as available again
        SprayingSessions timeSlot = sprayOrder.getSprayingSessions();
        if (sprayOrder.getSubSessionType() == SubSessionType.SUB_SESSION_1) {
            timeSlot.setSubSession1Booked(false);
        } else if (sprayOrder.getSubSessionType() == SubSessionType.SUB_SESSION_2) {
            timeSlot.setSubSession2Booked(false);
        }

        updateOrderStatus(sprayOrder.getSprayId(), OrderStatus.CANCELLED);

        SprayOrder updatedOrder = sprayOrderRepository.save(sprayOrder);


        return new SprayOrderResponse(updatedOrder);
    }

    //Confirm Order by Receptionist
    public SprayOrderResponse confirmSprayOrder(Long orderId, String receptionistEmail){
        SprayOrder sprayOrder = sprayOrderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Spray Order not found with id " + orderId));

//        Receptionists receptionist = receptionistRepository.findReceptionistByEmail(receptionistEmail)
//                .orElseThrow(() -> new EntityNotFoundException("Receptionist not found"));

//        sprayOrder.setReceptionist(receptionist);

        if (sprayOrder.getOrderStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Order is not in the correct state to Confirm");
        }

        //Send Email
        updateOrderStatus(sprayOrder.getSprayId(), OrderStatus.CONFIRMED);

        SprayOrder updatedOrder = sprayOrderRepository.save(sprayOrder);


        return new SprayOrderResponse(updatedOrder);
    }


    public SprayOrderResponse farmerCreateSprayOrder(SprayOrderRequest request, String farmerEmail) {
        // Retrieve Farmer entity
        Farmer farmerEntity = farmerRepository.findFarmerByEmail(farmerEmail)
                .orElseThrow(() -> new EntityNotFoundException("Farmer not found"));

        SprayingSessions timeSlot = getTimeSlot(request);
        // Check the calendar type and retrieve the appropriate time slot


        SprayOrder sprayOrder = new SprayOrder();

        // Mark sub-session as booked
        isSubSessionBooked(request,timeSlot);

        sprayOrder.setFarmer(farmerEntity);
        sprayOrder.setSubSessionType(request.getSubSessionType());
        sprayOrder.setCropType(request.getCropType());
        sprayOrder.setFarmArea(request.getFarmArea());
        sprayOrder.setPaymentType(request.getPaymentType());
        sprayOrder.setCreatedDateTime(LocalDateTime.now());
        sprayOrder.setSprayingSessions(timeSlot);
        sprayOrder.setLocation(request.getLocation());

        sprayOrder.setCalenderType(request.getCalenderType());
        // Default Values
        setDefaultFields(sprayOrder);

        // Calculate total cost
        long costPerHectare = 30000L;
        long farmAreaInLong = (long) request.getFarmArea(); // Convert farm area to long
        long totalCostInVND = costPerHectare * farmAreaInLong; // Total cost in VND

        sprayOrder.setTotalCost(totalCostInVND); // Store total cost as Long in SprayOrder

        SprayOrder savedOrder = sprayOrderRepository.save(sprayOrder);

        // Handle Stripe payment if payment type is CARD
        if (request.getPaymentType() == PaymentType.CARD) {
            updateOrderStatus(savedOrder.getSprayId(), OrderStatus.PENDING);
            sprayOrder.setReceivedAmount(sprayOrder.getTotalCost());

            initStripe(savedOrder, totalCostInVND);
            return new SprayOrderResponse(savedOrder);
        } else {
            sprayOrder.setReceivedAmount(0.0);

        }

        // Call the updateOrderStatus method to update the order status and send emails
        updateOrderStatus(savedOrder.getSprayId(), OrderStatus.PENDING);

        return new SprayOrderResponse(savedOrder);
    }

    public SprayOrder assignSprayers(AssignSprayerRequest assignSprayerRequest) throws Exception {
        Optional<SprayOrder> sprayOrderOpt = sprayOrderRepository.findById(assignSprayerRequest.getSprayId());

        if (sprayOrderOpt.isPresent()) {
            SprayOrder sprayOrder = sprayOrderOpt.get();
            List<String> sprayerEmails = assignSprayerRequest.getSprayerEmails();

            // Fetch sprayers by their emails
            List<Sprayer> requestedSprayers = sprayerRepository.findAllByEmailIn(sprayerEmails);

            // Validate the number of sprayers
            if (requestedSprayers.size() > 2) {
                throw new IllegalArgumentException("At most 2 sprayers can be assigned.");
            }

            // Check if there is at least one expert
            boolean hasExpert = requestedSprayers.stream()
                    .anyMatch(s -> s.getSprayerExpertise() == SprayerExpertise.EXPERT);

            // If there is no expert, check if there is an apprentice without an accompanying adept or expert
            boolean hasApprenticeWithoutAccompaniment = requestedSprayers.stream()
                    .anyMatch(s -> s.getSprayerExpertise() == SprayerExpertise.APPRENTICE) &&
                    requestedSprayers.stream()
                            .noneMatch(s -> s.getSprayerExpertise() == SprayerExpertise.ADEPT || s.getSprayerExpertise() == SprayerExpertise.EXPERT);

            if (hasApprenticeWithoutAccompaniment) {
                throw new IllegalArgumentException("An Apprentice sprayer must be accompanied by an Adept or Expert sprayer.");
            }

            // If the list contains an expert or valid apprentice assignment, proceed
            sprayOrder.setSprayers(requestedSprayers);

            // Update the order status to "ASSIGNED"
            updateOrderStatus(sprayOrder.getSprayId(), OrderStatus.ASSIGNED);

            // Save and return the updated spray order
            return sprayOrderRepository.save(sprayOrder);
        } else {
            throw new Exception("Order Not Found");
        }
    }





    public SprayOrderResponse markOrderInProgress(Long orderId, String sprayerEmail) {
        SprayOrder sprayOrder = sprayOrderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Spray Order not found with id " + orderId));

        if (sprayOrder.getSprayers() == null || sprayOrder.getSprayers().isEmpty()) {
            throw new IllegalStateException("Sprayers list is null or empty for order ID " + orderId);
        }

        System.out.println("Sprayer num: "+ sprayOrder.getSprayers().size());

        if (sprayOrder.getOrderStatus() != OrderStatus.ASSIGNED) {
            throw new IllegalStateException("Order is not in a state to be started");
        }

        boolean isSprayerAssigned = sprayOrder.getSprayers().stream()
                .anyMatch(assignedSprayer -> assignedSprayer.getEmail().equals(sprayerEmail));
        if (!isSprayerAssigned) {
            throw new SecurityException("You are not assigned to this order");
        }

        updateOrderStatus(sprayOrder.getSprayId(), OrderStatus.IN_PROGRESS);

        SprayOrder updatedOrder = sprayOrderRepository.save(sprayOrder);


        return new SprayOrderResponse(updatedOrder);
    }

    public void markOrderAsCompletedIfBothConfirmed(SprayOrder order) {
        if (order.isSprayerConfirmation() && order.isFarmerConfirmation()) {
            // Automatically mark the order as completed if both confirmed
            updateOrderStatus(order.getSprayId(), OrderStatus.COMPLETED);
            sprayOrderRepository.save(order);
            System.out.println("Order marked as completed automatically.");
        } else {
            System.out.println("Needs Farmer/Sprayer to mark the Spray Order in order to be Completed");
        }
    }


    public SprayOrder getSprayOrderById(Long sprayId) {
        return sprayOrderRepository.findById(sprayId)
                .orElseThrow(() -> new EntityNotFoundException("Spray order not found with id " + sprayId));
    }







    /*-----------------Helper Method----------------------- */

    //MANAGE ORDER

    //Handle SubSession Booked
    public void isSubSessionBooked(SprayOrderRequest request ,SprayingSessions timeSlot){
        if (request.getSubSessionType() == SubSessionType.SUB_SESSION_1 && !timeSlot.isSubSession1Booked()) {
            timeSlot.setSubSession1Booked(true);
        } else if (request.getSubSessionType() == SubSessionType.SUB_SESSION_2 && !timeSlot.isSubSession2Booked()) {
            timeSlot.setSubSession2Booked(true);
        } else {
            throw new IllegalStateException("Session already booked");
        }
    }

    //Set Default Values
    public void setDefaultFields(SprayOrder sprayOrder){
        sprayOrder.setChange(0.0);
        sprayOrder.setSprayerConfirmation(false);
        sprayOrder.setFarmerConfirmation(false);
        sprayOrder.setStripeId("");
    }

    private SprayingSessions getTimeSlot(SprayOrderRequest request) {
        LocalDate parsedDate = request.getTimeSlot().getParsedDate();
        // Check the calendar type and retrieve the appropriate time slot
        if (request.getCalenderType() == CalenderType.LUNAR) {
            request.setCalenderType(CalenderType.LUNAR);
            // Retrieve the time slot by lunar date, start time, and end time
            return sprayingSessionsRepository.findByLunarDateAndStartTimeAndEndTime(
                    parsedDate,
                    request.getTimeSlot().getStartTime(),
                    request.getTimeSlot().getEndTime()
            ).orElseThrow(() -> new EntityNotFoundException("Time slot not found"));

        } else {
            request.setCalenderType(CalenderType.GREGORIAN);
            // Retrieve the time slot by Gregorian date, start time, and end time
            return sprayingSessionsRepository.findByDateAndStartTimeAndEndTime(
                    parsedDate,
                    request.getTimeSlot().getStartTime(),
                    request.getTimeSlot().getEndTime()
            ).orElseThrow(() -> new EntityNotFoundException("Time slot not found"));
        }
    }


    //HANDLE STRIPE PAYMENT

    private void initStripe(SprayOrder savedOrder, long totalCostInVND){
        try {
            JSONObject stripeSession = stripeService.createCheckoutSession(totalCostInVND);
            savedOrder.setStripeId(stripeSession.getString("id"));
            sprayOrderRepository.save(savedOrder);

            // Update the response with Stripe details

        } catch (StripeException e) {
            System.err.println("StripeException: " + e.getMessage());
            throw new RuntimeException("Failed to create Stripe payment session", e);
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            throw new RuntimeException("Error in Stripe payment", e);
        }
    }

    public void handlePaymentSuccess(String stripeId) {
        SprayOrder order = sprayOrderRepository.findByStripeId(stripeId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        System.out.println(stripeId);
        order.setReceivedAmount(order.getTotalCost());

        System.out.println(order.getReceivedAmount());
        sprayOrderRepository.save(order);

        // Optionally, you can mark the order as completed here if necessary

    }

    public void handlePaymentCancellation(String stripeId) {
        SprayOrder order = sprayOrderRepository.findByStripeId(stripeId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        // Optionally, you can cancel the order and unbook the session here
        // Example: unbook the session
        SprayingSessions timeSlot = order.getSprayingSessions();
        if (order.getSubSessionType() == SubSessionType.SUB_SESSION_1) {
            timeSlot.setSubSession1Booked(false);
        } else if (order.getSubSessionType() == SubSessionType.SUB_SESSION_2) {
            timeSlot.setSubSession2Booked(false);
        }
        // Update the time slot in the repository
        sprayingSessionsRepository.save(timeSlot);

        // Optionally, set the order status to canceled
        order.setOrderStatus(OrderStatus.CANCELLED);
        sprayOrderRepository.save(order);
    }

    //HANDLE EMAIL SENDER
    // Method to update the status of an order and trigger mock email sending
    public void updateOrderStatus(Long orderId, OrderStatus newStatus) {
        // Retrieve the order by ID
        SprayOrder order = sprayOrderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        // Update the status of the order
        order.setOrderStatus(newStatus);
        sprayOrderRepository.save(order); // Save the updated order

        // Send the mock emails and real-time notifications based on the new status
        switch (newStatus) {
            case PENDING:
                // No email or notification is sent for Pending status
                break;

            case CANCELLED:
                sendOrderCancelledEmail(order);
                notificationController.notifyFarmer(order.getFarmer().getEmail(),
                        "Your order has been cancelled.");
                break;

            case CONFIRMED:
                sendOrderConfirmedEmail(order);
                notificationController.notifyFarmer(order.getFarmer().getEmail(),
                        "Your order has been confirmed.");
                break;

            case ASSIGNED:
                sendOrderAssignedEmail(Optional.of(order));
                notificationController.notifyFarmer(order.getFarmer().getEmail(),
                        "Your order has been assigned!");

                List<String> sprayerEmails = order.getSprayers().stream()
                        .map(Sprayer::getEmail) // Get the email of each sprayer
                        .collect(Collectors.toList());

                // Notify all sprayers
                notificationController.notifySprayers(sprayerEmails, "You have been assigned to order " + order.getSprayId());
                break;

            case IN_PROGRESS:
                sendOrderInProgress(order);
                notificationController.notifyFarmer(order.getFarmer().getEmail(),
                        "Your order is now in progress.");
                break;

            case COMPLETED:
                sendOrderCompletedEmail(order);
                notificationController.notifyFarmer(order.getFarmer().getEmail(),
                        "Your order has been completed.");
                break;
        }
    }


    //Email Sending Logics
    // Send email for Cancelled status
    private void sendOrderCancelledEmail(SprayOrder order) {
        notificationService.sendMockEmail(
                order.getFarmer().getEmail(),  // Fetch the farmer's email from the order
                "Order Cancelled",
                 "Order " + order.getSprayId()+ " has been Cancelled"
        );
    }

    // Send email for Confirmed status
    private void sendOrderConfirmedEmail(SprayOrder order) {
        notificationService.sendMockEmail(
                order.getFarmer().getEmail(),
                "Order Confirmed",
                "Order " + order.getSprayId() + " is Confirmed! HoverSprite is assigning Sprayers to process your order."
        );
    }

    // Send email for Assigned status
    private void sendOrderAssignedEmail(Optional<SprayOrder> order) {
        String sprayerNames = order.get().getSprayers().stream()
                .map(Sprayer::getSprayerFullName)  // Get the full name of each sprayer
                .collect(Collectors.joining(", "));

        // Send email to the farmer
        notificationService.sendMockEmail(
                order.get().getFarmer().getEmail(),
                "Order Assigned",
                "Order " + order.get().getSprayId() + " has been assigned to the following sprayer(s): " + sprayerNames
        );

        // Send email to each assigned sprayer
        for (Sprayer sprayer : order.get().getSprayers()) {
            notificationService.sendMockEmail(
                    sprayer.getEmail(),
                    "New Spraying Order Assigned",
                    "Order Details:\nFarmer: " + order.get().getFarmer().getEmail() +
                            "\nLocation: " + order.get().getLocation() +
                            "\nPhone: " + order.get().getFarmer().getPhoneNumber()
            );
        }
    }

    //Send email to Farmer on Completed.
    private void sendOrderCompletedEmail(SprayOrder sprayOrder){
        notificationService.sendMockEmail(
                sprayOrder.getFarmer().getEmail(),  // Fetch the farmer's email from the order
                "Order Completed",
                "Order " + sprayOrder.getSprayId()+ " has been Completed! Please rate our service now."
        );
    }

    private void sendOrderInProgress(SprayOrder sprayOrder){
        notificationService.sendMockEmail(
                sprayOrder.getFarmer().getEmail(),  // Fetch the farmer's email from the order
                "Order In Progress",
                "Order " + sprayOrder.getSprayId()+ " is being executed."
        );
    }
}

