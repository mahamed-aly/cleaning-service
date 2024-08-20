package com.cleaningservice.util;


import lombok.experimental.UtilityClass;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@UtilityClass
public class UriUtil {

    public static URI locationUri(String id) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
    }

    public static <T> List<Link> addLinks(Long id, Class<T> controllerClass) {

        Link allResourcesLink = WebMvcLinkBuilder.linkTo(controllerClass).withRel("All " + controllerClass.getName() + "s");
        Link selfLink = WebMvcLinkBuilder.linkTo(controllerClass).slash(id).withRel("self link");

        return List.of(allResourcesLink, selfLink);
    }
}
