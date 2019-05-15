package planet_obcapp.com.obc_kyvapp.Helper;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by planet on 3/30/17.
 */

public class ListAndGirdShow {

    private static final String CNAME = ListAndGirdShow.class.getSimpleName();

    public static void sethight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static void getGridViewHeight(GridView gridView) {
            ListAdapter myListAdapter = gridView.getAdapter();

            if (myListAdapter == null) {
                return;
            }
            int totalHeight = 0;
            for (int size = 0; size < myListAdapter.getCount(); size++) {
                View listItem = myListAdapter.getView(size, null, gridView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = gridView.getLayoutParams();

            int gridViewMeasuredHeight = gridView.getMeasuredHeight();
            if (gridViewMeasuredHeight != 0) {
                gridViewMeasuredHeight = 0;
            }

            params.height = totalHeight + (gridViewMeasuredHeight * (myListAdapter.getCount() - 1));
            gridView.setLayoutParams(params);
            Log.i("height of listItem:", String.valueOf(totalHeight));
        }



}
