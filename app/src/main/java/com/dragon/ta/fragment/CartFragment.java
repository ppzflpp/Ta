package com.dragon.ta.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dragon.ta.R;
import com.dragon.ta.activity.PayActivity;
import com.dragon.ta.manager.CartManager;
import com.dragon.ta.model.CartGood;
import com.dragon.ta.model.Good;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {

    private Context mContext;
    private RecyclerView mRecycleView;
    private TextView mEmptyView;
    private TextView mPayView;
    private TextView mCartGoodsMoneyView;
    private TextView mCartGoodCount;

    private CartManager.CartManagerStateChangeListener mCartManagerStateChangeListener = new CartManager.CartManagerStateChangeListener() {
        @Override
        public void onCartManagerStateChangeListener() {
            updateViews(mContext);
        }
    };


    private OnFragmentInteractionListener mListener;

    public CartFragment() {
        // Required empty public constructor
    }

    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cart_fragment_list, container, false);
        mEmptyView = (TextView) view.findViewById(R.id.cart_empty_view);
        mCartGoodsMoneyView = (TextView)view.findViewById(R.id.cart_goods_all_money);

        mPayView  = (TextView)view.findViewById(R.id.cart_goods_pay);
        mPayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay();
            }
        });

        mRecycleView = (RecyclerView) view.findViewById(R.id.cart_list);

        mRecycleView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mRecycleView.setAdapter(
                new CartItemRecyclerViewAdapter(view.getContext(), CartManager.getInstance().getCartGoods(), mListener, mEmptyView));

        updateViews(view.getContext());
        return view;
    }

    private void updateViews(Context context){
        mCartGoodsMoneyView.setText(context.getString(R.string.all_money) + CartManager.getInstance().getAllCheckedMoney());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        mContext = context;
        CartManager.getInstance().registerCartManagerStateChangeListener(mCartManagerStateChangeListener);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        CartManager.getInstance().unRegisterCartManagerStateChangeListener(mCartManagerStateChangeListener);
    }

    private void pay(){
        Intent intent = new Intent(getActivity(), PayActivity.class);
        getActivity().startActivity(intent);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(CartGood good);
    }
}
