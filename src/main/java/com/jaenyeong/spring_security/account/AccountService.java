package com.jaenyeong.spring_security.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements UserDetailsService {

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = accountRepository.findByUsername(username);
		if (account == null) {
			throw new UsernameNotFoundException(username);
		}

		// 계정 정보를 UserDetails 타입으로 변경
		// Security가 제공하는 User 빌더 사용
//		return User.builder()
//				.username(account.getUsername())
//				.password(account.getPassword())
//				.roles(account.getRole())
//				.build();

		// Account 도메인 자체를 반환하여 사용하려면
		// Account가 시큐리티에 User 클래스를 상속하거나 UserDetails 인터페이스를 구현해야 함
		return new UserAccount(account);
	}

	public Account createNewAccount(Account account) {
		account.encodePassword(passwordEncoder);
		return accountRepository.save(account);
	}
}
