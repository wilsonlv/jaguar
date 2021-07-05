package org.jaguar.commons.oauth2.component;

import cn.hutool.core.collection.CollectionUtil;
import org.jaguar.commons.web.context.SpringContext;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.resource.OAuth2AccessDeniedException;
import org.springframework.security.oauth2.common.exceptions.InsufficientScopeException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.NoSuchClientException;

import java.util.Collection;
import java.util.Set;

/**
 * @author lvws
 * @since 2021/7/5
 */
public class ClientBasicAuthServiceImpl implements UserDetailsService {

    private static String resourceId;

    private static ClientDetailsService clientDetailsService;

    private final Set<String> scopes;

    public ClientBasicAuthServiceImpl(String... scopes) {
        this.scopes = CollectionUtil.newHashSet(scopes);
    }

    public static String getResourceId() {
        if (resourceId == null) {
            resourceId = SpringContext.getBean(Environment.class).getProperty("spring.application.name");
        }
        return resourceId;
    }

    public static ClientDetailsService getClientDetailsService() {
        if (clientDetailsService == null) {
            clientDetailsService = SpringContext.getBean(ClientDetailsService.class);
        }
        return clientDetailsService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ClientDetails clientDetails;
        try {
            clientDetails = getClientDetailsService().loadClientByClientId(username);
        } catch (NoSuchClientException e) {
            throw new UsernameNotFoundException(e.getMessage(), e);
        }

        String resourceId = getResourceId();
        Collection<String> resourceIds = clientDetails.getResourceIds();
        if (resourceId != null && resourceIds != null && !resourceIds.isEmpty() && !resourceIds.contains(resourceId)) {
            throw new OAuth2AccessDeniedException("Invalid token does not contain resource id (" + resourceId + ")");
        }

        for (String scope : clientDetails.getScope()) {
            if (this.scopes.contains(scope)) {
                return new User(username, clientDetails.getClientSecret(), clientDetails.getAuthorities());
            }
        }

        throw new InsufficientScopeException(null, this.scopes);
    }

}
