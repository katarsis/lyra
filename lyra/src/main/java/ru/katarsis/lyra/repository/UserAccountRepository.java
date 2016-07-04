/**
 * @author Petr Klimov
 *
 */
package ru.katarsis.lyra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ru.katarsis.lyra.model.UserAccount;

public interface UserAccountRepository extends JpaRepository<UserAccount, Integer>{

	@Query("FROM UserAccount where userLogin = ?1")
	UserAccount getUserAccountByName(String userName);
}
