package com.vasyerp.rolebasedsystem.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletRequest request) {
        Object rawStatus = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();

        if (rawStatus != null) {
            try {
                statusCode = Integer.parseInt(rawStatus.toString());
            } catch (NumberFormatException ignored) {
                statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
            }
        }

        HttpStatus httpStatus = HttpStatus.resolve(statusCode);
        if (httpStatus == null) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        String message = (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        String path = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.setStatus(httpStatus);
        modelAndView.addObject("statusCode", httpStatus.value());
        modelAndView.addObject("error", httpStatus.getReasonPhrase());
        modelAndView.addObject("message", message == null || message.isBlank() ? "Unexpected error" : message);
        modelAndView.addObject("path", path == null ? request.getRequestURI() : path);
        return modelAndView;
    }


}
