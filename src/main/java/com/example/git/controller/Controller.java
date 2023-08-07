package com.example.git.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RequestMapping("/api/v1/test")
@RestController
public class Controller {
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public String greeting(){
    return "Say Hello";
  }
}
