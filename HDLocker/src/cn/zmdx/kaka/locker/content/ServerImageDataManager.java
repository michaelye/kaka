
package cn.zmdx.kaka.locker.content;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Message;
import android.util.Log;
import cn.zmdx.kaka.locker.BuildConfig;
import cn.zmdx.kaka.locker.RequestManager;
import cn.zmdx.kaka.locker.database.MySqlitDatabase;
import cn.zmdx.kaka.locker.database.ServerImageDataModel;
import cn.zmdx.kaka.locker.network.DownloadRequest;
import cn.zmdx.kaka.locker.utils.HDBLOG;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

public class ServerImageDataManager {

    private static ServerImageDataManager INSTANCE;

    private ServerImageDataManager() {
    }

    public static ServerImageDataManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ServerImageDataManager();
        }
        return INSTANCE;
    }

    /**
     * 
     * @param limit
     * @param dataType S_DATATYPE_JOKE， S_DATATYPE_FILM， S_DATATYPE_NEWS， S_DATATYPE_GAME， S_DATATYPE_ALL
     * @param webSite 
     */
    public void pullServerImageData(int limit, String dataType, String webSite) {
        JsonObjectRequest request = null;
        request = new JsonObjectRequest(getUrl(limit, dataType, webSite), null,
                new Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        final List<ServerImageData> sdList = ServerImageData.parseJson(response);
                        if (sdList.size() <= 0) {
                            return;
                        }
                        Message msg = Message.obtain();
                        msg.what = PandoraBoxDispatcher.MSG_SERVER_IMAGE_DATA_ARRIVED;
                        msg.obj = sdList;
                        PandoraBoxDispatcher.getInstance().sendMessage(msg);
                    }

                }, new ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (BuildConfig.DEBUG) {
                            error.printStackTrace();
                        }
                    }
                });
        RequestManager.getRequestQueue().add(request);

    }

    public void downloadImage(final ServerImageData bd) {
        Request<String> request = new DownloadRequest(bd.mUrl, new Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (BuildConfig.DEBUG) {
                    HDBLOG.logD("download image finished,path=" + response);
                }
                if (ServerImageDataModel.getInstance().markAlreadyDownload(bd.mId)) {
                    Log.e("zy","markAlreadyDownload,id:" + bd.mId);
                } else {
                    Log.e("zy", "markAlreadyDownload failed");
                }
            }

        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    // invalidate url
                    DiskImageHelper.remove(bd.mImageUrl);
            }
        });
        RequestManager.getRequestQueue().add(request);
    }

    public void batchDownloadServerImage(List<ServerImageData> list) {
        int size = list.size();
        if (BuildConfig.DEBUG) {
            HDBLOG.logD("开始批量下载pandora server图片程序，总数：" + size);
        }
        for (int i = 0; i < size; i++) {
            ServerImageData bd = list.get(i);
            downloadImage(bd);
        }
    }

    /**
     * 
     */
    public String getUrl(int limit, String dataType, String webSite) {
        StringBuilder sb = new StringBuilder(getBaseUrl());
        sb.append("limit=" + limit);
        sb.append("&dataType=" + dataType);
        sb.append("&webSite=" + webSite);
        return sb.toString();
    }

    public String getBaseUrl() {
        if (BuildConfig.DEBUG) {
            return "http://182.254.159.63:8080/pandora/locker!queryDataImgTable.action?";
        } else {
            return "http://182.254.159.63:8080/pandora/locker!queryDataImgTable.action?";
        }
    }

    public static class ServerImageData extends BaseDataManager {

        /**
         * 图片的原地址
         */
        private String mUrl;

        /**
         * 图片的自己服务器地址
         */
        private String mImageUrl;

        public int mIsImageDownloaded;

        public String getUrl() {
            return mUrl;
        }

        public void setUrl(String mUrl) {
            this.mUrl = mUrl;
        }

        public String getImageUrl() {
            return mImageUrl;
        }

        public void setImageUrl(String mImageUrl) {
            this.mImageUrl = mImageUrl;
        }

        public int isImageDownloaded() {
            return mIsImageDownloaded;
        }

        public void setIsImageDownloaded(int mIsImageDownloaded) {
            this.mIsImageDownloaded = mIsImageDownloaded;
        }

        public static List<ServerImageData> parseJson(JSONObject jsonObj) {
            List<ServerImageData> sdList = new ArrayList<ServerImageData>();
            String state = jsonObj.optString("state");
            if (state.equals("success")) {
                JSONArray jsonArray = jsonObj.optJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.optJSONObject(i);
                    ServerImageData serverImageData = new ServerImageData();
                    serverImageData.parseBaseJson(jsonObject);
                    String url = jsonObject.optString("url");
                    String imgUrl = jsonObject.optString("imgUrl");
                    serverImageData.setUrl(url);
                    serverImageData.setImageUrl(imgUrl);
                    serverImageData.setIsImageDownloaded(MySqlitDatabase.DOWNLOAD_FALSE);
                    sdList.add(serverImageData);
                }
            }
            return sdList;
        }

        public static void saveToDatabase(List<ServerImageData> sidList) {
            ServerImageDataModel.getInstance().saveServerImageData(sidList);
        }

    }

}
