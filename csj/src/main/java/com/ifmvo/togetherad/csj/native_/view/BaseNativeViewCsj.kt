package com.ifmvo.togetherad.csj.native_.view

import android.view.View
import android.view.ViewGroup
import com.bytedance.sdk.openadsdk.TTAdConstant
import com.bytedance.sdk.openadsdk.TTAppDownloadListener
import com.bytedance.sdk.openadsdk.TTFeedAd
import com.bytedance.sdk.openadsdk.TTNativeAd
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.listener.NativeViewListener

/**
 *
 * Created by Matthew Chen on 2020/9/27.
 */
abstract class BaseNativeViewCsj(onClose: ((adProviderType: String) -> Unit)? = null) : BaseNativeViewCsjFeed(onClose) {

    //关闭按钮的回调
    private var mOnClose = onClose

    override fun showNative(adProviderType: String, adObject: Any, container: ViewGroup, listener: NativeViewListener?) {

        if (adObject is TTFeedAd) {
            super.showNative(adProviderType, adObject, container, listener)
            return
        }

        if (adObject !is TTNativeAd) {
            return
        }

        container.removeAllViews()

        //findView
        rootView = View.inflate(container.context, getLayoutRes(), container)

        //AdLogo
        getAdLogoImageView()?.setImageBitmap(adObject.adLogo)

        //Icon
        getIconImageView()?.let {
            TogetherAd.mImageLoader?.loadImage(container.context, it, adObject.icon.imageUrl)
        }

        //CloseBtn
        getCloseButton()?.visibility = if (mOnClose == null) View.GONE else View.VISIBLE
        getCloseButton()?.setOnClickListener {
            mOnClose?.invoke(adProviderType)
        }

        //标题和描述
        getTitleTextView()?.text = adObject.title
        getDescTextView()?.text = adObject.description
        getSourceTextView()?.text = if (adObject.source?.isNotEmpty() == true) adObject.source else "广告来源"
        getActionButton()?.text = getActionBtnText(adObject)

        adObject.setDownloadListener(object : TTAppDownloadListener {
            override fun onIdle() {
                getActionButton()?.text = "开始下载"
            }

            override fun onDownloadPaused(totalBytes: Long, currBytes: Long, fileName: String?, appName: String?) {
                getActionButton()?.text = "下载暂停"
            }

            override fun onDownloadFailed(totalBytes: Long, currBytes: Long, fileName: String?, appName: String?) {
                getActionButton()?.text = "重新下载"
            }

            override fun onDownloadActive(p0: Long, p1: Long, p2: String?, p3: String?) {
                getActionButton()?.text = "下载中"
            }

            override fun onDownloadFinished(totalBytes: Long, fileName: String?, appName: String?) {
                getActionButton()?.text = "点击安装"
            }

            override fun onInstalled(fileName: String?, appName: String?) {
                getActionButton()?.text = "点击打开"
            }
        })

        // 注册普通点击区域，创意点击区域。重要! 这个涉及到广告计费及交互，必须正确调用。convertView必须使用ViewGroup。
        adObject.registerViewForInteraction(
                rootView as ViewGroup,
                getClickableViews() ?: mutableListOf(),
                getCreativeViews() ?: mutableListOf(),
                object : TTNativeAd.AdInteractionListener {
                    override fun onAdClicked(view: View, ad: TTNativeAd) {
                        // 点击普通区域的回调
                    }

                    override fun onAdCreativeClick(view: View, ad: TTNativeAd) {
                        // 点击创意区域的回调
                        listener?.onAdClicked(adProviderType)
                    }

                    override fun onAdShow(ad: TTNativeAd) {
                        // 广告曝光展示的回调
                        listener?.onAdExposed(adProviderType)
                    }
                })

        when (adObject.imageMode) {
            //视频类型
            TTAdConstant.IMAGE_MODE_VIDEO, TTAdConstant.IMAGE_MODE_VIDEO_VERTICAL -> {
                getImageContainer()?.visibility = View.VISIBLE
                getVideoContainer()?.visibility = View.GONE
                getMainImageView_2()?.visibility = View.GONE
                getMainImageView_3()?.visibility = View.GONE
                val videoCoverImage = adObject.videoCoverImage

                //信息流如果是视频的话，就只展示视频封面
                if (videoCoverImage != null && videoCoverImage.imageUrl != null) {
                    getMainImageView_1()?.let {
                        TogetherAd.mImageLoader?.loadImage(container.context, it, videoCoverImage.imageUrl)
                    }
                }
            }
            //单个图片的类型
            TTAdConstant.IMAGE_MODE_LARGE_IMG, TTAdConstant.IMAGE_MODE_SMALL_IMG, TTAdConstant.IMAGE_MODE_VERTICAL_IMG -> {
                getImageContainer()?.visibility = View.VISIBLE
                getVideoContainer()?.visibility = View.GONE
                getMainImageView_2()?.visibility = View.GONE
                getMainImageView_3()?.visibility = View.GONE
                val imageList = adObject.imageList

                if (imageList.isNotEmpty() && imageList[0] != null && imageList[0].isValid) {
                    getMainImageView_1()?.let {
                        TogetherAd.mImageLoader?.loadImage(container.context, it, imageList[0].imageUrl)
                    }
                }
            }
            //多个图片的类型
            TTAdConstant.IMAGE_MODE_GROUP_IMG -> {
                getImageContainer()?.visibility = View.VISIBLE
                getVideoContainer()?.visibility = View.GONE
                getMainImageView_2()?.visibility = View.VISIBLE
                getMainImageView_3()?.visibility = View.VISIBLE
                val imageList = adObject.imageList

                if (imageList.isNotEmpty() && imageList[0] != null && imageList[0].isValid) {
                    getMainImageView_1()?.let {
                        TogetherAd.mImageLoader?.loadImage(container.context, it, imageList[0].imageUrl)
                    }
                }
                if (imageList.isNotEmpty() && imageList.size > 1 && imageList[1] != null && imageList[1].isValid) {
                    getMainImageView_2()?.let {
                        TogetherAd.mImageLoader?.loadImage(container.context, it, imageList[1].imageUrl)
                    }
                }
                if (imageList.isNotEmpty() && imageList.size > 2 && imageList[2].isValid) {
                    getMainImageView_3()?.let {
                        TogetherAd.mImageLoader?.loadImage(container.context, it, imageList[2].imageUrl)
                    }
                }
            }
        }
    }

}