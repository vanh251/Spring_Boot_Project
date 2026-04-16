package va.project.security.principle;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import va.project.entity.User;
import va.project.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceCustom implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username không tồn tại"));
        List<SimpleGrantedAuthority> list = user.getRoleSet().stream().map(
                role -> new SimpleGrantedAuthority(role.getRoleName().name())
        ).toList();
        return UserDetailCustom.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(list)
                .build();
    }
}
