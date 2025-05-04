// servlet/ProductServlet.java
package servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Product;
import service.ProductService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Product> allProducts = productService.findAll();

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        out.println("<html><body>");
        out.println("<h1>Product List</h1>");
        for (Product product : allProducts) {
            out.println("<p>" + product.getId() + " - " + product.getName() + " : $" + product.getPrice() + "</p>");
        }
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        double price = Double.parseDouble(req.getParameter("price"));

        Product product = new Product();
        product.setName(name);
        product.setPrice(price);

        productService.save(product);
        resp.sendRedirect("products");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Get product ID and updated details from request parameters
        String productId = req.getParameter("id");
        String name = req.getParameter("name");
        String priceStr = req.getParameter("price");

        if (productId != null && name != null && priceStr != null) {
            try {
                int id = Integer.parseInt(productId);
                double price = Double.parseDouble(priceStr);

                Product updatedProduct = new Product();
                updatedProduct.setId(id);
                updatedProduct.setName(name);
                updatedProduct.setPrice(price);

                boolean updated = productService.update(updatedProduct);

                if (updated) {
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getWriter().write("Product updated successfully.");
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write("Product not found.");
                }
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Invalid ID or price format.");
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Product ID, name, and price are required.");
        }
    }

    // DELETE: Delete a product
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Get the product ID from the URL (e.g., /products?id=1)
        String productId = req.getParameter("id");

        if (productId != null) {
            try {
                int id = Integer.parseInt(productId);
                boolean deleted = productService.delete(id);

                if (deleted) {
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getWriter().write("Product deleted successfully.");
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write("Product not found.");
                }
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Invalid product ID.");
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Product ID is required.");
        }
    }
}
