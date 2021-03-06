package com.dragon.ta.fragment;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.dragon.ta.R;
import com.dragon.ta.fragment.HomeFragment.OnListFragmentInteractionListener;
import com.dragon.ta.manager.CartManager;
import com.dragon.ta.model.CartGood;
import com.dragon.ta.model.Good;
import com.facebook.drawee.view.SimpleDraweeView;

import org.w3c.dom.Text;

import java.util.List;


public class CartItemRecyclerViewAdapter extends RecyclerView.Adapter<CartItemRecyclerViewAdapter.ViewHolder> {

    private final List<CartGood> mValues;
    private final CartFragment.OnFragmentInteractionListener mListener;
    private Context mContext;
    private TextView mEmptyView;

    public CartItemRecyclerViewAdapter(Context context, List<CartGood> items, CartFragment.OnFragmentInteractionListener listener,TextView emptyView) {
        mContext = context;
        mValues = items;
        mListener = listener;
        mEmptyView = emptyView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_fragment_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mCartGood = mValues.get(position);
        holder.mCheckedView.setChecked(holder.mCartGood.isChecked());
        holder.mCheckedView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateCart(holder.mCartGood,b);
            }
        });
        holder.mThumbView.setImageURI(Uri.parse(mValues.get(position).getGood().getThumb().getFileUrl(mContext)));
        holder.mContentView.setText(mValues.get(position).getGood().getName());
        holder.mPriceView.setText(mContext.getString(R.string.symbol) + mValues.get(position).getGood().getPrice());
        holder.mCountView.setText(mContext.getString(R.string.cart_good_count) + mValues.get(position).getCount());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onFragmentInteraction(holder.mCartGood);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        int size = mValues.size();

        if(size == 0){
            mEmptyView.setVisibility(View.VISIBLE);
        }else{
            mEmptyView.setVisibility(View.GONE);
        }

        return size;
    }

    public void updateCart(CartGood cartGood,boolean checked){
        CartManager.getInstance().updateCartGoodsState(cartGood,checked);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final CheckBox mCheckedView;
        public final SimpleDraweeView mThumbView;
        public final TextView mCountView;
        public final TextView mContentView;
        public final TextView mPriceView;
        public CartGood mCartGood;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mCheckedView = (CheckBox)view.findViewById(R.id.cart_list_item_checked_view);
            mThumbView = (SimpleDraweeView)view.findViewById(R.id.good_thumb);
            mCountView = (TextView) view.findViewById(R.id.cart_good_count);
            mContentView = (TextView) view.findViewById(R.id.content);
            mPriceView = (TextView)view.findViewById(R.id.price);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
