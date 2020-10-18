package pl.surveyormanagement.api.security.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import pl.surveyormanagement.api.dao.UserDAO;
import pl.surveyormanagement.api.entities.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	UserDAO userDAO;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userDAO.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email:"  + email));

		return UserDetailsImpl.build(user);
	}
}