package com.agencybanking.security.users;

import com.agencybanking.core.data.SearchRequest;
import com.agencybanking.core.services.BaseService;
import com.agencybanking.core.services.ModelExistsException;
import com.agencybanking.core.utils.Utils;
import com.agencybanking.security.SecurityModule;
import com.agencybanking.security.auth.LoginHistory;
import com.agencybanking.security.auth.LoginHistoryRepo;
import com.agencybanking.security.password.PasswordService;
import com.agencybanking.security.tokens.TokenRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Optional;

import static org.springframework.util.Assert.notNull;
import static org.springframework.util.ObjectUtils.isEmpty;

/**
 * @author dubic
 */
@Service
@Slf4j
public class UserService extends BaseService {

    private final UserRepository userRepository;
    private final PasswordService passwordService;

    private final LockedUserRepository lockRepository;
    private final LoginHistoryRepo loginHistoryRepo;
    private final TokenRepository tokenRepository;

    public UserService(UserRepository userRepository,
                       PasswordService passwordService, LockedUserRepository lockRepository, LoginHistoryRepo loginHistoryRepo,
                       TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
        this.lockRepository = lockRepository;
        this.loginHistoryRepo = loginHistoryRepo;
        this.tokenRepository = tokenRepository;
    }

    public Page<LoginHistory> findAllUserHistory(String loginId) {
        PageRequest p = PageRequest.of(1, 10);
        return loginHistoryRepo.findByLoginId(loginId, p);
    }

    public User create(User user, String code) {
        user.setUsername(user.getUsername().toLowerCase());
        user.setEmail(user.getEmail().toLowerCase());
        user.setConcurrentFg(Utils.getBoolean(user.getConcurrentFg()));
        // user.setLastLoginDate(LocalDateTime.now());
        user.setLoginAttempt(0);
        user.setFirstLogin(true);
        if (exists(user, null)) {
            throw new ModelExistsException(SecurityModule.Companion.getERR_USERNAME_EXISTS(), user.getUsername());
        }
        String pword = makePassword(user);
        System.out.println("Generated password :" + user.getPassword());
        user.setPassword(passwordService.encodePassword(pword));
        user.validate();
        User created = this.userRepository.save(user);
        //
        broadcast(created, "CREATE");
        return created;
    }

    public boolean userNameExists(String username) {
        User found = userRepository.findByUsername(username);
        return found != null;
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public User update(User user) {
        user.validate();
        User found = this.userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalStateException("Resource not found"));
        found.copyForUpdate(user, "password", "loginAttempt", "lastLoginDate", "firstLogin");
        if (exists(user, user.getId())) {
            throw new ModelExistsException(SecurityModule.Companion.getERR_USERNAME_EXISTS(), user.getUsername());
        }
        User saved = userRepository.save(found);
        broadcast(saved, "UPDATE");
        return saved;
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Page<User> query(User user, PageRequest p) {
        if (isEmpty(user)) {
            return userRepository.findAll(p);
        }
        return userRepository.findAll(Example.of(user), p);
    }

    public boolean exists(User user, Long id) {
        if (ObjectUtils.isEmpty(id)) {
            return !isEmpty(userRepository.findByUsername(user.getUsername()));
        }
        return userRepository.findByUsernameAndIdNot(user.getUsername(), id).isPresent();
    }

    public Page<User> findAll(User user, PageRequest p) {
        QUser qUser = QUser.user;
        if (ObjectUtils.isEmpty(user)) {
            return userRepository
                    .findAll(qUser.isNotNull(), p);
        }
        BooleanExpression expression = qUser.username.containsIgnoreCase(Utils.nullSafeString(user.getUsername()))
                .and(qUser.phoneNumber.containsIgnoreCase(Utils.nullSafeString(user.getPhoneNumber())))
                .and(qUser.name.firstName.containsIgnoreCase(Utils.nullSafeString(user.getName().getFirstName())))
                .and(qUser.name.lastName.containsIgnoreCase(Utils.nullSafeString(user.getName().getLastName())))
                .and(qUser.email.containsIgnoreCase(Utils.nullSafeString(user.getEmail())));
        return userRepository.findAll(expression, p);
    }

    public Page<User> singleSearch(SearchRequest request, PageRequest p) {
        QUser qUser = QUser.user;

        if (StringUtils.isEmpty(request.getSearchItem())) {
            return userRepository
                    .findAll(qUser.isNotNull(), p);
        }
        return userRepository.findAll(qUser.name.firstName.containsIgnoreCase(request.getSearchItem())
                .or(qUser.name.lastName.containsIgnoreCase(request.getSearchItem()))
                .or(qUser.email.containsIgnoreCase(request.getSearchItem()))
                .or(qUser.phoneNumber.containsIgnoreCase(request.getSearchItem()))
                .or(qUser.username.containsIgnoreCase(request.getSearchItem()))
                .or(qUser.createDate.stringValue().containsIgnoreCase(request.getSearchItem())), p);
    }

//    @Override
//    public Page<User> single(String... args) {
//        return singleSearch(SearchRequest.builder().searchItem(args[0]).build(),
//                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id")));
//    }

    public String makePassword(User user) {
        notNull(user, "Valid user required");
        if (isEmpty(user.getPassword())) {
            String generatedPassword = passwordService.generatePassword();
            user.setPassword(generatedPassword);
            user.setPlainPassword(generatedPassword);
        }
        return user.getPassword();
    }

    public boolean isAccountLocked(User user) {
        Optional<LockedUser> locks = lockRepository.findByUserId(user.getId());
        return locks.isPresent();
    }

    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void changeUserPassword(User user, String password) {
        user.setPassword(passwordService.encodePassword(password));
        userRepository.save(user);
    }
}
