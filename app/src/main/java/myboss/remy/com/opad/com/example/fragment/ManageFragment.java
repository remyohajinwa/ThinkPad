package myboss.remy.com.opad.com.example.fragment;


import android.app.ExpandableListActivity;


import android.content.Loader;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v4.content.CursorLoader;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import myboss.remy.com.opad.Controller;
import myboss.remy.com.opad.R;

import myboss.remy.com.opad.com.example.database.Category;
import myboss.remy.com.opad.com.example.database.MyContentProvider;
import myboss.remy.com.opad.com.example.database.Note;

/**
 * Created by Chimere on 9/22/2016.
 */
public class ManageFragment extends Fragment implements  android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

    private ImageButton newButton;
    private ExpandableListActivity listActivity;
    private ExpandableListView listView;

    private Category category;
    private Note note;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(-1, null,this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listActivity = new ExpandableListActivity();

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == android.R.id.list) {
            ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;
            int type = ExpandableListView.getPackedPositionType(info.packedPosition);

            getActivity().getMenuInflater().inflate(R.menu.contextmenu_explore, menu);
            menu.setHeaderTitle("Choose an Option");

            if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                note.reset();
                note.setId(info.id);
                note.load(MyContentProvider.CONTENT_URI_NOTE);

                category.reset();
                category.setId(note.getCategoryId());
                category.load(MyContentProvider.CONTENT_URI_CATEGORY);

                if (category.isLocked()) {
                    menu.removeItem(R.id.menu_lock);
                    menu.removeItem(R.id.menu_lock);
                }else if (note.isLocked())
                    menu.removeItem(R.id.menu_lock);
                else
                    menu.removeItem(R.id.menu_unlock);
            }else if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                category.reset();
                category.setId(info.id);
                category.load(MyContentProvider.CONTENT_URI_CATEGORY);

                if (category.isLocked())
                    menu.removeItem(R.id.menu_lock);
                else
                    menu.removeItem(R.id.menu_unlock);

                if (info.id == Controller.PUBLIC_CATEGORYID) {
                    menu.removeItem(R.id.menu_delete);
                    menu.removeItem(R.id.menu_edit);
                }
            }

        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_manage, container, false);
        newButton = (ImageButton)view.findViewById(R.id.new_btn);
         listView = (ExpandableListView)view.findViewById(android.R.id.list);

        getActivity().registerForContextMenu(view);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return true;
            }
        });

        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });


        return view;
    }


    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri ;
        String[] columns = null;

        switch (id) {
            case MyContentProvider.NOTE_ID:
                uri = MyContentProvider.CONTENT_URI_NOTE;
                columns = new String[]{Note.COLUMN_ID, Note.COLUMN_LOCKED, Note.COLUMN_TITLE, Note.COLUMN_TYPE, Note.COLUMN_MODIFIEDTIME, Note.COLUMN_CREATEDTIME};
                break;
            case MyContentProvider.ATTACHMENT_ID:
                uri = MyContentProvider.CONTENT_URI_ATTACHMENT;
                break;
            case MyContentProvider.CATEGORY_ID:
                uri = MyContentProvider.CONTENT_URI_CATEGORY;
                columns = new String[]{Category.COLUMN_ID, Category.COLUMN_LOCKED, Category.COLUMN_NAME};
                break;
            case MyContentProvider.CHECKITEM_ID:
                uri = MyContentProvider.CONTENT_URI_CHECKITEM;
                break;
            default:
                throw new  IllegalArgumentException("Unknown URI");
        }
        //return new CursorLoader(getActivity(), MyContentProvider.CONTENT_URI_CATEGORY, null, null, null, null);
        return null;
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }


}
