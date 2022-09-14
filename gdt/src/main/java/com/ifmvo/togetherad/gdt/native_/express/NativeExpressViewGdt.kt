package com.ifmvo.togetherad.gdt.native_.express

import android.view.ViewGroup
import com.ifmvo.togetherad.core.custom.express.BaseNativeExpressView
import com.ifmvo.togetherad.gdt.TogetherAdGdt
import com.qq.e.ads.nativ.NativeExpressADView
import com.qq.e.ads.nativ.NativeExpressMediaListener
import com.qq.e.comm.constants.AdPatternType
import com.qq.e.comm.util.AdError

/**
 * Created by Matthew Chen on 2020/11/25.
 */
class NativeExpressViewGdt : BaseNativeExpressView() {

    override fun showNativeExpress(adProviderType: String, adObject: Any, container: ViewGroup) {
        if (adObject !is NativeExpressADView) return

        TogetherAdGdt.downloadConfirmListener?.let {
            adObject.setDownloadConfirmListener(it)
        }

        //设置下载确认
        if (adObject.boundData.adPatternType == AdPatternType.NATIVE_VIDEO) {
            adObject.setMediaListener(object : NativeExpressMediaListener {
                override fun onVideoInit(adView: NativeExpressADView?) {}
                override fun onVideoPageClose(adView: NativeExpressADView?) {}
                override fun onVideoPause(adView: NativeExpressADView?) {}
                override fun onVideoStart(adView: NativeExpressADView?) {}
                override fun onVideoError(adView: NativeExpressADView?, adError: AdError?) {}
                override fun onVideoPageOpen(adView: NativeExpressADView?) {}
                override fun onVideoLoading(adView: NativeExpressADView?) {}
                override fun onVideoReady(adView: NativeExpressADView?, time: Long) {}
                override fun onVideoCached(adView: NativeExpressADView?) {}
                override fun onVideoComplete(adView: NativeExpressADView?) {}
            })
        }

        adObject.render()

        if (container.childCount > 0) {
            container.removeAllViews()
        }

        // 需要保证 View 被绘制的时候是可见的，否则将无法产生曝光和收益。
        container.addView(adObject)
    }

}