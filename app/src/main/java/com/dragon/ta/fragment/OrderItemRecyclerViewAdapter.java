package com.dragon.ta.fragment;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.dragon.ta.R;
import com.dragon.ta.manager.CartManager;
import com.dragon.ta.model.CartGood;
import com.dragon.ta.model.Order;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;


public class OrderItemRecyclerViewAdapter extends RecyclerView.Adapter<OrderItemRecyclerViewAdapter.ViewHolder> {

    private List<Order> mValues;
    private Context mContext;
    private TextView mEmptyView;

    public OrderItemRecyclerViewAdapter(Context context, List<Order> orders,TextView emptyView) {
        mContext = context;
        mValues = orders;
        mEmptyView = emptyView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_order_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mOrderNameView.setText(mValues.get(position).getOrderName());
        holder.mOrderInfoView.setText(mValues.get(position).getOrderInfo());
        holder.mOrderPriceView.setText(mContext.getString(R.string.symbol) + mValues.get(position).getOrderPrice());
    }

    @Override
    public int getItemCount() {
        int size = mValues != null ? mValues.size() : 0;

        if(size == 0){
            mEmptyView.setVisibility(View.VISIBLE);
        }else{
            mEmptyView.setVisibility(View.GONE);
        }

        return size;
    }

    public void updateList(List<Order> list){
        this.mValues = list;
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mOrderNameView;
        public final TextView mOrderInfoView;
        public final TextView mOrderPriceView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mOrderNameView = (TextView) view.findViewById(R.id.activity_order_name);
            mOrderInfoView = (TextView) view.findViewById(R.id.activity_order_info);
            mOrderPriceView = (TextView)view.findViewById(R.id.activity_order_price);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mOrderNameView.getText() + "'";
        }
    }
}
