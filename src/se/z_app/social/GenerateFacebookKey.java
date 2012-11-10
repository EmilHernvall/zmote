package se.z_app.social;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

public class GenerateFacebookKey {
	private Context context;

	public GenerateFacebookKey(Context context) {
		this.context = context;
	}

	public void genHashKeyForFacebook() {
		PackageInfo info;
		try {
			info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md;

				md = MessageDigest.getInstance("SHA1");
				md.update(signature.toByteArray());
				String hash = new String(Base64.encode(md.digest(), 0));
				Log.e("hash key", hash);
				System.out.println("hash key: " +hash);
			}
		} catch (NameNotFoundException e) {
			Log.e("name not found", e.toString());
		}

		catch (NoSuchAlgorithmException e) {
			Log.e("no such an algorithm", e.toString());
		} catch (Exception e) {
			Log.e("exception", e.toString());
		}
	}
}
