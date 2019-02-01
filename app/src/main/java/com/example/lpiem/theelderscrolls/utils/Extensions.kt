package com.example.lpiem.theelderscrolls.utils

import android.graphics.Bitmap
import com.squareup.picasso.Transformation
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun Disposable.disposedBy(disposeBag: CompositeDisposable) {
    disposeBag.add(this)
}