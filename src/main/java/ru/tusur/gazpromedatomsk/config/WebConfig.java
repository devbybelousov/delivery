package ru.tusur.gazpromedatomsk.config;

import java.util.Properties;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.tusur.gazpromedatomsk.properties.MailProperties;

@Configuration
@AllArgsConstructor
public class WebConfig implements WebMvcConfigurer {

  private final MailProperties mailProperties;

  @Bean
  public JavaMailSender javaMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(mailProperties.getHost());
    mailSender.setPort(mailProperties.getPort());

    mailSender.setUsername(mailProperties.getUser());
    mailSender.setPassword(mailProperties.getPassword());

    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.transport.protocol", mailProperties.getProtocol());
    props.put("mail.smtp.auth", mailProperties.getAuth());
    props.put("mail.smtp.starttls.enable", mailProperties.getStarttls());
    props.put("mail.debug", mailProperties.getDebug());

    return mailSender;
  }

  @Bean
  public CorsFilter corsFilter() {
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    final CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOriginPattern("*");
    config.addAllowedHeader("*");
    config.addAllowedMethod("OPTIONS");
    config.addAllowedMethod("HEAD");
    config.addAllowedMethod("GET");
    config.addAllowedMethod("PUT");
    config.addAllowedMethod("POST");
    config.addAllowedMethod("DELETE");
    config.addAllowedMethod("PATCH");
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }

}
