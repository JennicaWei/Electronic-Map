/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mwq.map.mwing;

import com.mwq.map.dao.Dao;
import com.mwq.map.tool.InstancePool;
import com.mwq.map.tool.MapProcessor;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.JLabel;

public class BigMapLabel extends JLabel {

    private static final Dao dao = Dao.getInstance();
    private static final Vector<Integer> ids = new Vector();// 标记主键集
    private static final Vector<Integer> xs = new Vector();// X轴坐标集
    private static final Vector<Integer> ys = new Vector();// Y轴坐标集
    private static final Vector<String> texts = new Vector();// 标记文本集
    private static final Vector<Boolean> shows = new Vector();// 是否显示集
    private static MapProcessor mapProcessor;// 地图处理器对象
    private static int operateIndex = -1;// 操作标记点索引
    private static int stressId = -1;// 着重标记点主键

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.ORANGE);// 设置笔触颜色

        for (int i = 0; i < texts.size(); i++) {
            int x = xs.get(i);// 标记点的X轴坐标

            int y = ys.get(i);// 标记点的Y轴坐标

            if (ids.get(i) == stressId) {// 为着重标记点

                g.setColor(Color.RED);// 修改笔触颜色

                g.fillOval(x - 5, y - 5, 10, 10);// 绘制标记点

                if (shows.get(i)) {// 如果显示标记文本

                    g.drawString(texts.get(i), x + 8, y + 5);// 绘制标记文本

                }
                g.setColor(Color.ORANGE);// 还原笔触颜色

            } else {// 为普通标记点

                g.fillOval(x - 5, y - 5, 10, 10);// 绘制标记点

                if (shows.get(i)) {// 如果显示标记文本

                    g.drawString(texts.get(i), x + 8, y + 5);// 绘制标记文本

                }
            }
        }
    }

    public static void addSign(int id, int x, int y, String sign, boolean show) {
        ids.add(id);
        xs.add(x);
        ys.add(y);
        texts.add(sign);
        shows.add(show);
    }

    public static void updateSign(int x, int y, String sign, boolean show) {
        xs.set(operateIndex, x);
        ys.set(operateIndex, y);
        texts.set(operateIndex, sign);
        shows.set(operateIndex, show);
    }

    public void removeSign() {
        ids.remove(operateIndex);
        xs.remove(operateIndex);
        ys.remove(operateIndex);
        texts.remove(operateIndex);
        shows.remove(operateIndex);
    }

    public void setStress(int id) {
        stressId = id;// 设置着重标记点主键

    }

public boolean isEnteredSign(int x, int y) {
    int xDistance, yDistance;// 距标记点的距离

    for (int i = 0; i < texts.size(); i++) {
        xDistance = Math.abs(x - xs.get(i));// 计算水平距离

        yDistance = Math.abs(y - ys.get(i));// 计算垂直距离

        if (xDistance < 5 && yDistance < 5) {// 光标在该标记点之上

            operateIndex = i;// 保存该标记点的索引

            mapProcessor.rightClick(xs.get(i), ys.get(i));// 保存该标记点的坐标

            return true;// 光标在该标记点之上

        }
    }
    mapProcessor.rightClick(x, y);// 保存该标记点的坐标

    return false;// 光标未在任何标记点之上

}

    @Override
    public void setIcon(Icon icon) {
        if (mapProcessor == null) {// 尚未获得地图处理器对象

            if (InstancePool.getMapProcessor() != null) {// 如果地图处理器对象已经创建

                mapProcessor = InstancePool.getMapProcessor();// 获得地图处理器对象

                refreshSigns();// 刷新标记

            }
        } else {// 已经获得地图处理器对象

            refreshSigns();// 刷新标记

        }
        super.setIcon(icon);
    }

    private void refreshSigns() {
        ids.clear();
        xs.clear();
        ys.clear();
        texts.clear();
        shows.clear();

        // 查询符合绘制条件的标记
        Vector signs = dao.selectShowSigns(mapProcessor.getShowCenterX(), mapProcessor.getShowCenterY(),
                mapProcessor.getCutMapWidth(), mapProcessor.getCutMapHeight(), mapProcessor.getScale());

        // 计算相对原点的坐标
        int originX = mapProcessor.getShowCenterX() - mapProcessor.getCutMapWidth() / 2;
        int originY = mapProcessor.getShowCenterY() - mapProcessor.getCutMapHeight() / 2;
        float scale = mapProcessor.getScale();
        for (int i = 0; i < signs.size(); i++) {// 遍历标记点集合

            Vector sign = (Vector) signs.get(i);// 获得标记点

            // 计算相对坐标
            int x = (Integer) sign.get(2) - originX;
            int y = (Integer) sign.get(3) - originY;
            if (scale != 0) {// 进行了缩放

                if (scale > 0) {// 放大

                    x = (int) (x * scale);
                    y = (int) (y * scale);
                } else {// 缩小

                    x = (int) (x / -scale);
                    y = (int) (y / -scale);
                }
            }
            addSign((Integer) sign.get(0), x, y, sign.get(4).toString(), ((Integer) sign.get(5) == 1 ? true : false));
        }
    }
}
