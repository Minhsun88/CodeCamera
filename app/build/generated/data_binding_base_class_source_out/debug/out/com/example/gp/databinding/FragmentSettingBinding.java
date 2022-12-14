// Generated by view binder compiler. Do not edit!
package com.example.gp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.gp.R;
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentSettingBinding implements ViewBinding {
  @NonNull
  private final FrameLayout rootView;

  @NonNull
  public final TextView GroupName;

  @NonNull
  public final CircleImageView ImageView;

  @NonNull
  public final Button SignOut;

  @NonNull
  public final TextView loginId;

  @NonNull
  public final TextView nickName;

  private FragmentSettingBinding(@NonNull FrameLayout rootView, @NonNull TextView GroupName,
      @NonNull CircleImageView ImageView, @NonNull Button SignOut, @NonNull TextView loginId,
      @NonNull TextView nickName) {
    this.rootView = rootView;
    this.GroupName = GroupName;
    this.ImageView = ImageView;
    this.SignOut = SignOut;
    this.loginId = loginId;
    this.nickName = nickName;
  }

  @Override
  @NonNull
  public FrameLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentSettingBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentSettingBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_setting, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentSettingBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.GroupName;
      TextView GroupName = ViewBindings.findChildViewById(rootView, id);
      if (GroupName == null) {
        break missingId;
      }

      id = R.id.ImageView;
      CircleImageView ImageView = ViewBindings.findChildViewById(rootView, id);
      if (ImageView == null) {
        break missingId;
      }

      id = R.id.SignOut;
      Button SignOut = ViewBindings.findChildViewById(rootView, id);
      if (SignOut == null) {
        break missingId;
      }

      id = R.id.loginId;
      TextView loginId = ViewBindings.findChildViewById(rootView, id);
      if (loginId == null) {
        break missingId;
      }

      id = R.id.nickName;
      TextView nickName = ViewBindings.findChildViewById(rootView, id);
      if (nickName == null) {
        break missingId;
      }

      return new FragmentSettingBinding((FrameLayout) rootView, GroupName, ImageView, SignOut,
          loginId, nickName);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
