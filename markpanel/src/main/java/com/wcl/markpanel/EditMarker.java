package com.wcl.markpanel;

import android.view.View;
import android.widget.FrameLayout;

/**
 * <p>Describe:marker标记点
 * <p>Author:王春龙
 * <p>CreateTime:2016/11/14
 */
public class EditMarker{
    private boolean clickEnable = false;
    private float percentX;
    private float percentY;

    private Integer width;
    private Integer height;

    private Float anchorX;
    private Float anchorY;

    private View markerView;

    private Object data;

    public EditMarker(View markerView, float percentX, float percentY){
        this(markerView, percentX, percentY, -0.5f, -1f);
    }

    public EditMarker(View markerView, float percentX, float percentY, int width, int height){
        this(markerView, percentX, percentY, width, height, -0.5f, -1f);
    }

    public EditMarker(View markerView, float percentX, float percentY, Float anchorX, Float anchorY) {
        this(markerView, percentX, percentY, FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, anchorX, anchorY);
    }

    public EditMarker(View markerView, float percentX, float percentY, Integer width, Integer height, Float anchorX, Float anchorY) {
        this.percentX = percentX;
        this.percentY = percentY;
        this.width = width;
        this.height = height;
        this.anchorX = anchorX;
        this.anchorY = anchorY;
        this.markerView = markerView;

        View markerChildView = markerView.findViewById(R.id.marker_child_id);
        View markerRemoveView = markerView.findViewById(R.id.marker_remove_id);
        if(markerChildView != null){
            markerView.setTag(R.id.marker_child_id, markerChildView);
            markerChildView.setTag(R.id.marker_parent_id, markerView);
        }
        else {
            markerView.setTag(R.id.marker_child_id, markerView);
            markerView.setTag(R.id.marker_parent_id, markerView);
        }
        if(markerRemoveView != null){
            markerView.setTag(R.id.marker_remove_id, markerRemoveView);
        }
        markerView.setTag(R.id.marker_id, this);
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public float getPercentX() {
        return percentX;
    }

    public boolean isClickEnable() {
        return clickEnable;
    }

    public void setClickEnable(boolean clickEnable) {
        this.clickEnable = clickEnable;
    }

    public float getPercentY() {
        return percentY;
    }

    public Float getAnchorX() {
        return anchorX;
    }

    public Float getAnchorY() {
        return anchorY;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public View getMarkerView() {
        return markerView;
    }

    public void setPercentX(float percentX) {
        this.percentX = percentX;
    }

    public void setPercentY(float percentY) {
        this.percentY = percentY;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public void setAnchorX(Float anchorX) {
        this.anchorX = anchorX;
    }

    public void setAnchorY(Float anchorY) {
        this.anchorY = anchorY;
    }
}
