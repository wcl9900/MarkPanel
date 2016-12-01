package com.wcl.markpanel;

/**
 * <p>Describe:标记点增删状态监听器
 * <p>Author:王春龙
 * <p>CreateTime:2016/11/15
 */
public interface OnMarkerChangeListener {
    void OnMarkerAdd(EditMarker marker);
    void OnMarkerRemove(EditMarker marker);
    void OnMarkerChangeListener(EditMarker marker);
}
