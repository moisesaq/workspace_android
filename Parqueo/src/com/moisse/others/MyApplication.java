package com.moisse.others;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Application;

public class MyApplication extends Application{

	@Override
	public void onCreate() {
		super.onCreate();
		ImageLoaderConfiguration config =  new ImageLoaderConfiguration.Builder(getApplicationContext())
		//.defaultDisplayImageOptions(options)
		.threadPriority(Thread.NORM_PRIORITY - 2) //establece la priorida de subproceso
		.denyCacheImageMultipleSizesInMemory() // si es true puedes almacenar imagenes en diferentes tamanios el ultimo reeplaza el anterior
		.diskCacheFileNameGenerator(new Md5FileNameGenerator()) //genera un nombre para imagen en cache
		.tasksProcessingOrder(QueueProcessingType.LIFO) //pone orden el cargar imagen LIFO primero en entrar primero en salir 
		.build();
		ImageLoader.getInstance().init(config);
//		.memoryCache(new WeakMemoryCache())
//		.diskCacheSize(100*1024*1024).build();
//		ImageLoader.getInstance().init(config);
	}
	
}
