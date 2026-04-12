package va.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import va.project.dto.request.RegisterRequest;
import va.project.dto.response.RegisterResponse;
import va.project.entity.Role;
import va.project.entity.RoleName;
import va.project.entity.User;
import va.project.mapper.RegisterMapper;
import va.project.repository.RoleRepository;
import va.project.repository.UserRepository;
import va.project.exception.ResourceAlreadyExistsException;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor

public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public RegisterResponse register(RegisterRequest requestDto) {
        if (userRepository.existsByUsername(requestDto.getUsername())) {
            throw new ResourceAlreadyExistsException("Tên đăng nhập đã tồn tại!");
        }
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new ResourceAlreadyExistsException("Email đã tồn tại!");
        }

        User user = RegisterMapper.mapToEntity(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        Role userRole = roleRepository.findByRoleName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Lỗi hệ thống: Không tìm thấy quyền ROLE_USER trong Database!"));
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoleSet(roles);
        userRepository.save(user);
        return RegisterMapper.mapToResponse(user);
    }
}
