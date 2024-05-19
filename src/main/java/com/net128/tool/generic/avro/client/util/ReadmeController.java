// src/main/java/com/yourcompany/yourapp/controller/ReadmeController.java

package com.net128.tool.generic.avro.client.util;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Controller
public class ReadmeController {

    @GetMapping(value = {"/", "/index.html"}, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String getReadmeAsHtml() throws IOException {
        ClassPathResource resource = new ClassPathResource("static/README.adoc");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String asciidoc = reader.lines().collect(Collectors.joining("\n"));

                asciidoc = asciidoc.replace("http://localhost:8080", "");

            try (Asciidoctor asciidoctor = Asciidoctor.Factory.create()) {
                Options options = Options.builder().toFile(false).build();
                return asciidoctor.convert(asciidoc, options);
            }
        }
    }
}
