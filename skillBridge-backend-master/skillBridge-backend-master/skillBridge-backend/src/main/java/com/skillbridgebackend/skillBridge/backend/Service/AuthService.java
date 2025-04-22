package com.skillbridgebackend.skillBridge.backend.Service;

import com.skillbridgebackend.skillBridge.backend.Dto.LoginDto;
import com.skillbridgebackend.skillBridge.backend.Dto.RegisterDto;

public interface AuthService {
    String login(LoginDto loginDto);
    String register(RegisterDto registerDto);
}
