package com.example.mynetworkcall



import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build

import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

class InstallerImpl : InstallerInterface {
    @SuppressLint("QueryPermissionsNeeded")
    override fun installApk(context: Context, uri: Uri,onError: (String) -> Unit) {
        try {


//            if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(Intent.ACTION_VIEW).also {
                it.setDataAndType(uri, "application/vnd.android.package-archive")
                it.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                it.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)
//                    it.putExtra(Intent.EXTRA_RETURN_RESULT, true)
                it.putExtra(
                    Intent.EXTRA_INSTALLER_PACKAGE_NAME, context.applicationInfo.packageName
                )
            }
            //            intent.setDataAndType(uri, "application/vnd.android.package-archive")
//            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
//            intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
//            intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
//            intent.putExtra(Intent.EXTRA_INSTALLER_PACKAGE_NAME, context.applicationInfo.packageName)
//        startActivity(intent)
            val activities = context.packageManager.queryIntentActivities(intent, 0)
            if (activities.isNotEmpty()) {
                context.startActivity(intent)
            } else {
                onError("Install failed, Please Provide Proper URI")
            }
//            } else {
//                onError("External Package Permission Not Granted")
//            }


        } catch (e: Exception) {
            onError("Installation failed")
        }
    }

    override fun installApk(
        context: Context,
        fileUri: String,
        packageName: String,
        onError: (String) -> Unit
    ) {
        try {

        }catch (e:Exception){

            onError("Installation failed")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun unInstallApk(context: Context, packageName: String,onError: (String) -> Unit) {
        val packageInstallPermission = Manifest.permission.REQUEST_DELETE_PACKAGES
        val permissionStatus = ContextCompat.checkSelfPermission(context, packageInstallPermission)

        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            try {
                val intent = Intent(Intent.ACTION_DELETE).apply {
                    this.data = Uri.parse(
                        "package:$packageName"
                    )
                    this.putExtra(Intent.EXTRA_RETURN_RESULT, true)
                }
//        intent.putExtra(Intent.EXTRA_RETURN_RESULT, true)
//        startActivityForResult(intent, REQUEST_UNINSTALL)
                context.startActivity(intent)
            } catch (e: Exception) {
                onError("unable to uninstall the package")
            }
        } else {
            onError("Application don't have package uninstallation permission")
        }
    }


    override fun isAppInstalled(context: Context, packageName: String): Boolean {
        val packageManager = context.packageManager
        return try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}