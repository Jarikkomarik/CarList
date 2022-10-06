package com.example.carlist.utils;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

import java.sql.SQLException;

public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        try {
            CarDBProcessor.getCarDBProcessor().removeUserCars(se.getSession().getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
