/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mwq.map.dao;

import java.util.Iterator;
import java.util.Vector;


public class Dao extends BaseDao {

    private static final Dao dao;
    

    static {
        dao = new Dao();
    }

    public static Dao getInstance() {
        return dao;
    }

    //tb_map
    public String getMapName() {
        return selectOnlyValue("select name from tb_map where id=1").toString();
    }

    public boolean setMapName(String name) {
        return longHaul("update tb_map set name='" + name + "' where id=1");
    }

    //tb_sort
    public Vector selectChildSortByFatherId(int sortId) {
        return selectSomeNote("select * from tb_sort where father_id=" + sortId);
    }

    public int insertSort(int fatherId, String name) {
        Object maxId = super.selectOnlyValue("select max(id) from tb_sort");
        int id = (maxId == null ? 1 : (Integer) maxId + 1);
        super.longHaul("insert into tb_sort(id,father_id,name) values(" + id + "," + fatherId + ",'" + name + "')");
        return id;
    }

    public boolean updateSortNameById(int id, String newName) {
        return super.longHaul("update tb_sort set name='" + newName + "' where id=" + id);
    }

    public boolean deleteSortById(int id) {
        StringBuffer sb = new StringBuffer(" ( ");
        sb.append(id);
        Iterator it = selectAllChildSortId(id + "").iterator();
        while (it.hasNext()) {
            sb.append(", ");
            sb.append(it.next());
        }
        sb.append(" )");
        super.longHaul("delete from tb_sign where sort_id in" + sb.toString());
        return super.longHaul("delete from tb_sort where id in" + sb.toString());
    }

    //tb_sign
    public Vector selectClickSign(int x, int y) {
        return super.selectOnlyNote("select * from tb_sign where " + x + " between x-5 and x+5 and " + y + " between y-5 and y+5");
    }

    public Vector selectShowSigns(int x, int y, int width, int height, float scale) {
        int w = width / 2;
        int h = height / 2;
        int minX = x - w;
        int maxX = x + w;
        int minY = y - h;
        int maxY = y + h;
        return super.selectSomeNote("select * from tb_sign where x between " + minX + " and " + maxX + " and y between " + minY + " and " + maxY + " and scale <=" + scale);
    }

    public Vector<Vector> selectSimpleSign(String keyword) {
        return super.selectSomeNote("select * from tb_sign where title like '%" + keyword + "%' or remark like '%" + keyword + "%'");
    }

    public Vector<Vector> selectAppointSign(String keyword, String keywordArea, String date, String compare, String sort, String sortArea) {
        String sql = selectAdvancedSign(keyword, keywordArea, sort, sortArea);
        if (date.length() > 0) {
            if (sql.length() > 27) {
                sql += " and";
            }
            if (compare.equals("大于")) {
                sql += " date > '" + date + "'";
            } else if (compare.equals("小于")) {
                sql += " date < '" + date + "'";
            } else {//等于

                sql += " date = '" + date + "'";
            }
        }
        return super.selectSomeNote(sql);
    }

    public Vector<Vector> selectSpaceSign(String keyword, String keywordArea, String startDate, String endDate, String sort, String sortArea) {
        String sql = selectAdvancedSign(keyword, keywordArea, sort, sortArea);
        if (startDate.length() > 0) {
            if (sql.length() > 27) {
                sql += " and";
            }
            sql += " date >= '" + startDate + "' and date <= '" + endDate + "'";
        }
        return super.selectSomeNote(sql);
    }

    private String selectAdvancedSign(String keyword, String keywordArea, String sort, String sortArea) {
        String sql = "select * from tb_sign where";
        boolean isAdd = false;
        if (keyword.length() > 0) {
            isAdd = true;
            keyword = keyword.replace(' ', '%');
            if (keywordArea.equals("名称")) {
                sql += " title like '%" + keyword + "%'";
            } else if (keywordArea.equals("说明")) {
                sql += " remark like '%" + keyword + "%'";
            } else {//全部

                sql += " ( title like '%" + keyword + "%' or remark like '%" + keyword + "%' )";
            }
        }
        if (sort != null) {
            if (sortArea.equals("仅当前类")) {
                if (isAdd) {
                    sql += " and";
                } else {
                    isAdd = true;
                }
                sql += " sort_id = " + sort;
            } else {//包含子类

                if (isAdd) {
                    sql += " and";
                } else {
                    isAdd = true;
                }
                sql += " sort_id in ( " + sort;
                Iterator it = selectAllChildSortId(sort).iterator();
                while (it.hasNext()) {
                    sql += ", " + it.next();
                }
                sql += " )";
            }
        }
        return sql;
    }

    public Vector selectAllChildSortId(String fatherId) {
        Vector ids = new Vector(super.selectSomeValue("select id from tb_sort where father_id=" + fatherId));
        int index = 0;
        while (ids.size() != index) {
            int count = ids.size();
            for (int i = index; i < count; i++) {
                ids.addAll(super.selectSomeValue("select id from tb_sort where father_id=" + ids.get(i)));
            }
            index = count;
        }
        return ids;
    }

    public int insertSign(String sort_id, int x, int y, String title, int show, float scale, String date, String remark) {
        Object maxId = super.selectOnlyValue("select max(id) from tb_sign");
        int id = (maxId == null ? 1 : (Integer) maxId + 1);
        super.longHaul("insert into tb_sign values(" + id + "," + sort_id + "," + x + "," + y + ",'" + title + "'," + show + "," + scale + ",'" + date + "','" + remark + "')");
        return id;
    }

    public void updateSign(int x, int y, String title, String sortId, int show, String date, String remark) {
        super.longHaul("update tb_sign set title='" + title + "', sort_id=" + sortId + ", show=" + show + ", remark='" + remark + "' where " + x + " between x-5 and x+5 and " + y + " between y-5 and y+5");
    }

    public boolean deleteClickSign(int x, int y) {
        return super.longHaul("delete from tb_sign where " + x + " between x-5 and x+5 and " + y + " between y-5 and y+5");
    }

    //v_sign_sort 
    public Vector selectClickSignV(int x, int y) {
        return super.selectOnlyNote("select * from v_sign_sort where " + x + " between x-5 and x+5 and " + y + " between y-5 and y+5");
    }

    void a() {
        Vector signs = super.selectSomeNote("select * from tb_sign");
        for (int i = 0; i < signs.size(); i++) {
            Vector sign = (Vector) signs.get(i);
            for (int j = 0; j < sign.size(); j++) {
                System.out.println(sign.get(j));
            }
            System.out.println("--------------------------");
        }
    }

    public static void main(String[] a) {
        new Dao().a();
    }
}
