package com.magpie.base.view;

public class BaseView<T> {

	private T content;
	
	private Result result;
	
	

	public BaseView() {
		this.result = Result.SUCCESS;
	}
	
	public BaseView(Result result){
		this.result = result;
	}
	
	public BaseView(T content){
		this.content = content;
	}
	
	public T getContent() {
		return content;
	}

	public void setContent(T content) {
		this.content = content;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}
	
}
