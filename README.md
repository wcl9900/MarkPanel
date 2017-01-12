# MarkPanel

    标记面板，可向视图面板中加入标记点，可监听标记点的添加、移除、更新等回调处理

    效果图
    ![image](https://github.com/wcl9900/MarkPanel/blob/5324ff3d79acc651b41a41ee5ddc2b74e0a38706/markpanel.gif)

    使用方式

    1.点击触发添加标记点
    markPanelView.setOnMarkerFireAddListener(new OnMarkerFireAddListener() {
            @Override
            public void OnMarkerFireAdd(MarkPanelView markPanelView, float percentX, float percentY) {
                EditMarker editMarker = new EditMarker(LayoutInflater.from(MainActivity.this).inflate(R.layout.marker_layout, null), percentX, percentY);
                editMarker.setData("percentX:"+percentX + " percentY:"+percentY);
                markPanelView.addMarker(editMarker);
            }
        });


    2.标记点位置等信息变动监听
    
    markPanelView.setOnMarkerChangeListener(new OnMarkerChangeListener() {
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
      });
      
      
      3.标记点点击监听
      
        markPanelView.setOnMarkerClickListener(new OnMarkerClickListener() {
        @Override
        public void OnMarkerClick(EditMarker marker) {
            Toast.makeText(MainActivity.this, "点击了一个标记点" + "percentX:"+marker.getPercentX() + " percentY:"+ marker.getPercentY(), Toast.LENGTH_SHORT).show();
        }
    });
