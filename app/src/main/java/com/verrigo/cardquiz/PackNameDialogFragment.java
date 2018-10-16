package com.verrigo.cardquiz;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class PackNameDialogFragment extends DialogFragment {


    public PackNameDialogFragment() {
    }


    public interface PackNameDialogListener {
        void onDialogPositiveButtonClick(DialogFragment dialogFragment, EditText editTextPackName);
    }

    private PackNameDialogListener listener;
    private EditText editTextPackName;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (PackNameDialogListener) context;
        } catch (Exception ex)  {

        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_pack_name, null);
        editTextPackName = view.findViewById(R.id.dialog_pack_name_edit_text);
        builder.setView(view)
                .setPositiveButton("Готово", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onDialogPositiveButtonClick(PackNameDialogFragment.this, editTextPackName);
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setTitle("Введите название пакета");
        return builder.create();
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_pack_name_dialog, container, false);
//    }

}
