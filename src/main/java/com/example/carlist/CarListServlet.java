package com.example.carlist;

import java.io.*;
import java.sql.SQLException;


import com.example.carlist.utils.CarDBProcessor;
import com.example.carlist.dto.Car;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/car-list")
public class CarListServlet extends HttpServlet {

    private String htmlPrefix = "<html>\n" +
            "<head>\n" +
            "    <title>Car List 2000</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "<h1>Car List *2000*</h1>\n";

    private String htmlAddCar = "<h3>Add car:</h3>\n" +
            "<form action=\"/CarList_war/car-list\" method=\"post\">\n" +
            "\n" +
            "    <label for=\"plate\">License plate:</label>\n" +
            "    <input type=\"text\" id=\"plate\" name=\"plate\"><br>\n" +
            "\n" +
            "    <label for=\"make\">Make:</label>\n" +
            "    <input type=\"text\" id=\"make\" name=\"make\"><br>\n" +
            "\n" +
            "    <label for=\"model\">Model:</label>\n" +
            "    <input type=\"text\" id=\"model\" name=\"model\"><br>\n" +
            "\n" +
            "    <label for=\"doors\">Door amount:</label>\n" +
            "    <input type=\"number\" id=\"doors\" name=\"doors\"><br>\n" +
            "\n" +
            "    <label for=\"maxSpeed\">Max Speed:</label>\n" +
            "    <input type=\"number\" id=\"maxSpeed\" name=\"maxSpeed\"><br>\n" +
            "\n" +
            "    <label for=\"price\">Price:</label>\n" +
            "    <input type=\"number\" id=\"price\" name=\"price\"><br>\n" +
            "\n" +
            "    <button type=\"submit\">Add Car</button>\n" +
            "\n" +
            "</form>\n";

    private String htmlRemoveCar = "<h3>Remove car:</h3>\n" +
            "<form action=\"/CarList_war/car-list\" method=\"post\">\n" +
            "    <input type=\"hidden\" name=\"_method\" value=\"delete\">\n" +
            "    <label for=\"deltePlate\">License plate:</label>\n" +
            "    <input type=\"text\" id=\"deltePlate\" name=\"deletePlate\"><br>\n" +
            "\n" +
            "\n" +
            "    <button type=\"submit\">Remove Car</button>\n" +
            "\n" +
            "</form>\n";

    private String htmlCarList = "<h3>Here is list of your cars:</h3>";

    private String htmlPostFix = "</body>\n" +
            "</html>";


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        printResponse(response.getWriter(), null, null, request.getSession().getId());
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String errorMessage = null;

        if (request.getParameter("_method") != null && request.getParameter("_method").equals("delete")) {
            doDelete(request, response);
            return;
        }

        try {
            CarDBProcessor.getCarDBProcessor().addCar(new Car(
                            request.getParameter("plate"),
                            request.getParameter("make"),
                            request.getParameter("model"),
                            request.getParameter("doors"),
                            request.getParameter("maxSpeed"),
                            request.getParameter("price")
                    ), request.getSession().getId()
            );
        } catch (SQLException e) {
            errorMessage = e.getMessage();
        }

        printResponse(response.getWriter(), errorMessage, null, request.getSession().getId());

    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String errorMessage = null;
        Car carToBeRemoved = new Car(request.getParameter("deletePlate"));

        try {
            CarDBProcessor.getCarDBProcessor().removeCar(carToBeRemoved, request.getSession().getId());
        } catch (SQLException e) {
            errorMessage = e.getMessage();
        }

        printResponse(response.getWriter(), null, errorMessage, request.getSession().getId());
    }

    private void printResponse(PrintWriter writer, String addCarError, String removeCarError, String requestID) {
        writer.println(htmlPrefix);
        writer.println(htmlAddCar);

        if (addCarError != null) writer.println("<h3 style = \"color: red\">" + "Error: " + addCarError + "</h3>");
        writer.println(htmlRemoveCar);

        if (removeCarError != null) writer.println("<h3 style = \"color: red\">" + "Error: " + removeCarError + "</h3>");
        writer.println(htmlCarList);

        try {
            writer.println(CarDBProcessor.getCarDBProcessor().getCars(requestID));
        } catch (SQLException e) {
            writer.println("<h3 style = \"color: red\">" + "Error: " + e.getMessage() + "</h3>");
        }

        writer.println(htmlPostFix);
        writer.flush();
    }
}