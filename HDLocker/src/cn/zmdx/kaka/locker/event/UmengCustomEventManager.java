
package cn.zmdx.kaka.locker.event;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;
import cn.zmdx.kaka.locker.HDApplication;
import cn.zmdx.kaka.locker.content.box.IPandoraBox;
import cn.zmdx.kaka.locker.settings.config.PandoraConfig;
import cn.zmdx.kaka.locker.theme.ThemeManager;

import com.umeng.analytics.MobclickAgent;

public class UmengCustomEventManager {

    public static final String EVENT_FIXED_TIMES = "fixedTimes";// 拉开锁屏固定住，计数一次；

    public static final String EVENT_CLICK_WHEN_FIXED = "clickWhenFixed";// 固定后，点击解锁计数一次；

    public static final String EVENT_DIRECT_UNLOCK = "directUnlock";// 未固定，直接解锁，计数一次；

    public static final String EVENT_GUESTURE_LOCK_ENABLED_DAILY = "guestureLockEnabledDaily";// 若用户开启了手势锁，每日上报一次；

    public static final String EVENT_GUESTURE_UNLOCK_SUCCESS_TIMES = "guestureUnLockSuccessTimes";// 手势锁成功解锁的次数；

    public static final String EVENT_GUESTURE_UNLOCK_FAIL_TIMES = "guestureUnLockFailTimes";// 手势锁成功解锁的次数；

    public static final String EVENT_CURRENT_THEME_DAILY = "currentThemeDaily";// 每日上报一次使用的主题事件

    public static final String EVENT_ACTIVE_DAILY = "activeDaily"; // 进入锁屏页，上报一次事件，作为统计活跃，每日一次；

    public static final String EVENT_TYPE_PLAIN_TEXT_JOKE = "plainTextJoke"; // dataType只计算开锁到固定解锁的时间

    public static final String EVENT_TYPE_MIX_NEWS = "mixNews"; // dataType只计算开锁到固定解锁的时间

    public static final String EVENT_TYPE_MIX_JOKE = "mixJoke"; // dataType只计算开锁到固定解锁的时间

    public static final String EVENT_TYPE_MIX_BAIDU = "mixBaidu"; // dataType只计算开锁到固定解锁的时间

    public static final String EVENT_TYPE_DEFAULT = "default"; // dataType只计算开锁到固定解锁的时间

    public static final String EVENT_PANDORA_SWITCH_OPEN_TIMES = "pandoraSwitchOpen"; // 打开/关闭锁屏开关，上报对应事件；

    public static final String EVENT_PANDORA_SWITCH_CLOSE_TIMES = "pandoraSwitchClose"; // 打开/关闭锁屏开关，上报对应事件；

    public static final String EVENT_WALLPAPER_BLUE_TIMES = "wallpaperBlue"; // 点击某个主题壁纸时上报一次事件，包括当前选中的壁纸信息；

    public static final String EVENT_WALLPAPER_TIFFANY_TIMES = "wallpaperTiffany";// 点击某个主题壁纸时上报一次事件，包括当前选中的壁纸信息；

    public static final String EVENT_WALLPAPER_JEAN_TIMES = "wallpaperJean"; // 点击某个主题壁纸时上报一次事件，包括当前选中的壁纸信息；

    public static final String EVENT_WALLPAPER_WOOD_GRAIN_TIMES = "wallpaperWoodGrain"; // 点击某个主题壁纸时上报一次事件，包括当前选中的壁纸信息；

    public static final String EVENT_GUIDE_PAGE_DURATION = "guidePageDuration"; // 计算引导页的总展示时间；

    /**
     * 统计 若用户开启了手势锁，每日上报一次
     * 
     * @param pandoraConfig
     * @param currentDate
     */
    public static void statisticalGuestureLockTime(PandoraConfig pandoraConfig, String currentDate) {
        String saveDate = pandoraConfig.getEventGuestureLockEnabledDailyString();
        if (!currentDate.equals(saveDate)) {
            if (pandoraConfig.isPandolaLockerOn()) {
                MobclickAgent.onEvent(HDApplication.getInstannce(),
                        UmengCustomEventManager.EVENT_GUESTURE_LOCK_ENABLED_DAILY);
                pandoraConfig.saveEventGuestureLockEnabledDaily(currentDate);
            }
        }
    }

    /**
     * 统计当前使用的主题,每日上报一次
     * 
     * @param pandoraConfig
     * @param currentDate
     */
    public static void statisticalUseTheme(PandoraConfig pandoraConfig, String currentDate) {
        String saveDate = pandoraConfig.getEventCurrentThemeDailyString();
        if (!currentDate.equals(saveDate)) {
            int themeId = pandoraConfig.getCurrentThemeIdForStatistical();
            String themeName = "";
            switch (themeId) {
                case ThemeManager.THEME_ID_BLUE:
                    themeName = "blue";
                    break;
                case ThemeManager.THEME_ID_TIFFANY:
                    themeName = "tiffany";
                    break;
                case ThemeManager.THEME_ID_JEAN:
                    themeName = "jean";
                    break;
                case ThemeManager.THEME_ID_WOOD_GRAIN:
                    themeName = "woodGrain";
                    break;

                default:
                    break;
            }
            if (themeId != -1) {
                Map<String, String> map_value = new HashMap<String, String>();
                map_value.put("themeName", themeName);
                MobclickAgent.onEvent(HDApplication.getInstannce(),
                        UmengCustomEventManager.EVENT_CURRENT_THEME_DAILY, map_value);
                pandoraConfig.saveEventCurrentThemeDaily(currentDate);
            }
        }
    }

    /**
     * 统计 进入锁屏页，上报一次事件，作为统计活跃，每日一次；
     * 
     * @param pandoraConfig
     * @param currentDate
     */
    public static void statisticalEntryLockTimes(PandoraConfig pandoraConfig, String currentDate) {
        String saveDate = pandoraConfig.getEventActiveDailyString();
        if (!currentDate.equals(saveDate)) {
            MobclickAgent.onEvent(HDApplication.getInstannce(),
                    UmengCustomEventManager.EVENT_ACTIVE_DAILY);
            pandoraConfig.saveEventActiveDaily(currentDate);
        }

    }

    /**
     * 统计手势锁成功解锁的次数
     */
    public static void statisticalGuestureUnLockSuccess() {
        MobclickAgent.onEvent(HDApplication.getInstannce(),
                UmengCustomEventManager.EVENT_GUESTURE_UNLOCK_SUCCESS_TIMES);
    }

    /**
     * 统计手势锁失败解锁的次数
     */
    public static void statisticalGuestureUnLockFail() {
        MobclickAgent.onEvent(HDApplication.getInstannce(),
                UmengCustomEventManager.EVENT_GUESTURE_UNLOCK_FAIL_TIMES);
    }

    /**
     * 统计未固定，直接解锁次数
     */
    public static void statisticalUnLockTimes() {
        MobclickAgent.onEvent(HDApplication.getInstannce(),
                UmengCustomEventManager.EVENT_DIRECT_UNLOCK);
    }

    /**
     * 统计固定的次数
     */
    public static void statisticalFixedTimes() {
        MobclickAgent.onEvent(HDApplication.getInstannce(),
                UmengCustomEventManager.EVENT_FIXED_TIMES);
    }

    /**
     * 统计固定之后解锁次数
     */
    public static void statisticalFixedUnLockTimes() {
        MobclickAgent.onEvent(HDApplication.getInstannce(),
                UmengCustomEventManager.EVENT_CLICK_WHEN_FIXED);
    }

    /**
     * 统计拉开锁屏到解锁的时间，及当前内容的dataType信息； 只计算开锁到固定解锁的时间
     */
    public static void statisticalLockTime(IPandoraBox mPandoraBox, int duration) {
        if (null == mPandoraBox || null == mPandoraBox.getData()) {
            return;
        }
        String dataType = String.valueOf(mPandoraBox.getData().getDataType());
        Map<String, String> map_value = new HashMap<String, String>();
        if (dataType.equals("TYPE_MIX_JOKE")) {
            map_value.put("dataType", dataType);
            MobclickAgent.onEventValue(HDApplication.getInstannce(),
                    UmengCustomEventManager.EVENT_TYPE_MIX_JOKE, map_value, duration);
        } else if (dataType.equals("TYPE_MIX_NEWS")) {
            map_value.put("dataType", dataType);
            MobclickAgent.onEventValue(HDApplication.getInstannce(),
                    UmengCustomEventManager.EVENT_TYPE_MIX_NEWS, map_value, duration);
        } else if (dataType.equals("TYPE_PLAIN_TEXT_JOKE")) {
            map_value.put("dataType", dataType);
            MobclickAgent.onEventValue(HDApplication.getInstannce(),
                    UmengCustomEventManager.EVENT_TYPE_PLAIN_TEXT_JOKE, map_value, duration);
        } else if (dataType.equals("TYPE_MIX_BAIDU")) {
            map_value.put("dataType", dataType);
            MobclickAgent.onEventValue(HDApplication.getInstannce(),
                    UmengCustomEventManager.EVENT_TYPE_MIX_BAIDU, map_value, duration);
        } else {
            map_value.put("dataType", dataType);
            MobclickAgent.onEventValue(HDApplication.getInstannce(),
                    UmengCustomEventManager.EVENT_TYPE_DEFAULT, map_value, duration);
        }

    }

    /**
     * 计算引导页的总展示时间
     */
    public static void statisticalGuideTime(int duration) {
        Map<String, String> map_value = new HashMap<String, String>();
        MobclickAgent.onEventValue(HDApplication.getInstannce(),
                UmengCustomEventManager.EVENT_GUIDE_PAGE_DURATION, map_value, duration);
    }

    /**
     * 点击某个主题壁纸时统计一次事件
     */
    public static void statisticalSelectTheme(int themeId) {
        switch (themeId) {
            case ThemeManager.THEME_ID_BLUE:
                MobclickAgent.onEvent(HDApplication.getInstannce(),
                        UmengCustomEventManager.EVENT_WALLPAPER_BLUE_TIMES);
                break;
            case ThemeManager.THEME_ID_TIFFANY:
                MobclickAgent.onEvent(HDApplication.getInstannce(),
                        UmengCustomEventManager.EVENT_WALLPAPER_TIFFANY_TIMES);
                break;
            case ThemeManager.THEME_ID_JEAN:
                MobclickAgent.onEvent(HDApplication.getInstannce(),
                        UmengCustomEventManager.EVENT_WALLPAPER_JEAN_TIMES);
                break;
            case ThemeManager.THEME_ID_WOOD_GRAIN:
                MobclickAgent.onEvent(HDApplication.getInstannce(),
                        EVENT_WALLPAPER_WOOD_GRAIN_TIMES);
                break;

            default:
                MobclickAgent.onEvent(HDApplication.getInstannce(),
                        UmengCustomEventManager.EVENT_WALLPAPER_BLUE_TIMES);
                break;
        }
    }

    /**
     * 统计打开锁屏开关次数
     */
    public static void statisticalPandoraSwitchOpenTimes() {
        MobclickAgent.onEvent(HDApplication.getInstannce(),
                UmengCustomEventManager.EVENT_PANDORA_SWITCH_OPEN_TIMES);
    }

    /**
     * 统计关闭锁屏开关次数
     */
    public static void statisticalPandoraSwitchCloseTimes() {
        MobclickAgent.onEvent(HDApplication.getInstannce(),
                UmengCustomEventManager.EVENT_PANDORA_SWITCH_CLOSE_TIMES);
    }
}
