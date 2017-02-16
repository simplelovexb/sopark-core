/**
	This class is used for ...
	@version	1.0
	@author		zhangfeng@yy.com
	@time	 	下午7:56:08 2013-12-18
 */
package cn.suxiangbao.sopark.time;

import java.util.Timer;
import java.util.TimerTask;

public class MaskClock {
	private static long curtime = -1;
	
	static {
		init();
	}
	
	public static long getCurtime() {
//		return curtime;
	    return System.currentTimeMillis();
	}
    
    public static long calCostTime(long startTimeInMillis) {
        return (System.currentTimeMillis() - startTimeInMillis);
    }

	protected static synchronized void init() {
		if(curtime == -1) {
			updateCurtime();
			scheduleMySelf();
		}
	}

	protected static long updateCurtime() {
		return curtime = System.currentTimeMillis();
	}

	protected static void scheduleMySelf() {
		Timer timer = new Timer();
		int period = 50; // 100ms/update
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				updateCurtime();
			}
		}, period, period);
	}
	
	public static void main(String[] args) throws InterruptedException {
		System.out.println(MaskClock.getCurtime());
		for(long i=0;i<500000000;i++) ; 
		System.out.println(MaskClock.getCurtime());
		Thread.currentThread();
		Thread.sleep(200);
		System.out.println(MaskClock.getCurtime());
		Thread.sleep(200);
		System.out.println(MaskClock.getCurtime());
		Thread.sleep(200);
		System.out.println(MaskClock.getCurtime());
	}
}
