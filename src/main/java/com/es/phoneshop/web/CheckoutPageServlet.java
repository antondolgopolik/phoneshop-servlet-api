package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.order.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

public class CheckoutPageServlet extends HttpServlet {
    private static final String ORDER_ATTR = "order";
    private static final String PAYMENT_METHODS_ATTR = "paymentMethods";
    private static final String ERRORS_ATTR = "errors";
    private static final String FIRST_NAME_PARAM = "firstName";
    private static final String LAST_NAME_PARAM = "lastName";
    private static final String PHONE_PARAM = "phone";
    private static final String DELIVERY_ADDRESS_PARAM = "deliveryAddress";
    private static final String DELIVERY_DATE_PARAM = "deliveryDate";
    private static final String PAYMENT_METHOD_PARAM = "paymentMethod";

    private CartService cartService;
    private OrderService orderService;
    private OrderDao orderDao;

    @Override
    public void init() throws ServletException {
        super.init();
        cartService = DefaultCartService.getInstance();
        orderService = DefaultOrderService.getInstance();
        orderDao = HashMapOrderDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get order
        Cart cart = cartService.getCart(request);
        Order order = orderService.getOrder(cart);
        // Send response
        request.setAttribute(ORDER_ATTR, order);
        request.setAttribute(PAYMENT_METHODS_ATTR, orderService.getPaymentMethods());
        request.getRequestDispatcher("/WEB-INF/pages/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> errors = new HashMap<>();
        // Read params
        String firstName = readRequiredFieldParameter(request, FIRST_NAME_PARAM, errors);
        String lastName = readRequiredFieldParameter(request, LAST_NAME_PARAM, errors);
        String phone = readRequiredFieldParameter(request, PHONE_PARAM, errors);
        LocalDate deliveryDate = readDeliveryDate(request, errors);
        String deliveryAddress = readRequiredFieldParameter(request, DELIVERY_ADDRESS_PARAM, errors);
        PaymentMethod paymentMethod = readPaymentMethod(request, errors);
        // Check for errors
        if (errors.isEmpty()) {
            Cart cart = cartService.getCart(request);
            Order order = orderService.getOrder(cart);
            // Update order
            order.setFirstName(firstName);
            order.setLastName(lastName);
            order.setPhone(phone);
            order.setDeliveryDate(deliveryDate);
            order.setDeliveryAddress(deliveryAddress);
            order.setPaymentMethod(paymentMethod);
            // Save order
            orderDao.save(order);
            // Clear cart
            cartService.clear(cart);
            // Redirect
            response.sendRedirect(request.getContextPath() + "/order/overview/" + order.getId());
        } else {
            // Send response
            request.setAttribute(PAYMENT_METHODS_ATTR, orderService.getPaymentMethods());
            request.setAttribute(ERRORS_ATTR, errors);
            doGet(request, response);
        }
    }

    private String readRequiredFieldParameter(HttpServletRequest request, String parameter, Map<String, String> errors) {
        String value = request.getParameter(parameter);
        if ((value == null) || value.isBlank()) {
            errors.put(parameter, "The field is required");
            return null;
        } else {
            return value;
        }
    }

    private LocalDate readDeliveryDate(HttpServletRequest request, Map<String, String> errors) {
        String deliveryDateParameter = readRequiredFieldParameter(request, DELIVERY_DATE_PARAM, errors);
        if (deliveryDateParameter != null) {
            try {
                return LocalDate.parse(deliveryDateParameter);
            } catch (DateTimeParseException e) {
                errors.put("deliveryDate", "Use yyyy-mm-dd date format");
            }
        }
        return null;
    }

    private PaymentMethod readPaymentMethod(HttpServletRequest request, Map<String, String> errors) {
        String paymentMethodParameter = readRequiredFieldParameter(request, PAYMENT_METHOD_PARAM, errors);
        PaymentMethod[] paymentMethods = PaymentMethod.values();
        for (PaymentMethod paymentMethod : paymentMethods) {
            if (paymentMethod.getPaymentMethod().equals(paymentMethodParameter)) {
                return paymentMethod;
            }
        }
        errors.put("paymentMethod", "Wrong payment method");
        return null;
    }
}
