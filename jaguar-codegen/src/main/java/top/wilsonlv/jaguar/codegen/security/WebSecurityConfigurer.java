package top.wilsonlv.jaguar.codegen.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author lvws
 * @since 2021/7/27
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/swagger-resources", "/swagger-resources/**", "/v2/**").permitAll()
                .antMatchers("/error").permitAll()
                .anyRequest().authenticated()

                //异常处理
                .and().cors()
                .and().csrf().disable()
                .headers().frameOptions().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("*.html")
                .antMatchers("*.css")
                .antMatchers("*.js")
                .antMatchers("*.png")
                .antMatchers("*.ico")
                .antMatchers("*.ttf")
                .antMatchers("*.woff")
                .antMatchers("/swagger-ui/**");
    }
}
