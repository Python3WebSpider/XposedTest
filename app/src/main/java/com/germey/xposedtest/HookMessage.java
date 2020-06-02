package com.germey.xposedtest;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookMessage implements IXposedHookLoadPackage {

    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {

        if (loadPackageParam.packageName.equals("com.germey.xposedtest")) {
            XposedBridge.log("Hooked com.germey.xposedtest Package");
            Class clazz = loadPackageParam.classLoader.loadClass(
                    "com.germey.xposedtest.MainActivity");
            XposedHelpers.findAndHookMethod(clazz, "showMessage", int.class, int.class, new XC_MethodHook() {
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log("Called beforeHookedMethod");
                    param.args[0] = 2;
                    XposedBridge.log("Changed args 0 to " + param.args[0]);
                }

                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log("Called afterHookedMethod");
                    param.setResult("Hooked");
                }
            });
        }
    }

}