package com.kagubuzz.spring.hibernate;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;    
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kagubuzz.database.dao.CRUDDAO;
import com.kagubuzz.database.dao.UserDAO;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.spring.utilities.SpringSecurityUtilities;

@Service("userDetailsService") 
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired private UserDAO dao;
  @Autowired private CRUDDAO crudDao;
  
  @Override
  @Transactional(readOnly = false)
  public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException, DataAccessException {

    TBLUser userEntity = dao.getUserByEmail(id);
    
    if (userEntity == null) { 
        throw new UsernameNotFoundException("user not found");
    }

    userEntity.setLastLogin(new Date());
    
    crudDao.update(userEntity);
    
    return SpringSecurityUtilities.userFromKaguBuzzUser(userEntity);
  }
  
}