import Mocks.MockHttpSession;
import com.example.carlist.CarListServlet;
import com.example.carlist.utils.CarDBProcessor;
import com.example.carlist.dto.Car;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CarListServletTests {

    CarListServlet servlet = new CarListServlet();
    private HttpServletRequest mockRequest = mock(HttpServletRequest.class);
    private HttpServletResponse mockResponse = mock(HttpServletResponse.class);

    private String htmlReportName = "generatedHtml";

    @AfterEach
    void clearDBRecords() throws SQLException {
        CarDBProcessor.getCarDBProcessor().removeUserCars(new MockHttpSession().getId());
    }


    @Test
    void addCarWithAllParams() throws ServletException, IOException {
        Car car = new Car("999", "Audi", "TT", "2", "230", "400");
        Set<Car> expectedCarSet = new HashSet<>();
        expectedCarSet.add(car);


        when(mockRequest.getParameter("plate")).thenReturn(car.getPlate());
        when(mockRequest.getParameter("make")).thenReturn(car.getMake());
        when(mockRequest.getParameter("model")).thenReturn(car.getModel());
        when(mockRequest.getParameter("doors")).thenReturn(car.getDoorAmount());
        when(mockRequest.getParameter("maxSpeed")).thenReturn(car.getMaxSpeed());
        when(mockRequest.getParameter("price")).thenReturn(car.getPrice());
        when(mockRequest.getSession()).thenReturn(new MockHttpSession());

        when(mockResponse.getWriter()).thenReturn(new PrintWriter(htmlReportName));

        servlet.doPost(mockRequest, mockResponse);

        assertEquals(expectedCarSet.toString(), getCars());

    }

    @Test
    void addFiveCarsWithAllParams() throws ServletException, IOException {
        Car car1 = new Car("999", "Audi", "TT", "2", "230", "400");
        Car car2 = new Car("555", "BMW", "M3", "4", "260", "600");
        Car car3 = new Car("444", "Mercedes", "Vito", "3", "190", "500");
        Car car4 = new Car("333", "Doge", "RAM", "4", "180", "500");
        Car car5 = new Car("222", "Skoda", "Octavia", "4", "200", "200");

        Set<Car> expectedCarSet = new HashSet<>();
        expectedCarSet.add(car1);
        expectedCarSet.add(car2);
        expectedCarSet.add(car3);
        expectedCarSet.add(car4);
        expectedCarSet.add(car5);

        for (Car car : expectedCarSet) {
            when(mockRequest.getParameter("plate")).thenReturn(car.getPlate());
            when(mockRequest.getParameter("make")).thenReturn(car.getMake());
            when(mockRequest.getParameter("model")).thenReturn(car.getModel());
            when(mockRequest.getParameter("doors")).thenReturn(car.getDoorAmount());
            when(mockRequest.getParameter("maxSpeed")).thenReturn(car.getMaxSpeed());
            when(mockRequest.getParameter("price")).thenReturn(car.getPrice());
            when(mockRequest.getSession()).thenReturn(new MockHttpSession());
            when(mockResponse.getWriter()).thenReturn(new PrintWriter(htmlReportName));

            servlet.doPost(mockRequest, mockResponse);
        }


        assertEquals(expectedCarSet.toString(), getCars());

    }

    @Test
    void addTwoCarsWithAllParamsAnd3WithNecessary() throws ServletException, IOException {
        Car car1 = new Car("999", "Audi", "TT", "2", "230", "400");
        Car car2 = new Car("555");
        Car car3 = new Car("444", "Mercedes", "Vito", "3", "190", "500");
        Car car4 = new Car("333");
        Car car5 = new Car("222");

        Set<Car> expectedCarSet = new HashSet<>();
        expectedCarSet.add(car1);
        expectedCarSet.add(car2);
        expectedCarSet.add(car3);
        expectedCarSet.add(car4);
        expectedCarSet.add(car5);

        for (Car car : expectedCarSet) {
            when(mockRequest.getParameter("plate")).thenReturn(car.getPlate());
            when(mockRequest.getParameter("make")).thenReturn(car.getMake());
            when(mockRequest.getParameter("model")).thenReturn(car.getModel());
            when(mockRequest.getParameter("doors")).thenReturn(car.getDoorAmount());
            when(mockRequest.getParameter("maxSpeed")).thenReturn(car.getMaxSpeed());
            when(mockRequest.getParameter("price")).thenReturn(car.getPrice());
            when(mockRequest.getSession()).thenReturn(new MockHttpSession());
            when(mockResponse.getWriter()).thenReturn(new PrintWriter(htmlReportName));

            servlet.doPost(mockRequest, mockResponse);
        }


        assertEquals(expectedCarSet.toString(), getCars());

    }

    @Test
    void addCarWithOnlyNecessaryParams() throws IOException, ServletException {
        Car car = new Car("999");
        Set<Car> expectedCarSet = new HashSet<>();
        expectedCarSet.add(car);


        when(mockRequest.getParameter("plate")).thenReturn(car.getPlate());
        when(mockRequest.getSession()).thenReturn(new MockHttpSession());

        when(mockResponse.getWriter()).thenReturn(new PrintWriter(htmlReportName));

        servlet.doPost(mockRequest, mockResponse);

        assertEquals(expectedCarSet.toString(), getCars());
    }

    @Test
    void addCarWithEmptyParams() throws IOException, ServletException {
        String expectedErrorMessage = "Error: [SQLITE_ERROR] SQL error or missing database (near \",\": syntax error)";

        when(mockRequest.getParameter("plate")).thenReturn("");
        when(mockRequest.getSession()).thenReturn(new MockHttpSession());
        when(mockResponse.getWriter()).thenReturn(new PrintWriter(htmlReportName));

        servlet.doPost(mockRequest, mockResponse);

        assertEquals(expectedErrorMessage, getErrorMessage());

    }

    @Test
    void addDuplicateCar() throws IOException, ServletException {
        Car car = new Car("999");
        String expectedErrorMessage = "Error: [SQLITE_CONSTRAINT_UNIQUE] A UNIQUE constraint failed (UNIQUE constraint failed: cars.plate)";


        when(mockRequest.getParameter("plate")).thenReturn(car.getPlate());
        when(mockRequest.getSession()).thenReturn(new MockHttpSession());
        when(mockResponse.getWriter()).thenReturn(new PrintWriter(htmlReportName));

        servlet.doPost(mockRequest, mockResponse);

        when(mockResponse.getWriter()).thenReturn(new PrintWriter(htmlReportName));

        servlet.doPost(mockRequest, mockResponse);

        assertEquals(expectedErrorMessage, getErrorMessage());


    }

    @Test
    void removeCar() throws IOException, ServletException {
        Car car = new Car("999");

        when(mockRequest.getParameter("plate")).thenReturn(car.getPlate());
        when(mockRequest.getSession()).thenReturn(new MockHttpSession());
        when(mockResponse.getWriter()).thenReturn(new PrintWriter(htmlReportName));

        servlet.doPost(mockRequest, mockResponse);

        when(mockRequest.getParameter("_method")).thenReturn("delete");
        when(mockRequest.getParameter("deletePlate")).thenReturn(car.getPlate());
        when(mockResponse.getWriter()).thenReturn(new PrintWriter(htmlReportName));

        servlet.doPost(mockRequest, mockResponse);

        assertEquals("[]", getCars());
    }

    @Test
    void removeCarWithNoData() throws IOException, ServletException {
        String expectedErrorMessage = "Error: No records with plate: null, were found in database.";
        when(mockRequest.getParameter("_method")).thenReturn("delete");
        when(mockRequest.getSession()).thenReturn(new MockHttpSession());
        when(mockResponse.getWriter()).thenReturn(new PrintWriter(htmlReportName));

        servlet.doPost(mockRequest, mockResponse);

        assertEquals(expectedErrorMessage, getErrorMessage());
    }

    @Test
    void removeNotExistingCar() throws IOException, ServletException {
        Car car = new Car("999");
        String expectedErrorMessage = "Error: No records with plate: " + car.getPlate() + ", were found in database.";

        when(mockRequest.getParameter("deletePlate")).thenReturn(car.getPlate());
        when(mockRequest.getParameter("_method")).thenReturn("delete");
        when(mockRequest.getSession()).thenReturn(new MockHttpSession());
        when(mockResponse.getWriter()).thenReturn(new PrintWriter(htmlReportName));

        servlet.doPost(mockRequest, mockResponse);

        assertEquals(expectedErrorMessage, getErrorMessage());
    }


    private String getCars() throws IOException {
        return new String(Files.readAllBytes(Paths.get(htmlReportName)))
                .split("</h3>\n")[3]
                .split("\n</body>")[0];
    }

    private String getErrorMessage() throws IOException {
        return new String(Files.readAllBytes(Paths.get(htmlReportName)))
                .split("<h3 style = \"color: red\">")[1]
                .split("</h3>")[0];
    }
}
