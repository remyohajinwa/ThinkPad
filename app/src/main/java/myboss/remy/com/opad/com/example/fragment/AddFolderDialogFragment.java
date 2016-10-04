package myboss.remy.com.opad.com.example.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.SimpleCursorTreeAdapter;

import myboss.remy.com.opad.Controller;
import myboss.remy.com.opad.R;
import myboss.remy.com.opad.com.example.database.Category;
import myboss.remy.com.opad.com.example.database.MyContentProvider;

/**
 * Created by Chimere on 9/23/2016.
 */
public class AddFolderDialogFragment extends DialogFragment {
    private EditText categoryNameEdit;
    private Category category;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        category = new Category(getActivity());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //int title = getArguments().getInt("New Folder");
        categoryNameEdit = new EditText(getActivity());
        return new AlertDialog.Builder(getActivity())
                .setTitle("New Folder")
                .setView(categoryNameEdit)
                .setCancelable(false)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ContentValues contentValues = new ContentValues();
                        if (!TextUtils.isEmpty(categoryNameEdit.getText())) {
                            contentValues.clear();
                            category.setName(categoryNameEdit.getText().toString());
                            contentValues.put(MyContentProvider.TABLE_CATEGORY, category.getName());

                            Controller.LASTCREATED_CATEGORYID = category.persist(MyContentProvider.CONTENT_URI_CATEGORY, contentValues);


                        }
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();

    }




}
