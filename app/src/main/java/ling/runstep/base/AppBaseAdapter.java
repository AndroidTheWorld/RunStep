package ling.runstep.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Jalyn on 2016/1/19.
 */
public abstract class AppBaseAdapter<T> extends BaseAdapter {
    protected List<T> datas;

    public void addDatas(List<T> datas) {
        if (this.datas == null) return;
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);

}
