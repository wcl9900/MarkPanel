package com.wcl.markpanel;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Describe:标记点编辑视图
 * <p>Author:王春龙
 * <p>CreateTime:2016/11/14
 */
public class MarkPanelView extends FrameLayout {
    private FrameLayout panelLayout;
    private FrameLayout markerLayout;
    private ImageView mapView;

    private List<EditMarker> markerList;

    private OnMarkerClickListener onMarkerClickListener;
    private OnMarkerChangeListener onMarkerChangeListener;
    private OnMarkerFireAddListener onMarkerFireAddListener;

    private boolean isEditing = false;

    private int minDragDistance = 4;//dp
    private int maxMarkDistance = 10;//dp

    public MarkPanelView(Context context) {
        super(context);
        init();
    }

    public MarkPanelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MarkPanelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        panelLayout = new FrameLayout(getContext());
        FrameLayout.LayoutParams allLayoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        allLayoutParams.gravity = Gravity.CENTER;
        addView(panelLayout, allLayoutParams);

        mapView = new ImageView(getContext());
        mapView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mapView.setAdjustViewBounds(true);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;
        panelLayout.addView(mapView, layoutParams);

        markerLayout = new FrameLayout(getContext());
        FrameLayout.LayoutParams layoutParamsMarker = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParamsMarker.gravity = Gravity.CENTER;
        panelLayout.addView(markerLayout, layoutParamsMarker);

        markerList = new ArrayList<>();

        markerLayout.setOnTouchListener(markLayoutOnTouchListener);
        markerLayout.setOnClickListener(markLayoutClickListener);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int markerWidth, markerHeight;

        if(mapView.getDrawable() == null) {
            markerWidth = mapView.getMeasuredWidth();
            markerHeight = mapView.getMeasuredHeight();
        }
        else {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) mapView.getDrawable();
            if(bitmapDrawable.getBitmap() == null){
                markerWidth = mapView.getMeasuredWidth();
                markerHeight = mapView.getMeasuredHeight();
            }
            else {
                float imageWidth = bitmapDrawable.getBitmap().getWidth();
                float imageHeight = bitmapDrawable.getBitmap().getHeight();
                float panelWidth = panelLayout.getMeasuredWidth();
                float panelHeight = panelLayout.getMeasuredHeight();
                int aimWidth, aimHeight;
                if (panelWidth / panelHeight > imageWidth / imageHeight) {
                    aimHeight = (int) panelHeight;
                    aimWidth = (int) (aimHeight * imageWidth / imageHeight);
                } else {
                    aimWidth = (int) panelWidth;
                    aimHeight = (int) (aimWidth * imageHeight / imageWidth);
                }

                mapView.measure(MeasureSpec.makeMeasureSpec(aimWidth, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(aimHeight, MeasureSpec.EXACTLY));

                markerWidth = aimWidth;
                markerHeight = aimHeight;
            }
        }
        markerLayout.measure(MeasureSpec.makeMeasureSpec(markerWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(markerHeight, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int childCount = markerLayout.getChildCount();
        for(int index = 0; index < childCount; index++){
            View markerView = markerLayout.getChildAt(index);
            if(markerView.getVisibility() != VISIBLE) continue;

            MarkerLayoutParams markerLayoutParams = (MarkerLayoutParams) markerView.getLayoutParams();
            View markerTouchView = (View) markerView.getTag(R.id.marker_view_touch);
            int offSetX, offSetY;
            if(markerView != markerTouchView){
                offSetX = (int) (-markerTouchView.getLeft() + markerTouchView.getWidth() * markerLayoutParams.anchorX);
                offSetY = (int) (-markerTouchView.getTop() + markerTouchView.getHeight() * markerLayoutParams.anchorY);
            }
            else {
                offSetX = (int) (markerTouchView.getWidth() * markerLayoutParams.anchorX);
                offSetY = (int) (markerTouchView.getHeight() * markerLayoutParams.anchorY);
            }

            int markerLeft = (int) (markerLayout.getWidth() * markerLayoutParams.getPercentX() + offSetX);
            int markerTop = (int) (markerLayout.getHeight() * markerLayoutParams.getPercentY() + offSetY);

            EditMarker editMarker = (EditMarker) markerView.getTag(R.id.marker_edit);
            editMarker.setContentSize(
                    markerTouchView.getMeasuredWidth() / (float)(markerLayout.getMeasuredWidth()),
                    markerTouchView.getMeasuredHeight() / (float)(markerLayout.getMeasuredHeight()));
            if(onMarkerChangeListener != null){
                onMarkerChangeListener.OnMarkerChangeListener(editMarker);
            }

            markerView.layout(markerLeft, markerTop,
                    markerLeft + markerView.getMeasuredWidth(),
                    markerTop + markerView.getMeasuredHeight());
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(ev.getPointerCount() > 1) return true;
        return super.onInterceptTouchEvent(ev);
    }

    private OnClickListener markLayoutClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isEditing) {
                showRemoveView(false);
            }
        }
    };

    private float markStartX, markStartY;
    private boolean enableMark = true;
    private OnTouchListener markLayoutOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(isEditing || event.getPointerCount() > 1) return false;
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                markStartX = event.getX();
                markStartY = event.getY();
                enableMark = true;
                return false;
            }
            else if(event.getAction() == MotionEvent.ACTION_MOVE){
                enableMark = Math.sqrt(Math.pow(Math.abs(event.getX() - markStartX), 2) + Math.pow(Math.abs(event.getY() - markStartY), 2))
                        < dpToPx(getResources(), maxMarkDistance);
                return false;
            }
            else if(event.getAction() == MotionEvent.ACTION_UP && enableMark){
                if(onMarkerFireAddListener != null){
                    float percentX = event.getX() / markerLayout.getWidth();
                    float percentY = event.getY() / markerLayout.getHeight();
                    onMarkerFireAddListener.OnMarkerFireAdd(MarkPanelView.this, percentX, percentY);
                }
            }
            return false;
        }
    } ;

    public void setOnMarkerFireAddListener(OnMarkerFireAddListener onMarkerFireAddListener) {
        this.onMarkerFireAddListener = onMarkerFireAddListener;
    }

    public void setOnMarkerClickListener(OnMarkerClickListener onMarkerClickListener) {
        this.onMarkerClickListener = onMarkerClickListener;
    }

    public void setOnMarkerChangeListener(OnMarkerChangeListener onMarkerChangeListener) {
        this.onMarkerChangeListener = onMarkerChangeListener;
    }

    public List<EditMarker> getMarkerList() {
        return markerList;
    }

    /**
     * 设定Map图片
     * @param path 磁盘文件路径
     */
    public void setMapImage(String path){
        Bitmap bmp = BitmapFactory.decodeFile(path);
        setMapImage(bmp);
    }

    /**
     * 设定Map图片
     * @param bitmap
     */
    public void setMapImage(Bitmap bitmap){
        mapView.setImageBitmap(bitmap);
    }

    /**
     * 添加标记点
     * @param marker
     */
    public void addMarker(final EditMarker marker){
        if(!markerList.contains(marker)){
            markerList.add(marker);
            MarkerLayoutParams layoutParams = new MarkerLayoutParams(marker.getPercentX(), marker.getPercentY(),
                    marker.getWidth(), marker.getHeight(),
                    marker.getAnchorX(), marker.getAnchorY());
            markerLayout.addView(marker.getMarkerView(), layoutParams);

            View markerTouchView = marker.getTouchView();
            markerTouchView.setOnTouchListener(touchViewTouchListener);
            markerTouchView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onMarkerClickListener != null && marker.isClickEnable()){
                        onMarkerClickListener.OnMarkerClick(marker);
                    }
                }
            });

            if(marker.getMarkerView().findViewById(R.id.marker_view_remove) != null){
                marker.getMarkerView().findViewById(R.id.marker_view_remove).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeMarker(marker);
                    }
                });
            }
            if(onMarkerChangeListener != null){
                onMarkerChangeListener.OnMarkerAdd(marker);
            }
        }
    }

    OnLongClickListener onLongClickListener = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            showRemoveView(true);
            return true;
        }
    };
    /**
     * 移除标记点
     * @param marker
     */
    public void removeMarker(EditMarker marker){
        if(!markerList.contains(marker)) return;
        markerLayout.removeView(marker.getMarkerView());
        markerList.remove(marker);
        if(onMarkerChangeListener != null){
            onMarkerChangeListener.OnMarkerRemove(marker);
        }
        if(markerList.size() == 0){
            isEditing = false;
        }
    }

    private void showRemoveView(boolean show) {
        isEditing = show;
        Object removeMarkerView;
        for (EditMarker marker : markerList){
            removeMarkerView = marker.getMarkerView().getTag(R.id.marker_view_remove);
            if(removeMarkerView != null){
                ((View)removeMarkerView).setVisibility(show ? VISIBLE : GONE);
            }
        }
    }

    float startX, startY;
    PointF prePoint = new PointF();
    private OnTouchListener touchViewTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getPointerCount() > 1){
                return false;
            }
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                View markerView = (View) v.getTag(R.id.marker_view);
                ((EditMarker)markerView.getTag(R.id.marker_edit)).setClickEnable(true);
                ((View)markerView.getTag(R.id.marker_view_touch)).setLongClickable(true);
                ((View)markerView.getTag(R.id.marker_view_touch)).setOnLongClickListener(onLongClickListener);

                //把要拖动的marker置顶
                markerLayout.removeView(markerView);
                markerLayout.addView(markerView);

                prePoint.set(event.getRawX(), event.getRawY());
                startX = event.getRawX();
                startY = event.getRawY();
                return false;
            }
            else if(event.getAction() == MotionEvent.ACTION_MOVE){
                if(Math.sqrt(Math.pow(Math.abs(event.getRawX() - startX),2) + Math.pow(Math.abs(event.getRawY() - startY),2))
                        < dpToPx(getResources(), minDragDistance)){
                    return true;
                }
                View markerView = (View) v.getTag(R.id.marker_view);
                EditMarker editMarker = (EditMarker) markerView.getTag(R.id.marker_edit);
                editMarker.setClickEnable(false);
                ((View)markerView.getTag(R.id.marker_view_touch)).setLongClickable(false);
                ((View)markerView.getTag(R.id.marker_view_touch)).setOnLongClickListener(null);

                MarkerLayoutParams markerLayoutParams = (MarkerLayoutParams) markerView.getLayoutParams();
                markerLayoutParams.percentX += (event.getRawX() - prePoint.x) / (float) markerLayout.getWidth();
                markerLayoutParams.percentY += (event.getRawY() - prePoint.y) / (float) markerLayout.getHeight();
                if(markerLayoutParams.percentX < 0){
                    markerLayoutParams.percentX = 0;
                }
                else if(markerLayoutParams.percentX > 1){
                    markerLayoutParams.percentX = 1;
                }
                if(markerLayoutParams.percentY <= 0){
                    markerLayoutParams.percentY = 0;
                }
                else if(markerLayoutParams.percentY > 1){
                    markerLayoutParams.percentY = 1;
                }
                markerView.requestLayout();
                prePoint.set(event.getRawX(), event.getRawY());

                editMarker.setPercentX(markerLayoutParams.percentX);
                editMarker.setPercentY(markerLayoutParams.percentY);
                if(onMarkerChangeListener != null){
                    onMarkerChangeListener.OnMarkerChangeListener(editMarker);
                }
                return true;
            }
            else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL){
                prePoint.set(0, 0);
                startX = 0;
                startY = 0;
                return false;
            }
            return false;
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        release();
        super.onDetachedFromWindow();
    }

    /**
     * 释放资源
     */
    private void release(){
        mapView.setImageBitmap(null);
        markerLayout.removeAllViews();
        markerList.clear();
    }

    private int dpToPx(Resources res, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                res.getDisplayMetrics());
    }

    private class MarkerLayoutParams extends FrameLayout.LayoutParams {
        float percentX;
        float percentY;
        Float anchorX;
        Float anchorY;
        public MarkerLayoutParams(float percentX, float percentY, int width, int height, Float anchorX, Float anchorY) {
            super(width, height);
            this.percentX = percentX;
            this.percentY = percentY;
            this.anchorX = anchorX;
            this.anchorY = anchorY;
        }

        public void setPercentX(float percentX) {
            this.percentX = percentX;
        }

        public void setPercentY(float percentY) {
            this.percentY = percentY;
        }

        public float getPercentX() {
            return percentX;
        }

        public float getPercentY() {
            return percentY;
        }

        public Float getAnchorX() {
            return anchorX;
        }

        public void setAnchorX(Float anchorX) {
            this.anchorX = anchorX;
        }

        public Float getAnchorY() {
            return anchorY;
        }

        public void setAnchorY(Float anchorY) {
            this.anchorY = anchorY;
        }
    }
}
