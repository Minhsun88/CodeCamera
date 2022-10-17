// Generated by view binder compiler. Do not edit!
package com.example.gp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.gp.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentSelectGroupBinding implements ViewBinding {
  @NonNull
  private final FrameLayout rootView;

  @NonNull
  public final Button AddGroup;

  @NonNull
  public final LinearLayout layout;

  private FragmentSelectGroupBinding(@NonNull FrameLayout rootView, @NonNull Button AddGroup,
      @NonNull LinearLayout layout) {
    this.rootView = rootView;
    this.AddGroup = AddGroup;
    this.layout = layout;
  }

  @Override
  @NonNull
  public FrameLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentSelectGroupBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentSelectGroupBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_select_group, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentSelectGroupBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.AddGroup;
      Button AddGroup = ViewBindings.findChildViewById(rootView, id);
      if (AddGroup == null) {
        break missingId;
      }

      id = R.id.layout;
      LinearLayout layout = ViewBindings.findChildViewById(rootView, id);
      if (layout == null) {
        break missingId;
      }

      return new FragmentSelectGroupBinding((FrameLayout) rootView, AddGroup, layout);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}