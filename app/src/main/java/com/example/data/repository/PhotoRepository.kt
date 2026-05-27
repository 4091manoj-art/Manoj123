package com.example.data.repository

import com.example.data.database.PhotoDao
import com.example.data.database.RecentEdit
import com.example.data.database.UserSession
import kotlinx.coroutines.flow.Flow

class PhotoRepository(private val dao: PhotoDao) {

    val userSession: Flow<UserSession?> = dao.getUserSession()
    val allRecentEdits: Flow<List<RecentEdit>> = dao.getAllRecentEdits()

    suspend fun saveSession(session: UserSession) {
        dao.saveUserSession(session)
    }

    suspend fun logout() {
        dao.clearUserSession()
    }

    suspend fun registerOrUpdateSession(email: String, name: String, isPremium: Boolean) {
        val currentSession = UserSession(
            id = 1,
            isLoggedIn = true,
            email = email,
            name = name,
            isPremium = isPremium,
            remainingEdits = if (isPremium) 99999 else 5
        )
        dao.saveUserSession(currentSession)
    }

    suspend fun useEditToken() {
        // Decrement remaining edits for free user
        // Note: Flow collector can get it or we just do a direct update
    }

    suspend fun insertRecentEdit(edit: RecentEdit) {
        dao.insertRecentEdit(edit)
    }

    suspend fun deleteEdit(id: Int) {
        dao.deleteEditById(id)
    }

    suspend fun clearHistory() {
        dao.clearHistory()
    }
}
