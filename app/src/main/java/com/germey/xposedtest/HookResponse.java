package com.germey.xposedtest;

import java.io.IOException;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HookResponse implements IXposedHookLoadPackage {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public void sendDataToServer(String data) throws IOException {
        String server = "https://97598b51889e.ngrok.io/data";
        RequestBody formBody = new FormBody.Builder()
                .add("data", data)
                .add("from", "Xposed")
                .add("crawled_at", String.valueOf(System.currentTimeMillis()))
                .build();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(server)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                XposedBridge.log("Save failed:" + e.getMessage());
            }

            public void onResponse(Call call, Response response) throws IOException {
                XposedBridge.log("Saved successfully：" + response.body().string());
            }
        });

    }


    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {

        if (loadPackageParam.packageName.equals("com.goldze.mvvmhabit")) {
            XposedBridge.log("Hooked com.goldze.mvvmhabit Package");
            final Class clazz = loadPackageParam.classLoader.loadClass(
                    "com.goldze.mvvmhabit.data.source.HttpResponse");
            XposedHelpers.findAndHookMethod(clazz, "getResults", new XC_MethodHook() {
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log("Called beforeHookedMethod");
                }

                protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                    XposedBridge.log("Called afterHookedMethod");
                    List results = (List) param.getResult();
                    for (Object o : results) {
                        XposedBridge.log(o.toString());
                        String entity = o.toString();
                        XposedBridge.log("MovieEntity" + entity);
                        sendDataToServer(o.toString());
                    }
                }
            });

        }
    }

}
