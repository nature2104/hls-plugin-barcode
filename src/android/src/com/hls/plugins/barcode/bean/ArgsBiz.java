package com.hls.plugins.barcode.bean;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by panx on 2017/3/15.
 */
public class ArgsBiz {
    private final static String TAG = "ArgsBiz";

    public ArgsBean getArgsBeanByJSONObject(JSONObject object) {
        ArgsBean argsBean = new ArgsBean();
        try {
            JSONObject pageObject = object.getJSONObject("message");
            argsBean.setPageInfoBean(getPageInfoBeanByJsonObject(pageObject));
            String operateMethod = object.optString("operationType", "");
            argsBean.setOperateMethod(operateMethod);
            JSONArray httpArray = object.optJSONArray("requests");
            argsBean.setHttpInfos(getHttpInfosByJsonArray(httpArray));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return argsBean;
    }

    private PageInfoBean getPageInfoBeanByJsonObject(JSONObject object) {
        PageInfoBean pageInfoBean = new PageInfoBean();
        pageInfoBean.setTitle(object.optString("title", ""));
        pageInfoBean.setTipScan(object.optString("tipScan", ""));
        pageInfoBean.setTipInput(object.optString("tipInput", ""));
        pageInfoBean.setTipLoading(object.optString("tipLoading", ""));
        pageInfoBean.setTipNetworkError(object.optString("tipNetworkError", ""));
        pageInfoBean.setTipOffline(object.optString("tipOffline", ""));
        pageInfoBean.setOpenButton(object.optString("openButton", ""));
        pageInfoBean.setFooterFirst(object.optString("footerFirst", ""));
        pageInfoBean.setFooterSecond(object.optString("footerSecond", ""));
        pageInfoBean.setBarTitle(object.optString("barTitle", ""));
        pageInfoBean.setExplainFlag(object.optBoolean("explainFlag", false));
        pageInfoBean.setInputFlag(object.optBoolean("inputFlag", true));
        pageInfoBean.setLightFlag(object.optBoolean("lightFlag", true));
        pageInfoBean.setTipLightOff(object.optString("tipLightOff", ""));
        pageInfoBean.setTipLightOn(object.optString("tipLightOn", ""));
        pageInfoBean.setDelayTime(object.optLong("delayTime"));
        return pageInfoBean;
    }

    private ArrayList<HttpInfoBean> getHttpInfosByJsonArray(JSONArray array) {
        if (array == null) return null;
        ArrayList<HttpInfoBean> httpInfos = new ArrayList<HttpInfoBean>();
        try {
            for (int i = 0; i < array.length(); i++) {
                HttpInfoBean httpInfoBean = getHttpInfoBeanByJsonObject(array.getJSONObject(i));
                httpInfos.add(httpInfoBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "wrong arguments http infos");
            return null;
        }

        return httpInfos;
    }

    private HttpInfoBean getHttpInfoBeanByJsonObject(JSONObject object) {
        HttpInfoBean httpInfoBean = new HttpInfoBean();
        HttpInfoBean.Config config = new HttpInfoBean.Config();
        HttpInfoBean.Headers headers = new HttpInfoBean.Headers();
        try {
            httpInfoBean.setUrl(object.getString("url"));
            httpInfoBean.setMethod(object.getString("method"));
            JSONObject headerObject = object.getJSONObject("headers");
            JSONObject configObject = object.getJSONObject("config");
            JSONObject dataObject = object.optJSONObject("data");
            if (dataObject == null) {
                dataObject = new JSONObject();
            }
            headers.setAuthorization(headerObject.getString("Authorization"));
            headers.setContentType(headerObject.getString("Content-Type"));
            config.setTimeout(configObject.getInt("timeout"));
            httpInfoBean.setConfig(config);
            httpInfoBean.setData(dataObject.toString());
            httpInfoBean.setHeaders(headers);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "wrong arguments http info");
            return null;
        }
        return httpInfoBean;
    }
}
