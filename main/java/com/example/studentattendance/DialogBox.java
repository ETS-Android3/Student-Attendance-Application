package com.example.studentattendance;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DialogBox extends DialogFragment {
    public static final String CLASS_ADD_DIALOG = "addClass";
    public static final String CLASS_UPDATE_DIALOG = "updateClass";
    public static final String STUDENT_ADD_DIALOG = "addStudent";
    public static final String STUDENT_UPDATE_DIALOG = "updateStudent";

    private OnClickListener listener;
    private int rollno;
    private String name;

    public DialogBox(int rollno, String name) {

        this.rollno = rollno;
        this.name = name;
    }

    public DialogBox() {
    }

    public interface OnClickListener{
        void onClick(String text1, String text2);
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = null;

        if(getTag().equals(CLASS_ADD_DIALOG))
            dialog=getAddClassDialog();
        if(getTag().equals(CLASS_UPDATE_DIALOG))
            dialog=getUpdateClassDialog();
        if(getTag().equals(STUDENT_ADD_DIALOG))
            dialog=getAddStudentDialog();
        if(getTag().equals(STUDENT_UPDATE_DIALOG))
            dialog=getUpdateStudentDialog();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    private Dialog getUpdateStudentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_box_layout,null);
        builder.setView(view1);

        TextView titleTv = view1.findViewById(R.id.titletvid);
        titleTv.setText("Update Student");

        final EditText rollnoEt = view1.findViewById(R.id.classetid);
        final EditText nameEt = view1.findViewById(R.id.subjectetid);

        rollnoEt.setHint("Roll");
        nameEt.setHint("Name");

        Button cancel = view1.findViewById(R.id.cancelbtnid);
        Button add = view1.findViewById(R.id.addbtnid);
        add.setText("update");
        rollnoEt.setText(rollno+"");
        rollnoEt.setEnabled(false);
        nameEt.setText(name);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rollno = rollnoEt.getText().toString();
                String name = nameEt.getText().toString();
                listener.onClick(rollno, name);
                dismiss();
            }
        });
        return builder.create();
    }

    private Dialog getUpdateClassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_box_layout,null);
        builder.setView(view1);

        TextView titleTv = view1.findViewById(R.id.titletvid);
        titleTv.setText("Update Class");

        final EditText classEt = view1.findViewById(R.id.classetid);
        final EditText subjectEt = view1.findViewById(R.id.subjectetid);

        classEt.setHint("Section");
        subjectEt.setHint("Subject");

        Button cancel = view1.findViewById(R.id.cancelbtnid);
        Button add = view1.findViewById(R.id.addbtnid);
        add.setText("Update");

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String className = classEt.getText().toString();
                String subjectName = subjectEt.getText().toString();
                listener.onClick(className, subjectName);
                dismiss();
            }
        });
        return builder.create();
    }

    private Dialog getAddStudentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_box_layout,null);
        builder.setView(view1);

        TextView titleTv = view1.findViewById(R.id.titletvid);
        titleTv.setText("Add New Student");

        final EditText rollnoEt = view1.findViewById(R.id.classetid);
        final EditText nameEt = view1.findViewById(R.id.subjectetid);

        rollnoEt.setHint("Roll no");
        nameEt.setHint("Name");

        Button cancel = view1.findViewById(R.id.cancelbtnid);
        Button add = view1.findViewById(R.id.addbtnid);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rollno = rollnoEt.getText().toString();
                String name = nameEt.getText().toString();
                rollnoEt.setText(String.valueOf(Integer.parseInt(rollno)+1));
                nameEt.setText("");
                listener.onClick(rollno, name);
            }
        });
        return builder.create();
    }

    private Dialog getAddClassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_box_layout,null);
        builder.setView(view1);

        TextView titleTv = view1.findViewById(R.id.titletvid);
        titleTv.setText("Add New Class");

        final EditText classEt = view1.findViewById(R.id.classetid);
        final EditText subjectEt = view1.findViewById(R.id.subjectetid);

        classEt.setHint("Section");
        subjectEt.setHint("Subject");

        Button cancel = view1.findViewById(R.id.cancelbtnid);
        Button add = view1.findViewById(R.id.addbtnid);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String className = classEt.getText().toString();
                String subjectName = subjectEt.getText().toString();
                listener.onClick(className, subjectName);
                dismiss();
            }
        });
        return builder.create();
    }
}
