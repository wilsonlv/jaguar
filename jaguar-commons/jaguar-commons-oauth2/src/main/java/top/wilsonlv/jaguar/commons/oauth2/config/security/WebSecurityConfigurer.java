package top.wilsonlv.jaguar.commons.oauth2.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author lvws
 * @since 2021/7/5
 */
@Order(200)
@Configuration
@EnableWebSecurity
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) {
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
