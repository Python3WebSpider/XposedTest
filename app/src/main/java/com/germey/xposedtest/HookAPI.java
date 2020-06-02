package com.germey.xposedtest;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookAPI implements IXposedHookLoadPackage {

    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {

        if (loadPackageParam.packageName.equals("com.goldze.mvvmhabit")) {
            XposedBridge.log("Hooked com.goldze.mvvmhabit Package");
            Class clazz = loadPackageParam.classLoader.loadClass(
                    "com.goldze.mvvmhabit.data.MainRepository");
            XposedHelpers.findAndHookMethod(clazz, "index", int.class, int.class, new XC_MethodHook() {
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log("Called beforeHookedMethod");
                    param.args[1] = 5;
                    XposedBridge.log("Changed args 0 to " + param.args[0]);
                }

                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log("Called afterHookedMethod");
                }
            });
        }
    }

}