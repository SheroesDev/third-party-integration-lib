package com.sheroes.thirdparty;

import com.sheroes.thirdparty.model.ThirdPartyClaim;
import com.sheroes.thirdparty.model.Token;
import com.sheroes.thirdparty.utils.ThirdPartyUtils;

public class Main {

	public static void main(String[] args){
		ThirdPartyClaim thirdPartyClaim  = new ThirdPartyClaim();
		thirdPartyClaim.setApikey("apkey");

		Token token  = ThirdPartyUtils.encrypt(thirdPartyClaim);

		System.out.println(" Token "+token.getToken());

		ThirdPartyClaim decryptedClaim = ThirdPartyUtils.decrypt(new Token(token.getToken()));

		System.out.println(" Decrypted" + decryptedClaim.getApikey());
	}
}
