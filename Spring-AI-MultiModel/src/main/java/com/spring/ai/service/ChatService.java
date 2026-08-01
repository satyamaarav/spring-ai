package com.spring.ai.service;

import com.spring.ai.entity.Tut;

import java.util.List;

public interface ChatService {

    String chat(String query);

    Tut getTut(String query);

    List<Tut> getTutResponses(String query);
}
