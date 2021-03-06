package ru.p3tr0vich.calibration;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

public class DividerItemDecorationFueling extends DividerItemDecorationBase {

    private int mFooterType;

    DividerItemDecorationFueling(Context context) {
        super(context);
        mFooterType = -1;
    }

    public void setFooterType(int footerType) {
        mFooterType = footerType;
    }

    @Override
    public boolean shouldDrawDivider(RecyclerView parent, int childViewIndex) {
        return mFooterType <= -1 || childViewIndex != parent.getChildCount() - 2 ||
                parent.getAdapter().getItemViewType(
                        parent.getChildAdapterPosition(parent.getChildAt(childViewIndex + 1))) !=
                        mFooterType;
    }
}