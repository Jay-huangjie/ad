package com.zlfc.test

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.zlfc.test.util.ActivityHelperUtil
import com.zlfc.test.util.DensityUtil
import com.zlfc.test.util.LoggerUtil
import com.beizi.fusion.AdListener
import com.beizi.fusion.SplashAd
import com.zlfc.test.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private var splashAd: SplashAd? = null
    private var mTimeout: Long = 5000
    private var canJumpImmediately: Boolean = false
    private lateinit var binding: ActivitySplashBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadAd()
    }

    override fun onResume() {
        super.onResume()
        if (canJumpImmediately) {
            jumpWhenCanClick()
        }
        canJumpImmediately = true
    }

    override fun onPause() {
        super.onPause()
        canJumpImmediately = false
    }


    @SuppressLint("MissingPermission")
    private fun loadAd() {
        splashAd = SplashAd(this, null, GlobalConstant.ADHUB_SPLASH_AD_ID, object : AdListener {
            override fun onAdLoaded() {
                LoggerUtil.e("onAdLoaded")

                binding?.splashAdFl?.let {
                    splashAd?.show(it)
                }
//                binding?.splashAdFl?.visibility = View.VISIBLE
            }

            override fun onAdShown() {
                //TODO 广告展示
                LoggerUtil.e("onAdShown")
            }

            override fun onAdFailedToLoad(code: Int) {
                //TODO 广告加载失败，跳转到应用主界面
                LoggerUtil.e("onAdFailedToLoad：$code")
                jumpMainActivity()
            }

            override fun onAdClosed() {
                //TODO 广告关闭，跳转到应用主界面
                LoggerUtil.e("onAdClosed")
                jumpWhenCanClick()
            }

            override fun onAdTick(millisUnitFinished: Long) {
                //TODO 广告倒计时回调，返回广告剩余时间
//                LoggerUtil.e("onAdTick：$millisUnitFinished")
                /* splash_skip_tv?.text =
                     String.format("跳过 %d", (millisUnitFinished / 1000f).roundToInt())*/
            }

            override fun onAdClicked() {
                //TODO 广告点击
                LoggerUtil.e("onAdClicked")

            }
        }, mTimeout)
        LoggerUtil.e(
            "width:${DensityUtil.getScreenWidth(this)},height:${
                DensityUtil.getScreenHeight(
                    this
                )
            }"
        )
        splashAd?.setSupportRegionClick(true)
        splashAd?.loadAd(DensityUtil.getScreenWidth(this), DensityUtil.getScreenHeight(this))

    }

    private fun loadEyeAd() {
//        splashAd = SplashAd(this,splash_ad_fl,null,"103222",object:AdListener{},mTimeout)
    }

    /**
     * 判断跳转到主页面
     */
    private fun jumpWhenCanClick() {
        when (canJumpImmediately) {
            true -> jumpMainActivity()
            else -> canJumpImmediately = true
        }
    }

    /**
     * 跳转到主页面
     */
    private fun jumpMainActivity() {
        ActivityHelperUtil.startNewActivity(this@SplashActivity, MainActivity::class.java)
        finish()
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_BACK, KeyEvent.KEYCODE_HOME -> true
            else -> super.onKeyDown(keyCode, event)
        }
    }

    override fun onDestroy() {
        splashAd?.cancel(this)
        super.onDestroy()
    }

}