package alumni;

import java.io.IOException;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/LoginData")
public class LoginData extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (validateUser(email, password)) {
            request.getRequestDispatcher("home.html").forward(request, response);
        } else {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body><p>Invalid username or password. Please try again.</p></body></html>");
        }
    }

    private boolean validateUser(String email, String password) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/alumni";
        String dbUser = "root";
        String dbPassword = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);

            System.out.println("Connected to the database");

            System.out.println("Received username: " + email);

            String query = "SELECT * FROM registration WHERE email=? AND password=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            System.out.println("Executing query: " + preparedStatement.toString());

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                System.out.println("User found in the database");
                resultSet.close();
                preparedStatement.close();
                connection.close();
                return true;
            }

            System.out.println("No matching user found in the database");

            resultSet.close();
            preparedStatement.close();
            connection.close();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error during user validation: " + e.getMessage());
            return false;
        }
    }
 {}
}