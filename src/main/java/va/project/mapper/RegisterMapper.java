package va.project.mapper;

import va.project.dto.request.RegisterRequest;
import va.project.dto.response.RegisterResponse;
import va.project.entity.User;

public class RegisterMapper {
    public static User mapToEntity(RegisterRequest registerRequest){
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setFullName(registerRequest.getFullName());
        user.setEmail(registerRequest.getEmail());
        user.setPhoneNumber(registerRequest.getPhone());
        return user;
    }

    public static RegisterResponse mapToResponse (User user){
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setId(user.getId());
        registerResponse.setUsername(user.getUsername());
        registerResponse.setEmail(user.getEmail());
        return registerResponse;
    }
}
