package com.ifmvo.togetherad.demo.inter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.ifmvo.togetherad.core.helper.AdHelperInter
import com.ifmvo.togetherad.core.listener.InterListener
import com.ifmvo.togetherad.demo.app.AdProviderType
import com.ifmvo.togetherad.demo.R
import com.ifmvo.togetherad.demo.app.TogetherAdAlias
import kotlinx.android.synthetic.main.activity_inter.*

/**
 *
 * Created by Matthew Chen on 2020/7/6.
 */
class InterActivity : AppCompatActivity() {

    private var adHelperInter: AdHelperInter? = null

    companion object {
        fun action(context: Context) {
            context.startActivity(Intent(context, InterActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inter)

        //设置 穿山甲 Inter插屏广告 可接受图片尺寸,可以不设置，默认为 600 600
        //CsjProvider.Inter.setImageAcceptedSize(600, 600)

        //使用 Map<String, Int> 配置广告商 权重，通俗的讲就是 随机请求的概率占比
        val ratioMapInter = linkedMapOf(
                AdProviderType.GDT.type to 1,
                AdProviderType.CSJ.type to 1,
                AdProviderType.BAIDU.type to 1
        )
        adHelperInter = AdHelperInter(activity = this, alias = TogetherAdAlias.AD_INTER, /*ratioMap = ratioMapInter,*/ listener = object : InterListener {
            override fun onAdStartRequest(providerType: String) {
                //在开始请求之前会回调此方法，失败切换的情况会回调多次
                addLog("\n开始请求了，$providerType")
            }

            override fun onAdLoaded(providerType: String) {
                //广告请求成功的回调，每次请求只回调一次
                addLog("请求到了，$providerType")
            }

            override fun onAdFailed(providerType: String, failedMsg: String?) {
                //请求失败的回调，失败切换的情况会回调多次
                addLog("单个广告请求失败, $providerType, $failedMsg")
            }

            override fun onAdFailedAll(failedMsg: String?) {
                //所有配置的广告商都请求失败了，只有在全部失败之后会回调一次
                addLog("全部请求失败了")
            }

            override fun onAdClicked(providerType: String) {
                //点击广告的回调
                addLog("点击了，$providerType")
            }

            override fun onAdExpose(providerType: String) {
                //广告展示曝光的回调；由于 Banner 广告存在自动刷新功能，所以曝光会回调多次，每次刷新都会回调
                addLog("曝光了，$providerType")
            }

            override fun onAdClose(providerType: String) {
                //广告被关闭的回调
                addLog("关闭了，$providerType")
            }
        })

        btnRequest.setOnClickListener {
            //开始请求插屏广告
            adHelperInter?.load()
        }

        btnShow.setOnClickListener {
            //开始展示插屏广告，必须在 onAdLoaded 回调之后展示
            adHelperInter?.show()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        adHelperInter?.destroy()
    }

    private var logStr = "日志: \n"

    private fun addLog(content: String?) {
        logStr = logStr + content + "\n"
        log.text = logStr

        info.post { info.fullScroll(View.FOCUS_DOWN) }
    }
}