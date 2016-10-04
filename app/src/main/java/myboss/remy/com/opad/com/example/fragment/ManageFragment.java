package myboss.remy.com.opad.com.example.fragment;


import android.app.ExpandableListActivity;


import android.content.ContentValues;
import android.content.Context;
import android.content.Loader;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;


import android.support.v4.content.CursorLoader;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.SimpleCursorTreeAdapter;
import android.widget.TextView;

import myboss.remy.com.opad.Controller;
import myboss.remy.com.opad.R;

import myboss.remy.com.opad.com.example.activity.MainActivity;
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

    public static final int CATEGORY_ID = 1;
    public static final int NOTE_ID = 2;

    private Category category;
    private Note note;
    private EditText categoryEdit;
    private static int DIALOG_RENAME_CATEGORY = 2;
    public static final int DIALOG_NEW_CATEGORY = 1;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        category = new Category(getActivity());



    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listActivity = new ExpandableListActivity();
        categoryEdit = new EditText(getContext());
        getLoaderManager().initLoader(CATEGORY_ID, null,this);
        getLoaderManager().initLoader(NOTE_ID, null,this);

        note = new Note();

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

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) item.getMenuInfo();
        int type = ExpandableListView.getPackedPositionType(info.packedPosition);

        if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            note.reset();
            note.setId(info.id);
        }else if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
            category.reset();
            category.setId(info.id);
        }

        boolean refresh = false;
        switch (item.getItemId()) {
            case R.id.menu_edit:
                if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    BrowseFragment.openNote(info.id, getContext());
                }else if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    ContentValues values = new ContentValues();
                    category.load(MyContentProvider.CONTENT_URI_CATEGORY);
                    categoryEdit.setText(category.getName());
                    MainActivity activity = new MainActivity();

                    activity.onClick(getView());

                }
                break;
            case R.id.menu_delete:
                if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    note.delete(MyContentProvider.CONTENT_URI_CATEGORY);
                }else if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    category.delete(MyContentProvider.CONTENT_URI_CATEGORY);
                }

                refresh = true;
                break;
        }

        if (refresh) {
            SimpleCursorTreeAdapter adapter = (SimpleCursorTreeAdapter)listView.getExpandableListAdapter();
            adapter.getCursor().requery();
            adapter.notifyDataSetChanged();
        }

        return true;

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
            case NOTE_ID:
                uri = MyContentProvider.CONTENT_URI_NOTE;
                //columns = new String[]{Note.COLUMN_ID, Note.COLUMN_LOCKED, Note.COLUMN_TITLE, Note.COLUMN_TYPE, Note.COLUMN_MODIFIEDTIME, Note.COLUMN_CREATEDTIME};
                break;

            case CATEGORY_ID:
                uri = MyContentProvider.CONTENT_URI_CATEGORY;
                //columns = new String[]{Category.COLUMN_ID, Category.COLUMN_LOCKED, Category.COLUMN_NAME};
                break;

            default:
                throw new  IllegalArgumentException("Unknown URI");
        }
        return new CursorLoader(getActivity(), uri, null, null, null, null);
        //return null;
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }

    private class CategoryTreeAdapter extends SimpleCursorTreeAdapter {

        public CategoryTreeAdapter(Context context, Cursor cursor) {
            super(context, cursor, R.layout.row_category, new String[]{Category.COLUMN_NAME}, new int[]{android.R.id.text1}, R.layout.row_category_note, new String[]{Note.COLUMN_TITLE}, new int[]{android.R.id.text1});
        }

        @Override
        protected Cursor getChildrenCursor(Cursor groupCursor) {
            Cursor c = Note.list(MyContentProvider.CONTENT_URI_CATEGORY, groupCursor.getString(groupCursor.getColumnIndex(Category.COLUMN_ID)));
            return c;
        }

        @Override
        protected void bindGroupView(View view, Context context, Cursor cursor, boolean isExpanded) {
            super.bindGroupView(view, context, cursor, isExpanded);

            boolean showLock = cursor.getInt(cursor.getColumnIndex(Note.COLUMN_LOCKED)) == 1 ? true : false;

            ((TextView)view.findViewById(android.R.id.text1)).setCompoundDrawablesWithIntrinsicBounds(
                    showLock ? R.drawable.folder_locked : R.drawable.folder,
                    0,
                    0,
                    0);
        }

        @Override
        protected void bindChildView(View view, Context context, Cursor cursor, boolean isLastChild) {
            super.bindChildView(view, context, cursor, isLastChild);

            boolean showLock = getCursor().getInt(cursor.getColumnIndex(Note.COLUMN_LOCKED)) == 1 ? true : false;
            // if group is locked then show lock on a child
            showLock = showLock || cursor.getInt(cursor.getColumnIndex(Note.COLUMN_LOCKED)) == 1 ? true : false;

            ((TextView)view.findViewById(android.R.id.text1)).setCompoundDrawablesWithIntrinsicBounds(
                    showLock ? R.drawable.file_locked : R.drawable.file,
                    0,
                    0,
                    0);
        }
    }


}
