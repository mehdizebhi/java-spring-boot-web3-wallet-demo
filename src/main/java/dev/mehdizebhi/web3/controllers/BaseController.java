package dev.mehdizebhi.web3.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mehdizebhi.web3.entities.UserEntity;
import dev.mehdizebhi.web3.models.SpringUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

public class BaseController {

    @Autowired
    private ObjectMapper mapper;

    public String json(Object obj) throws JsonProcessingException {
        if (obj == null) {
            return null;
        }
        return mapper.writeValueAsString(obj);
    }

    public SpringUser springUser() {
        return (SpringUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public UserEntity user() {
        return springUser().getUser();
    }
}