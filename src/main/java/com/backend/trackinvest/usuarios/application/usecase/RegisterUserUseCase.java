//package com.backend.trackinvest.usuarios.application.usecase;
//
//import com.backend.trackinvest.usuarios.application.ports.in.dto.RegisterUserDTO;
//import com.backend.trackinvest.usuarios.application.ports.in.dto.GetUserDTO;
//import com.backend.trackinvest.usuarios.application.ports.in.mapper.UserDTOMapper;
//import com.backend.trackinvest.usuarios.application.ports.in.service.RegisterUserPort;
//import com.backend.trackinvest.usuarios.application.ports.out.PasswordEncoderPort;
//import com.backend.trackinvest.usuarios.application.ports.out.UserRepositoryPort;
//import com.backend.trackinvest.usuarios.domain.models.user.UserDomain;
//import com.backend.trackinvest.usuarios.domain.models.user.valueobjects.Email;
//import com.backend.trackinvest.usuarios.domain.models.user.valueobjects.Name;
//import com.backend.trackinvest.usuarios.domain.models.user.valueobjects.Password;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class RegisterUserUseCase implements RegisterUserPort {
//
//    private final UserRepositoryPort userRepository;
//    private final PasswordEncoderPort passwordEncoder;
//    private final UserDTOMapper userDTOMapper;
//
//    @Override
//    @Transactional
//    public GetUserDTO execute(RegisterUserDTO dto) {
//
//        // 1. Validate and map input data
//        Name name = userDTOMapper.mapName(dto);
//        Email email = new Email(dto.email());
//
//        // 2. Prepare data
//        UUID id = UUID.randomUUID();
//        String passHashed = passwordEncoder.encode(dto.password());
//        Password password = new Password(passHashed);
//
//        // 3. Create domain object
//        UserDomain user = UserDomain.create(
//                id,
//                name,
//                email,
//                password
//        );
//
//        // 4. Persist data
//        UserDomain usuarioGuardado = userRepository.save(user);
//        return userDTOMapper.toDto(usuarioGuardado);
//    }
//}
