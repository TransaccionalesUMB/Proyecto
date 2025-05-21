package com.example.transactional.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@Controller
public class CustomErrorController implements ErrorController {

    private final ErrorAttributes errorAttributes;

    public CustomErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        WebRequest webRequest = new ServletWebRequest(request);
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(
            webRequest, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.STACK_TRACE, ErrorAttributeOptions.Include.MESSAGE, ErrorAttributeOptions.Include.EXCEPTION));
        
        // Obtener información detallada del error
        HttpStatus status = getStatus(request);
        
        // Agregar atributos al modelo para mostrarlos en la página de error
        model.addAttribute("status", status.value());
        model.addAttribute("error", errorAttributes.get("error"));
        model.addAttribute("message", errorAttributes.get("message"));
        model.addAttribute("path", errorAttributes.get("path"));
        model.addAttribute("timestamp", errorAttributes.get("timestamp"));
        
        // Registrar el error para depuración
        System.err.println("Error " + status.value() + " en ruta: " + errorAttributes.get("path"));
        System.err.println("Mensaje: " + errorAttributes.get("message"));
        
        // Devolver la vista de error
        return "error";
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            return HttpStatus.valueOf(statusCode);
        } catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
