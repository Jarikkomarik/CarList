package com.example.carlist.utils;

import com.example.carlist.dto.Car;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class CarDBProcessor {

    private static CarDBProcessor carDBProcessor;
    private static final String DB_URL = "jdbc:sqlite:/Users/yoelufland/IdeaProjects/CarList/carList.db";
    private SQLiteDataSource sqLiteDataSource;

    private CarDBProcessor() {

    }

    public static CarDBProcessor getCarDBProcessor() {
        if (carDBProcessor == null) {
            carDBProcessor = new CarDBProcessor();
        }
        return carDBProcessor;
    }


    private Connection getConnection() throws SQLException {

        if (sqLiteDataSource == null) {
            sqLiteDataSource = new SQLiteDataSource();
            sqLiteDataSource.setUrl(DB_URL);
        }

        Connection connection = sqLiteDataSource.getConnection();

        if (connection != null && connection.isValid(5)) {
            return connection;
        } else {
            throw new SQLException("Cannot connect to Database!");
        }

    }

    public Set<Car> getCars(String userID) throws SQLException {
        Set<Car> carSet = new HashSet<>();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            try (ResultSet resSet = statement.executeQuery("SELECT * FROM cars WHERE ownerIn = \'" + userID + "\' ;")) {
                while (resSet.next()) {
                    carSet.add(new Car(
                            resSet.getString("plate"),
                            resSet.getString("make"),
                            resSet.getString("model"),
                            resSet.getString("Doors"),
                            resSet.getString("maxSpeed"),
                            resSet.getString("price")
                    ));
                }
            }


            return carSet;
        }
    }

    public void addCar(Car car, String userID) throws SQLException {

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("INSERT INTO cars VALUES (" +
                    car.getPlate() + ", " +
                    "\'" + car.getMake() + "\'" + ", " +
                    "\'" + car.getModel() + "\'" + ", " +
                    car.getDoorAmount() + ", " +
                    car.getMaxSpeed() + ", " +
                    car.getPrice() + ", " +
                    "\'" + userID + "\'" +
                    ");");

        }

    }

    public void removeCar(Car car, String userId) throws SQLException {

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            if (statement.executeQuery("SELECT * FROM cars WHERE plate = " + "\'" + car.getPlate() + "\'AND ownerIn = \'" + userId + "\';").next() == true) {
                statement.executeUpdate("DELETE FROM cars WHERE plate = " + "\'" + car.getPlate() + "\'AND ownerIn = \'" + userId + "\';");
            } else {
                throw new SQLException("No records with plate: " + car.getPlate() + ", were found in database.");
            }

        }

    }

    public void removeUserCars(String userId) throws SQLException {

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {


            statement.executeUpdate("DELETE FROM cars WHERE ownerIn = \'" + userId + "\';");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
