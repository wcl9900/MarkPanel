package com.wcl.markpanel.demo;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.wcl.markpanel.EditMarker;
import com.wcl.markpanel.MarkPanelView;
import com.wcl.markpanel.OnMarkerChangeListener;
import com.wcl.markpanel.OnMarkerClickListener;
import com.wcl.markpanel.OnMarkerFireAddListener;

/**
 * 王春龙
 * 标记面板demo
 */
public class MainActivity extends Activity {

    MarkPanelView markPanelView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        markPanelView = (MarkPanelView) findViewById(R.id.panelview);

        markPanelView.showImage(BitmapFactory.decodeResource(getResources(), R.drawable.test_image));

        markPanelView.setOnMarkerFireAddListener(onMarkerFireAddListener);
        markPanelView.setOnMarkerChangeListener(onMarkerChangeListener);
        markPanelView.setOnMarkerClickListener(onMarkerClickListener);
    }

    private OnMarkerFireAddListener onMarkerFireAddListener = new OnMarkerFireAddListener() {
        @Override
        public void OnMarkerFireAdd(MarkPanelView markPanelView, float percentX, float percentY) {
            EditMarker editMarker = new EditMarker(LayoutInflater.from(MainActivity.this).inflate(R.layout.marker_layout, null), percentX, percentY);
            editMarker.setData("percentX:"+percentX + " percentY:"+percentY);
            markPanelView.addMarker(editMarker);
        }
    };

    private OnMarkerChangeListener onMarkerChangeListener = new OnMarkerChangeListener() {
        @Override
        public void OnMarkerAdd(EditMarker marker) {
            Toast.makeText(MainActivity.this, "添加了一个标记点", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void OnMarkerRemove(EditMarker marker) {
            Toast.makeText(MainActivity.this, "移除了一个标记点", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void OnMarkerChangeListener(EditMarker marker) {

        }
    };

    private OnMarkerClickListener onMarkerClickListener = new OnMarkerClickListener() {
        @Override
        public void OnMarkerClick(EditMarker marker) {
            Toast.makeText(MainActivity.this, "点击了一个标记点" + "percentX:"+marker.getPercentX() + " percentY:"+ marker.getPercentY(), Toast.LENGTH_SHORT).show();
        }
    };
}
