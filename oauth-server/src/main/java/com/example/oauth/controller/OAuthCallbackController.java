package com.example.author.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuthCallbackController {

    @GetMapping(value = "/authorized", produces = MediaType.TEXT_HTML_VALUE)
    public String authorized(
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "error", required = false) String error) {

        if (error != null) {
            return String.format(
                    "<html><body>" +
                            "<h1 style='color: red;'>Error de Autorización</h1>" +
                            "<p>Error: %s</p>" +
                            "</body></html>",
                    error
            );
        }

        if (code == null) {
            return "<html><body>" +
                    "<h1>Error</h1>" +
                    "<p>No se recibió código de autorización</p>" +
                    "</body></html>";
        }
        return String.format(
                "<html><body>" +
                        "<h1>Autorización Exitosa</h1>" +
                        "<p>Código de autorización recibido:</p>" +
                        "<code>%s</code>" +
                        "<h3>Siguiente paso:</h3>" +
                        "<p>Usa este código para obtener el token de acceso con el siguiente comando curl:</p>" +
                        "<pre style='background: #f4f4f4; padding: 15px; border-radius: 5px;'>" +
                        "curl -X POST http://localhost:9000/oauth2/token \\\n" +
                        "  -H \"Content-Type: application/x-www-form-urlencoded\" \\\n" +
                        "  -u client-app:secret \\\n" +
                        "  -d \"grant_type=authorization_code\" \\\n" +
                        "  -d \"code=%s\" \\\n" +
                        "  -d \"redirect_uri=http://localhost:8081/authorized\"" +
                        "</pre>" +
                        "</body></html>",
                code, code
        );
    }
}