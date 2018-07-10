package com.wcl.markpanel.demo;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wcl.markpanel.EditMarker;
import com.wcl.markpanel.MarkPanelView;
import com.wcl.markpanel.OnMarkerChangeListener;
import com.wcl.markpanel.OnMarkerClickListener;
import com.wcl.markpanel.OnMarkerFireAddListener;

public class MultiActivity extends Activity {

    private MarkPanelView markPanelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi);
        markPanelView = findViewById(R.id.markerView);
        markPanelView.setMapImage(BitmapFactory.decodeResource(getResources(), R.drawable.test_image_2));

        markPanelView.setOnMarkerFireAddListener(onMarkerFireAddListener);
        markPanelView.setOnMarkerClickListener(onMarkerClickListener);
        markPanelView.setOnMarkerChangeListener(onMarkerChangeListener);
    }

    int type = 0;
    OnMarkerFireAddListener onMarkerFireAddListener = new OnMarkerFireAddListener() {
        @Override
        public void OnMarkerFireAdd(MarkPanelView markPanelView, float percentX, float percentY) {
            View markerView ;
            if((type++)%2 == 0){
                markerView = LayoutInflater.from(MultiActivity.this).inflate(R.layout.marker_1, null);
            }
            else {
                markerView = LayoutInflater.from(MultiActivity.this).inflate(R.layout.marker_2, null);
            }
            EditMarker editMarker = new EditMarker(markerView, percentX, percentY, -0.5f, -0.5f);
            TextView textView = editMarker.getMarkerView().findViewById(R.id.tv_info);
            editMarker.setTouchView(textView);
            markerView.findViewById(R.id.btn_edt).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MultiActivity.this, "编辑1", Toast.LENGTH_SHORT).show();
                }
            });
            markerView.findViewById(R.id.btn_edt_2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MultiActivity.this, "编辑2", Toast.LENGTH_SHORT).show();
                }
            });
            markPanelView.addMarker(editMarker);
        }
    };

    OnMarkerClickListener onMarkerClickListener= new OnMarkerClickListener() {
        @Override
        public void OnMarkerClick(EditMarker marker) {
            TextView textView = (TextView) marker.getTouchView();
            textView.append("信息信息信息");
        }
    };
    OnMarkerChangeListener onMarkerChangeListener = new OnMarkerChangeListener() {
        @Override
        public void OnMarkerAdd(EditMarker marker) {

        }

        @Override
        public void OnMarkerRemove(EditMarker marker) {

        }

        @Override
        public void OnMarkerChangeListener(EditMarker marker) {

        }
    };
}
