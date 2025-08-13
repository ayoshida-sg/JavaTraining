package com.s_giken.training.webapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

/**
 * Spring Securityの設定クラス
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    /**
     * Spring Securityの設定
     *
     * @param http HttpSecurityオブジェクト
     * @return SecurityFilterChainオブジェクト
     * @throws Exception 例外全般
     */
    @Bean
    SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF対策を無効化
                .headers((header) -> header
                        .frameOptions((frame) -> frame.disable()))
                .formLogin((form) -> form
                        .defaultSuccessUrl("/")
                        .loginProcessingUrl("/login")
                        .loginPage("/login")
                        .failureUrl("/login?error")
                        .permitAll())
                .logout((logout) -> logout
                        .logoutSuccessUrl("/"))
                .authorizeHttpRequests((authorize) -> authorize
                        // 特例として認証を無視するURL
                        .requestMatchers(
                                PathPatternRequestMatcher
                                        .withDefaults()
                                        .matcher("/h2-console/**"),
                                PathPatternRequestMatcher
                                        .withDefaults()
                                        .matcher("/image/**"),
                                PathPatternRequestMatcher
                                        .withDefaults()
                                        .matcher("/css/**"))
                        .permitAll()
                        // 特例以外のURLは要認証
                        .anyRequest().authenticated());

        return http.build();
    }

    /**
     * ログインユーザー情報を設定する
     *
     * ユーザ名user、パスワードpasswordでログインできるようになる。
     *
     * ※パスワードはハッシュ化せずにそのまま設定
     *
     * @return ログインユーザー情報
     */
    @Bean
    UserDetailsService users() {
        var user = User
                .builder()
                .username("user")
                .password("$2a$10$Jjf89PMWEuE1JOFF57rw2.Ny64pJbWTnvJB5PBLSbP27R6.nKOC1e") // password
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    /**
     * パスワードをBcryptでハッシュ化するオブジェクトを生成する
     *
     * @return パスワードをハッシュ化するエンコーダーのオブジェクト
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
