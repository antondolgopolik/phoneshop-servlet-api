package com.es.phoneshop.web;

import com.es.phoneshop.exceptions.CartItemNotFoundException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteCartItemServlet extends HttpServlet {
    private CartService cartService;

    @Override
    public void init() throws ServletException {
        super.init();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Read id
        long id;
        try {
            id = Long.parseLong(request.getPathInfo().substring(1));
        } catch (NumberFormatException e) {
            response.sendError(400);
            return;
        }
        // Delete cart item
        Cart cart = cartService.getCart(request);
        try {
            cartService.delete(cart, id);
        } catch (CartItemNotFoundException e) {
            response.sendError(400);
            return;
        }
        // Send response
        response.sendRedirect(request.getContextPath() + "/cart?status=Cart item deletion succeeded");
    }
}
