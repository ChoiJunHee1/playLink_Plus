package com.playLink_Plus.controller;

import com.playLink_Plus.repository.Auth_Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
@Service
public class ProductController  {

    final Auth_Repository auth_repository;



}
