package com.jbcb.idong.async;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import com.jbcb.idong.R;
import com.jbcb.idong.cache.PartyListViewCache;
import com.jbcb.idong.utilities.CommonUtility;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.BaseAdapter;

public class ListViewImageLoader {

	private static final String TAG = "ImageLoader";
	private static final int MAX_CAPACITY = 10;// һ����������ռ�
	private static final long DELAY_BEFORE_PURGE = 10 * 1000;// ��ʱ������
	private int displaypixels = 0;
	
	// 0.75�Ǽ�������Ϊ����ֵ��true���ʾ��������������ĸߵ�����false���ʾ���ղ���˳������
	private HashMap<String, Bitmap> mFirstLevelCache = new LinkedHashMap<String, Bitmap>(
			MAX_CAPACITY / 2, 0.75f, true) {
		private static final long serialVersionUID = 1L;

		protected boolean removeEldestEntry(Entry<String, Bitmap> eldest) {
			if (size() > MAX_CAPACITY) {// ������һ��������ֵ��ʱ�򣬽��ϵ�ֵ��һ������ᵽ��������
				mSecondLevelCache.put(eldest.getKey(),
						new SoftReference<Bitmap>(eldest.getValue()));
				return true;
			}
			return false;
		};
	};
	
	// �������棬���õ�����Ӧ�ã�ֻ�����ڴ�Խ���ʱ����Ӧ�òŻᱻ���գ���Ч�ı�����oom
	private ConcurrentHashMap<String, SoftReference<Bitmap>> mSecondLevelCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>(
			MAX_CAPACITY / 2);

	// ��ʱ������
	private Runnable mClearCache = new Runnable() {
		@Override
		public void run() {
			clear();
		}
	};
	private Handler mPurgeHandler = new Handler();

	Executor mExecutor = new Executor();

	public ListViewImageLoader(int displaypixels) {
		this.displaypixels = displaypixels;
		mExecutor.start();
	}

	private static final BlockingQueue<ImageLoadTask> mTasks = new LinkedBlockingQueue<ListViewImageLoader.ImageLoadTask>();
	// ͨ���ź�������ͬʱִ�е��߳���
	Semaphore mSemaphore = new Semaphore(50);

	// ����������������ߣ�ȥ�������ȡ����������Ȼ��ִ�У���û�������ʱ�������߾͵ȴ�
	class Executor extends Thread {
		@Override
		public void run() {
			while (true) {
				ImageLoadTask task = null;
				try {
					task = mTasks.take();
					if (task != null) {
						mSemaphore.acquire();
						task.execute();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// ���û��������timer
	private void resetPurgeTimer() {
		mPurgeHandler.removeCallbacks(mClearCache);
		mPurgeHandler.postDelayed(mClearCache, DELAY_BEFORE_PURGE);
	}

	/**
	 * ������
	 */
	private void clear() {
		mFirstLevelCache.clear();
		mSecondLevelCache.clear();
	}

	/**
	 * ���ػ��棬���û���򷵻�null
	 * 
	 * @param url
	 * @return
	 */
	public Bitmap getBitmapFromCache(String url) {
		Bitmap bitmap = null;
		bitmap = getFromFirstLevelCache(url);// ��һ����������
		if (bitmap != null) {
			return bitmap;
		}
		bitmap = getFromSecondLevelCache(url);// �Ӷ�����������
		return bitmap;
	}

	/**
	 * �Ӷ�����������
	 * 
	 * @param url
	 * @return
	 */
	private Bitmap getFromSecondLevelCache(String url) {
		Bitmap bitmap = null;
		SoftReference<Bitmap> softReference = mSecondLevelCache.get(url);
		if (softReference != null) {
			bitmap = softReference.get();
			if (bitmap == null) {// �����ڴ�Խ����������Ѿ���gc������
				mSecondLevelCache.remove(url);
			}
		}
		return bitmap;
	}

	/**
	 * ��һ����������
	 * 
	 * @param url
	 * @return
	 */
	private Bitmap getFromFirstLevelCache(String url) {
		Bitmap bitmap = null;
		synchronized (mFirstLevelCache) {
			bitmap = mFirstLevelCache.get(url);
			if (bitmap != null) {// ��������ʵ�Ԫ�طŵ�����ͷ���������һ�η��ʸ�Ԫ�صļ����ٶȣ�LRU�㷨��
				mFirstLevelCache.remove(url);
				mFirstLevelCache.put(url, bitmap);
			}
		}
		return bitmap;
	}

	/**
	 * ����ͼƬ������������о�ֱ�Ӵӻ������ã�������û�о�����
	 * 
	 * @param url
	 * @param adapter
	 * @param holder
	 */
	int i = 0;

	public void loadImage(String url, BaseAdapter adapter, PartyListViewCache cache) {
		resetPurgeTimer();
		// get bitmap from the cache
		Bitmap bitmap = getBitmapFromCache(url);
		if (bitmap == null) {
			cache.getImageViewIcon().setImageResource(R.drawable.ic_launcher);// ����û����ΪĬ��ͼƬ
			if (!"".equals(url) && url != null) {
				ImageLoadTask imageLoadTask = new ImageLoadTask(url, adapter);
				try {
					// put the task into the task queue
					mTasks.put(imageLoadTask);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else {
			Bitmap img = CommonUtility.getImageThumbnail(bitmap, 80, 80);
			cache.getImageViewIcon().setImageBitmap(img);// ��Ϊ����ͼƬ
		}
	}

	/**
	 * ���뻺��
	 * 
	 * @param url
	 * @param value
	 */
	public void addImage2Cache(String url, Bitmap value) {
		if (value == null || url == null) {
			return;
		}
		synchronized (mFirstLevelCache) {
			mFirstLevelCache.put(url, value);
		}
	}

	public class ImageLoadTask extends AsyncTask<Object, Void, Bitmap> {
		String url;
		BaseAdapter adapter;

		public ImageLoadTask(String url, BaseAdapter adapter) {
			this.url = url;
			this.adapter = adapter;
		}

		@Override
		protected Bitmap doInBackground(Object... params) {
			Log.d(TAG, "func doInBackground-----");
			Bitmap drawable = CommonUtility.loadImageFromInternet(url, displaypixels);// ��ȡ����ͼƬ
			return drawable;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			mSemaphore.release();
			if (result == null) {
				return;
			}
			addImage2Cache(url, result);// ���뻺��
			adapter.notifyDataSetChanged();// ����getView����ִ�У����ʱ��getViewʵ���ϻ��õ��ոջ���õ�ͼƬ
		}
	}
}
