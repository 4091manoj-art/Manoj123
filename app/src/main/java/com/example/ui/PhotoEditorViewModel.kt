package com.example.ui

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.database.RecentEdit
import com.example.data.database.UserSession
import com.example.data.repository.PhotoRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

sealed interface Screen {
    object Splash : Screen
    object Login : Screen
    object SignUp : Screen
    object MainContainer : Screen // Houses Home, Edit, History, Profile (Bottom Nav)
}

enum class HomeTab {
    HOME, EDIT, HISTORY, PROFILE
}

enum class EditingFeature(val displayName: String, val rawDescription: String) {
    ENHANCE("AI Enhance", "Upscales image, restores fine details, balances exposure."),
    BG_REMOVE("BG Remove", "Isolates the foreground subject with clean transparency."),
    CARTOON("Cartoon Maker", "Converts photo to anime, Pixar, or classic comics style."),
    COLORIZE("Restore & Color", "Colorizes old B&W frames and paints over scratches."),
    AVATAR("AI Avatar", "Generates high quality styled cyberpunk or futuristic portraits."),
    BLUR_FIX("Blur Fix", "De-blurs images and applies high-definition AI sharpening.")
}

sealed interface EditorStep {
    object ChooseSource : EditorStep
    object Processing : EditorStep
    object ActiveTool : EditorStep
    object Result : EditorStep
}

data class UserSessionState(
    val isLoggedIn: Boolean = false,
    val email: String = "",
    val name: String = "Guest User",
    val isPremium: Boolean = false,
    val remainingEdits: Int = 3
)

data class PhotoEditorUiState(
    val currentScreen: Screen = Screen.Splash,
    val activeTab: HomeTab = HomeTab.HOME,
    val session: UserSessionState = UserSessionState(),
    val history: List<RecentEdit> = emptyList(),
    
    // Editor Workflow States
    val activeFeature: EditingFeature? = null,
    val editorStep: EditorStep = EditorStep.ChooseSource,
    val originalImage: String? = null, // Holds chosen image data (e.g. preset identifier or base64)
    val processedImage: String? = null, // Holds resultant image data
    val originalBitmap: Bitmap? = null, // Handles actually uploaded user bitmaps
    val isProcessing: Boolean = false,
    val progress: Float = 0f,
    
    // Feature parameters
    val beforeAfterSplit: Float = 0.5f,
    val bgFillType: String = "Transparent", // Transparent, Royal, Green, Cyan, Space
    val cartoonStyle: String = "Anime Spark", // Anime Spark, Pixar Cute, Vintage Comics
    val avatarTheme: String = "Cyberpunk Ninja", // Cyberpunk Ninja, Synthwave DJ, Sci-Fi Pilot, Royal Aura
    val sharpenLevel: Float = 60f, // 0 - 100
    val colorizeMode: Boolean = true,
    
    // Ad & Monetization State
    val isBannerAdVisible: Boolean = true,
    val isRewardedAdShowing: Boolean = false,
    val adTimeRemaining: Int = 5,
    val rewardUnlocked: Boolean = false,
    val pendingHdExport: Boolean = false
)

class PhotoEditorViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = PhotoRepository(db.photoDao())

    private val _uiState = MutableStateFlow(PhotoEditorUiState())
    val uiState: StateFlow<PhotoEditorUiState> = _uiState.asStateFlow()

    init {
        // Collect db user session
        viewModelScope.launch {
            repository.userSession.collect { session ->
                if (session != null) {
                    _uiState.update {
                        it.copy(
                            session = UserSessionState(
                                isLoggedIn = session.isLoggedIn,
                                email = session.email,
                                name = session.name,
                                isPremium = session.isPremium,
                                remainingEdits = session.remainingEdits
                            )
                        )
                    }
                } else {
                    _uiState.update { it.copy(session = UserSessionState()) }
                }
            }
        }

        // Collect db history
        viewModelScope.launch {
            repository.allRecentEdits.collect { historyList ->
                _uiState.update { it.copy(history = historyList) }
            }
        }
    }

    fun navigateTo(screen: Screen) {
        _uiState.update { it.copy(currentScreen = screen) }
    }

    fun setTab(tab: HomeTab) {
        _uiState.update { it.copy(activeTab = tab) }
    }

    // Login system
    fun handleLogin(email: String, name: String, isPremium: Boolean) {
        viewModelScope.launch {
            repository.registerOrUpdateSession(email, name, isPremium)
            _uiState.update {
                it.copy(currentScreen = Screen.MainContainer, activeTab = HomeTab.HOME)
            }
        }
    }

    fun handleSignUp(email: String, name: String) {
        viewModelScope.launch {
            repository.registerOrUpdateSession(email, name, isPremium = false)
            _uiState.update {
                it.copy(currentScreen = Screen.MainContainer, activeTab = HomeTab.HOME)
            }
        }
    }

    fun skipLogin() {
        viewModelScope.launch {
            // Log in as Guest session, not saved in persistent DB as premium, remainingEdits = 3
            _uiState.update {
                it.copy(
                    session = UserSessionState(
                        isLoggedIn = false,
                        email = "guest@ai.editor",
                        name = "Guest Artist",
                        isPremium = false,
                        remainingEdits = 3
                    ),
                    currentScreen = Screen.MainContainer,
                    activeTab = HomeTab.HOME
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _uiState.update {
                it.copy(
                    currentScreen = Screen.Login,
                    session = UserSessionState(),
                    activeTab = HomeTab.HOME
                )
            }
        }
    }

    fun purchasePremium() {
        viewModelScope.launch {
            val user = _uiState.value.session
            val email = if (user.email.isEmpty()) "premium.member@ai.editor" else user.email
            val name = if (user.name == "Guest Artist") "Premium Member" else user.name
            repository.registerOrUpdateSession(email, name, isPremium = true)
        }
    }

    // AI Workflow logic
    fun selectFeature(feature: EditingFeature) {
        _uiState.update {
            it.copy(
                activeFeature = feature,
                editorStep = EditorStep.ChooseSource,
                progress = 0f,
                isProcessing = false,
                originalImage = null,
                processedImage = null,
                originalBitmap = null
            )
        }
        setTab(HomeTab.EDIT)
    }

    fun selectPresetImage(presetId: String) {
        _uiState.update {
            it.copy(
                originalImage = presetId,
                originalBitmap = null,
                editorStep = EditorStep.Processing
            )
        }
        triggerAiProcessing()
    }

    fun selectUserBitmap(bitmap: Bitmap) {
        _uiState.update {
            it.copy(
                originalBitmap = bitmap,
                originalImage = "custom_upload",
                editorStep = EditorStep.Processing
            )
        }
        triggerAiProcessing()
    }

    private fun triggerAiProcessing() {
        _uiState.update { it.copy(isProcessing = true, progress = 0.0f) }
        viewModelScope.launch {
            for (i in 1..10) {
                kotlinx.coroutines.delay(200)
                _uiState.update { it.copy(progress = i / 10f) }
            }
            // Processing done, transition to ActiveTool adjustment screen
            _uiState.update {
                it.copy(
                    isProcessing = false,
                    editorStep = EditorStep.ActiveTool
                )
            }
        }
    }

    fun applyActiveAdjustments() {
        _uiState.update { it.copy(isProcessing = true) }
        viewModelScope.launch {
            kotlinx.coroutines.delay(1000)
            
            // Generate processed image ID and save in database history
            val feat = _uiState.value.activeFeature ?: EditingFeature.ENHANCE
            val orig = _uiState.value.originalImage ?: "preset_potrait"
            val processed = "proc_${feat.name.lowercase()}_$orig"

            _uiState.update {
                it.copy(
                    isProcessing = false,
                    processedImage = processed,
                    editorStep = EditorStep.Result
                )
            }

            // Save project to history in database
            val entity = RecentEdit(
                featureName = feat.displayName,
                originalImage = orig,
                processedImage = processed
            )
            repository.insertRecentEdit(entity)
        }
    }

    fun setBeforeAfterRatio(ratio: Float) {
        _uiState.update { it.copy(beforeAfterSplit = ratio) }
    }

    fun updateBgFill(fill: String) {
        _uiState.update { it.copy(bgFillType = fill) }
    }

    fun updateCartoonStyle(style: String) {
        _uiState.update { it.copy(cartoonStyle = style) }
    }

    fun updateAvatarTheme(theme: String) {
        _uiState.update { it.copy(avatarTheme = theme) }
    }

    fun updateSharpenLevel(level: Float) {
        _uiState.update { it.copy(sharpenLevel = level) }
    }

    fun toggleColorize(mode: Boolean) {
        _uiState.update { it.copy(colorizeMode = mode) }
    }

    // Re-edit action
    fun startReedit() {
        _uiState.update {
            it.copy(
                editorStep = EditorStep.ActiveTool,
                processedImage = null
            )
        }
    }

    fun resetEditor() {
        _uiState.update {
            it.copy(
                activeFeature = null,
                editorStep = EditorStep.ChooseSource,
                originalImage = null,
                processedImage = null,
                originalBitmap = null
            )
        }
        setTab(HomeTab.HOME)
    }

    // AdMob Simulation System
    fun startHdExport() {
        if (_uiState.value.session.isPremium) {
            // Instant full HD export
            _uiState.update { it.copy(rewardUnlocked = true) }
        } else {
            // Free user must watch ad
            _uiState.update {
                it.copy(
                    isRewardedAdShowing = true,
                    adTimeRemaining = 5,
                    rewardUnlocked = false,
                    pendingHdExport = true
                )
            }
            playSimulatedAd()
        }
    }

    private fun playSimulatedAd() {
        viewModelScope.launch {
            for (sec in 5 downTo 1) {
                kotlinx.coroutines.delay(1000)
                _uiState.update { it.copy(adTimeRemaining = sec) }
            }
            _uiState.update {
                it.copy(
                    rewardUnlocked = true,
                    isRewardedAdShowing = false
                )
            }
        }
    }

    fun closeAdEarly() {
        // User aborted the loaded AdMob incentivized ad
        _uiState.update {
            it.copy(
                isRewardedAdShowing = false,
                rewardUnlocked = false,
                pendingHdExport = false
            )
        }
    }

    fun resetReward() {
        _uiState.update { it.copy(rewardUnlocked = false) }
    }

    fun deleteHistoryItem(id: Int) {
        viewModelScope.launch {
            repository.deleteEdit(id)
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }
}
