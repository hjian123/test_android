package com.example.jamh.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.pax.exdev.ExDevLib;
import com.pax.exdev.ExSys;
import com.pax.gl.commhelper.ICommUsbHost;
import com.pax.gl.commhelper.exception.CommException;
import com.pax.gl.commhelper.impl.PaxGLComm;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test();

    }

    private void test() {
       ICommUsbHost mCommUsbHost = PaxGLComm.getInstance(this).createUsbHost();
        for (ICommUsbHost.IUsbDeviceInfo deviceInfo : mCommUsbHost.getPeerDevice()) {
            if (deviceInfo.isPaxDevice()) {
                Log.d("exdev", "dName: " + deviceInfo.getDevice().getDeviceName());
                Log.d("exdev", "vid: " + deviceInfo.getDevice().getVendorId());
                Log.d("exdev", "pid: " + deviceInfo.getDevice().getProductId());

                /* 连接百富的设备，后面2个参数 可取任意值，内部会忽略这两个参数，自动匹配对应的规则
                 * 如果连接的是其非百富的标准的usb设备，则需要从中选择合适的interface 和 传输类型
                 * 如果拔插过设备则需要重新getPeerDevice，重新调用setUsbDevice
                 */
                mCommUsbHost.setUsbDevice(deviceInfo.getDevice(), null, 0);
                mCommUsbHost.setPaxSpecialDevice(true);
                break;
            }
        }

        try {
            mCommUsbHost.connect();

            ExDevLib exDevLib = ExDevLib.getInstance();
            exDevLib.setCommunicator(mCommUsbHost);
            ExSys.StTime stTime = new ExSys.StTime();
            int ret = ExSys.getTime(stTime);
            Log.e("TEST","getTime return "+ret+",time="+stTime.getYear()+"/"+stTime.getMonth()+"/"+stTime.getDay()+" "+stTime.getHour()+":"+stTime.getMinute()+":"+stTime.getSecond());

        } catch (CommException e) {
            e.printStackTrace();
        }

    }

    private void test1(){

    }
}
