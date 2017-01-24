package com.kagubuzz.spring.utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kagubuzz.database.dao.CRUDDAO;
import com.kagubuzz.datamodels.enums.SpringRoles;
import com.kagubuzz.datamodels.hibernate.TBLUser;

@Service("springSecurityUtilities")
public class SpringSecurityUtilities {
    
    @Autowired CRUDDAO crudDAO;
    
  @Transactional(readOnly = true)
  public static User userFromKaguBuzzUser(TBLUser userEntity) {
	  
    String username = userEntity.getId().toString();
    String password = userEntity.isSocialAccount() ? UUID.randomUUID().toString() : userEntity.getPassword();
    boolean enabled = true;//userEntity.isActive();
    boolean accountNonExpired = true;//userEntity.isActive();
    boolean credentialsNonExpired = true;//userEntity.isActive();
    boolean accountNonLocked =true; //userEntity.isActive();

    Collection<GrantedAuthority> authorities =  getRoles(userEntity);
    
    User user = new User(username, 
    					 password, 
    					 enabled,
    					 accountNonExpired, 
    					 credentialsNonExpired, 
    					 accountNonLocked, 
    					 authorities);
    return user;
  }
  
  public static User getLoggedInUser() {
	  User user = null;
      SecurityContext securityContext = SecurityContextHolder.getContext();
      Authentication auth;
      if (securityContext != null) {
          auth = securityContext.getAuthentication();
          if (auth != null) {
              Object principal = auth.getPrincipal();
              if (principal instanceof UserDetails) {
                  user = (User) principal;
              }
          }
      }
      return user;
  }

  public Authentication signInUser(TBLUser kbUser, HttpSession session) {
      
      List<GrantedAuthority> roles = getRoles(kbUser);
      
	  SecurityContext securityContext = SecurityContextHolder.getContext();
	  
      User user = SpringSecurityUtilities.userFromKaguBuzzUser(kbUser);
      
	  Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), roles);
      
      SecurityContextHolder.getContext().setAuthentication(authentication);
      securityContext.setAuthentication(authentication);

      session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
      
      kbUser.setLastLogin(new Date());
      
      crudDAO.update(kbUser);
      
      return authentication;
  }

  public static List<GrantedAuthority> getRoles(TBLUser kbUser) {
	  
      String[] userRoles = kbUser.getSpringSecurityRole().split(",");
      
      List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
      
      for(String role: userRoles) {      
          roles.add(new SimpleGrantedAuthority(role));
      }
      return roles;
  }
  
  public static boolean isAdmin(TBLUser kbUser) {
      return getRoles(kbUser).contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
  }
  
  public static boolean hasRole(TBLUser kbUser, SpringRoles springRole) {
      return getRoles(kbUser).contains(new SimpleGrantedAuthority(springRole.getRoleName()));
  }
}