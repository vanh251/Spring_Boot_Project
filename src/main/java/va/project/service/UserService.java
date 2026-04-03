package va.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import va.project.dto.RegisterRequestDto;
import va.project.entity.Role;
import va.project.entity.RoleName;
import va.project.entity.User;
import va.project.repository.RoleRepository;
import va.project.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor

public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public String register(RegisterRequestDto requestDto) {
        if (userRepository.existsByUsername(requestDto.getUsername())) {
            return "Tên đăng nhập đã tồn tại";
        }
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            return "Email đã tồn tại";
        }

        User user = new User();
        user.setUsername(requestDto.getUsername());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setFullName(requestDto.getFullName());
        user.setEmail(requestDto.getEmail());
        user.setPhoneNumber(requestDto.getPhone());
        Role userRole = roleRepository.findByRoleName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Lỗi hệ thống: Không tìm thấy quyền ROLE_USER trong Database!"));
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoleSet(roles);
        userRepository.save(user);
        return "Đăng ký tài khoản thành công!";
    }
}
