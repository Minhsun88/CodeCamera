// Generated by view binder compiler. Do not edit!
package com.example.gp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.gp.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentAddNoteBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final TextView TextViewDate;

  @NonNull
  public final TextView TextViewTime;

  @NonNull
  public final Button btnSetDate;

  @NonNull
  public final Button btnSetTime;

  @NonNull
  public final Button buttonSave;

  @NonNull
  public final EditText edText;

  @NonNull
  public final EditText edTitle;

  @NonNull
  public final TextView textViewTitle;

  private FragmentAddNoteBinding(@NonNull LinearLayout rootView, @NonNull TextView TextViewDate,
      @NonNull TextView TextViewTime, @NonNull Button btnSetDate, @NonNull Button btnSetTime,
      @NonNull Button buttonSave, @NonNull EditText edText, @NonNull EditText edTitle,
      @NonNull TextView textViewTitle) {
    this.rootView = rootView;
    this.TextViewDate = TextViewDate;
    this.TextViewTime = TextViewTime;
    this.btnSetDate = btnSetDate;
    this.btnSetTime = btnSetTime;
    this.buttonSave = buttonSave;
    this.edText = edText;
    this.edTitle = edTitle;
    this.textViewTitle = textViewTitle;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentAddNoteBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentAddNoteBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_add_note, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentAddNoteBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.TextViewDate;
      TextView TextViewDate = ViewBindings.findChildViewById(rootView, id);
      if (TextViewDate == null) {
        break missingId;
      }

      id = R.id.TextViewTime;
      TextView TextViewTime = ViewBindings.findChildViewById(rootView, id);
      if (TextViewTime == null) {
        break missingId;
      }

      id = R.id.btnSetDate;
      Button btnSetDate = ViewBindings.findChildViewById(rootView, id);
      if (btnSetDate == null) {
        break missingId;
      }

      id = R.id.btnSetTime;
      Button btnSetTime = ViewBindings.findChildViewById(rootView, id);
      if (btnSetTime == null) {
        break missingId;
      }

      id = R.id.buttonSave;
      Button buttonSave = ViewBindings.findChildViewById(rootView, id);
      if (buttonSave == null) {
        break missingId;
      }

      id = R.id.edText;
      EditText edText = ViewBindings.findChildViewById(rootView, id);
      if (edText == null) {
        break missingId;
      }

      id = R.id.edTitle;
      EditText edTitle = ViewBindings.findChildViewById(rootView, id);
      if (edTitle == null) {
        break missingId;
      }

      id = R.id.textViewTitle;
      TextView textViewTitle = ViewBindings.findChildViewById(rootView, id);
      if (textViewTitle == null) {
        break missingId;
      }

      return new FragmentAddNoteBinding((LinearLayout) rootView, TextViewDate, TextViewTime,
          btnSetDate, btnSetTime, buttonSave, edText, edTitle, textViewTitle);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
