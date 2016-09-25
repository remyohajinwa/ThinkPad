package myboss.remy.com.opad.com.example.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

import myboss.remy.com.opad.R;

/**
 * Created by Chimere on 9/23/2016.
 */
public class AddFolderDialogFragment extends DialogFragment {
    private EditText categoryNameEdit;



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

                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();

    }


}
