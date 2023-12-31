package com.anbu.mfaserver.service.impl;

import com.anbu.mfaserver.exception.MFAServerAppException;
import com.anbu.mfaserver.exception.UserAlreadyExistException;
import com.anbu.mfaserver.model.MfaTokenData;
import com.anbu.mfaserver.model.User;
import com.anbu.mfaserver.repository.UserRepository;
import com.anbu.mfaserver.service.TotpManager;
import com.anbu.mfaserver.service.UserService;
import dev.samstevens.totp.exceptions.QrGenerationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TotpManager totpManager;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, TotpManager totpManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.totpManager = totpManager;
    }

    @Override
    public MfaTokenData registerUser(User user) throws UserAlreadyExistException, QrGenerationException {
        try{
            if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                throw new UserAlreadyExistException("Username already exists");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            //some additional work
            user.setSecretKey(totpManager.generateSecretKey()); //generating the secret and store with profile
            User savedUser = userRepository.save(user);

            //Generate the QR Code
            String qrCode = totpManager.getQRCode(savedUser.getSecretKey());
            return MfaTokenData.builder()
                    .mfaCode(savedUser.getSecretKey())
                    .qrCode(qrCode)
                    .build();
        } catch (Exception e){
            throw new MFAServerAppException("Exception while registering the user", e);
        }
    }

    @Override
    public boolean verifyTotp(String code, String username) {
        User user = userRepository.findByUsername(username).get();
        return totpManager.verifyTotp(code, user.getSecretKey());
    }
}
