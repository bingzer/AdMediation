package com.bingzer.android.ads;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.amazon.device.ads.AdError;
import com.amazon.device.ads.AdLayout;
import com.amazon.device.ads.AdListener;
import com.amazon.device.ads.AdProperties;
import com.amazon.device.ads.AdRegistration;
import com.amazon.device.ads.AdSize;
import com.amazon.device.ads.AdTargetingOptions;

public final class AmazonAdNetwork extends AbsAdNetwork<AdLayout> implements AdListener{
    public static final int SIZE_300x50  = 0;
    public static final int SIZE_320x50  = 1;
    public static final int SIZE_600x90  = 2;
    public static final int SIZE_728x90  = 3;
    public static final int SIZE_1024x50 = 4;
    public static final int SIZE_AUTO    = 5;

	private AdTargetingOptions ops;
	private AdSize adSize;

	
	public AmazonAdNetwork(String pubId){
		this(SIZE_AUTO, pubId);
	}
	
	public AmazonAdNetwork(int size, String pubId){
		this.pubId = pubId;
		this.ops = new AdTargetingOptions();

        switch (size){
            default:
            case SIZE_AUTO:
                adSize = AdSize.SIZE_AUTO;
                break;
            case SIZE_300x50:
                adSize = AdSize.SIZE_300x50;
                break;
            case SIZE_320x50:
                adSize = AdSize.SIZE_320x50;
                break;
            case SIZE_600x90:
                adSize = AdSize.SIZE_600x90;
                break;
            case SIZE_728x90:
                adSize = AdSize.SIZE_728x90;
                break;
            case SIZE_1024x50:
                adSize = AdSize.SIZE_1024x50;
                break;
        }
	}

	@Override
	public String name() {
		return "Amazon";
	}

	@Override
	public View load(Context context, String... keywords) {
        if(!(context instanceof Activity))
            throw new IllegalArgumentException("Amazon load() is expecting an Activity from context param");
		if(adView == null){			
			AdRegistration.setAppKey(pubId);
            // auto size thing
            adView = new AdLayout((Activity)context, adSize);

			adView.setListener(this);
			adView.setBackgroundResource(android.R.color.transparent);
			adView.setLayoutParams(params());
		}
		ops.setGender(Helper.getRandom(AdTargetingOptions.Gender.MALE, AdTargetingOptions.Gender.FEMALE));
		ops.setAge(Helper.getRandom(10, 50));

		return adView;
	}

	@Override
	public IAdNetwork unload() {
		adView.destroy();
		adView = null;
		return this;
	}

	@Override
	public IAdNetwork showAd(Callback callback) {
		this.callback = callback;
		adView.loadAd(ops);
		return this;
	}

	
	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////

	@Override
	public void onAdCollapsed(AdLayout ad) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdExpanded(AdLayout ad) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdFailedToLoad(AdLayout ad, AdError err) {
		callback.onAdReceived(this, Result.Bad.message(err.getMessage()));
	}

	@Override
	public void onAdLoaded(AdLayout ad, AdProperties prop) {
		callback.onAdReceived(this, Result.Good);
	}

}
