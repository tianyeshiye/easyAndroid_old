package com.kubeiwu.easyandroid.mvp.presenter;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import com.kubeiwu.easyandroid.mvp.PresenterLoader;
import com.kubeiwu.easyandroid.mvp.kabstract.KRxJavaPresenter;
import com.kubeiwu.easyandroid.mvp.view.ISimpleThreadView;

public class KSimpleThreadPresenter<T> extends KRxJavaPresenter<ISimpleThreadView<T>, T> {

	private Observable<T> observable = Observable.create(new Observable.OnSubscribe<T>() {

		@Override
		public void call(Subscriber<? super T> sub) {
			if (!sub.isUnsubscribed()) {
				PresenterLoader<T> presenterLoader = getView().onCreatPresenterLoader(presenterId);
				try {
					T t = presenterLoader.loadInBackground();
					sub.onNext(t);
					sub.onCompleted();
				} catch (Exception e) {
					sub.onError(e);
				}
			}
		}
	}).subscribeOn(Schedulers.io())//
			.observeOn(AndroidSchedulers.mainThread());

	public void loadData() {
		onCancel();
		subscriber = new KSubscriber(this.mController);
		Observable<T> observable = creatObservable();
		observable.subscribe(subscriber);
	}

	@Override
	public Observable<T> creatObservable() {
		return observable;
	}
}