// src/main/java/com/yourcompany/yourapp/controller/ReadmeController.java

package com.net128.tool.generic.avro.client.util;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.URISyntaxException;

@Controller
public class ReadmeController {
    private String indexPage = null;

    @GetMapping(value = {"/", "/index.html"}, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String getReadmeAsHtml() throws IOException, URISyntaxException {
        if(indexPage != null) return indexPage;
        var asciidoc = ResourceUtil.loadResourceFromLocation("static/README.adoc");
        asciidoc = asciidoc.replaceAll("([^ :])http://localhost:8080/([^ ]*)", "$1 link:$2");
        var htmlContent = "";
        try (var asciidoctor = Asciidoctor.Factory.create()) {
            var options = Options.builder()
                    .backend("html")
                    .safe(org.asciidoctor.SafeMode.UNSAFE)
                    .build();
            //var options = Options.builder().toFile(false).build();
            htmlContent = asciidoctor.convert(asciidoc, options);
        }

        var indexTemplate = ResourceUtil.loadResourceFromLocation("static/index-template.html");
        indexPage = indexTemplate.replace("${content}", htmlContent);
        return indexPage;
    }
}