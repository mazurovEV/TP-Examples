package tp.emazurov.lesson1.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import tp.emazurov.lesson1.R;

/**
 * Created by r.kildiev on 11.09.2014.
 */
public class DetailFragment extends Fragment {

    public static String NEWS_NO = "news_no";

    public static DetailFragment getInstance(int news_no) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(NEWS_NO, news_no);
        detailFragment.setArguments(bundle);
        return detailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.news_detail_fargment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        TextView newsDetailTextview = (TextView) view.findViewById(R.id.news_detail_textview);
        Button understoodButton = (Button) view.findViewById(R.id.understood);
        understoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(DetailFragment.this).commit();
            }
        });
        newsDetailTextview.setText("Детализация новости номер " + getArguments().getInt(NEWS_NO));
    }
}
