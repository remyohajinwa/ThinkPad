package myboss.remy.com.opad.com.example.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.DateFormat;
import android.icu.util.Freezable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import myboss.remy.com.opad.R;
import myboss.remy.com.opad.com.example.database.MyContentProvider;
import myboss.remy.com.opad.com.example.database.Note;

/**
 * Created by Chimere on 9/20/2016.
 */
public class BrowseFragment extends Fragment  {
    private ListView noteList;
    private TextView emptyView;
    private ImageButton newButton;

    private SQLiteDatabase db;

    private AlertDialog newButtonDialog;
    private AlertDialog sortButtonDialog;

    //private DialogInterface.OnClickListener


  //  private final DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_main, container, false);
    }
   /* private final Calendar calendar = Calendar.getInstance();

    private DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (dialog == newButtonDialog ) {
                Intent intent = new Intent();
                switch (which) {
                    case 0:
                        intent.setClass(BrowseFragment.this, GeneralActivity.class);
                        break;
                    case 1:
                        intent.setClass(BrowseFragment.this, CheckListActivity.class);
                        break;
                    case 2:
                        intent.setClass(BrowseFragment.this, SnapshotActivity.class);
                        break;
                }
                startActivity(intent);
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = new CursorLoader(getActivity(), MyContentProvider.CONTENT_URI_NOTE,null,null,null, null);

        return cursorLoader;

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }







    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }*/

    public static void openNote(long id, Context context) {
        Note note = new Note(id);
        note.load(MyContentProvider.CONTENT_URI_NOTE);

        String type = note.getType();
        if (Note.GENERAL.equals(type)) {

        }
    }

}
