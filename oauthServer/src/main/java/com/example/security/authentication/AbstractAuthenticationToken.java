package com.example.security.authentication;

import com.example.security.core.Authentication;
import com.example.security.core.CredentialsContainer;
import com.example.security.core.GrantedAuthority;
import com.example.security.core.authority.AuthorityUtils;
import com.example.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * 用于Authentication的基类。
 * 使用这个类的实现应该是不可变的
 */
public abstract class AbstractAuthenticationToken implements Authentication,
        CredentialsContainer {
    // ~ Instance fields
    // ================================================================================================

    private final Collection<GrantedAuthority> authorities;
    private Object details;
    private boolean authenticated = false;

    // ~ Constructors
    // ===================================================================================================

    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal
     *                    represented by this authentication object.
     */
    public AbstractAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        if (authorities == null) {
            this.authorities = AuthorityUtils.NO_AUTHORITIES;
            return;
        }

        for (GrantedAuthority a : authorities) {
            if (a == null) {
                throw new IllegalArgumentException(
                        "Authorities collection cannot contain any null elements");
            }
        }
        ArrayList<GrantedAuthority> temp = new ArrayList<GrantedAuthority>(
                authorities.size());
        temp.addAll(authorities);
        this.authorities = Collections.unmodifiableList(temp);
    }

    // ~ Methods
    // ========================================================================================================

    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getName() {
        if (this.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) this.getPrincipal()).getUsername();
        }

        if (getPrincipal() instanceof Principal) {
            return ((Principal) getPrincipal()).getName();
        }

        return (this.getPrincipal() == null) ? "" : this.getPrincipal().toString();
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public Object getDetails() {
        return details;
    }

    public void setDetails(Object details) {
        this.details = details;
    }

    /**
     * Checks the {@code credentials}, {@code principal} and {@code details} objects,
     * invoking the {@code eraseCredentials} method on any which implement
     * {@link CredentialsContainer}.
     */
    public void eraseCredentials() {
        eraseSecret(getCredentials());
        eraseSecret(getPrincipal());
        eraseSecret(details);
    }

    private void eraseSecret(Object secret) {
        if (secret instanceof CredentialsContainer) {
            ((CredentialsContainer) secret).eraseCredentials();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AbstractAuthenticationToken)) {
            return false;
        }

        AbstractAuthenticationToken test = (AbstractAuthenticationToken) obj;

        if (!authorities.equals(test.authorities)) {
            return false;
        }

        if ((this.details == null) && (test.getDetails() != null)) {
            return false;
        }

        if ((this.details != null) && (test.getDetails() == null)) {
            return false;
        }

        if ((this.details != null) && (!this.details.equals(test.getDetails()))) {
            return false;
        }

        if ((this.getCredentials() == null) && (test.getCredentials() != null)) {
            return false;
        }

        if ((this.getCredentials() != null)
                && !this.getCredentials().equals(test.getCredentials())) {
            return false;
        }

        if (this.getPrincipal() == null && test.getPrincipal() != null) {
            return false;
        }

        if (this.getPrincipal() != null
                && !this.getPrincipal().equals(test.getPrincipal())) {
            return false;
        }

        return this.isAuthenticated() == test.isAuthenticated();
    }

    @Override
    public int hashCode() {
        int code = 31;

        for (GrantedAuthority authority : authorities) {
            code ^= authority.hashCode();
        }

        if (this.getPrincipal() != null) {
            code ^= this.getPrincipal().hashCode();
        }

        if (this.getCredentials() != null) {
            code ^= this.getCredentials().hashCode();
        }

        if (this.getDetails() != null) {
            code ^= this.getDetails().hashCode();
        }

        if (this.isAuthenticated()) {
            code ^= -37;
        }

        return code;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append(": ");
        sb.append("Principal: ").append(this.getPrincipal()).append("; ");
        sb.append("Credentials: [PROTECTED]; ");
        sb.append("Authenticated: ").append(this.isAuthenticated()).append("; ");
        sb.append("Details: ").append(this.getDetails()).append("; ");

        if (!authorities.isEmpty()) {
            sb.append("Granted Authorities: ");

            int i = 0;
            for (GrantedAuthority authority : authorities) {
                if (i++ > 0) {
                    sb.append(", ");
                }

                sb.append(authority);
            }
        } else {
            sb.append("Not granted any authorities");
        }

        return sb.toString();
    }
}
