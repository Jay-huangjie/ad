package com.ifmvo.togetherad.baidu.native_.view

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.baidu.mobad.feeds.NativeResponse
import com.ifmvo.togetherad.baidu.R
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.custom.native_.BaseNativeView
import com.ifmvo.togetherad.core.listener.NativeViewListener

/**
 *
 * Created by Matthew Chen on 2020/9/14.
 */
abstract class BaseNativeViewBaidu(onClose: ((adProviderType: String) -> Unit)? = null) : BaseNativeView() {

    private var mOnClose = onClose

    var rootView: View? = null

    open fun getLayoutRes(): Int {
        return R.layout.layout_native_view_baidu
    }

    open fun getMainImageView(): ImageView? {
        return rootView?.findViewById(R.id.native_main_image)
    }

    open fun getIconImageView(): ImageView? {
        return rootView?.findViewById(R.id.native_icon_image)
    }

    open fun getBaiduLogoImageView(): ImageView? {
        return rootView?.findViewById(R.id.native_baidulogo)
    }

    open fun getAdLogoImageView(): ImageView? {
        return rootView?.findViewById(R.id.native_adlogo)
    }

    open fun getTitleTextView(): TextView? {
        return rootView?.findViewById(R.id.native_title)
    }

    open fun getDescTextView(): TextView? {
        return rootView?.findViewById(R.id.native_text)
    }

    open fun getBrandNameTextView(): TextView? {
        return rootView?.findViewById(R.id.native_brand_name)
    }

    //关闭按钮，可以重写为任意类型的View
    open fun getCloseButton(): View? {
        return rootView?.findViewById(R.id.btn_close)
    }

    override fun showNative(adProviderType: String, adObject: Any, container: ViewGroup, listener: NativeViewListener?) {
        if (adObject !is NativeResponse) {
            return
        }

        //findView
        rootView = View.inflate(container.context, getLayoutRes(), container)

        //Image
        getMainImageView()?.let { TogetherAd.mImageLoader?.loadImage(container.context, it, adObject.imageUrl) }
        getIconImageView()?.let { TogetherAd.mImageLoader?.loadImage(container.context, it, adObject.iconUrl) }
        getBaiduLogoImageView()?.let { TogetherAd.mImageLoader?.loadImage(container.context, it, adObject.baiduLogoUrl) }
        getAdLogoImageView()?.let { TogetherAd.mImageLoader?.loadImage(container.context, it, adObject.adLogoUrl) }

        //Text
        getTitleTextView()?.let { it.text = adObject.title }
        getDescTextView()?.let { it.text = adObject.desc }
        getBrandNameTextView()?.let { it.text = adObject.brandName }

        //CloseBtn
        getCloseButton()?.visibility = if (mOnClose == null) View.GONE else View.VISIBLE
        getCloseButton()?.setOnClickListener {
            mOnClose?.invoke(adProviderType)
        }

        //Handle
        adObject.recordImpression(rootView)
        listener?.onAdExposed(adProviderType)
        rootView?.setOnClickListener {
            adObject.handleClick(it)
            listener?.onAdClicked(adProviderType)
        }
    }
}