// Generated by view binder compiler. Do not edit!
package com.example.gp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public final class ItemViewBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final TextView MyText;

  @NonNull
  public final TextView MyTitle;

  @NonNull
  public final TextView Time;

  private ItemViewBinding(@NonNull LinearLayout rootView, @NonNull TextView MyText,
      @NonNull TextView MyTitle, @NonNull TextView Time) {
    this.rootView = rootView;
    this.MyText = MyText;
    this.MyTitle = MyTitle;
    this.Time = Time;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ItemViewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ItemViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.item_view, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ItemViewBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.MyText;
      TextView MyText = ViewBindings.findChildViewById(rootView, id);
      if (MyText == null) {
        break missingId;
      }

      id = R.id.MyTitle;
      TextView MyTitle = ViewBindings.findChildViewById(rootView, id);
      if (MyTitle == null) {
        break missingId;
      }

      id = R.id.Time;
      TextView Time = ViewBindings.findChildViewById(rootView, id);
      if (Time == null) {
        break missingId;
      }

      return new ItemViewBinding((LinearLayout) rootView, MyText, MyTitle, Time);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
