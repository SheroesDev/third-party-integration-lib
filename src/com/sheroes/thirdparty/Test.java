package com.sheroes.thirdparty;

import com.sheroes.thirdparty.model.ClaimResponse;
import com.sheroes.thirdparty.model.ThirdPartyClaim;
import com.sheroes.thirdparty.model.Token;
import com.sheroes.thirdparty.utils.ThirdPartyUtils;

public class Test {

	public static void main(String[] args){
		ThirdPartyClaim thirdPartyClaim  = new ThirdPartyClaim();
		thirdPartyClaim.setApikey("apikey");
		thirdPartyClaim.setApiSecret("mysecret");
		thirdPartyClaim.setMobile("mobile");
		thirdPartyClaim.setFirstName("firstname");
		thirdPartyClaim.setLocation("mylocation");
		ClaimResponse  claimResponse  = ThirdPartyUtils.encrypt(thirdPartyClaim);
		System.out.println("Message "+claimResponse.getMessage());
		System.out.println(" Token "+claimResponse.getToken());

		ThirdPartyClaim decryptedClaim = ThirdPartyUtils.decrypt(new Token(claimResponse.getToken()));

		if(decryptedClaim != null)
		System.out.println(" Decrypted" + decryptedClaim.getApikey());
	}
}
