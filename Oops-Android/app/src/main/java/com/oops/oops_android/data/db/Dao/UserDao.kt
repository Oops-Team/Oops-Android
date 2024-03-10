package com.oops.oops_android.data.db.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.oops.oops_android.data.db.Entity.User

/* User Data Access Object */
@Dao
interface UserDao {

    // 사용자 정보 추가
    @Insert
    fun insertUser(user: User)

    // 다중 로그인을 했을 경우
    // 이메일, 로그인 유형이 일치하는 사용자 정보를 가져오는 쿼리문
    @Query("SELECT * FROM UserTable")
    fun getUser(): User

    // 로그인 유형 반환
    @Query("SELECT loginId FROM UserTable")
    fun getLoginId(): String

    // 닉네임 저장
    @Query("UPDATE UserTable set name = :name WHERE loginId IN (:loginId)")
    fun insertUserName(name: String, loginId: String)

    // 사용자 정보 삭제
    @Delete
    fun deleteUser(user: User)

    // 모든 사용자 삭제
    @Query("DELETE FROM UserTable")
    fun deleteAllUser()

    // 모든 사용자 가져오기
    @Query("SELECT * FROM UserTable")
    fun getAllUser(): User
}