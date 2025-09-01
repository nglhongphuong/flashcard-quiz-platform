package com.zotriverse.demo.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.zotriverse.demo.dto.request.AccountCreationRequest;
import com.zotriverse.demo.dto.response.AccountResponse;
import com.zotriverse.demo.enums.Role;
import com.zotriverse.demo.exception.AppException;
import com.zotriverse.demo.exception.ErrorCode;
import com.zotriverse.demo.mapper.AccountMapper;
import com.zotriverse.demo.pojo.Account;
import com.zotriverse.demo.pojo.Admin;
import com.zotriverse.demo.pojo.User;
import com.zotriverse.demo.repository.AccountRepository;
import com.zotriverse.demo.repository.AdminRepository;
import com.zotriverse.demo.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j //log cua lomboo inject 1 logger
public class AccountService {
    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AdminRepository adminRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${PAGE_SIZE}")
    private int PAGE_SIZE;

    public Account createAccount(Map<String, String> params, MultipartFile avatar){
        if (accountRepo.existsByUsername(params.get("username"))) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        Account account = new Account();
        account.setName(params.get("name"));
        account.setUsername(params.get("username"));// tuc la email
        account.setPassword(this.passwordEncoder.encode(params.get("password")));
        account.setRole(params.get("role"));
        account.setIsActive(Boolean.valueOf(params.get("isActive")));
        account.setGender(params.get("gender"));
        String birthdayStr = params.get("birthday"); // ví dụ: "2004-06-15"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthday = LocalDate.parse(birthdayStr, formatter);

        account.setBirthday(birthday);

        try {
            if (avatar != null && !avatar.isEmpty()) {
                File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + avatar.getOriginalFilename());
                FileOutputStream fos = new FileOutputStream(convFile);
                fos.write(avatar.getBytes());
                fos.close();
//Chuaw co avatars no tu tao folder cho minh
                var pic = cloudinary.uploader().upload(convFile, ObjectUtils.asMap("folder", "/avatars/"));
                account.setAvatar(pic.get("url").toString());
            }
            //Luu account
            this.accountRepo.save(account);
            //Lay id account vua duoc luu trong database
            Account acc = this.accountRepo.findByUsername(account.getUsername())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            if(acc.getRole().equals(Role.USER.getDisplayName())){
                User user = new User();
                user.setAccountId(acc.getId());
                this.userRepo.save(user);
            } else if (acc.getRole().equals(Role.ADMIN.getDisplayName())) {
                Admin admin = Admin.builder()
                        .accountId(acc.getId())
                        .build();
                adminRepo.save(admin);
            }
            return acc;
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Failed to upload the file.");
        }
    }
    //Update

    public Account updateAccount( Map<String, String> params, MultipartFile avatar) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        Account account = accountRepo.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (params.containsKey("name"))
            account.setName(params.get("name"));

        if (params.containsKey("username"))
            account.setUsername(params.get("username")); // email

        if (params.containsKey("password"))
            account.setPassword(this.passwordEncoder.encode(params.get("password")));

        if (params.containsKey("role"))
            account.setRole(params.get("role"));

        if (params.containsKey("isActive"))
            account.setIsActive(Boolean.valueOf(params.get("isActive")));

        if (params.containsKey("gender"))
            account.setGender(params.get("gender"));

        if (params.containsKey("birthday")) {
            try {
                String birthdayStr = params.get("birthday"); // ví dụ: "2004-06-15"
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate birthday = LocalDate.parse(birthdayStr, formatter);
                account.setBirthday(birthday);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ngày sinh không hợp lệ.");
            }
        }

        // Xử lý avatar nếu có
        if (avatar != null && !avatar.isEmpty()) {
            try {
                File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + avatar.getOriginalFilename());
                try (FileOutputStream fos = new FileOutputStream(convFile)) {
                    fos.write(avatar.getBytes());
                }

                var pic = cloudinary.uploader().upload(convFile, ObjectUtils.asMap("folder", "/avatars/"));
                account.setAvatar(pic.get("url").toString());
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Không thể tải ảnh lên.");
            }
        }


        return this.accountRepo.save(account);
    }


    public Account updateAccount(int accountId, Map<String, String> params, MultipartFile avatar) {
        Account account = accountRepo.findById(accountId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (params.containsKey("name"))
            account.setName(params.get("name"));

        if (params.containsKey("username"))
            account.setUsername(params.get("username")); // email

        if (params.containsKey("password"))
            account.setPassword(this.passwordEncoder.encode(params.get("password")));

        if (params.containsKey("isActive"))
            account.setIsActive(Boolean.valueOf(params.get("isActive")));

        if (params.containsKey("gender"))
            account.setGender(params.get("gender"));

        if (params.containsKey("birthday")) {
            try {
                String birthdayStr = params.get("birthday"); // ví dụ: "2004-06-15"
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate birthday = LocalDate.parse(birthdayStr, formatter);
                account.setBirthday(birthday);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ngày sinh không hợp lệ.");
            }
        }

        // Xử lý avatar nếu có
        if (avatar != null && !avatar.isEmpty()) {
            try {
                File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + avatar.getOriginalFilename());
                try (FileOutputStream fos = new FileOutputStream(convFile)) {
                    fos.write(avatar.getBytes());
                }

                var pic = cloudinary.uploader().upload(convFile, ObjectUtils.asMap("folder", "/avatars/"));
                account.setAvatar(pic.get("url").toString());
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Không thể tải ảnh lên.");
            }
        }
        if (params.containsKey("role")) {
            String oldRole = account.getRole();//luu lai role cu
            String newRole = params.get("role");
            // Cập nhật lại role cho account
            account.setRole(params.get("role"));
            account.setUser(null);
            account.setAdmin(null);
            //cap nhap lai account
            this.accountRepo.save(account);
            System.out.println("role" +  oldRole);
            // Nếu role thay đổi
            if (!newRole.equals(oldRole)) {
                // Chuyển từ USER sang ADMIN
                if (newRole.equals(Role.ADMIN.getDisplayName())) {
                    userRepo.findById(accountId).ifPresent(user -> {
                        userRepo.delete(user);
                        userRepo.flush();              // Bắt buộc để xóa thực sự
                        entityManager.detach(user);    // Dọn khỏi persistence context
                        System.out.println("Đã xóa User");
                    });
                    Admin admin = new Admin();
                    admin.setAccountId(accountId);
                    adminRepo.save(admin);
                    System.out.println("Đã tạo Admin mới");
                }
                // Chuyển từ ADMIN sang USER
                else if (newRole.equals(Role.USER.getDisplayName())) {
                    adminRepo.findById(accountId).ifPresent(admin -> {
                        adminRepo.delete(admin);
                        adminRepo.flush();
                        entityManager.detach(admin);
                        System.out.println("Đã xóa Admin");
                    });
                    User user = new User();
                    user.setAccountId(accountId);
                    userRepo.save(user);
                    System.out.println("Đã tạo User mới");
                }
            }
            return account;
        }
        return this.accountRepo.save(account);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    public List<AccountResponse> getAccounts(){
        log.info("In method get Accounts");
        List<Account> accounts = accountRepo.findAll();
        if (accounts.isEmpty()) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        return  accountRepo.findAll().stream().map(accountMapper::toAccountResponse).toList();
    }
    public List<AccountResponse> getAccounts(Map<String, String> params) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Account> query = builder.createQuery(Account.class);
        Root<Account> root = query.from(Account.class);
        query.select(root);

        List<Predicate> predicates = new ArrayList<>();

        // Filter theo id
        if (params.containsKey("id")) {
            try {
                int id = Integer.parseInt(params.get("id"));
                predicates.add(builder.equal(root.get("id"), id));
            } catch (NumberFormatException ignored) {}
        }

        // Filter theo username
        if (params.containsKey("username")) {
            String username = params.get("username");
            if (!username.isEmpty()) {
                predicates.add(builder.like(root.get("username"), "%" + username + "%"));
            }
        }

        // Filter theo role
        if (params.containsKey("role")) {
            String role = params.get("role");
            if (!role.isEmpty()) {
                predicates.add(builder.equal(root.get("role"), role));
            }
        }

        // Filter theo isActive
        if (params.containsKey("isActive")) {
            String isActive = params.get("isActive");
            if (!isActive.isEmpty()) {
                predicates.add(builder.equal(root.get("isActive"), Boolean.parseBoolean(isActive)));
            }
        }

        // Filter theo khoảng thời gian createdAt
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (params.containsKey("startDate")) {
            try {
                LocalDateTime start = LocalDate.parse(params.get("startDate"), formatter).atStartOfDay();
                predicates.add(builder.greaterThanOrEqualTo(root.get("createdAt"), start));
            } catch (DateTimeParseException ignored) {}
        }

        if (params.containsKey("endDate")) {
            try {
                LocalDateTime end = LocalDate.parse(params.get("endDate"), formatter).atTime(LocalTime.MAX);
                predicates.add(builder.lessThanOrEqualTo(root.get("createdAt"), end));
            } catch (DateTimeParseException ignored) {}
        }

        query.where(predicates.toArray(new Predicate[0]));

        // Order
        String order = params.getOrDefault("order", "desc");
        if (order.equalsIgnoreCase("asc")) {
            query.orderBy(builder.asc(root.get("createdAt")));
        } else {
            query.orderBy(builder.desc(root.get("createdAt")));
        }

        TypedQuery<Account> q = entityManager.createQuery(query);

        // Phân trang
        int page = 1;
        int pageSize = PAGE_SIZE;

        if (params.containsKey("page")) {
            page = Integer.parseInt(params.get("page"));
        }

        if (params.containsKey("size")) {
            pageSize = Integer.parseInt(params.get("size"));
        }

        q.setFirstResult((page - 1) * pageSize);
        q.setMaxResults(pageSize);

        return q.getResultList().stream().map(accountMapper::toAccountResponse).toList();
    }





    public Account getAccount(int id){
        return accountRepo.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @Transactional
    public void deleteAccount(int id){
        Account account = accountRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        System.out.println("Account to be deleted: " + account);
        if (account.getUser() != null) {
            userRepo.findById(account.getUser().getAccount().getId()); // attach lại user
        }
        accountRepo.delete(account);
    }

    public AccountResponse getInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        Account user = accountRepo.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        log.info("Account from DB: {}", user.getRole());
        return accountMapper.toAccountResponse(user);

    }

    public Account findByUsername(String username){
        return this.accountRepo.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

    }


}
