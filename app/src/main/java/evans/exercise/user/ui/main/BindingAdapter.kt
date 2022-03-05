package evans.exercise.user.ui.main

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter

/**
 * Created by evans.lai on 2022/3/5.
 */
@BindingAdapter("avatar")
fun ImageView.setAvatar(bitmap: Bitmap?) {
    setImageBitmap(bitmap)
}