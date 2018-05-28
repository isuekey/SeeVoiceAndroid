package com.futhark.android.seevoice.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.futhark.android.seevoice.R;
import com.futhark.android.seevoice.base.BaseFragment;
import com.futhark.android.seevoice.base.BaseFragmentActivity;
import com.futhark.android.seevoice.controller.adapter.HomeActionGridAdapter;
import com.futhark.android.seevoice.model.domain.ItemElement;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 首页的内容
 * Created by liuhr on 06/04/2017.
 */

public class HomePageFragment extends BaseFragment {
    @BindView(R.id.home_action_grid_container)
    GridView actionContainer;
    private HomeActionGridAdapter gridAdapter;
    private ArrayList<ItemElement> itemElementArrayList = new ArrayList<>();


    private void initItemElementArrayList(){
        itemElementArrayList.clear();
        itemElementArrayList.add(new ItemElement(R.mipmap.ic_machine_bike, R.string.label_record));
        itemElementArrayList.add(new ItemElement(R.mipmap.ic_try_voice, R.string.label_exercise_supervise));
        itemElementArrayList.add(new ItemElement(R.mipmap.ic_eat_melon, R.string.label_exercise_examine));
        itemElementArrayList.add(new ItemElement(R.mipmap.ic_any_try, R.string.label_self));
        itemElementArrayList.add(new ItemElement(R.mipmap.ic_voice_mountain, R.string.label_about));
//        itemElementArrayList.add(new ItemElement(R.mipmap.ic_launcher, R.string.label_donate));
        itemElementArrayList.add(new ItemElement(R.mipmap.ic_history_voice, R.string.label_declaration));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_home_page, container, false);
        ButterKnife.bind(this, fragmentView);
        actionContainer.setOnItemClickListener(onItemClickListener);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gridAdapter = new HomeActionGridAdapter(this.getActivity());
        actionContainer.setAdapter(gridAdapter);
        initItemElementArrayList();
        gridAdapter.addAll(itemElementArrayList);
        gridAdapter.notifyDataSetChanged();
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ItemElement action = gridAdapter.getItem(position);
            Intent intent = new Intent(getActivity(), BaseFragmentActivity.class);
            switch (action.getText()){
                case R.string.label_record:
                    intent.putExtra(BaseFragmentActivity.Companion.getDUTY_FATE_FRAGMENT_INTENT(), SpecificationListFragment.class.getName());
                    break;
                case R.string.label_exercise_supervise:
                    intent.putExtra(BaseFragmentActivity.Companion.getDUTY_FATE_FRAGMENT_INTENT(), SupervisingListFragment.class.getName());
                    break;
                case R.string.label_exercise_examine:
                    intent.putExtra(BaseFragmentActivity.Companion.getDUTY_FATE_FRAGMENT_INTENT(), MeFragment.class.getName());
                    break;
                case R.string.label_self:
                    intent.putExtra(BaseFragmentActivity.Companion.getDUTY_FATE_FRAGMENT_INTENT(), ExercisingFragment.class.getName());
                    break;
                case R.string.label_about:
                    intent.putExtra(BaseFragmentActivity.Companion.getDUTY_FATE_FRAGMENT_INTENT(), ExercisingFragment.class.getName());
                    break;
                case R.string.label_donate:
                    intent.putExtra(BaseFragmentActivity.Companion.getDUTY_FATE_FRAGMENT_INTENT(), ExercisingFragment.class.getName());
                    break;
                case R.string.label_declaration:
                    intent.putExtra(BaseFragmentActivity.Companion.getDUTY_FATE_FRAGMENT_INTENT(), ExercisingFragment.class.getName());
                    break;
            }
            startActivity(intent);
        }
    };

}
