package org.recurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // 이 애노테이션은 요청이 Servlet Container에 연결되게 한다.
@EnableWebSecurity // 이 클래스가 spring에게 관리되게 한다.
public class SecurityConfig {

    // 아래의 메서드는 인가을 어떻게 할것인지을 커스텀하기 위해서 사용한다.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((auto) ->
                        auto
                                .requestMatchers("/", "/login").permitAll()
                                .requestMatchers("/admin").hasRole("ADMIN")
                                .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER").
                                anyRequest() // 지정하기 않은 링크에대한 패턴 지정, 로그인을 한사람만 또는 아무도 접근하지 않게 하는것이 원칙
                                .authenticated() // 로그인을 한 사람만
                                //.notify() 아무도 접근 안됨
                );

        http.
                formLogin((auto) ->
                        auto
                                .loginPage("/login") // 로그인 페이지 경로, 로그인을 해야하거나 권한이 필요할때 로그인 페이지로 이동
                                .loginProcessingUrl("/loginProc") // 이러하게 데이터을 받아 자동으로 로그인을 한다.
                                .permitAll() // 이러하게 한다면 아무나 들어오게 할수있다.
                        // 그치만 DB에 연결을 하여 이미 데이터가 없는지 있는지을 판단하게 만들 수 있다.
                );
        http.
                csrf((auto) ->
                        auto.disable()
                );
        return http.build(); // 이러한 리턴값으로 인가의 커스텀을 넘긴다.
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
