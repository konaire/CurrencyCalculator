package com.konaire.revolut.util

import android.content.Context
import android.widget.Toast

/**
 * Created by Evgeny Eliseyev on 24/04/2018.
 */
fun Context.toast(message: CharSequence) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()