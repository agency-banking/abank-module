/**
 *
 */
package com.agencybanking.security.users

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.transaction.annotation.Transactional

import java.util.Optional

interface UserRepository : JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {

    fun findByUsername(username: String): User?

    fun findByEmail(email: String): User

    fun findByCode(code: String): User

    fun findByUsernameAndIdNot(username: String, id: Long?): Optional<User>

    @Query("select u.email from User u where u.username = ?1")
    fun findEmailByUsername(username: String): Optional<String>

    @Transactional
    @Modifying
    @Query("update User u set u.password = ?1 where u.id = ?2")
    fun updatePassword(encoded: String, userId: Long?)

    @Transactional
    @Modifying
    @Query("update User u set u.password = ?1, u.firstLogin = false where u.username = ?2")
    fun updatePassword(encoded: String, username: String)

}