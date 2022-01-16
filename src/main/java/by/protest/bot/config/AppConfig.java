package by.protest.bot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(
  basePackages = "by.protest.bot.domain")
public class AppConfig{}
