package com.dragon.ta.fragment;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dragon.ta.R;
import com.dragon.ta.fragment.HomeFragment.OnListFragmentInteractionListener;
import com.dragon.ta.manager.DataManager;
import com.dragon.ta.model.Good;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;


public class HomeItemRecyclerViewAdapter extends RecyclerView.Adapter<HomeItemRecyclerViewAdapter.ViewHolder> {

    private List<Good> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context mContext;

    public HomeItemRecyclerViewAdapter(Context context, List<Good> items, OnListFragmentInteractionListener listener) {
        mContext = context;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_fragment_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(String.valueOf(mValues.get(position).getId()));
        holder.mIdView.setVisibility(View.GONE);
        holder.mThumbView.setImageURI(Uri.parse(mValues.get(position).getThumb().getFileUrl(mContext)));
        holder.mContentView.setText(mValues.get(position).getName());
        holder.mPriceView.setText(mContext.getString(R.string.symbol) + mValues.get(position).getPrice());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues != null ? mValues.size() : 0;
    }

    public void updateData(){
        mValues = DataManager.getInstance(mContext).getData();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final SimpleDraweeView mThumbView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final TextView mPriceView;
        public Good mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mThumbView = (SimpleDraweeView)view.findViewById(R.id.good_thumb);
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            mPriceView = (TextView)view.findViewById(R.id.price);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
