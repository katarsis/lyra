package ru.katarsis.lyra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ru.katarsis.lyra.model.Role;
import ru.katarsis.lyra.model.UserAccount;


public interface RoleRepository extends JpaRepository<UserAccount, Integer>{

	@Query("FROM Role where id = ?1")
	Role getRoleById(int idRole);
}
