package com.black.ecoms.service.impl;

import com.black.ecoms.dto.UserRegistrationRequest;

import java.util.Map;

public interface UsersImp {

    Map<String, Object> createUsers(UserRegistrationRequest request);

}
