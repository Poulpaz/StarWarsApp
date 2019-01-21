package com.example.lpiem.theelderscrolls.utils

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun Disposable.disposedBy(disposeBag: CompositeDisposable) {
    disposeBag.add(this)
}