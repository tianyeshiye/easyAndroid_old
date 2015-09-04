package com.kubeiwu.easyandroid.mvp.utils;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.Future;

import rx.Observable;
import rx.schedulers.Schedulers;

import com.kubeiwu.commontool.khttp.KRequestQueueManager;
import com.kubeiwu.commontool.khttp.Request.Method;
import com.kubeiwu.commontool.khttp.Request.RequestMode;
import com.kubeiwu.commontool.khttp.toolbox.notused.RequestFuture;
import com.kubeiwu.easyandroid.kretrofit.KResult;
import com.kubeiwu.easyandroid.kvolley.KGsonRequest;

public class VoleyToObservableUtil {
	public static <T> Observable<T> futureToObservable(Future<T> future) {
		Observable<T> observable = Observable.from(future, Schedulers.io());
		return observable;
	}

	public static <T extends KResult> RequestFuture<T> voleyToFuture(int method, String url, Map<String, String> headers, Map<String, String> params, Type type, int requestMode) {
		RequestFuture<T> future = RequestFuture.newFuture();
		KGsonRequest<T> gsonRequest = new KGsonRequest<T>(method, url, headers, params, future, future);
		gsonRequest.setShouldAddCookiesToRequest(true);
		gsonRequest.setCache_Duration(1000 * 60 * 60 * 24 * 300l);// 300天
		gsonRequest.setResponseType(type);
		gsonRequest.setRequestMode(requestMode);// 请求缓存策越
		KRequestQueueManager.getRequestQueue().add(gsonRequest);
		return future;
	}

	public static <T extends KResult> Observable<T> executeHttpRequestToObservable(int method, String url, Map<String, String> headers, Map<String, String> params, Type type, int requestMode) {
		Future<T> future = voleyToFuture(method, url, headers, params, type, requestMode);
		return futureToObservable(future);
	}

	public static <T extends KResult> Observable<T> executeHttpRequestToObservable(String url, Map<String, String> params, Type type, int requestMode) {
		Future<T> future = voleyToFuture(Method.POST, url, null, params, type, requestMode);
		return futureToObservable(future);
	}

	public static <T extends KResult> Observable<T> executeHttpRequestToObservable(String url, Type type, int requestMode) {
		Future<T> future = voleyToFuture(Method.GET, url, null, null, type, requestMode);
		return futureToObservable(future);
	}

	public static <T extends KResult> Observable<T> executeHttpRequestToObservable(String url, Type type) {
		Future<T> future = voleyToFuture(Method.GET, url, null, null, type, RequestMode.LOAD_NETWORK_ONLY);
		return futureToObservable(future);
	}
}
