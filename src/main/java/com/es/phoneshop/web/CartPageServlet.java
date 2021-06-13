package com.es.phoneshop.web;

import com.es.phoneshop.exceptions.CartItemNotFoundException;
import com.es.phoneshop.exceptions.IllegalProductQuantityValueException;
import com.es.phoneshop.exceptions.NoProductWithSuchIdException;
import com.es.phoneshop.exceptions.ProductNotEnoughException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CartPageServlet extends HttpServlet {
    private static final String ID_PARAM = "id";
    private static final String QUANTITY_PARAM = "quantity";
    private static final String ERRORS_ATTR = "errors";
    private static final String CART_ATTR = "cart";

    private CartService cartService;

    @Override
    public void init() throws ServletException {
        super.init();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get cart
        Cart cart = DefaultCartService.getInstance().getCart(request);
        // Send response
        request.setAttribute(CART_ATTR, cart);
        request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Read params
        String[] productIdParams = request.getParameterValues(ID_PARAM);
        String[] quantityParams = request.getParameterValues(QUANTITY_PARAM);
        if (productIdParams.length != quantityParams.length) {
            // Bad request
            response.sendError(400);
            return;
        }
        // Parse params
        Map<Long, String> errors = new HashMap<>();
        long[] ids;
        try {
            ids = parseProductIdParams(productIdParams);
        } catch (NumberFormatException e) {
            // Bad request
            response.sendError(400);
            return;
        }
        int[] quantities = parseQuantityParams(quantityParams, ids, errors);
        // Update
        Cart cart = cartService.getCart(request);
        for (int i = 0; i < ids.length; i++) {
            if (!errors.containsKey(ids[i])) {
                try {
                    cartService.update(cart, ids[i], quantities[i]);
                } catch (IllegalProductQuantityValueException | ProductNotEnoughException e) {
                    errors.put(ids[i], e.getMessage());
                } catch (NoProductWithSuchIdException | CartItemNotFoundException e) {
                    // Bad request
                    response.sendError(400);
                    return;
                }
            }
        }
        // Send response
        if (errors.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart?status=Cart update succeeded");
        } else {
            request.setAttribute(ERRORS_ATTR, errors);
            doGet(request, response);
        }
    }

    private long[] parseProductIdParams(String[] productIdParams) {
        long[] productIds = new long[productIdParams.length];
        for (int i = 0; i < productIds.length; i++) {
            productIds[i] = Long.parseLong(productIdParams[i]);
        }
        return productIds;
    }

    private int[] parseQuantityParams(String[] quantityParams, long[] ids, Map<Long, String> errors) {
        int[] quantities = new int[quantityParams.length];
        for (int i = 0; i < quantities.length; i++) {
            try {
                quantities[i] = Integer.parseInt(quantityParams[i]);
            } catch (NumberFormatException e) {
                errors.put(ids[i], "Number format exception");
            }
        }
        return quantities;
    }
}
