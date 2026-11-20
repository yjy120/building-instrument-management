package com.example.device.service;

import com.example.device.entity.SysUser;
import com.example.device.repository.SysUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final SysUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserService(SysUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public List<SysUser> getAllUsers() {
        return userRepository.findAll();
    }
    
    public List<SysUser> getUsersByRole(String role) {
        return userRepository.findByRole(role);
    }
    
    public SysUser getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
    public Optional<SysUser> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    /**
     * 创建用户 - MANAGER可创建任何角色，ADMIN只能创建TESTER
     */
    public SysUser createUser(SysUser user, String creatorRole) {
        // ADMIN只能创建TESTER
        if ("ADMIN".equals(creatorRole) && !"TESTER".equals(user.getRole())) {
            throw new RuntimeException("设备管理员只能创建项目检测员账号");
        }
        
        // 检查用户名是否已存在
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(1);
        
        return userRepository.save(user);
    }
    
    /**
     * 更新用户 - MANAGER可更新任何用户，ADMIN只能更新TESTER
     */
    public SysUser updateUser(Long id, SysUser updateUser, String updaterRole, Long updaterId) {
        SysUser existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 不能修改自己的账号
        if (id.equals(updaterId)) {
            throw new RuntimeException("不能修改自己的账号");
        }
        
        // ADMIN只能更新TESTER
        if ("ADMIN".equals(updaterRole) && !"TESTER".equals(existingUser.getRole())) {
            throw new RuntimeException("设备管理员只能修改项目检测员账号");
        }
        
        // 更新信息
        if (updateUser.getRealName() != null) {
            existingUser.setRealName(updateUser.getRealName());
        }
        if (updateUser.getDepartment() != null) {
            existingUser.setDepartment(updateUser.getDepartment());
        }
        if (updateUser.getPhone() != null) {
            existingUser.setPhone(updateUser.getPhone());
        }
        if (updateUser.getEmail() != null) {
            existingUser.setEmail(updateUser.getEmail());
        }
        if (updateUser.getPassword() != null && !updateUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updateUser.getPassword()));
        }
        
        return userRepository.save(existingUser);
    }
    
    /**
     * 停用/启用用户 - MANAGER可操作任何用户，ADMIN只能操作TESTER
     */
    public SysUser toggleUserStatus(Long id, String operatorRole, Long operatorId) {
        SysUser user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 不能停用自己的账号
        if (id.equals(operatorId)) {
            throw new RuntimeException("不能停用自己的账号");
        }
        
        // ADMIN只能操作TESTER
        if ("ADMIN".equals(operatorRole) && !"TESTER".equals(user.getRole())) {
            throw new RuntimeException("设备管理员只能停用/启用项目检测员账号");
        }
        
        user.setStatus(user.getStatus() == 1 ? 0 : 1);
        return userRepository.save(user);
    }
    
    /**
     * 删除用户 - MANAGER可删除任何用户，ADMIN只能删除TESTER
     */
    public void deleteUser(Long id, String operatorRole, Long operatorId) {
        SysUser user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 不能删除自己的账号
        if (id.equals(operatorId)) {
            throw new RuntimeException("不能删除自己的账号");
        }
        
        // ADMIN只能删除TESTER
        if ("ADMIN".equals(operatorRole) && !"TESTER".equals(user.getRole())) {
            throw new RuntimeException("设备管理员只能删除项目检测员账号");
        }
        
        userRepository.delete(user);
    }
}