package com.rawabi.xtml;

import java.math.BigDecimal;

public class App {

	
	/* test pins
	 * 
Test pins:

491 960 950
615 079 781
307 289 991
314 007 457
356 062 054
978 744 834
553 170 472
683 843 462
949 145 426
242 192 047
141 752 825
213 114 148
961 130 410
859 303 570
494 418 768
563 721 252
266 332 811
350 878 026
333 233 571
230 853 312
	 * 
	 * 
	 * 
	 */
	public static void main(String[] args) {

		String xtmlSessionId = "1.1.foo@bar" ;
		String pin = "8660878233684" ;
		String ani = "5083084899" ;
		Long spId = 10300L ;
		StringBuffer newPin = new StringBuffer() ;
		
		int rc = XtmlInterface.RegisterPrepaidAuthAni(xtmlSessionId, pin, ani, spId, 30, newPin) ;

	}

}
