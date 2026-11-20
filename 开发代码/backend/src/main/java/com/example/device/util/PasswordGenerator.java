package com.example.device.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 演示账号密码哈希生成工具。
 * 密码不写入代码，运行时通过环境变量注入，避免明文泄露。
 *
 * 用法示例：
 *   ADMIN_PWD=xxx MANAGER_PWD=xxx TESTER_PWD=xxx java PasswordGenerator
 */
public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String[] passwords = {
            envOrDefault("ADMIN_PWD", "YOUR_ADMIN_PASSWORD"),
            envOrDefault("MANAGER_PWD", "YOUR_MANAGER_PASSWORD"),
            envOrDefault("TESTER_PWD", "YOUR_TESTER_PASSWORD")
        };
        for (String pwd : passwords) {
            if (pwd.startsWith("YOUR_")) {
                System.out.println("【占位符】" + pwd + " -> 请通过环境变量注入真实密码后再生成哈希");
                continue;
            }
            System.out.println(pwd + " -> " + encoder.encode(pwd));
        }
    }

    private static String envOrDefault(String key, String def) {
        String v = System.getenv(key);
        return (v == null || v.trim().isEmpty()) ? def : v;
    }
}
