package com.parisara.cycle.data.repository

import com.parisara.cycle.data.local.dao.UserDao
import com.parisara.cycle.data.local.entity.User

class UserRepository(private val userDao: UserDao) {

    suspend fun registerUser(user: User): Long {
        return userDao.insertUser(user)
    }

    suspend fun getUserByEmailOrMobile(identifier: String): User? {
        return userDao.getUserByEmailOrMobile(identifier)
    }

    suspend fun getUserById(userId: Long): User? {
        return userDao.getUserById(userId)
    }
}
