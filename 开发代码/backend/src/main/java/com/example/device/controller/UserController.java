package com.example.device.controller;

import com.example.device.entity.SysUser;
import com.example.device.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<SysUser>> getAllUsers(Authentication authentication) {
        // ADMIN只能看到TESTER列表
        String role = authentication.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
        if ("ADMIN".equals(role)) {
            return ResponseEntity.ok(userService.getUsersByRole("TESTER"));
        }
        // MANAGER可以看到所有人
        return ResponseEntity.ok(userService.getAllUsers());
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<?> getUserById(@PathVariable Long id, Authentication authentication) {
        String role = authentication.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
        SysUser user = userService.getUserById(id);
        
        if (user == null) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "用户不存在");
            return ResponseEntity.badRequest().body(error);
        }
        
        // ADMIN只能查看TESTER
        if ("ADMIN".equals(role) && !"TESTER".equals(user.getRole())) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "无权限查看此用户");
            return ResponseEntity.status(403).body(error);
        }
        
        return ResponseEntity.ok(user);
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<?> createUser(@RequestBody SysUser user, Authentication authentication) {
        try {
            String creatorRole = authentication.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
            SysUser created = userService.createUser(user, creatorRole);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody SysUser user, Authentication authentication) {
        try {
            String updaterRole = authentication.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
            Long updaterId = getCurrentUserId(authentication);
            SysUser updated = userService.updateUser(id, user, updaterRole, updaterId);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PutMapping("/{id}/toggle-status")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<?> toggleUserStatus(@PathVariable Long id, Authentication authentication) {
        try {
            String operatorRole = authentication.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
            Long operatorId = getCurrentUserId(authentication);
            SysUser user = userService.toggleUserStatus(id, operatorRole, operatorId);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, Authentication authentication) {
        try {
            String operatorRole = authentication.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
            Long operatorId = getCurrentUserId(authentication);
            userService.deleteUser(id, operatorRole, operatorId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    private Long getCurrentUserId(Authentication authentication) {
        String username = authentication.getName();
        return userService.getUserByUsername(username)
                .map(SysUser::getId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }
}