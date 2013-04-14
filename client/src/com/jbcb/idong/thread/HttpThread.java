package com.jbcb.idong.thread;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

import com.jbcb.idong.LoginActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.DropBoxManager.Entry;
import android.os.Handler;
import android.os.Message;

public class HttpThread extends Thread {
	private Handler handler = null;
	private Context context = null;
	String url = null;
	String nameSpace = null;
	String methodName = null;
	HashMap<String, Object> params = null;
	ProgressDialog progressDialog = null;

	public HttpThread(Handler handler, Context context) {
		this.handler = handler;
		this.context = context;
	}

	public void doStart(String url, String nameSpace, String methodName,
			HashMap<String, Object> params) {
		// �Ѳ���������
		this.url = url;
		this.nameSpace = nameSpace;
		this.methodName = methodName;
		this.params = params;
		// ����ʹ���ߣ�����ʼ��
		progressDialog = new ProgressDialog(context);
		progressDialog.setTitle("��������");
		progressDialog.setMessage("�����������Ե�......");
		progressDialog.setIndeterminate(true);
		// progressDialog=ProgressDialog.show(clswdy.this,
		// "��������","������֤�����Ե�......",true,true);
		progressDialog.setButton("ȡ��", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int i) {
				progressDialog.cancel();

			}
		});
		progressDialog
				.setOnCancelListener(new DialogInterface.OnCancelListener() {
					public void onCancel(DialogInterface dialog) {
					}
				});
		progressDialog.show();
		this.start(); // �߳̿�ʼ��
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			// web service����,resultΪ���ؽ��
			int result = CallWebService();

			if (result == 1) {

				// ȡ�����ȶԻ���
				progressDialog.dismiss();
				// clswdy.this.setProgressBarIndeterminateVisibility(false);
				// ������Ϣ,��֤ͨ����
				Message message = handler.obtainMessage();
				Bundle b = new Bundle();
				message.what = 1; // ��������Ϣ������
				b.putInt("data", 1); // ��������Ϣ���͵�����

				message.setData(b);
				handler.sendMessage(message);
			} else {
				progressDialog.dismiss();

				Message message = handler.obtainMessage();
				Bundle b = new Bundle();
				message.what = 1;
				b.putInt("data", result); // ��������Ϣ���͵�����
				message.setData(b);
				handler.sendMessage(message);

			}
		} catch (Exception ex) {
			progressDialog.dismiss();
			// ������Ϣ�����������
			Message message = handler.obtainMessage();
			Bundle b = new Bundle();
			message.what = 2;

			b.putString("error", ex.getMessage());

			message.setData(b);
			handler.sendMessage(message);

		} finally {

		}
	}

	protected int CallWebService() throws Exception {
		String SOAP_ACTION = nameSpace + methodName;
		int response = 0;
		SoapObject request = new SoapObject(nameSpace, methodName);
		// boolean request=false;
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);

		envelope.dotNet = true; // .net ֧��

		// ����

		if (params != null && !params.isEmpty()) {
			for (Iterator it = params.entrySet().iterator(); it.hasNext();) {
				Map.Entry e = (Map.Entry) it.next();
				request.addProperty(e.getKey().toString(), e.getValue());

			}
		}
		envelope.bodyOut = request;
		//
		AndroidHttpTransport androidHttpTrandsport = new AndroidHttpTransport(url);
		SoapObject result = null;
		try {
			// web service����
			androidHttpTrandsport.call(SOAP_ACTION, envelope);
			// �õ����ؽ��
			Object temp = envelope.getResponse();
			response = Integer.parseInt(temp.toString());
		} catch (Exception ex) {
			throw ex;
		}
		return response;
	}
}
