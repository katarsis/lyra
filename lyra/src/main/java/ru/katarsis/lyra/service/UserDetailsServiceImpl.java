/**
 * @author Petr Klimov
 *
 */
package ru.katarsis.lyra.service;

import javax.inject.Inject;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import ru.katarsis.lyra.model.UserAccount;
import ru.katarsis.lyra.repository.UserAccountRepository;

@Repository("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService{

	@Inject UserAccountRepository userAccountService;
	
	@Override
	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException {
		UserAccount userAccount = userAccountService.getUserAccountByName(userName);
		if(userAccount==null)
			throw new UsernameNotFoundException("No such user"+userName);
		UserDetailsAdapter user =new UserDetailsAdapter(userAccount);
		return user;
	}

}
