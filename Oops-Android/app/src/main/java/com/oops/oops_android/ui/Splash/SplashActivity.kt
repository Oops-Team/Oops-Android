package com.oops.oops_android.ui.Splash

import android.animation.ObjectAnimator
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.oops.oops_android.databinding.ActivitySplashBinding
import com.oops.oops_android.ui.Main.MainActivity
import com.oops.oops_android.ui.Base.BaseActivity
import com.oops.oops_android.ui.Login.LoginActivity
import com.oops.oops_android.utils.clearToken
import com.oops.oops_android.utils.getToken

/* 스플래시 화면 */
class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    private lateinit var splashScreen: SplashScreen

    override fun beforeSetContentView() {
        splashScreen = installSplashScreen()
    }

    override fun initAfterBinding() {
        // 스플래시 띄우기
        splashScreen.setOnExitAnimationListener{ splashScreenView ->
            val slideUp = ObjectAnimator.ofFloat(
                splashScreenView.iconView,
                View.TRANSLATION_Y,
                0.0f,
                0.0f
            )

            slideUp.interpolator = AnticipateInterpolator()
            slideUp.duration = 150L

            // 애니메이션 이후, 스플래시 삭제
            slideUp.doOnEnd { splashScreenView.remove() }

            slideUp.start()
        }

        // 토큰 여부에 따른 화면 전환
        if (getToken() == null) {
            startActivityWithClear(LoginActivity::class.java) // 로그인 화면 이동
        }
        else {
            startActivityWithClear(MainActivity::class.java) // 홈 화면 이동
        }
    }
}