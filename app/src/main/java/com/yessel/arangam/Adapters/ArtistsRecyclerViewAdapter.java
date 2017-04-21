package com.yessel.arangam.Adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.yessel.arangam.ArtistsGroupActivity;
import com.yessel.arangam.R;
import com.yessel.arangam.model.Artists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.myinnos.alphabetsindexfastscrollrecycler.utilities_fs.StringMatcher;

/**
 * Created by yasar on 3/3/17.
 */

public class ArtistsRecyclerViewAdapter extends RecyclerView.Adapter<ArtistsRecyclerViewAdapter.ViewHolder> implements SectionIndexer {

    private ArrayList<Artists> mDataset;
    private String mSections = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public ArtistsRecyclerViewAdapter(ArrayList<Artists> myDataset) {
        this.mDataset = myDataset;
    }


    public void filter(ArrayList<Artists> filterlist) {
        mDataset = filterlist;
        notifyDataSetChanged();
    }

    public void setFilter(List<Artists> filteredModelList) {
        mDataset = new ArrayList<>();
        mDataset.addAll(filteredModelList);
        notifyDataSetChanged();
    }

    public void upDateList(ArrayList<Artists> myDataset) {
        mDataset = myDataset;

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mDataset == null)
            return 0;
        return mDataset.size();
    }

    @Override
    public ArtistsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTextView.setText(mDataset.get(position).getName());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(v.getContext(), ArtistsGroupActivity.class);
                in.putExtra("artist", mDataset.get(position));
                v.getContext().startActivity(in);
            }
        });
    }

    @Override
    public int getPositionForSection(int section) {
        // If there is no item for current section, previous section will be selected
        for (int i = section; i >= 0; i--) {
            for (int j = 0; j < getItemCount(); j++) {
                if (i == 0) {
                    // For numeric section
                    for (int k = 0; k <= 9; k++) {
                        if (StringMatcher.match(String.valueOf(mDataset.get(j).getName().charAt(0)), String.valueOf(k)))
                            return j;
                    }
                } else {
                    if (StringMatcher.match(String.valueOf(mDataset.get(j).getName().charAt(0)), String.valueOf(mSections.charAt(i))))
                        return j;
                }
            }
        }
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    @Override
    public Object[] getSections() {
        String[] sections = new String[mSections.length()];
        for (int i = 0; i < mSections.length(); i++)
            sections[i] = String.valueOf(mSections.charAt(i));
        return sections;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            mTextView = (TextView) itemView.findViewById(R.id.tv_alphabet);

        }
    }

}