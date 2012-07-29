//
// $Id: javascript_utils.jsh,v 1.238.2.2 2010/02/20 15:00:51 jgibson Exp $
//  
// $Name: PCS-A-4-1-1-1-4-c6 $

//strip leading one off inputString

function js_parse_ep ( endpoint ) 
{
var endpointString = "";
var i = endpoint.indexOf("@");

i--;

while ('/' != endpoint.charAt(i)  ) {

	endpointString = endpoint.charAt(i) + endpointString;
	i--;
}
return endpointString;
}

function js_strip_one(inputString)
{
	if ( 0 == Clib.strcspn(inputString, "1") )
	{
		inputString = inputString.substr(1);
	}
	return inputString;
}

//strip any non digit off inputString
function js_strip_non_digits(inputString)
{
	var outputString = "";
	var position = 0;
	
	while (position < inputString.length)
	{
		position = Clib.strcspn(inputString, "0123456789");	//find first digit
		if (position <= inputString.length)	//if found
		{
			inputString = inputString.substr(position);	//strip all the non digit before the first digit
			position = Clib.strspn(inputString, "0123456789");	//find first non digit
			if (position <= inputString.length)	//if found
			{
				Clib.strncat(outputString, inputString, position);	//copy to the output string
				inputString = inputString.substr(position);	//new string to check
			}
		}
		else
			Clib.strcat(outputString, inputString);
	}

	return outputString;
}

//prepend inputDigits to inputString
function js_prepend_digits(inputString, inputDigits)
{
	if (inputDigits.length > 0)
	{
		inputString = inputDigits + inputString;
	}
	
	return inputString;
}

//strip tag= off inputString
function js_strip_tag(inputString)
{
	var i = 0;
	var outputString = "";

	var token = Clib.strtok(inputString, ";");
	while (null != token)
	{
		if ( null == Clib.strstr(token, "tag") )
		{
			if (0 == i)
				outputString += token;
			else
				outputString = outputString + ";" + token;
		}
		i++;
		token = Clib.strtok(null, ";");
	}
	return outputString;
}

function js_strip_oli(inputString)
{
	var i = 0;
	var outputString = "";

	var token = Clib.strtok(inputString, ";");
	while (null != token)
	{
		if ( null == Clib.strstr(token, "oli") )
		{
			if (0 == i)
				outputString += token;
			else
				outputString = outputString + ";" + token;
		} else {
			// Need to do this if oli is last token, eg: "From: <sip:2502045511@10.10.100.132;user=phone;oli=99>;tag=1013826889"
			// We don't want to lose the '>'
			// found OLI, check if its the last token before '>'.
			var strOli = Clib.strstr(token, "oli");
			if(strOli.indexOf('>') > 0) {
				outputString = outputString + ">";			
			}
		}
		
		i++;
		token = Clib.strtok(null, ";");
	}
	return outputString;
}

function js_set_ani_and_info_digits(p_strFrom,
    p_strRemotePartyId,
    p_strPAssertedIdentity,
    p_strDefaultAni,
    p_strCheckAniForInfoDigits,
    p_bUseFromHeaderForAni,
    &p_strOriginationNum,
    &p_strCallingNum,
    &p_strUsedDefaultAni,
    &p_strInfoDigits,
    &p_strHost,
    p_customerCode)
{

	var from_uri = new SipFrom(p_strFrom) ;
	var strANI = new String(from_uri.url.user);
	var bAniIsHidden = false;
	var strANIFromRpi = "";
	var strHostFromRpi = "";
	var strANIFromPai = "";
	var strHostFromPai = "";

	Server.logInfo("js_set_ani_and_info_digits - p_strFrom: <" + p_strFrom + ">");
	Server.logInfo("js_set_ani_and_info_digits - p_strRemotePartyId: <" + p_strRemotePartyId + ">");
	Server.logInfo("js_set_ani_and_info_digits - p_strPAssertedIdentity <" + p_strPAssertedIdentity + ">");
	Server.logInfo("js_set_ani_and_info_digits - p_strCheckAniForInfoDigits: <" + p_strCheckAniForInfoDigits + ">");
	Server.logInfo("js_set_ani_and_info_digits - p_bUseFromHeaderForAni: <" + p_bUseFromHeaderForAni + ">");
	Server.logInfo("js_set_ani_and_info_digits - strANI: <" + strANI + ">");
        Server.logInfo("js_set_ani_and_info_digits - p_customerCode: <" + p_customerCode +">");


	if (from_uri.url.user == undefined || strANI.length <= 0 || 
	    strANI.indexOf("restricted") != -1 || strANI.indexOf("Restricted") != -1 || 
            strANI.indexOf("RESTRICTED") != -1 || strANI.indexOf("anonymous") != -1 || 
            strANI.indexOf("Anonymous") != -1 || strANI.indexOf("ANONYMOUS") != -1)
	{
		bAniIsHidden = true;
	}
	
	if (!p_bUseFromHeaderForAni || bAniIsHidden)
	{
		if ( 0 < p_strPAssertedIdentity.length ) {
			js_get_ani_and_host_from_p_asserted_identity(p_strPAssertedIdentity, strANIFromPai, strHostFromPai);
		}
		
		if ( 0 == strANIFromPai.length ) {
			Server.logInfo("Could not parse ani from P-Asserted-Identity.");
			
			if ( 0 < p_strRemotePartyId.length ) {
				js_get_ani_and_host_from_remote_party_id(p_strRemotePartyId, strANIFromRpi, strHostFromRpi);
			}

			if(strANIFromRpi == "" && !p_bUseFromHeaderForAni && !bAniIsHidden) {
				p_strOriginationNum = from_uri.url.user;
				p_strCallingNum = from_uri.url.user;
				p_strHost = from_uri.url.host;
				Server.logInfo("<use-from-header-for-ani> is F and RemotePartyId does not exist and ANI exists in From header. App will set ANI using the From header.");
			} else if(strANIFromRpi == "") {
				p_strOriginationNum = p_strDefaultAni;
				p_strCallingNum = p_strDefaultAni;
				p_strUsedDefaultAni = "T";
				p_strHost = from_uri.url.host;
				Server.logInfo("Using default ani and host of the From header.");
			} else {
				p_strOriginationNum = strANIFromRpi;
				p_strCallingNum = strANIFromRpi;
				p_strHost = strHostFromRpi;
			}
		}
		else {
			p_strOriginationNum = strANIFromPai;
			p_strCallingNum = strANIFromPai;
			p_strHost = strHostFromPai;
		}
	}
	else
	{
		p_strOriginationNum = from_uri.url.user;
		p_strCallingNum = from_uri.url.user;
		p_strHost = from_uri.url.host;
	}
        var plusFound = false;
	Server.logInfo("BEFORE strip non digit off origination number: <" + p_strOriginationNum + ">");
        if(p_customerCode != "sawtel" && p_strOriginationNum.indexOf("+") != -1){
            plusFound = true;
            Server.logInfo("Origination number contains a '+'");
        }
	var strOriginationNum = p_strOriginationNum;
	p_strOriginationNum = js_strip_non_digits(strOriginationNum);
	Server.logInfo("AFTER strip non digit off origination number: <" + p_strOriginationNum + ">");


	if("T" == p_strCheckAniForInfoDigits) {
		p_strInfoDigits = p_strOriginationNum.substring(0,2);
		p_strOriginationNum = p_strOriginationNum.substring(2, p_strOriginationNum.length);
		p_strCallingNum = p_strCallingNum.substring(2, p_strCallingNum.length);
	} else {
		p_strInfoDigits = js_get_info_digits(p_strFrom);
		if( 0 == p_strInfoDigits.length ) {
                  var s = new String() ;
                  if( js_parse_value( p_strFrom, "isup-oli", s ) ) {
                        var n = Clib.atoi( s ) ;
                        p_strInfoDigits = n.toString() ;
                  }
                }

	}	
        if(plusFound){
            Server.logInfo("prepending '+' to start of origination number");
            p_strOriginationNum = "+"+p_strOriginationNum;
	}	

	Server.logInfo("AFTER calling js_set_ani_and_info_digits - p_strCallingNum: <" + p_strCallingNum + ">");
	Server.logInfo("AFTER calling js_set_ani_and_info_digits - p_strOriginationNum: <" + p_strOriginationNum + ">");
	Server.logInfo("AFTER calling js_set_ani_and_info_digits - p_strHost: <" + p_strHost.toString() + ">");
	Server.logInfo("AFTER calling js_set_ani_and_info_digits - p_strInfoDigits: <" + p_strInfoDigits + ">");

}

function js_get_ani_and_host_from_remote_party_id(p_strRemotePartyId, &strAni, &strHost)
{

	Server.logInfo("Parsing Remote Party Id");
	if(p_strRemotePartyId.length >= 0) {
	
		var iIndexOfSip = p_strRemotePartyId.indexOf("sip:");

		if(-1 != iIndexOfSip) {
	
			iIndexOfSip+= 4;

			var iIndexOfAt = p_strRemotePartyId.indexOf("@");
	 
			if(-1 != iIndexOfAt) {
	 
				strAni = p_strRemotePartyId.substring(iIndexOfSip, iIndexOfAt);
				if (Clib.strspn(strAni, "0123456789+-") != strAni.length) {
					Server.logError("Ani is not numerical.");
					strAni = "";
					return;
				}
				else {
					Server.logInfo("Parsed ANI from Remote Party Id: " + strAni.toString());
				}
				
				var iIndexOfSemi = p_strRemotePartyId.indexOf(";");
				var iIndexOfCloseBracket = p_strRemotePartyId.indexOf(">");
				if ( -1 != iIndexOfSemi && (iIndexOfSemi <  iIndexOfCloseBracket)){
					strHost = p_strRemotePartyId.substring((iIndexOfAt + 1), iIndexOfSemi);
					Server.logInfo("Parsed Host from p_strRemotePartyId: " + strHost.toString());
				}
				else {
			   		if ( -1 != iIndexOfCloseBracket ) {
						strHost = p_strRemotePartyId.substring((iIndexOfAt + 1), iIndexOfCloseBracket);
						Server.logInfo("Parsed Host from p_strRemotePartyId: " + strHost.toString());
					}
					else{
						Server.logError("Could not parse host from p_strRemotePartyId: " + p_strRemotePartyId);
					}
				}
			} // end -1 = iIndexOfAt
			else {
				Server.logInfo("Could not parse Ani out of strRemotePartyId: " + p_strRemotePartyId);
			}	
		
		} // end -1 != iIndexOfSip
		else {
			Server.logInfo("Could not parse Ani out of strRemotePartyId: " + p_strRemotePartyId);
		}
	} // end if p_strRemotePartyId.length > 0
	else {
		Server.logInfo("Could not parse Ani out of strRemotePartyId: " + p_strRemotePartyId);
	}	

} // end function


function js_get_privacy_from_remote_party_id(p_strRemotePartyId) 
{

	var strPrivacyValue = "";

	if(p_strRemotePartyId.length >= 0) {
	
		var iIndexOfPrivacy = p_strRemotePartyId.indexOf("privacy=");
	
		if(-1 != iIndexOfPrivacy) {
	
			iIndexOfPrivacy+= 8;

			var iIndexOfThirdChar = iIndexOfPrivacy + 3;
	 
			strPrivacyValue = p_strRemotePartyId.substring(iIndexOfPrivacy, iIndexOfThirdChar);
			return strPrivacyValue;

		} // end -1 != iIndexOfPrivacy

	} // end -1 != p_strRemotePartyId.length >= 0

	Server.logInfo("Could not determin value of privacy tag in strRemotePartyId: " + Session.strRemotePartyId);
	return strPrivacyValue;

}

function js_get_ani_and_host_from_p_asserted_identity(p_strPAssertedIdentity, &strAni, &strHost)
{

	Server.logInfo("Parsing P-Asserted-Identity header");

	if(p_strPAssertedIdentity.length >= 0) {
	
		var iIndexOfSip = p_strPAssertedIdentity.indexOf("sip:");

		if(-1 != iIndexOfSip) {
	
			iIndexOfSip += 4;

			var iIndexOfAt = p_strPAssertedIdentity.indexOf("@");
	 
			if(-1 != iIndexOfAt) {
	 
				strAni = p_strPAssertedIdentity.substring(iIndexOfSip, iIndexOfAt);
				if (Clib.strspn(strAni, "0123456789+-") != strAni.length) {
					Server.logError("Ani is not numerical.");
					strAni = "";
					var iIndexOfTel = p_strPAssertedIdentity.indexOf("tel:");
					if ( -1 != iIndexOfTel ) {
						strAni = p_strPAssertedIdentity.substr((iIndexOfTel + 5));
						Server.logInfo("Parsed ANI from P-Asserted-Identity: " + strAni.toString());
					}
					else {
						Server.logError("Unable to parse ANI from P-Asserted-Identity");
						return;
					}
				}
				else {
					Server.logInfo("Parsed ANI from P-Asserted-Identity: " + strAni.toString());
				}
				
				var iIndexOfSemi = p_strPAssertedIdentity.indexOf(";");
				var iIndexOfCloseBracket = p_strPAssertedIdentity.indexOf(">");
				if ( -1 != iIndexOfSemi && (iIndexOfSemi <  iIndexOfCloseBracket)){
					strHost = p_strPAssertedIdentity.substring((iIndexOfAt + 1), iIndexOfSemi);
					Server.logInfo("Parsed Host from P-Asserted-Identity: " + strHost.toString());
				}
				else {
			   		if ( -1 != iIndexOfCloseBracket ) {
						strHost = p_strPAssertedIdentity.substring((iIndexOfAt + 1), iIndexOfCloseBracket);
						Server.logInfo("Parsed Host from P-Asserted-Identity: " + strHost.toString());
					}
					else{
						Server.logError("Could not parse host from P-Asserted-Identity: " + p_strPAssertedIdentity);
					}
				}
				
			} // end -1 = iIndexOfAt
			else {
				Server.logError("Could not parse Ani out of p_strPAssertedIdentity: " + p_strPAssertedIdentity);
			}
		
		} // end -1 != iIndexOfSip
		else {
			Server.logError("Could not parse Ani out of p_strPAssertedIdentity: " + p_strPAssertedIdentity);
		}
	
	} // end if p_strPAssertedIdentity.length > 0
	else{
		Server.logError("Could not parse Ani out of p_strPAssertedIdentity: " + p_strPAssertedIdentity);
	}
	
} // end function

function js_check_to_strip_oli(p_strInfoDigits, p_strOLIsToStrip, p_strFrom) {


	var bMatch = false;
	var strFrom = p_strFrom;
	var i = 0;
	
	Server.logInfo("Start js_check_to_strip_oli.");
	Server.logInfo("p_strInfoDigits: " + p_strInfoDigits);	
	Server.logInfo("p_strOLIsToStrip: " + p_strOLIsToStrip);	
	Server.logInfo("p_strFrom: " + p_strFrom);			

	if("" != p_strInfoDigits) {
		Server.logInfo("Info Digits (OLI) Exist; Check if OLIs are set up for stripping.");
		if(p_strOLIsToStrip == "" || p_strOLIsToStrip.length <= 0) {
			Server.logInfo("No OLIs are set up for stripping.");	
		} else {
			Server.logInfo("OLIs are set up for stripping; Check for a match.");	
			var aOLIs = p_strOLIsToStrip.split(',');
			for(i=0; i < aOLIs.length; ++i) {
				if(aOLIs[i] == p_strInfoDigits) {
					bMatch = true;
					break;
				} // end if
			} // end for

			if(bMatch) {
				Server.logInfo("Match found. Strip OLI from Sip From.");
				strFrom = js_strip_oli(p_strFrom);
			} // end if bMatch

		} // end if p_strOLIsToStrip == ""			
		
	} else {
	
		Server.logInfo("Info Digits (OLI) do not exists.");
	} // end if("" != Session.g_oAPI.strInfoDigits)
	
	
	Server.logInfo("End js_check_to_strip_oli. strFrom: " + strFrom);
	return strFrom;

} // end function




//get CIC code
function js_get_cic_code(inputString)
{
	var cicCode = "";
	var request_uri = new SipUrl( inputString ) ;
	if (request_uri.cic != undefined)
	{
		cicCode = request_uri.cic;
	}
	else
	{
		cicCode = "0000";
	}
	return cicCode;
}

//get Info Digits
function js_get_info_digits(inputString)
{
	var infoDigits = "";
	var from_uri = new SipFrom( inputString ) ;
	
	if (from_uri.url.oli != undefined)
	{
		infoDigits = from_uri.url.oli;
	}
	else
	{
		infoDigits = "";
	}
	return infoDigits;
}

//is US dest valid
function js_is_dest_valid(inputString)
{
	//validate destination for North America dialing plan only
	
	var myDestination = new String(inputString);
	//011 + country code + city code.  This is the minimum
	if (myDestination.length < 7 || (myDestination.charAt(0) == "1" && myDestination.length < 11) || ( (myDestination.charAt(0) > "1" && myDestination.charAt(0) < "10") && myDestination.length < 10) )
	{
		return -1;
	}
	else
	{
		return 0;
	}
}

//build uri and route
function js_calculate_uri_and_route( p_bDialIn, p_strSipVersion, p_strFrom, p_strContact, p_strRecordRoute, &p_strURI, &p_strRoute )
{
	
	var myContact = new SipNameAddr(p_strContact.toString());
	var myFrom = new SipFrom( p_strFrom.toString() ) ;
		
	if (p_strRecordRoute != "null" && p_strRecordRoute.length > 0)
	{
		/* 	DH: modification to handle multiple record routes properly.
			(1) Reverse the order if this is an outbound call (2) add contact to the end, 
			(3) pop the first entry off for request-uri, (4) use the remainder as route header
		*/ 
		var uri_array = p_strRecordRoute.split(',') ;
		for ( var i = 0; i < uri_array.length; i++) {
			var routes = new SipNameAddr(uri_array.toString());
			uri_array[i] = routes.url;
		}
		if ( false == p_bDialIn ) {
			uri_array.reverse() ;
		}
		if( p_strContact.length > 0 ) {
			uri_array.push( new String(myContact.url.toString()) ) ;
		}
		p_strURI = uri_array.shift() ;
		p_strRoute = uri_array.join(',') ; 
	}
	else if (p_strContact.length > 0)
	{
		p_strURI = myContact.url.toString();
	}
	else if( p_bDialIn )
	{
		if (myFrom != undefined && p_strFrom.length > 0)
		{
			p_strURI = myFrom.url.toString() ;
		}
	}
}

//return index match call id
function js_search_callid(inputArray, callid)
{
	var myIndex = 0;
	var bFound = false;

	Server.logInfo("js_search_callid receives array.length: " + inputArray.length);
	
	for (myIndex = 0; myIndex < inputArray.length; myIndex++)
	{
		if (inputArray[myIndex].strCallId == callid)
		{
			bFound = true;
			break;
		}
	}
	if (bFound) {
		Server.logInfo("Successfully found call leg, index: " + myIndex);
		return myIndex;
	} else {
		Server.logError("ERROR FINDING CALL LEG IN ARRAY.");
		return -1;
	}
}

//return first null index
function js_search_null(inputArray)
{
	var myIndex = 0;
	for (myIndex = 0; myIndex < inputArray.length; myIndex++)
	{
		if (inputArray[myIndex] == null)
			return myIndex;
	}
}

//remove call leg object
function js_remove_call_leg(&inputArray, callid)
{
	var bFound = false ;
	var tempObject;
	var tempMS;
	
    Server.logInfo("js_remove_call_leg receives array.length: " + inputArray.length);
	
	if (0 == inputArray.length)
	{
		Server.logInfo("array.length IS ZERO, NO NEED TO REMOVE ANY CALL LEG");
		return;
	}
	else
	{
		for (var i = 0; i < inputArray.length; i++)
		{
		 	if( inputArray[i].strCallId == callid)
		 	{
		 		bFound = true;
		 		var j = i + 1;
		 		while (j < inputArray.length)
		 		{
		 			// don't swap if it is the last element
		 			//if ( (j) < inputArray.length )
		 			{
		 				//back it up
		 				tempMS.bCurrentlyConnected = inputArray[j].oMS.bCurrentlyConnected;
						tempMS.bCurrentlyInConf = inputArray[j].oMS.bCurrentlyInConf;
						tempMS.bDeafFlag = inputArray[j].oMS.bDeafFlag;
						tempMS.bMuteFlag = inputArray[j].oMS.bMuteFlag;
						tempMS.bPersistentFlag = inputArray[j].oMS.bPersistentFlag;
						tempMS.bRetry = inputArray[j].oMS.bRetry;
						
						tempMS.nConferenceMaxParties = inputArray[j].oMS.nConferenceMaxParties;
						tempMS.nConferenceMaxTalkers = inputArray[j].oMS.nConferenceMaxTalkers;
						tempMS.nPacketizationPeriod = inputArray[j].oMS.nPacketizationPeriod;
						
						tempMS.strCallId = inputArray[j].oMS.strCallId;
						tempMS.strCodec = inputArray[j].oMS.strCodec;
						tempMS.strConferenceId = inputArray[j].oMS.strConferenceId;
						tempMS.strConnectionId = inputArray[j].oMS.strConnectionId;
						tempMS.strContent = inputArray[j].oMS.strContent;
						tempMS.strEndPoint = inputArray[j].oMS.strEndPoint;
						tempMS.strType = inputArray[j].oMS.strType;
						
						tempObject.bAttemptedToOutdial = inputArray[j].bAttemptedToOutdial;
						tempObject.bConnected = inputArray[j].bConnected;
						tempObject.bCurrentlyDialing = inputArray[j].bCurrentlyDialing;
						tempObject.bReverseFromTo = inputArray[j].bReverseFromTo;
						
						tempObject.nDestCountryId = inputArray[j].nDestCountryId;
						tempObject.nDestRegionId = inputArray[j].nDestRegionId;
						tempObject.nGracePeriod = inputArray[j].nGracePeriod;

						tempObject.nRingNoAnswerTime = inputArray[j].nRingNoAnswerTime;
						tempObject.nSessionTimer = inputArray[j].nSessionTimer;
						tempObject.nSetUpTime = inputArray[j].nSetUpTime;
						tempObject.nTearDownTime = inputArray[j].nTearDownTime;
					
						tempObject.strCallType = inputArray[j].strCallType;
						tempObject.strWiretapFlag = inputArray[j].strWiretapFlag;
						tempObject.strWiretapDest = inputArray[j].strWiretapDest;
						tempObject.strStatus = inputArray[j].strStatus;
						
						tempObject.lTimeAnswered = inputArray[j].lTimeAnswered;
						tempObject.lTimeStart = inputArray[j].lTimeStart;
											
						tempObject.strBackupRequestUri = inputArray[j].strBackupRequestUri;
						tempObject.strBillableFlag = inputArray[j].strBillableFlag;
						tempObject.strCalledNumber = inputArray[j].strCalledNumber;
						tempObject.strCallingNumber = inputArray[j].strCallingNumber;
						tempObject.strCallId = inputArray[j].strCallId;
						tempObject.strCodec = inputArray[j].strCodec;
						tempObject.strConfCallFlag = inputArray[j].strConfCallFlag;
						tempObject.strContact = inputArray[j].strContact;
						tempObject.strContent = inputArray[j].strContent;
						tempObject.strContentType = inputArray[j].strContentType;
						tempObject.strCSeq = inputArray[j].strCSeq;
						tempObject.strCSROutdialFlag = inputArray[j].strCSROutdialFlag;
						tempObject.strIntlDestFlag = inputArray[j].strIntlDestFlag;
						tempObject.strDestAreaCode = inputArray[j].strDestAreaCode;
						tempObject.strDestRouteType = inputArray[j].strDestRouteType;
						tempObject.strDestCallingCode = inputArray[j].strDestCallingCode;
						tempObject.strDestDomIntlFlag = inputArray[j].strDestDomIntlFlag;
						tempObject.strDestTrunkGroup = inputArray[j].strDestTrunkGroup;
						tempObject.strEnteredDestNbr = inputArray[j].strEnteredDestNbr;
						tempObject.strEventId = inputArray[j].strEventId;
						tempObject.strFrom = inputArray[j].strFrom;
						tempObject.strOriginalContent = inputArray[j].strOriginalContent;
						tempObject.strOrigTrunkGroup = inputArray[j].strOrigTrunkGroup;
						tempObject.strOutdialDestNbr = inputArray[j].strOutdialDestNbr;
						tempObject.strRecordRoute = inputArray[j].strRecordRoute;
						tempObject.strRequestUri = inputArray[j].strRequestUri;
						tempObject.strRoute = inputArray[j].strRoute;
						tempObject.strSessionTimerFlag = inputArray[j].strSessionTimerFlag;
						tempObject.strSIPStatus = inputArray[j].strSIPStatus;
						tempObject.strStrippedDest = inputArray[j].strStrippedDest;
						tempObject.strTo = inputArray[j].strTo;
						tempObject.strVia = inputArray[j].strVia;
			 			
				 		//swap
			 					 			
			 			inputArray[j].oMS.bCurrentlyConnected = inputArray[i].oMS.bCurrentlyConnected;
						inputArray[j].oMS.bCurrentlyInConf = inputArray[i].oMS.bCurrentlyInConf;
						inputArray[j].oMS.bDeafFlag = inputArray[i].oMS.bDeafFlag;
						inputArray[j].oMS.bMuteFlag = inputArray[i].oMS.bMuteFlag;
						inputArray[j].oMS.bPersistentFlag = inputArray[i].oMS.bPersistentFlag;
						inputArray[j].oMS.bRetry = inputArray[i].oMS.bRetry;
						
						inputArray[j].oMS.nConferenceMaxParties = inputArray[i].oMS.nConferenceMaxParties;
						inputArray[j].oMS.nConferenceMaxTalkers = inputArray[i].oMS.nConferenceMaxTalkers;
						inputArray[j].oMS.nPacketizationPeriod = inputArray[i].oMS.nPacketizationPeriod;
						
						inputArray[j].oMS.strCallId = inputArray[i].oMS.strCallId;
						inputArray[j].oMS.strCodec = inputArray[i].oMS.strCodec;
						inputArray[j].oMS.strConferenceId = inputArray[i].oMS.strConferenceId;
						inputArray[j].oMS.strConnectionId = inputArray[i].oMS.strConnectionId;
						inputArray[j].oMS.strContent = inputArray[i].oMS.strContent;
						inputArray[j].oMS.strEndPoint = inputArray[i].oMS.strEndPoint;
						inputArray[j].oMS.strType = inputArray[i].oMS.strType;
						
						inputArray[j].bAttemptedToOutdial = inputArray[i].bAttemptedToOutdial;
						inputArray[j].bConnected = inputArray[i].bConnected;
						inputArray[j].bCurrentlyDialing = inputArray[i].bCurrentlyDialing;
						inputArray[j].bReverseFromTo = inputArray[i].bReverseFromTo;
						
						inputArray[j].nDestCountryId = inputArray[i].nDestCountryId;
						inputArray[j].nDestRegionId = inputArray[i].nDestRegionId;
						inputArray[j].nGracePeriod = inputArray[i].nGracePeriod;

						inputArray[j].strIntlDestFlag = inputArray[i].strIntlDestFlag;
						inputArray[j].strDestAreaCode = inputArray[i].strDestAreaCode;
						inputArray[j].strDestRouteType = inputArray[i].strDestRouteType;
						inputArray[j].nRingNoAnswerTime = inputArray[i].nRingNoAnswerTime;
						inputArray[j].nSessionTimer = inputArray[i].nSessionTimer;
						inputArray[j].nSetUpTime = inputArray[i].nSetUpTime;
						inputArray[j].nTearDownTime = inputArray[i].nTearDownTime;

						inputArray[j].strCallType = inputArray[i].strCallType;
						inputArray[j].strWiretapFlag = inputArray[i].strWiretapFlag;
						inputArray[j].strWiretapDest = inputArray[i].strWiretapDest;
						inputArray[j].strStatus = inputArray[i].strStatus;

						inputArray[j].lTimeAnswered = inputArray[i].lTimeAnswered;
						inputArray[j].lTimeStart = inputArray[i].lTimeStart;
						
						inputArray[j].strBackupRequestUri = inputArray[i].strBackupRequestUri;
						inputArray[j].strBillableFlag = inputArray[i].strBillableFlag;
						inputArray[j].strCalledNumber = inputArray[i].strCalledNumber;
						inputArray[j].strCallingNumber = inputArray[i].strCallingNumber;
						inputArray[j].strCallId = inputArray[i].strCallId;
						inputArray[j].strCodec = inputArray[i].strCodec;
						inputArray[j].strContact = inputArray[i].strContact;
						inputArray[j].strContent = inputArray[i].strContent;
						inputArray[j].strContentType = inputArray[i].strContentType;
						inputArray[j].strCSeq = inputArray[i].strCSeq;
						inputArray[j].strCSROutdialFlag = inputArray[i].strCSROutdialFlag;
						inputArray[j].strDestCallingCode = inputArray[i].strDestCallingCode;
						inputArray[j].strDestDomIntlFlag = inputArray[i].strDestDomIntlFlag;
						inputArray[j].strDestTrunkGroup = inputArray[i].strDestTrunkGroup;
						inputArray[j].strEnteredDestNbr = inputArray[i].strEnteredDestNbr;
						inputArray[j].strEventId = inputArray[i].strEventId;
						inputArray[j].strFrom = inputArray[i].strFrom;
						inputArray[j].strOriginalContent = inputArray[i].strOriginalContent;
						inputArray[j].strOrigTrunkGroup = inputArray[i].strOrigTrunkGroup;
						inputArray[j].strOutdialDestNbr = inputArray[i].strOutdialDestNbr;
						inputArray[j].strRecordRoute = inputArray[i].strRecordRoute;
						inputArray[j].strRequestUri = inputArray[i].strRequestUri;
						inputArray[j].strRoute = inputArray[i].strRoute;
						inputArray[j].strSIPStatus = inputArray[i].strSIPStatus;
						inputArray[j].strConfCallFlag = inputArray[i].strConfCallFlag;
						inputArray[j].strStrippedDest = inputArray[i].strStrippedDest;
						inputArray[j].strSessionTimerFlag = inputArray[i].strSessionTimerFlag;
						inputArray[j].strTo = inputArray[i].strTo;
						inputArray[j].strVia = inputArray[i].strVia;
						
			 				
			 			//restore
			 			inputArray[i].bReverseFromTo = tempObject.bReverseFromTo;
			 			
			 			inputArray[i].oMS.bCurrentlyConnected = tempMS.bCurrentlyConnected;
						inputArray[i].oMS.bCurrentlyInConf = tempMS.bCurrentlyInConf;
						inputArray[i].oMS.bDeafFlag = tempMS.bDeafFlag;
						inputArray[i].oMS.bMuteFlag = tempMS.bMuteFlag;
						inputArray[i].oMS.bPersistentFlag = tempMS.bPersistentFlag;
						inputArray[i].oMS.bRetry = tempMS.bRetry;
						
						inputArray[i].oMS.nConferenceMaxParties = tempMS.nConferenceMaxParties;
						inputArray[i].oMS.nConferenceMaxTalkers = tempMS.nConferenceMaxTalkers;
						inputArray[i].oMS.intPacketizationPeriod = tempMS.nPacketizationPeriod;
						
						inputArray[i].oMS.strCallId = tempMS.strCallId;
						inputArray[i].oMS.strCodec = tempMS.strCodec;
						inputArray[i].oMS.strConferenceId = tempMS.strConferenceId;
						inputArray[i].oMS.strConnectionId = tempMS.strConnectionId;
						inputArray[i].oMS.strContent = tempMS.strContent;
						inputArray[i].oMS.strEndPoint = tempMS.strEndPoint;
						inputArray[i].oMS.strType = tempMS.strType;
						
						inputArray[i].bAttemptedToOutdial = tempObject.bAttemptedToOutdial;
						inputArray[i].bConnected = tempObject.bConnected;
						inputArray[i].bCurrentlyDialing = tempObject.bCurrentlyDialing;
						inputArray[i].bReverseFromTo = tempObject.bReverseFromTo;
						
						inputArray[i].nDestCountryId = tempObject.nDestCountryId;
						inputArray[i].nDestRegionId = tempObject.nDestRegionId;
						inputArray[i].nGracePeriod = tempObject.nGracePeriod;

						inputArray[i].strIntlDestFlag = tempObject.strIntlDestFlag;
						inputArray[i].strDestAreaCode = tempObject.strDestAreaCode;
						inputArray[i].strDestRouteType = tempObject.strDestRouteType;
						inputArray[i].nRingNoAnswerTime = tempObject.nRingNoAnswerTime;
						inputArray[i].nSessionTimer = tempObject.nSessionTimer;
						inputArray[i].nSetUpTime = tempObject.nSetUpTime;
						inputArray[i].nTearDownTime = tempObject.nTearDownTime;

						inputArray[i].strCallType = tempObject.strCallType;
						inputArray[i].strWiretapFlag = tempObject.strWiretapFlag;
						inputArray[i].strWiretapDest = tempObject.strWiretapDest;
						inputArray[i].strStatus = tempObject.strStatus;

						inputArray[i].lTimeAnswered = tempObject.lTimeAnswered;
						inputArray[i].lTimeStart = tempObject.lTimeStart;
						
						inputArray[i].strBackupRequestUri = tempObject.strBackupRequestUri;
						inputArray[i].strBillableFlag = tempObject.strBillableFlag;
						inputArray[i].strCalledNumber = tempObject.strCalledNumber;
						inputArray[i].strCallingNumber = tempObject.strCallingNumber;
						inputArray[i].strCallId = tempObject.strCallId;
						inputArray[i].strCodec = tempObject.strCodec;
						inputArray[i].strContact = tempObject.strContact;
						inputArray[i].strContent = tempObject.strContent;
						inputArray[i].strContentType = tempObject.strContentType;
						inputArray[i].strCSeq = tempObject.strCSeq;
						inputArray[i].strCSROutdialFlag = tempObject.strCSROutdialFlag;
						inputArray[i].strDestCallingCode = tempObject.strDestCallingCode;
						inputArray[i].strDestDomIntlFlag = tempObject.strDestDomIntlFlag;
						inputArray[i].strDestTrunkGroup = tempObject.strDestTrunkGroup;
						inputArray[i].strEnteredDestNbr = tempObject.strEnteredDestNbr;
						inputArray[i].strEventId = tempObject.strEventId;
						inputArray[i].strFrom = tempObject.strFrom;
						inputArray[i].strOriginalContent = tempObject.strOriginalContent;
						inputArray[i].strOrigTrunkGroup = tempObject.strOrigTrunkGroup;
						inputArray[i].strOutdialDestNbr = tempObject.strOutdialDestNbr;
						inputArray[i].strRecordRoute = tempObject.strRecordRoute;
						inputArray[i].strRequestUri = tempObject.strRequestUri;
						inputArray[i].strRoute = tempObject.strRoute;
						inputArray[i].strSIPStatus = tempObject.strSIPStatus;
						inputArray[i].strConfCallFlag = tempObject.strConfCallFlag;
						inputArray[i].strStrippedDest = tempObject.strStrippedDest;
						inputArray[i].strSessionTimerFlag = tempObject.strSessionTimerFlag;
						inputArray[i].strTo = tempObject.strTo;
						inputArray[i].strVia = tempObject.strVia;
		 			}
			 		j++;
		 		} //done while loop
		 	} //end if match id
			if (bFound)
			{
				break;  //out of for loop
			}
		} //end for loop
	
		if (bFound)
		{	
			inputArray.length--;
		}
		else
		{
			Server.logInfo("WARNING: DOES NOT FIND CALL Id " + callid + " IN THE ARRAY.");
		}
	} //end array length > 0
}  //end js_remove_call_leg

function js_initCallLeg(&oCallLeg) {

	// Phone outdial call leg object
	oCallLeg.bAttemptedToOutdial = false;
	oCallLeg.bConnected = false;
	oCallLeg.bCurrentlyDialing = false;
	oCallLeg.bLegCallback = false;
	oCallLeg.bRefresher = false;
	oCallLeg.bReverseFromTo = false;
	oCallLeg.bUac = false;

	oCallLeg.fCallCost = 0;
	
	oCallLeg.nDestCountryId = 0;
	oCallLeg.nDestRegionId = 0;
	oCallLeg.nGracePeriod = 0;
	oCallLeg.nOrigCountryId = 0 ;
	oCallLeg.nRingNoAnswerTime = Session.s_nRING_NO_ANSWER_TIME;
	oCallLeg.nSessionTimer = 1800;
	oCallLeg.nSetUpTime = 0;
	oCallLeg.nTearDownTime = 0;
	
	oCallLeg.lTimeAnswered = 0;
	oCallLeg.lTimeStart = 0;
	oCallLeg.lTimeEnded = 0 ;

	oCallLeg.strBackupRequestUri = "";
	oCallLeg.strBillableFlag = "T";
	oCallLeg.strCalledNumber = "";
	oCallLeg.strCallingNumber = "";
	oCallLeg.strCallId = "";
	oCallLeg.strCSROutdialFlag = "F";
	oCallLeg.strConfCallFlag = "F";
	oCallLeg.strCodec = Session.s_strCodec;
	oCallLeg.strContact = "";
	oCallLeg.strContent = "";
	oCallLeg.strContentType = "";
	oCallLeg.strCSeq = "";
	oCallLeg.strDestAreaCode = "";
	oCallLeg.strDestCallingCode = "";
	oCallLeg.strDestDomIntlFlag = "F";
	oCallLeg.strDestTrunkGroup = "";
	oCallLeg.strEventId = "";
	oCallLeg.strFrom = "";
	oCallLeg.strFromUriUrlUser = "";
	oCallLeg.strOrigAreaCode = "" ;
	oCallLeg.strOrigCallingCode = "" ;
	oCallLeg.strOriginalContent = "";
	oCallLeg.strOrigTrunkGroup = "";
	oCallLeg.strOutdialDestNbr = "";
	oCallLeg.strOutdialDestNbrNoRouteCode = "";
	oCallLeg.strPrimaryRouteCode = ""; // Used for A leg. 
	oCallLeg.strRecordRoute = "";
	oCallLeg.strRefresher = "";
	oCallLeg.strRemoteCSeq = "";
	oCallLeg.strRemotePartyId = "";
	oCallLeg.strRemoteSdp = "";
	oCallLeg.strRemoteUri = "";
	oCallLeg.strRequestUri = "";
	oCallLeg.strRequire = "";
	oCallLeg.strRoute = "";
	oCallLeg.strSdp = "";
	oCallLeg.strSdpToOfferB = "";
	oCallLeg.strSessionTimerFlag = "";
	oCallLeg.strSIPStatus = "";
	oCallLeg.strStrippedDest = "";
	oCallLeg.strSupported = "";
	oCallLeg.strTo = "";
	oCallLeg.strVia = "";

	oCallLeg.b911 = false ;
	oCallLeg.b911_Forward = false ;
	oCallLeg.b911_ECRC = false ;

	// added to support session timer issue in calling card
	oCallLeg.lTimerGSXA_SleepTime = 0;
	
	return;
}

function js_initVoipCallLeg( &oCallLeg )
{
	js_initCallLeg(oCallLeg) ;
	oCallLeg.strCallIdInbound = "" ;
	oCallLeg.strCallIdOutbound = "" ;
	oCallLeg.strSvcProviderCallerId = "" ;
	oCallLeg.strSubscriberCallerId = "" ;
	oCallLeg.strEnteredDestNbr = "" ;
	oCallLeg.nRtpClockRate = 8000 ;
	oCallLeg.nPayloadType = 0 ;
	oCallLeg.nTerminationReason = 10 ; /* "Other" */
	oCallLeg.strRemoteSignalingAddress = "" ;
	oCallLeg.nRemoteSignalingPort = 5060 ;
	oCallLeg.strIntlDestFlag = "F"; 
	oCallLeg.strIntlOrigFlag = "F"; 
	oCallLeg.strOrigFlag = "" ;
	oCallLeg.strOrigRouteType = "" ;
	oCallLeg.strDestRouteType = "" ;
	oCallLeg.strScreenCallFlag = "F";
	oCallLeg.strWiretapFlag = "F" ;
	oCallLeg.strWiretapDest = "" ;
	
}

function js_initMS(&oMS, p_strMSType) {

	oMS.bCurrentlyConnected = false;
	oMS.bCurrentlyInConf = false;
	oMS.bDeafFlag = false;
	oMS.bDTMFClampFlag = true;
	oMS.bMuteFlag = false;
	oMS.bPersistentFlag = false;
	oMS.bRetry = false;
	oMS.bUseForACD = false;
	oMS.bUseForConf = false;
	oMS.bUseForMOH = false;

	oMS.nConferenceMaxParties = 125;
	oMS.nConferenceMaxTalkers = 6;
	oMS.nConfIndex = 0;
	oMS.nDBThreshold = 0;
	oMS.nNbrActiveSpeakers = 0;
	oMS.nPacketizationPeriod = Session.s_nPacketizationPeriod;
	oMS.nReportingInterval = 0;

	
	oMS.strCallId = "";
	oMS.strCodec = Session.s_strCodec;
	oMS.strConferenceId = "";
	oMS.strConnectionId = "";
	oMS.strContent = "";
	oMS.strEndPoint = "";
	oMS.strSessionId = "";
	oMS.strACDType = p_strMSType;
	oMS.strConfType = p_strMSType;
	oMS.strMOHType = p_strMSType;
	oMS.strType = p_strMSType;
	
	return;
}

function js_initAPI(&oAPI)
{
	oAPI.strAllowZeroCostFlag = "F";
	oAPI.bLastCallRedial = false;
	oAPI.bSvcDirectCallFlag = false;
        oAPI.nSvcDirectCallIdLength = 0;
	oAPI.nLongCallInterval = 0;
	oAPI.bLongCallIntervalTimerFlag = false;
	oAPI.strLastDestination = "";
	oAPI.nMaxRedialsAllowed = 0;
	oAPI.nTotalRedials = 0;
	oAPI.bAnnounceCurrencyAsUnits = false; // indicates whether balances will be played as units.
	oAPI.bALegCallback = false;
	oAPI.bALegRatingOn = false;
	oAPI.bANCallbackFlag = false; // indicates whether the access number is a callback number
	oAPI.bANDirectCallFlag = false; // indicates whether the access number is a direct call number
	oAPI.bANCallbackNoAniCollectPin = false; // indicates whether the application should collect a pin if ANI is missing or not set up for Access number call back. Set by psAPIASceInitializeSession.
	oAPI.bApplyPayphoneSurcharges = false;
	oAPI.bApplyPrecallRounding = false;
	oAPI.bBeginAConference = false;	//successfully add third party into conference, reset when reoriginate
	oAPI.bBlindCallback = false; // flag to determin if the session was initiated by a Message Initiated Callback, where the subscriber was prompted to enter their pin.
	oAPI.bChargeForIVRTime = false; // flag to determin if the app will charge the subscriber when the subscriber does not complete a billed call.
	oAPI.bCollectPinForCallback = false; // if  (bANCallbackFlag == true AND (ANI is missing OR not set for One Stage Dialing) AND bANCallbackNoAniCollectPin = true) this is true. Set by callingcard.xml 
	oAPI.bIVRCallbackAllowed = false;
	oAPI.bPayphoneSwipeFlag = false;
	oAPI.bPctMaxCallDuration = false; // indicates if the application is overriding the normal max call duration and using a % of the max call duration.
	oAPI.bRateByAccessNumber = false;
	oAPI.bSuppressOnHold = false; // indicates if the application should not send an On Hold Invite when connecting/disconnecting from a MS end point.
	oAPI.bWriteAtleastOneCDR = false;
	oAPI.bTryReceptionist = false; //ebb - if set to true, attempt to route the call to a receptionist phone before sending to the IVR.
        oAPI.bNoAnnouncedChargeCall = false;
        oAPI.bRateBySequence = false;
        oAPI.bFirstBilledAsFirstUse = false;
        oAPI.bIvrAniReg = false;
        oAPI.bRelayMedia = true ;       //in bbtel, if false SBC routes signaling only
        oAPI.bOutdial = true;
	
	oAPI.fCreditLimit = 0;
	oAPI.fCreditLimitWarningThreshold = 0; // The application will call a drop to java method that sends an email, if the credit limit falls below this threshold upon session completion.
	oAPI.fCreditUsed = 0;
	
	oAPI.nAccessNumberId = 0;
        oAPI.nAccessNumberGroupId = 0;
	oAPI.nAccessNumberType = 0;	//1=speaker, 2=non-speaker, 3=comm-line, 4=passcode
	oAPI.nAccountNumLength = 10;
	oAPI.nBadAniTimeToLive = 0;
	oAPI.nCallSequence = 1;
	oAPI.nCallTerminationReason = 10;
	oAPI.nCurrentNumberCallLegs = 1;
	oAPI.nDeptId = 0;
	oAPI.nDeptIdOut = 0;
	oAPI.nDestCountryId = 0;
	oAPI.nDestRegionId = 0;
	oAPI.nDialingPlanId = 1;
	oAPI.nDisconnectPrompt = 0;
	oAPI.nDomMinConnectTime = 10;
	oAPI.nGracePeriod = 0;
	oAPI.nGWPortEgress = 0;
	oAPI.nGWPortIngress = 0;
	oAPI.nIntlMinConnectTime = 10;
	oAPI.nLocalGMTOffset = 0;
	oAPI.nMaxAllowedSeconds = 0;
	oAPI.nMaxLoginAttempts = 3;
	oAPI.nMaxPartiesAllowed = 0;
	oAPI.nMaxTalkers = 0;
	oAPI.nMinBillingDurationInSec = 0;
	oAPI.nMode = 0;
	oAPI.nNumberLoginAttempts = 0;
	oAPI.nOrigCountryId = 1;
	oAPI.nOrigRegionId = 0;
	oAPI.nPinLength = 0;
	oAPI.nPostDestPromptIndex = 0;
	oAPI.nPostPinPromptIndex = 0;
	oAPI.nPromptFirstUseIndex = 0;
	oAPI.nPromptLanguageMenuIndex = 0;
	oAPI.nPromptRingback = 341;
	oAPI.nRoundingSeconds = 0;
	oAPI.nRTPClockRate = 8000;
	oAPI.nRTPEncoding = 0;
	oAPI.nServiceElementId = 0;  //1 = prepaid, 3 = postpaid, 0 = unknown
	oAPI.nStatePriorToCSR = 0;
	oAPI.nSessionTerminationReason = 10;
	oAPI.nSessionType = 1;
	oAPI.nThirdPartyOutdialTimeout = 0;
	oAPI.nWarningThreshold1 = 0;
	oAPI.nWarningThreshold2 = 0;
	oAPI.nWarningThreshold3 = 0;
	oAPI.nWelcomePromptIndex = 0;
        oAPI.nPromptFreeCallIndex = 0;
	oAPI.nBillingType = 2 ; //1=prepaid, 2=postpaid
	oAPI.nPostpaidPinLength = 0;
	oAPI.nPrepaidPinLength = 0;
        oAPI.nPromptAuthAniReqIndex = 0;
        oAPI.nPromptCallFromInvalidAniIndex = 0;
        oAPI.nPromptPostTimeDisclaimerIndex = 0;
	oAPI.nCommitInterval = 0;
	oAPI.nBillingLevel = 0;
	oAPI.nCurrencyPrecision = 0;
	oAPI.nSipPort = Server.sipPort ;
        oAPI.nPromptFirstBillCycleIndex= 0;
        oAPI.nPromptOffPlanAlertIndex= 0;
        oAPI.nPromptOnPlanMeteredAlertIndex= 0;
	oAPI.nPopdAcctNumberLength = 0;
	oAPI.nPrpdPinNumberLength = 0;
	oAPI.bSharedAccessNumber = false;
	oAPI.lDeptId = 0;
	oAPI.lPlatformSessionStartTime = Server.getUTCTime();
	oAPI.lServiceProviderId = 0;
	oAPI.lTimeAnswered = 0;
	oAPI.lTimeEnded = 0;
	oAPI.lTimeStart = 0;
        oAPI.lExpDateMs = 0;
	oAPI.lPhoneNumberId = 0 ;
	
	oAPI.strAccessNumber = "";
	oAPI.strAniToSend = "" ;
	oAPI.strAnonymousAttendeeId = "";
	oAPI.strApplicationName = Session._appName;
	oAPI.strAutoAssign = "T";  // F is for Event Conferences ONLY
	oAPI.strBackupSoftswitchIP = "";
	oAPI.strBadANIRouteToCSR = "F";
	oAPI.strCallInitiationType = "7"; // Outdial Via IVR
	oAPI.strCallType = "";
	oAPI.strConfEventId = "";
	oAPI.strConfScheduleId = "";
	oAPI.strConfStartTime
	oAPI.strCreditLimitFlag = "F";
	oAPI.strCreditLimitFlagFirm = "F";
	oAPI.strCustomerCode = Session.s_strCustomerCode;
	oAPI.strCustomerServicePhone = "";
	oAPI.strDBSessionId = "";
	oAPI.strDestAreaCode = "";
	oAPI.strDestCallingCode = "";
	oAPI.strDestDomIntlFlag = "F";
	oAPI.strDestinationNumber = "";
	oAPI.strDisconnectPromptType = "";
	oAPI.strDisconnectPromptUrl = "";
	oAPI.strEmailEvents = ""; // Application will call psAPISceMisc.prepareAndSendEmails, if this value is not null or empty after calling psAPIScePopdSessionComp.procSessionCompletion
	oAPI.strEventId = "";
	oAPI.strGWIPEgress = "";
	oAPI.strGWIPIngress = "";
	oAPI.strIgnoreAniAuthRequired = "F";
	oAPI.strInfoDigits = "";
	oAPI.strLanguage = "eng";
	oAPI.strLanguageOption1 = "";
	oAPI.strLanguageOption2 = "";
	oAPI.strLanguageOption3 = "";
	oAPI.strLanguageOption4 = "";
	oAPI.strLanguageOption5 = "";
	oAPI.strLanguageOption6 = "";
	oAPI.strLanguageOption7 = "";
	oAPI.strLanguageOption8 = "";
	oAPI.strLanguageOption9 = "";
	oAPI.strLocalHost = Session.s_strLocalHost;
	oAPI.strAppServerContact = Session.s_strLocalHost;
	oAPI.strMainMenuFlag = "F";
	oAPI.strMSResourceName = "";
	oAPI.strOlisToStrip = ""; // Will contain a comma delimited list of OLIs that should be stripped. eg: 27,30,70.
	oAPI.strOrigAreaCode = "";
	oAPI.strOrigCallingCode = "";
	oAPI.strOrigDomIntFlag = "F";
	oAPI.strOriginationNumber = "";
	oAPI.strOriginationType = "6"; // Two Stage Dialing
	oAPI.strOutdialCLI = "";
	oAPI.strOutdialDestNumber = ""; // Should use the one in the CallLeg Object
	oAPI.strOutdialOperatorFlag = "F";
	oAPI.strPayPhoneFlag = "F";
	oAPI.strPhoneMeFlag = "F";
	oAPI.strPlatformSessionId = Session._sessionId;
	oAPI.strPostDestPromptType = "";
	oAPI.strPostDestPromptUrl = "";
	oAPI.strPostPaidFlag = "F";
	oAPI.strPostPinPromptType = "";
	oAPI.strPostPinPromptUrl = "";
	oAPI.strPrimarySoftswitchIP = "";
	oAPI.strPrimaryRouteCode = "";
	oAPI.strPromptFirstUseType = "";
	oAPI.strPromptFirstUseUrl = "";
	oAPI.strProcDBName = "pactolusdb";
	oAPI.strPromptLanguageMenuUrl = "";
	oAPI.strRateCallsFlag = "F";
	oAPI.strScreenCallFlag = "F";
	oAPI.strSIPStatus = "";
	oAPI.strServiceProviderCode = "";
	oAPI.strStrippedDest = "";
	oAPI.strStrippedOrigNumber = "";
	oAPI.strSubRestrictionsFlag = "F";
	oAPI.strThirdPartyOutdialNumber = "";
	oAPI.strUsedDefaultANIFlag = "F";
	oAPI.strValidateByANI = "0";
	oAPI.strWelcomePromptType = "";
	oAPI.strWelcomePromptUrl = "";
	oAPI.strWarningAsToneFlag = "F";
	oAPI.strPromptFreeCall = "";
        oAPI.strPromptFreeCallUrl = "";
	oAPI.strOutboundCIC = "";
	oAPI.strSharedUseBillingFlag = "F";
	oAPI.strEmergencyCallId = "" ;
	oAPI.strBroadbandCallingFlag = "F"; 
        oAPI.strPromptAuthAniReq = "";
        oAPI.strPromptAuthAniReqUrl = "";
        oAPI.strPromptCallFromInvalidAni = "";
        oAPI.strPromptCallFromInvalidAniUrl = "";
        oAPI.strPromptPostTimeDisclaimer = "";
        oAPI.strPromptPostTimeDisclaimerUrl = "";
	oAPI.strSipCallId = "" ;
	oAPI.strSipFromTag = "" ;
	oAPI.strSipToTag = "" ;
	oAPI.strSipAddress = Server.sipAddress ;
        oAPI.strPromptFirstBillCycle= "";
        oAPI.strPromptFirstBillCycleUrl= "";
        oAPI.strPromptOffPlanAlert= "";
        oAPI.strPromptOffPlanAlertUrl= "";
        oAPI.strPromptOnPlanMeteredAlert= "";
        oAPI.strPromptOnPlanMeteredAlertUrl = "";

	return;
}

function js_initACD(&oACD)
{
	oACD.bCSRAssignmentFlag = false;
	oACD.bCSRWithAB = false;
	
	oACD.nACDControllerPort = 0;
	oACD.nCSRRequestType = 0;
	oACD.nCSRRouteReasonCode = 0;
	oACD.nCSRRouteReasonCodeTemp = 0; // this is needed, until more route reasons are defined in the DB
	oACD.nReturnReasonCode = 0;
	oACD.nSoapPort = Session.s_nSoapPort;
	oACD.nOnHoldTime = 0; // in seconds
	
	oACD.lCSRAssignmentDuration = 0;
	oACD.lCSRAssignmentEndTime = 0;
	//oACD.lCSRAssignmentGroupId = 0;  do not use, use lCSRRequestGroupId
	oACD.lCSRAssignmentId = 0;
	//oACD.lCSRAssignmentLanguageId = 0; do not use use lCSRRequestLanguageId 
	//oACD.lCSRAssignmentSkillId = 0; do not use, use lCSRRequestSkillId.
	oACD.lCSRAssignmentStartTime = 0;
	// oACD.lCSRAssignmentUserId = 0; do not use, use lCSRUserId;
	oACD.lCSRRequestGroupId = 0;
	oACD.lCSRRequestId = 0;
	oACD.lCSRRequestLanguageId = 0;
	//oACD.lCSRRequestOfferingId = 0; do not use, use SUB.nPrimaryOfferingId
	//oACD.lCSRRequestServiceId = 0; do not use, use SUB.nServiceId
	oACD.lCSRRequestSkillId = 0;
	oACD.lCSRUserId = 0;
	oACD.lRequestEndTime = 0;
	oACD.lRequestStartTime = 0;
	
	oACD.strACDControllerIP = "";
	oACD.strCSRAccessNumberName = "";
	oACD.strCSRAppServerIP = "";
	oACD.strCSRAssignmentAction = "";

	//oACD.strCSRAssignmentGroupName = ""; use strCSRRequestGroupName
	//oACD.strCSRAssignmentLanguageName = ""; strCSRRequestLanguageName
	//oACD.strCSRAssignmentSkillName = ""; strCSRRequestSkillName

	//oACD.strCSRAssignmentType = ""; use strCSRRequestTypeName
	oACD.strCSRDidOutdial = "F";
	oACD.strCSROnMaxLoginsEnabled = "F";
	oACD.strCSRRequestEndPoint = "";
	oACD.strCSRRequestGroupName = "";
	oACD.strCSRRequestLanguageName = "";
	oACD.strCSRRequestOfferingName = "";
	oACD.strCSRRequestPrivateTalk = "F";
	oACD.strCSRRequestServiceName = "";
	oACD.strCSRRequestSkillName = "";
	oACD.strCSRRequestTypeName = "";
	oACD.strCSRServiceProviderName = "";
	oACD.strCSRType = "C";
	oACD.strCustomerServicePhone = "";
	oACD.strEnableFlag = "F";
	oACD.strGreetEnabled = "F";
	oACD.strMusicOnHoldFlag = "F";
	oACD.strOutdialBLegPhoneNumber = "";
	oACD.strReturnMsg = "";
	oACD.strTransactionId = "";

	oACD.strPinZeroOut = "F";
	oACD.strPinMaxInvalids = "F";
	oACD.strPinMaxTimeouts = "F";

	oACD.strMainMenuZeroOut = "F";
	oACD.strMainMenuMaxInvalids = "F";
	oACD.strMainMenuMaxTimeouts = "F";

	oACD.strDestZeroOut = "F";
	oACD.strDestMaxInvalids = "F";
	oACD.strDestMaxTimeouts = "F";

	oACD.strLanguageMenuZeroOut = "F";
	oACD.strLanguageMenuMaxInvalids = "F";
	oACD.strLanguageMenuMaxTimeouts = "F";

	

	return;
}

function js_initTimer(&p_oTimer) {

	p_oTimer.bOn = false;
	
	p_oTimer.nThresholdTime = 0;
	
	p_oTimer.lSleepTime = 0;
	p_oTimer.lTimerId = 0;

	return;
}

function js_initSTB(&oSTB) {

	oSTB.lSecondsBalance00 = -1;
	oSTB.lSecondsUsed01 = -1;
	oSTB.strDescription02 = "";
	oSTB.lBucketSize03 = -1;
	oSTB.strLinkedFlag04 = "";
	
	oSTB.lSecondsBalance10 = -1;
	oSTB.lSecondsUsed11 = -1;
	oSTB.strDescription12 = "";
	oSTB.lBucketSize13 = -1;
	oSTB.strLinkedFlag14 = "";

	oSTB.lSecondsBalance20 = -1;
	oSTB.lSecondsUsed21 = -1;
	oSTB.strDescription22 = "";
	oSTB.lBucketSize23 = -1;
	oSTB.strLinkedFlag24 = "";
	oSTB.lSecondsBalance30 = -1;
	oSTB.lSecondsUsed31 = -1;
	oSTB.strDescription32 = "";
	oSTB.lBucketSize33 = -1;
	oSTB.strLinkedFlag34 = "";
	oSTB.lSecondsBalance40 = -1;
	oSTB.lSecondsUsed41 = -1;
	oSTB.strDescription42 = "";
	oSTB.lBucketSize43 = -1;
	oSTB.strLinkedFlag44 = "";
	oSTB.lSecondsBalance50 = -1;
	oSTB.lSecondsUsed51 = -1;
	oSTB.strDescription52 = "";
	oSTB.lBucketSize53 = -1;
	oSTB.strLinkedFlag54 = "";
	oSTB.lSecondsBalance60 = -1;
	oSTB.lSecondsUsed61 = -1;
	oSTB.strDescription62 = "";
	oSTB.lBucketSize63 = -1;
	oSTB.strLinkedFlag64 = "";
	oSTB.lSecondsBalance70 = -1;
	oSTB.lSecondsUsed71 = -1;
	oSTB.strDescription72 = "";
	oSTB.lBucketSize73 = -1;
	oSTB.strLinkedFlag74 = "";
	oSTB.lSecondsBalance80 = -1;
	oSTB.lSecondsUsed81 = -1;
	oSTB.strDescription82 = "";
	oSTB.lBucketSize83 = -1;
	oSTB.strLinkedFlag84 = "";
	oSTB.lSecondsBalance90 = -1;
	oSTB.lSecondsUsed91 = -1;
	oSTB.strDescription92 = "";
	oSTB.lBucketSize93 = -1;
	oSTB.strLinkedFlag94 = "";
	return;
}
function js_initSUB(&oSUB) {

	oSUB.bCallbackNumberEnable = false;
	oSUB.bLinked = false; // indicates if sub bucket balance is linked to Parent Bucket
        oSUB.bFirstUse = false;

        //outputs from auth dest, if true callflow should play the prompts
        oSUB.bPlayFirstBillCycle = false;
        oSUB.bPlayOnPlanMeteredAlert = false;
        oSUB.bPlayOffPlanAlert = false;

        //product offering settings for prompts
        oSUB.bFirstBillCycleEn = false;
        oSUB.nOnPlanMeteredAlertMax = 0;
        oSUB.nOffPlanAlertMax = 0;

        //subscriber values for prompts
        oSUB.bFirstBillCycleFlag = false;
        oSUB.nOnPlanMeteredAlertCount = 0;
        oSUB.nOffPlanAlertCount = 0;

	oSUB.fCallMarkupPercent = 0;
	oSUB.fPrepaidBalance = 0;
	
	oSUB.nCurrencyId = 0;
	oSUB.nDisabledReasonCode = 0;
	oSUB.nExpirationType = 0;
	oSUB.nNumExpDays = 0;
	oSUB.nPinLockFlag = 1;
	oSUB.nPrimaryOfferingId = 0;
	oSUB.nRoundingThreshold = 0;
	oSUB.nSecondsAnnounce = 0;
	oSUB.nSecondsAvailable = 0;
	oSUB.nSecsRemaining = 0;
	oSUB.nServiceId = 1;
        oSUB.nSecondsUntilBillingCycle = -1;
        oSUB.nPrecallPromptSetting = -1;
	
	oSUB.lCorpAcctNumber = 0;
	oSUB.lPinId = 0;
	oSUB.lSubscriberId = 0;
	oSUB.lStoredBalanceId = 0; //For Subrate bucket balance
	oSUB.lMinutesToNextBucketThreshold = 0; //For playing of time remaining Threshold prompts , SubUcket
	oSUB.strAccountNumber = "";
	oSUB.strCallbackNumber = "";
	oSUB.strCallerFirstName = "";
	oSUB.strCallerLastName = "";
	oSUB.strDigitMapAccountNbr = "";
	oSUB.strExpDate = "";
	oSUB.strFirstCallFlag = "";
	oSUB.strModPassCode = "";
	oSUB.strPinDigitMap = "";
	oSUB.strPinLanguage = "eng";
	oSUB.strPinLockFlag = "F";
	oSUB.strPinLockId = "";
	oSUB.strPinPassword = "";
	oSUB.strPlayBalanceFlag = "";
	oSUB.strPlayTimeAvailableFlag = "";
	oSUB.strUserPin = "";
        oSUB.strBillingId = "";

        // Direct Call - Subscriber level.

	oSUB.bSubDirectCallFlag = false;	
        oSUB.strDirectCallDigitMap = "";
        oSUB.strDirectCallId = "";
    
        // bucket of minute rate variables ...    
        oSUB.lSecsToFirstBucketThreshold = 0; //For playing of SubBucket and PrimaryBucket Threshold tones
        oSUB.lSecsBetweenBucketThresholds = 0;  // As above but for subsequent buckets
        oSUB.nSecondsBalance = 0;
        oSUB.nSecondsUsed = 0;
        oSUB.bBucketOfRatesFlag = false;
        oSUB.nTotalBucketSize = 0;
        oSUB.nBucketSize = 0;
        oSUB.nDailySecondsUsed = 0;
        oSUB.nDailyUsageThreshold = 0;
        oSUB.nBillingCycleDay = 0;
	oSUB.strBucketRefillWarningFlag = "F";
	oSUB.strBucketExhaustWarningFlag = "F";
	oSUB.strPlayBucketRefillWarning = "F";
	oSUB.strPlayBucketExhaustWarning = "F";
	oSUB.strFeatureCodeCallType = "";
	oSUB.strBucketRefillNotifyFlag = "F";
	oSUB.strBucketExhaustNotifyFlag = "F";
	oSUB.sPlaySubBucketWarnFlag = "F";
	
	oSUB.lBillingSessionId = 0;
	oSUB.lReservationId = 0;
	oSUB.nReservedSeconds = 0;

	return;
}

function js_initVoipAccessLine(&al)
{
	js_initSUB( al.oSub ) ;

	al.lAccessLineId = 0 ;
	al.lPhoneNumberId = 0 ;
	al.strCallerIdBlockFlag = "F" ;
	al.strCodec = "" ;
	al.strActivationPendingFlag = "F" ;
	al.strActivationPin = "" ;
	al.strAniToSend = "" ;
	al.strWiretapFlag = "F"; 
	al.strWiretapDest = "" ;
	al.strRealm = "" ;
	al.strUserName = "" ;
	al.strPassword = "" ;
	al.strUri = "" ;
	al.strNonce = "" ;
	al.strResponse = "" ;
	al.nMinTimeAnnounce = 0 ;
}

function js_initVoipService(&svc)
{
	svc.iServiceId = 0 ;

	svc.strPreActivationPromptType = "" ;
	svc.nPreActivationPromptIndex = 0 ;
	svc.strPreActivationPromptUrl = "" ;
	
	svc.strSuccessActivationPromptType = "" ;
	svc.nSuccessActivationPromptIndex = 0 ;
	svc.strSuccessActivationPromptUrl = "" ;

	svc.strFailActivationPromptType = "" ;
	svc.nFailActivationPromptIndex = 0 ;
	svc.strFailActivationPromptUrl = "" ;

	svc.nMaxCallDuration = 0 ;
}

function js_initRATE(&oRATE) {
	//oRATE.fAlegMeteredRate = 0.0; Can't set a float here, if we do when this value is set to 0.1 thru 0.9 it will stay 0 in javascript.
	oRATE.fAccessNumberRateAmt = 0;
	oRATE.fAccessNumberRate2Amt = 0;
	oRATE.fBongAmount = 0;
	oRATE.fBongCharge1Amt = 0;
	oRATE.fBongCharge2Amt = 0;
	oRATE.fBongCharge3Amt = 0;
	oRATE.fConnectionFee = 0;
	oRATE.fDomDestRateAmt = 0;
	oRATE.fDomOrigAndDestRateAmt = 0;
	oRATE.fDomOrigAndDestTDWRateAmt = 0;
	oRATE.fFirstUseRateAmt = 0;
	oRATE.fFixedRateAmt1 = 0;
	oRATE.fIntlDestRateAmt  = 0;
	oRATE.fIntlOrigRateAmt = 0;
	oRATE.fMeteredRateAmt1 = 0;
	oRATE.fPayphoneSrchgRateAmt = 0;
	oRATE.fTier1RateAmt = 0;
	oRATE.fTier2RateAmt = 0;
	oRATE.fTier3RateAmt = 0;
	oRATE.fTotalFixedRate = 0;
	oRATE.fTotalMeteredRate = 0;
	oRATE.fPayphone1RateAmt = 0;
	oRATE.fPayphone2RateAmt = 0;
	
	oRATE.nAccessNumberRateId = 0;
	oRATE.nAccessNumberRateType = 0;
	oRATE.nAccessNumberRate2Id = 0;
	oRATE.nAccessNumberRate2Type = 0;
	oRATE.nBongCharge1Time = 0;
	oRATE.nBongCharge2Time = 0;
	oRATE.nBongCharge3Time = 0;
	oRATE.nDomDestRateId = 0;
	oRATE.nDomOrigAndDestRateId = 0;
	oRATE.nDomOrigAndDestTDWRateId = 0;
	oRATE.nFirstUseRateId = 0;
	oRATE.nFixedRateId1 = 0;
	oRATE.nExtBongInitialTimeSecs = 0;
	oRATE.nExtBongIntervalSecs = 0;
	oRATE.nIntlDestRateId  = 0;
	oRATE.nIntlOrigRateId = 0;
	oRATE.nMeteredRateId1 = 0;
	oRATE.nPayphone1RateId = 0; // was oRATE.nPayphoneSrchgRateId = 0;
	oRATE.nPayphone1RateType = 0;
	oRATE.nPayphone2RateId = 0; 
	oRATE.nPayphone2RateType = 0;
	oRATE.nTier2ThresholdSecs = 0;
	oRATE.nTier3ThresholdSecs = 0;
	
	oRATE.strExtBongsFlag = "F";
	oRATE.strExtBongsNumCharges = "";
	oRATE.strTieredRatesFlag = "";

        oRATE.bDualRating = false;

	return;
}

function js_initBALXFER(&oBALXFER) {

	oBALXFER.bMaxAttempts = false;

	oBALXFER.fBalXferBalance = 0;

	oBALXFER.nAttempts = 0;	
	oBALXFER.nFromCurrencyId = "";
	oBALXFER.nLockSubscriber = 1;
	
	oBALXFER.lBalXferSubId = -1;
	
	oBALXFER.strBalanceTransferFlag = "";
	oBALXFER.strBalXferPin = "";
	oBALXFER.strDisabledReasonCode = "";
	oBALXFER.strDisableSubFlag = "";

	return;

}

function js_initCC(&oCC) {

	oCC.bPayoffEntireCreditBalance = false; //indicates if the entire postpaid credit balance should be paid off

	oCC.fCCMaxMonth = 0;
	oCC.fCCMaxWeek = 0;
	oCC.fMaxPrepaidBalance = 500;
	oCC.fMaxRechargeAmount = 0;
	oCC.fMinPrepaidBalance = 0;
	oCC.fMinRechargeAmount = 0;
	oCC.fRechargeAmount = 0;
	oCC.fRechargeFee = "";
	oCC.fRechargePlusFee = 0; // will pass back the amount recharged, inclusive of the credit fee
	oCC.fSubMaxCCMonth = 500;
	oCC.fSubMaxCCWeek = 500;
	
	oCC.nCreditCardId = 0;
	
	oCC.strCCNum = "";
	oCC.strExpDate = "";
	oCC.strRechargableFlag = "";
	
	return;
}

	

function js_initTimerPrompts(&p_oTimer) {

	p_oTimer.nPrompt1 = 99;
	p_oTimer.nTime = 0;
	p_oTimer.nPrompt2 = 46;
	p_oTimer.nPrompt3 = 99;
	p_oTimer.nMaxThresholdPrompt = 0;
	return;
}

function js_initTEST(&oTEST) {

	oTEST.strConferenceTestFlag = "";
	oTEST.strCardServicesTestFlag = "";
	oTEST.strTestDestinationNumber = "";
	oTEST.strTestInfoDigits = "";
	oTEST.strTestOriginationNumber = "";
        oTEST.strTestToField = "";
        oTEST.strTestFromField = "";

	return;

}

function js_initMessageBasedCallbackTestValues(&oMessageBasedCallbackTestValues) {

	oMessageBasedCallbackTestValues.strOutdialDestNumber1 = "";
	oMessageBasedCallbackTestValues.strOutdialDestNumber2 = "";
	oMessageBasedCallbackTestValues.nTreatmentCode = 0;
	oMessageBasedCallbackTestValues.strServiceProviderCode = "";
	oMessageBasedCallbackTestValues.lServiceProviderId = 0;
	oMessageBasedCallbackTestValues.strPrepaidPinNbr = "";
	oMessageBasedCallbackTestValues.strPostpaidAcctNbr = "";
	oMessageBasedCallbackTestValues.strPostpaidPinNbr = "";

	return;

}

function js_initCSR_REASONS(&oCSR_REASONS) {

	oCSR_REASONS.nLanguageMenuZeroOut = 7;
	oCSR_REASONS.nLanguageMenuMaxTimeouts = 8;
	oCSR_REASONS.nLanguageMenuMaxInvalids = 9;
	oCSR_REASONS.nPinZeroOut = 10;
	oCSR_REASONS.nPinMaxTimeouts = 11;
	oCSR_REASONS.nPinMaxInvalids = 12;
	oCSR_REASONS.nMainMenuZeroOut = 16;
	oCSR_REASONS.nMainMenuMaxTimeouts = 17;
	oCSR_REASONS.nMainMenuMaxInvalids = 18;
	oCSR_REASONS.nDestZeroOut = 13;
	oCSR_REASONS.nDestMaxTimeouts = 14;
	oCSR_REASONS.nDestMaxInvalids = 15;
	return;

}


function js_initORIG_TYPES(&oORIG_TYPES) {

	oORIG_TYPES.strClickToDial = "1";
	oORIG_TYPES.strMessageCallback = "2";
	oORIG_TYPES.strOneStageDialing = "3";
	oORIG_TYPES.strAccessNumCallback = "4";
	oORIG_TYPES.strIvrCallback = "5";
	oORIG_TYPES.strTwoStageDialing = "6";
	oORIG_TYPES.strCheckMinutesBalance = "7";

	return;

}

function js_initINITIATION_TYPES(&oINITIATION_TYPES) {

	oINITIATION_TYPES.strALegIvrInitiatedCallback = "1";
	oINITIATION_TYPES.strALegClickToDialOut = "2";
	oINITIATION_TYPES.strALegMsgInitiatedCallback = "3";
	oINITIATION_TYPES.strALegAccessNbrIniatedCallback = "4";
	oINITIATION_TYPES.strALegOneStageDialing = "5";
	oINITIATION_TYPES.strBLegOneStageDialing = "6";
	oINITIATION_TYPES.strBLegOutdialViaIvr = "7";
	oINITIATION_TYPES.strBLegOutdialViaClickToDial = "8";
	oINITIATION_TYPES.strBLegOutdialViaMessageCallback = "9";
	oINITIATION_TYPES.strBLegOutdialViaAccessNbrInitiatedCallback = "10";
	oINITIATION_TYPES.strBLegOutdialViaRedial = "11";

	return;
}


 
function js_parse_value( instring, token, &value )
{
	/*search for tokens as lowercase, but get the values from the original case correct string */
	var lowercase_instring = instring.toString().toLowerCase();
	var lowercase_token = token.toString().toLowerCase();	

	/* search for token="value" in instring; between each token value pairs is ' ' OR ';' */

	var search_token = lowercase_token + "=" ;
	var i = lowercase_instring.indexOf( search_token ) ;
	if( -1 == i ) {
		return false ;
	}
	
	/* if next char is double quote, search for terminating double-quote */
	var pos = i + search_token.length ;
	var quoted = false ;
	if( '"' == instring.charAt(pos) ) {
		pos++ ;
		quoted = true ;
	}
	var endPos = pos ;
	while(  (quoted && '"' != instring.charAt(endPos) ) ||
		(!quoted && (' ' != instring.charAt(endPos) && ';' != instring.charAt(endPos) ) && endPos < instring.length ) )
		endPos++ ;

	value = instring.slice( pos, endPos ) ;

	return true ;	
}


/*******************************************************************
This function is used by a proxy app, which receives a request with a
route header and needs to calculate the request-uri to send the request to
next, and also needs to update the route header by removing our entry
********************************************************************/
function js_calculate_uri_and_route_as_proxy( &route, &request_uri )
{
	request_uri = "" ;
	var strRoute = new String( route ) ;
	var uri_array = strRoute.split(",") ;
	
	if( 0 == uri_array.length ) return ;

	/* the first element of the route header might be us, if so discard and get next */
	var url = new SipUrl( uri_array[0] ) ;
	if( Server.ipAddress == url.host &&
		(Server.sipPort == url.port || (5060 == Server.sipPort && url.port == undefined ) ) ) 
	{

		uri_array.shift() ;
		url = new SipUrl( uri_array[0] ) ;
	}
	request_uri = url.encode() ;
	uri_array.shift() ;
	route = uri_array.join(",") ;
} 


function js_disable_event()
{
	Server.enableEvents(false);
	Server.logInfo("EVENT DISABLED");
	return;
}

function js_enable_event()
{
	Server.enableEvents(true);
	Server.logInfo("EVENT ENABLED");
	return;
}

function js_new_remove_call_leg(&inputArray, strCallId) {

	Server.logInfo("**BEGIN js_new_remove_call_leg");
	Server.logInfo("REmove this: inputArray.length: " + inputArray.length + " strCallId: " + strCallId);
	var i = 0;
	var z = 0;
	var intIndex = -1;
	var aTemp = new Array();


	for(i = 0; i < inputArray.length; i++) {

 		if(inputArray[i].strCallId == strCallId) {
			intIndex = i;
	 	} 

		// Save each object in an array, so it can easily be removed, using the splice method
		aTemp[i] = inputArray[i];

	} // end for

	Server.logInfo("REmove this: aTemp.length: " + aTemp.length);	
	// Remove the call leg

	if(intIndex >= -1) {

		aTemp.splice(intIndex, 1);
		Server.logInfo("REmove this: aTemp.length: " + aTemp.length);	

		/* This syntax does not work: inputArray = aTemp;
		    Need to loop through and restore each one seperately;
		*/

		for(z = 0; z < aTemp.length; z++) {
			inputArray[z] = aTemp[z];
		}

		// Remove the last element. The inputArray still contains the leg that was spliced out and that leg is the last one in the array.
		inputArray.length--;

	} else {
		Server.logError("There was an error removing the call leg. Could not find this CallId in the array: " + strCallId  );
	} // if intIndex >= -1

	Server.logInfo("**END js_new_remove_call_leg");

	return intIndex;
}

function js_CreateUniqueCallId()
{
	var curtime = new Date;
	var NewCallId = new String ;

	/* put special prefix on originate and terminate bbtel callids */
	if( -1 != Session._appName.indexOf( "_terminate") ) {
		NewCallId = "pcst" ;
	}
	else if( -1 != Session._appName.indexOf( "_originate") ) {
		NewCallId = "pcso" ;
	}

	NewCallId += curtime.getTime().toString() ;
	NewCallId + "_" ;
	
	/* Session id is formatted like this: x.y.app.z@ip-address */
	var sid = new String( Session._sessionId );
	var s ;
	if( null == (s = Clib.strtok( sid.toString(), "." ) ) ) {
		return NewCallId ;
	}
	NewCallId += s ;
	
	if( null == (s = Clib.strtok( null, "." ) ) ) {
		return NewCallId ;
	}
	NewCallId += s ;

	var appname = Clib.strtok( null, "." ) ;
	if( null == (s = Clib.strtok( null, " " ) ) ) {
		return NewCallId ;
	}
	NewCallId += s ;
	return NewCallId ;
}

function js_modifySdpForCodec( &strSdp, codec )
{
	var anSdp = new Sdp( strSdp.toString() ) ;

	if( 0 == anSdp.media.length || 0 == anSdp.media[0].rtpMaps.length ) 
		return ;
		
	/* if the requested codec is already first, or is not in the list at all, return the Sdp unchanged */
	var idx = -1 ;
	for( var i = 0; i < anSdp.media[0].rtpMaps.length; i++ ) {
		if( codec == anSdp.media[0].rtpMaps[i].type ) {
			idx = i ;
			break ; 
		}
	}	
	
	if( 0 == idx || -1 == idx ) { 
		return ;
	}
	
	var newSdp = new Sdp( strSdp.toString() ) ;
	newSdp.media[0].rtpMaps.length = 0 ;
	newSdp.media[0].rtpMaps[0] = anSdp.media[0].rtpMaps[idx] ;
	
	for( var i = 1; i <= idx; i++ ) {
		newSdp.media[0].rtpMaps[i] = anSdp.media[0].rtpMaps[i-1] ;	
	}
	
	for( var i = idx + 1; i < anSdp.media[0].rtpMaps.length; i++ ) {
		newSdp.media[0].rtpMaps[i] = anSdp.media[0].rtpMaps[i] ;
	}	
	
	strSdp = newSdp.encode() ;
} 

/* replace every instance of searchString in inString with replaceString */
function js_replaceString( inString, searchString, replaceString )
{
	var s = new String ;
	var i ;
	var nStart = 0 ;
	
	//Server.logInfo("String passed in: " + inString ) ;
	//Server.logInfo("String to search for: " + searchString ) ;
	//Server.logInfo("String to replace with: " + replaceString ) ;
	
	while( -1 != ( i = inString.indexOf( searchString, nStart ) ) ) {
		//Server.logInfo("Offset of search string: " + i ) ;
		s += inString.slice( nStart, i ) ;
		s += replaceString ;
		nStart = i + searchString.length ;
	}
	s+= inString.substr( nStart ) ;
	//Server.logInfo("Returning string: " + s ) ;
	return s ;	
}


/* given an Sdp, replace the connection ip address and rtp port */
function js_modify_sdp_address_and_port( inSdp, address, port ) 
{
	var newSdp = new Sdp( inSdp.toString() ) ;
	newSdp.origin.address = address ;
	newSdp.connection.address = address ;
	
	if( port > 0 ) {
		for( var i = 0; i < newSdp.media.length; i++ ) {
			newSdp.media[i].port = port ;
		}
	}
	return newSdp.encode() ;
}

/* replace the sip address and port in the URI with the provided replacement address */
function js_modify_url_for_nat( inUrl, natAddress )
{
	var pos1, pos2 ;
	var outUrl = inUrl ;
	
	if( -1 == ( pos1 = inUrl.indexOf( "@" ) ) ) {
		if( -1 != ( pos1 = inUrl.indexOf("sip:") ) ) {
			pos1 += 4 ;
		}
	}
	else {
		pos1++ ;
	}
	
	if( -1 != pos1 ) {
		outUrl = inUrl.substr( 0, pos1 ) ;
		outUrl += natAddress ;
		
		var bFound = false ;
		var i ;
		for( i = pos1; 	!bFound && i < inUrl.length; i++ ) {
			if( ';' == inUrl.charAt(i) || ' ' == inUrl.charAt(i) || '>' == inUrl.charAt(i) || 
				'\n' == inUrl.charAt(i) || '\r' == inUrl.charAt(i) ) {

				outUrl += inUrl.substr(i) ;
				bFound = true ;
			}
		}
	}	

	return outUrl ;
}

/* add a nat parameter to the URI with the provided address */
function js_modify_url_for_nat2( inUrl, natAddress )
{
	var pos1, pos2 ;
	var outUrl = inUrl ;
	
	if( -1 == ( pos1 = inUrl.indexOf( ">" ) ) ) {
		outUrl = inUrl ;
		outUrl += ";nat=" ;
		outUrl += natAddress ;
	}
	else {
		outUrl = inUrl.substr( 0, pos1 ) ;
		outUrl += ";nat=" ;
		outUrl += natAddress ;
		outUrl += ">" ;
		outUrl += inUrl.substr(++pos1) ;
	}
	
	return outUrl ;
}

/* calculate time remaining for collection of BLegs, the first of which may be   */
/* complex - ie. have tiered rates, have multiple fixed bong charges or extended */
/* bong charges.                                                                 */
function timeRemaining(dbPrepaidBalance, BLegs, roundingSeconds, &timeRemaining)
{
	// Calculate the time remaining given the passed prepaid balance and set of B Legs.
	// Part 1 - calculate the incurred cost of the B Legs and deduct from the passed
	// prepaid balance deriving the true current prepaid balance.  Part 2, calculate
	// how much time that prepaid balance will allow the call to continue with the
	// current set of B Legs.

	var curTime = 0;					// Current time in seconds.
	var rc = 0;							// Return code.
	
	// Attributes of a complex B Leg, if any.  Populated during Part 1 - used in
	// Part 2.  There can be only 1 complex B Leg.  If it exists, it must be the
	// first B Leg in the passed set of B Legs.  Additionally, it must use either
	// tiered rating or bong charges (either fixed or extended).

	var clxLeg = false;					// First passed is complex?
	var clxLegCurRate = 0.00;			// Current rate being assessed for complex leg.
	var clxLegTimeToNextTier = -1;		// Seconds to next rating tier.
	var clxLegNextBongAmt = 0.00;		// Next bong charge amount to assess.
	var clxLegTimeToNextBong = -1;		// Seconds to next bong charge assessment.

	var clxLegNumBongCharges = 0;		// Number of bong charges assessed (extended only).

	// Other variables populated by Part 1.

	var simLegsRate = 0.00;				// Total metered rate of all simple B Legs.
	var legsCost = 0.00;				// Total incurred cost of all B Legs (result of Part 1).
	var truePrepaidBalance = 0.00;		// Passed prepaid balance less legsCost.
	var curPrepaidBalance = 0.00;		// Passed prepaid balance less legsCost - decremented as
										// time remaining calculated.

	// Variables used in Part 1 loop through passed B Legs.

	var BLeg = null;					// Current B Leg being examined.  Used in Part 2 to 
										// hold the complex leg.
										
	var legDuration = 0;				// Duration of B Leg.										
	var legCost = 0.00;					// Total metered cost of current B Leg.
	var bongCost = 0.00;				// Total bong surcharges of current B Leg.

	var tier1Dur = 0;					// Duration a complex B Leg has spent in tier 1 rate.
	var tier2Dur = 0;					// Duration a complex B Leg has spent in tier 2 rate.
	var tier3Dur = 0;					// Duration a complex B Leg has spent in tier 3 rate.

	var tier1Amt = 0.00;				// Tier 1 rate amt - contains passed tier 1 amt plus total metered.
	var tier2Amt = 0.00;				// Tier 2 rate amt - contains passed tier 2 amt plus total metered.
	var tier3Amt = 0.00;				// Tier 3 rate amt - contains passed tier 3 amt plus total metered.

	var extBongCharges = 0;				// Used to calculate the number of bong charges charged. 
	var extBongDuration = 0;			// ""

	var idx = 0;						// loop counter.
	
	// Variable used in Part 2 calculation of time remaining.
	
	var bandCost = 0.00;				// Cost of a "band" of time.  With a complex B Leg, a 
										// band is from now until the next bong surcharge or
										// rate tier.
	var bandDur = 0;					// The duration of the current "band".
	var cumBandDur = 0;					// Cumulative duration of covered "bands".
    var partialBandDur = 0;             // Portion of "band" that can be covered to exhaust balance.	

	var tieredRates = "F";				// Tiered rate indicator.	
	var newTier = false;				// Next band is a new rate tier.
	var newBong = false;				// Next band is a new bong charge.

	Server.logInfo("Beginning timeRemaining calculations");	
	
	// Clear passed reference variable.
	
	timeRemaining = 0;
	
	// Get current time.
	
	curTime = Server.getUTCTime();
	
	// Part 1 - Loop through all passed B Legs, accumulating their cost to date.  If a complex
	// B Leg is present, while figuring it's cost, set it's attributes in the clxLegX variables to
	// be used in Part 2.  Determine true prepaid balance = passed prepaid balance - cost of B Legs

	for (idx = 0; idx < BLegs.length; idx++)
	{
		Server.logInfo("timeRemaining: processing B Leg: " + idx);	

		legCost = bongCost = 0.00;
//		tier1Dur = tier2Dur = tier3Dur = 0;
//		tier1Amt = tier2Amt = tier3Amt = 0.00;
//		extBongCharges = extBongDuration = 0;
		tieredRates = "F";
	
		BLeg = BLegs[idx].oRATE;

		Server.logInfo("idx: " + idx);
		Server.logInfo("BLeg.fTier1RateAmt: " + BLeg.fTier1RateAmt);
		Server.logInfo("BLeg.strTieredRatesFlag: " + BLeg.strTieredRatesFlag);
		Server.logInfo("BLeg.fTotalMeteredRate: " + BLeg.fTotalMeteredRate);
		Server.logInfo("BLegs[idx].strConfCallFlag: " + BLegs[idx].strConfCallFlag);

		// Properly set tiered rate indicator - can only be true for a leg with conference call flag = F;

		if(BLegs[idx].strConfCallFlag == "T" && BLeg.strTieredRatesFlag == "T") 
		{
			tieredRates = "F";
			// BLeg.strTieredRatesFlag = "F";
			Server.logInfo("B Leg " + idx + " identified as simple with tier1 rate. Ignoring tier1 rate. Total Metered Rate is: " + BLeg.fTotalMeteredRate);
		}
		else
			tieredRates = BLeg.strTieredRatesFlag;

		// Validate numeric attributes used in calculations as valid numerics.

		if (isNaN(BLegs[idx].lTimeAnswered))
		{
			Server.logError("B Leg: " + idx + " contains non-numeric time answered - leg discarded"); 		 

			continue;
		}

		if (isNaN(BLeg.fTotalFixedRate))
		{
			BLeg.fTotalFixedRate = 0.00;

			Server.logInfo("B Leg: " + idx + " contains non-numeric total fixed rate amount - set to 0.00");
		}

		if (tieredRates == "F" && isNaN(BLeg.fTotalMeteredRate))
		{
			BLeg.fTotalMeteredRate = 0.00;

			Server.logInfo("B Leg: " + idx + " is non-tiered but contains non-numeric total metered rate amount - set to 0.00");
		}

		if (tieredRates == "T" && isNaN(BLeg.fTier1RateAmt))
		{
			Server.logError("B Leg: " + idx + " is tiered rate but contains non-numeric tier 1 rate amount - leg discarded");

			continue;
		} 

		var badData = false;

		if (tieredRates == "T")
		{
		    if (isNaN(BLeg.nTier2ThresholdSecs))
			{
				BLeg.nTier2ThresholdSecs = 0;
				badData = true;
			}
			if (isNaN(BLeg.nTier3ThresholdSecs))
			{
				BLeg.nTier3ThresholdSecs = 0;
			 	badData = true;
			}
			if (isNaN(BLeg.fTier2RateAmt))
			{
				BLeg.fTier2RateAmt = 0.00;
			 	badData = true;
			}
			if (isNaN(BLeg.fTier3RateAmt))
			{
				BLeg.fTier3RateAmt = 0.00;
			 	badData = true;
			}

			if (badData)
				Server.logInfo("B Leg: " + idx + " is tiered rate but contains non-numeric rating data other than tier 1 rate amount - invalid items set to 0");
		} 

		if (BLeg.strExtBongsFlag == "F")
		{
			badData = false;

			if (isNaN(BLeg.nBongCharge1Time))
			{
				BLeg.nBongCharge1Time = 0;
				BLeg.fBongCharge1Amt = 0.00;

				badData = true;
			}
			if (isNaN(BLeg.nBongCharge2Time))
			{
				BLeg.nBongCharge2Time = 0;
				BLeg.fBongCharge2Amt = 0.00;

				badData = true;
			}
			if (isNaN(BLeg.nBongCharge3Time))
			{
				BLeg.nBongCharge3Time = 0;
				BLeg.fBongCharge3Amt = 0.00;

				badData = true;
			}
			if (isNaN(BLeg.fBongCharge1Amt))
			{
				BLeg.fBongCharge1Amt = 0.00;
				BLeg.nBongCharge1Time = 0;

				badData = true;
			}
			if (isNaN(BLeg.fBongCharge2Amt))
			{
				BLeg.fBongCharge2Amt = 0.00;
				BLeg.nBongCharge2Time = 0;

				badData = true;
			}
			if (isNaN(BLeg.fBongCharge3Amt))
			{
				BLeg.fBongCharge3Amt = 0.00;
				BLeg.nBongCharge3Time = 0;

				badData = true;
			}

			if (badData)
				Server.logInfo("B Leg: " + idx + " is non-extended bong charges but contains non-numeric bong charge data - invalid items set to 0");
		}
		
		if (BLeg.strExtBongsFlag == "T" && (isNaN(BLeg.nExtBongInitialTimeSecs) ||
		                                    isNaN(BLeg.nExtBongIntervalSecs) ||
	    	                                isNaN(BLeg.fBongAmount) ||
	        	                            ((BLeg.strExtBongsNumCharges != "U") && isNaN(parseInt(BLeg.strExtBongsNumCharges)))))
		{
			Server.logError("B Leg: " + idx + " with extended bong charges contains non-numeric bong charge amount or time data - leg discarded");

			continue;
		}

		if (tieredRates == "T")
		{
			// Construct true tiered rate amts = BLeg.fTierXRateAmt + BLeg.fTotalMeteredRate if necessary.

			tier1Amt = BLeg.fTier1RateAmt;
			tier2Amt = BLeg.fTier2RateAmt;
			tier3Amt = BLeg.fTier3RateAmt;
		
		 	if ((!isNaN(BLeg.fTotalMeteredRate)) && BLeg.fTotalMeteredRate > 0.00)
			{
				Server.logInfo("timeRemaining: found total metered rate " + BLeg.fTotalMeteredRate + " for tiered rate leg - adding total metered rate to tiers");	
				Server.logInfo("timeRemaining: tiered rates before: tier1 = " + tier1Amt + " tier2 = " + tier2Amt + " tier3 = " + tier3Amt);

				if (BLeg.fTier1RateAmt > 0)
					tier1Amt += BLeg.fTotalMeteredRate;

				if (BLeg.fTier2RateAmt > 0)
					tier2Amt += BLeg.fTotalMeteredRate;

				if (BLeg.fTier3RateAmt > 0)
					tier3Amt += BLeg.fTotalMeteredRate;

				Server.logInfo("timeRemaining: tiered rates after: tier1 = " + tier1Amt + " tier2 = " + tier2Amt + " tier3 = " + tier3Amt);
			}
		}

		legDuration = curTime - BLegs[idx].lTimeAnswered;
	
		// Add B Leg fixed cost to total legs cost.
	
		legsCost += BLeg.fTotalFixedRate;
	
		Server.logInfo("B Leg " + idx + " added fixed cost of " + BLeg.fTotalFixedRate);	
	
		// Accumulate the total metered cost to date for simple B Legs.  Also
		// accumulate the total metered rate for all simple B Legs.
	
		if (idx > 0 || (idx == 0 && tieredRates == "F" &&
			                        BLeg.strExtBongsFlag == "F" &&
	        	                    BLeg.fBongCharge1Amt == 0.00))
		{
			legCost = ((legDuration / 60.00) * BLeg.fTotalMeteredRate);
			legsCost += legCost;
			simLegsRate += BLeg.fTotalMeteredRate;

			Server.logInfo("B Leg " + idx + " identified as simple with metered rate: " + BLeg.fTotalMeteredRate);		
			Server.logInfo("B Leg " + idx + " added total metered cost of " + legCost + " based on duration of " + legDuration);
			Server.logInfo("B Leg " + idx + " added total leg cost of " + (BLeg.fTotalFixedRate + legCost));
			Server.logInfo("Total legs cost so far:  " + legsCost);
			Server.logInfo("Total simple leg metered rate so far: " + simLegsRate);		
		
			continue;
		}
	
		// If we got here, we have a complex B Leg - determine it's cost to date and
		// it's current rating and bong surcharge attributes to be used in Part 2.

		clxLeg = true;

		Server.logInfo("B Leg " + idx + " identified as complex");
	
		if (tieredRates == "F")
		{
			// Handle complex B Leg not using tiered rates.  Multiply the metered
			// rate amount by the B Leg's duration in minutes and add to the total
			// legs cost.  Indicate that this complex B Leg will not change it's
			// rate amount (ie. change to another tier).

			legCost = ((legDuration / 60.00) * BLeg.fTotalMeteredRate);
			legsCost += legCost;
		
			clxLegCurRate = BLeg.fTotalMeteredRate;
			clxLegTimeToNextTier = -1;		
		
			Server.logInfo("B Leg " + idx + " identified as complex with non-tiered metered rate: " + BLeg.fTotalMeteredRate);		
			Server.logInfo("B Leg " + idx + " added total metered cost of " + legCost + " based on duration of " + legDuration);
		}
		else
		{
			// Handle complex B Leg using tiered rates.  Figure out which tier the
			// B Leg is rating at, how much has already been used and how long until
			// the next rating tier kicks in.

			if (BLeg.nTier2ThresholdSecs > 0 && BLeg.nTier2ThresholdSecs < legDuration)
			{
				tier1Dur = BLeg.nTier2ThresholdSecs;
			}
			else if (BLeg.nTier2ThresholdSecs > 0)
			{
				tier1Dur = legDuration;
				
				clxLegCurRate = tier1Amt;
				clxLegTimeToNextTier = BLeg.nTier2ThresholdSecs - legDuration;
			}
			else
			{
				tier1Dur = legDuration;
			
				clxLegCurRate = tier1Amt;
				clxLegTimeToNextTier = -1;
			}

			if (tier1Dur < legDuration)
			{
				if (BLeg.nTier3ThresholdSecs > 0 && BLeg.nTier3ThresholdSecs < legDuration)
				{	
					tier2Dur = BLeg.nTier3ThresholdSecs - tier1Dur;
					tier3Dur = legDuration - BLeg.nTier3ThresholdSecs;
			
					clxLegCurRate = tier3Amt;
					clxLegTimeToNextTier = -1;
				}
				else if (BLeg.nTier3ThresholdSecs > 0)
				{
					tier2Dur = legDuration - tier1Dur;
			
					clxLegCurRate = tier2Amt;
					clxLegTimeToNextTier = BLeg.nTier3ThresholdSecs - legDuration;
				}
				else
				{
					tier2Dur = legDuration - tier1Dur;
			
					clxLegCurRate = tier2Amt;
					clxLegTimeToNextTier = -1;
				}
			}
		
			// Add the cost of this leg to the total legs cost by figuring the cost
			// of each rate tier.
		
			legCost = (((tier1Dur / 60.00) * tier1Amt) +
		    	       ((tier2Dur / 60.00) * tier2Amt) +
					   ((tier3Dur / 60.00) * tier3Amt));
            
            legsCost += legCost;
						 
			Server.logInfo("B Leg " + idx + " identified as complex with tiered metered rates");		
			Server.logInfo("B Leg " + idx + " added total metered cost of " + legCost + " based on total duration of " + legDuration);
			Server.logInfo("  Tier 1: " + tier1Dur + "@" + tier1Amt);
			Server.logInfo("  Tier 2: " + tier2Dur + "@" + tier2Amt);
			Server.logInfo("  Tier 3: " + tier3Dur + "@" + tier3Amt);
			Server.logInfo("Current metered rate: " + clxLegCurRate + "  Time to next rate tier: " + clxLegTimeToNextTier);			
		}
	
		// Process the bong charges portion of a complex B Leg.

		if (BLeg.strExtBongsFlag == "F")
		{
			// Handle complex B Leg not using extended bong charges.  Determine which,
			// if any of the three fixed bong charges have already been assessed and
			// identify which, if any will come next and when.
			
			Server.logInfo("B Leg " + idx + " identified as complex with fixed (or no) bong charges");			
		
			if (BLeg.nBongCharge3Time > 0 && BLeg.nBongCharge3Time <= legDuration)
			{
				bongCost = BLeg.fBongCharge3Amt + BLeg.fBongCharge2Amt + BLeg.fBongCharge1Amt;
				legsCost += bongCost;
				
                clxLegNextBongAmt = 0.00;
                clxLegTimeToNextBong = -1;				
				
				Server.logInfo("B Leg " + idx + " assessed bong charges");
				Server.logInfo("  bong1: " + BLeg.nBongCharge1Time + "@" + BLeg.fBongCharge1Amt);
				Server.logInfo("  bong2: " + BLeg.nBongCharge2Time + "@" + BLeg.fBongCharge2Amt);
				Server.logInfo("  bong3: " + BLeg.nBongCharge3Time + "@" + BLeg.fBongCharge3Amt);				
			}
			else if (BLeg.nBongCharge2Time > 0 && BLeg.nBongCharge2Time <= legDuration)
			{
				bongCost = BLeg.fBongCharge2Amt + BLeg.fBongCharge1Amt;
				legsCost += bongCost;
			
				if (BLeg.nBongCharge3Time > 0)
				{
					clxLegNextBongAmt = BLeg.fBongCharge3Amt;
					clxLegTimeToNextBong = BLeg.nBongCharge3Time - legDuration;
				}
				else
				{
					clxLegNextBongAmt = 0.00;
					clxLegTimeToNextBong = -1;
				}
				
				Server.logInfo("B Leg " + idx + " assessed bong charges");
				Server.logInfo("  bong1: " + BLeg.nBongCharge1Time + "@" + BLeg.fBongCharge1Amt);
				Server.logInfo("  bong2: " + BLeg.nBongCharge2Time + "@" + BLeg.fBongCharge2Amt);
			}
			else if (BLeg.nBongCharge1Time > 0 && BLeg.nBongCharge1Time <= legDuration)
			{
				legsCost += BLeg.fBongCharge1Amt;
			
				if (BLeg.nBongCharge2Time > 0)
				{
					clxLegNextBongAmt = BLeg.fBongCharge2Amt;
					clxLegTimeToNextBong = BLeg.nBongCharge2Time - legDuration;
				}
				else
				{
					clxLegNextBongAmt = 0.00;
					clxLegTimeToNextBong = -1;
				}				
				
				Server.logInfo("B Leg " + idx + " assessed bong charges");
				Server.logInfo("  bong1: " + BLeg.nBongCharge1Time + "@" + BLeg.fBongCharge1Amt);
			}
			else if (BLeg.nBongCharge1Time > 0)
			{
				clxLegNextBongAmt = BLeg.fBongCharge1Amt;
				clxLegTimeToNextBong = BLeg.nBongCharge1Time - legDuration;
				
				Server.logInfo("B Leg " + idx + " assessed no bong charges");				
			}
		}
		else if (BLeg.nExtBongInitialTimeSecs > 0)
		{
			// Handle complex B Leg using extended bong charges.  Determine which,
			// if any of the bong intervals charges have already been assessed and
			// identify which, if any will come next and when.
			
			Server.logInfo("B Leg " + idx + " identified as complex with extended bong charges");			

			if (BLeg.nExtBongInitialTimeSecs <= legDuration)
			{
				extBongCharges++;
				extBongDuration = legDuration - BLeg.nExtBongInitialTimeSecs;
			
				extBongCharges += Math.floor(extBongDuration / BLeg.nExtBongIntervalSecs);
				if (BLeg.strExtBongsNumCharges == "U" || BLeg.strExtBongsNumCharges > extBongCharges)
				{
					bongCost = extBongCharges * BLeg.fBongAmount;
					legsCost += bongCost;
				
					clxLegNextBongAmt = BLeg.fBongAmount;
					clxLegTimeToNextBong = BLeg.nExtBongIntervalSecs - (extBongDuration % BLeg.nExtBongIntervalSecs);
					
					clxLegNumBongCharges = extBongCharges;
				}
				else
				{
					bongCost = BLeg.nExtBongsNumCharges * BLeg.fBongAmount;
					legsCost += bongCost;
				
					clxLegNumBongCharges = BLeg.nExtBongsNumCharges;
					clxLegNextBongAmt = 0.00;					
					clxLegTimeToNextBong = -1;
				}
				
                Server.logInfo("B Leg " + idx + " assessed " + clxLegNumBongCharges + " bong charges which total " + bongCost);				
			}
			else
			{
				clxLegNextBongAmt = BLeg.fBongAmount;
				clxLegTimeToNextBong = BLeg.nExtBongInitialTimeSecs - legDuration;
				
				Server.logInfo("B Leg " + idx + " assessed no bong charges");				
			}
		}

		Server.logInfo("Next bong charge: " + clxLegNextBongAmt + "  Time to next bong charge: " + clxLegTimeToNextBong);		
		Server.logInfo("B Leg " + idx + " added total cost of " + (BLeg.fTotalFixedRate + legCost + bongCost) + " based on duration of " + legDuration);
		Server.logInfo("Total legs cost so far:  " + legsCost);		
	}

	// Calculate the true prepaid balance.

	truePrepaidBalance = curPrepaidBalance = (dbPrepaidBalance - legsCost);
	
	Server.logInfo("Input db prepaid balance: " + dbPrepaidBalance + " less  Cost of current legs: " + legsCost + " = true prepaid balance: " + curPrepaidBalance); 

	// If a complex B Leg has been detected, it can be treated as a simple B Leg in Part 2
	// if it's rate won't change (ie. it's using a fixed metered rate or is already on
	// the last tier) and it won't charge any more bong surcharges.  If so, add the
	// metered rate of the complex B Leg to the sum of metered rates for simple B Legs and
	// process it as a simple B Leg.

	if (clxLeg && clxLegTimeToNextTier == -1 && clxLegTimeToNextBong == -1)
	{
		simLegsRate += clxLegCurRate;
		clxLeg = false;
		
        Server.logInfo("Complex B Leg now treated as simple - no more rate tiers and/or bong charges");
        Server.logInfo("Complex B Leg metered rate of: " + clxLegCurRate + " will be added to other simple B Legs");		
	}

	// Part 2 - calculate the time remaining in the call for the current set of B Legs
	// and the true prepaid balance in curPrepaidBalance.
	
	if (!clxLeg && simLegsRate > 0)
	{
		// If there is no complex B Leg, calculate time remaining from the prepaid balance
		// and the combined metered rates of the B Legs.
		
		timeRemaining = (curPrepaidBalance / simLegsRate) * 60;
		
        Server.logInfo("No more complex B Leg - time remaining calculated based on total metered rate of " + simLegsRate + " and balance of " + curPrepaidBalance);		
	}
	else
	{
		// Calculate the time remaining given a complex B Leg.  The complex B Leg will be
		// the first element in the array of B Legs.
		
		// Grab the complex B Leg for ease of reference.
		
		BLeg = BLegs[0].oRATE;
		legDuration = curTime - BLegs[0].lTimeAnswered;
		cumBandDur += legDuration;		
		
		// Calculate the cost of each "band", decrementing the prepaid balance as we go.
		// A "band" is the period of time from now until the next bong charge or rate
		// tier.
		
		while (curPrepaidBalance > 0.00)
		{
			// Check to see if the complex attributes of the leg have been
			// exhausted (ie. arrived at the last rate tier and no more bong
			// surcharges.  If so, the rest of time remaining can be calculated
			// from the B Leg metered rates alone - there are no more bands.
			
			if (clxLegTimeToNextTier == -1 && clxLegTimeToNextBong == -1)
			{
				simLegsRate += clxLegCurRate;
			   	timeRemaining += ((curPrepaidBalance / simLegsRate) * 60);
				curPrepaidBalance = 0.00;
				
                Server.logInfo("Complex B Leg now treated as simple - no more rate tiers and/or bong charges");
                Server.logInfo("Complex B Leg metered rate of: " + clxLegCurRate + " will be added to other simple B Legs");
                Server.logInfo("No more complex B Leg - time remaining calculated based on total metered rate of " + simLegsRate);				
				
				continue;
			}			
			// Determine the current band duration based on the clxLegTimeToNextTier
			// and clxLegTimeToNextBong as determined in part 1.
			
			if (clxLegTimeToNextTier != -1 && (clxLegTimeToNextBong == -1 ||
				                               clxLegTimeToNextTier < clxLegTimeToNextBong))			
			{
				// The current band will be ended by a new rate tier.
				
				newTier = true;
				newBong = false;
				
				bandDur = clxLegTimeToNextTier;

				// This bad will be based on the time to next rate tier but the
				// time to next bong surcharge, if any, must be reduced by this
				// band's duration so the next bong surcharge will be assessed
				// properly.
 				
				if (clxLegTimeToNextBong != -1)
					clxLegTimeToNextBong -= clxLegTimeToNextTier;
				
                Server.logInfo("Current band to rate: " + bandDur + " seconds");
                Server.logInfo("Next band triggered by change in rate tier in " + clxLegTimeToNextTier + " seconds");				
			}
			else if ((clxLegTimeToNextBong != -1) && (clxLegTimeToNextTier == -1 ||
                                                      clxLegTimeToNextBong < clxLegTimeToNextTier))
			{
				// The current band will be ended by a bong surcharge.
				
				newBong = true;
				newTier = false;
				
				bandDur = clxLegTimeToNextBong;

				// This bad will be based on the time to next bong surcharge  but
				// the time to next rate tier, if any, must be reduced by this
				// band's duration so the next rate will be applied properly.
				
				if (clxLegTimeToNextTier != -1)
					clxLegTimeToNextTier -= clxLegTimeToNextBong;
				
                Server.logInfo("Current band to rate: " + bandDur + " seconds");
                Server.logInfo("Next band triggered by bong charge in " + clxLegTimeToNextBong + " seconds");				
			}
			else
			{
				// The current band will be ended by a new rate tier but a bong
				// surcharge will be assessed at the same time.  Time to next
				// rate tier (if any) and time to next bong surcharge (if any)
				// will be reset so neither has to be adjusted for the other.
				
				newTier = newBong = true;
				
				bandDur = clxLegTimeToNextTier;
				
                Server.logInfo("Current band to rate: " + bandDur + " seconds");
                Server.logInfo("Next band triggered by change in rate tier and bong charge in " + clxLegTimeToNextTier + " seconds");				
			}
			
			// Calculate the cost of this band.
			
			bandCost = (bandDur / 60.00) * (simLegsRate + clxLegCurRate);
			
            Server.logInfo("Current band will cost: " + bandCost);			
			
			// If the band cost is greater than or equal to the prepaid balance
			// compute what portion of the band can be covered by the balance
			// and finish.
			
			if (bandCost >= curPrepaidBalance)
			{
                Server.logInfo("Current band cost greater than remaining balance of: " + curPrepaidBalance);
				
                partialBandDur = (curPrepaidBalance / bandCost) * bandDur;
				timeRemaining += partialBandDur;
				curPrepaidBalance = 0.00;

                Server.logInfo("Band cost greater than remaining prepaid balance - can cover: " + partialBandDur + " seconds");
				
				continue;
			}
			
			// This band can be covered by the prepaid balance so add the
			// band duration to time remaining and decrement the prepaid 
			// balance.
			
			timeRemaining += bandDur;
			curPrepaidBalance -= bandCost;
			cumBandDur += bandDur;
			
            Server.logInfo("Remaining balance after cost of band: " + curPrepaidBalance);			
			
			// If a bong charge is to be assessed here as well, do so and see if
			// there is any prepaid balance left to look at the next band.
			
			if (newBong)
			{
				curPrepaidBalance -= clxLegNextBongAmt;
				clxLegNumBongCharges++;
				
                Server.logInfo("Bong surcharge of: " + clxLegNextBongAmt);				
				
    			if (curPrepaidBalance <= 0.00)
                {
                    Server.logInfo("Bong surcharge more than remaining balance - last band effectively exhausts remaining balance");

	    			continue;
                }
                else
                {
                    Server.logInfo("Remaining balance after bong charge: " + curPrepaidBalance);
                }
			}
			
			// Determine what will indicate the end of the next band (rate tier 
			// and/or bong surcharge) and how long that band will last.
			
			if (newTier)
			{
				// Determine what the next rate tier per-minute rate is and how
				// long that rate will be in effect.
				
				// Determine what the next rate tier per-minute rate is and how
				// long that rate will be in effect.

                if (BLeg.nTier2ThresholdSecs == cumBandDur)
                {
                    clxLegCurRate = tier2Amt;
					
                    Server.logInfo("Entered rate tier 2 at metered rate: " + tier2Amt);					

                    if (BLeg.nTier3ThresholdSecs > 0)
					{
                        clxLegTimeToNextTier = BLeg.nTier3ThresholdSecs - BLeg.nTier2ThresholdSecs;
						
                        Server.logInfo("Next rate tier in: " + clxLegTimeToNextTier + " seconds");						
					}
                    else
					{
                        clxLegTimeToNextTier = -1;
						
                        Server.logInfo("No more rate tiers");						
					}
                }
                else
                {
                    clxLegCurRate = tier3Amt;
                    clxLegTimeToNextTier = -1;
					
                    Server.logInfo("Entered rate tier 3 at metered rate: " + tier3Amt);
                    Server.logInfo("No more rate tiers");					
                }				
			}
			
			if (newBong)
			{
				// Determine what the next bong surcharge will be and when it will
				// be assessed.

                if (BLeg.strExtBongsFlag == "F")
                {
                    // Determine which bong surchage is next if using fixed bong surcharges.

                    if (cumBandDur == BLeg.nBongCharge1Time)
                    {
                        Server.logInfo("Fixed bong charge 1 assessed");
						
                        if (BLeg.nBongCharge2Time > 0)
                        {
                            clxLegNextBongAmt = BLeg.fBongCharge2Amt;
                            clxLegTimeToNextBong = BLeg.nBongCharge2Time - BLeg.nBongCharge1Time;
							
                            Server.logInfo("Fixed bong charge 2 of: " + clxLegNextBongAmt + " in: " + clxLegTimeToNextBong + " seconds");							
                        }
                        else
                        {
                            clxLegNextBongAmt = 0.00;
                            clxLegTimeToNextBong = -1;
							
                            Server.logInfo("No Fixed bong charge 2 - no more bong charges");							
                        }
                    }
                    else if (cumBandDur == BLeg.nBongCharge2Time)
                    {
                        Server.logInfo("Fixed bong charge 2 assessed");
							
                        if (BLeg.nBongCharge3Time > 0)
                        {
                            clxLegNextBongAmt = BLeg.fBongCharge3Amt;
                            clxLegTimeToNextBong = BLeg.nBongCharge3Time - BLeg.nBongCharge2Time;
							
                            Server.logInfo("Fixed bong charge 3 of: " + clxLegNextBongAmt + " in: " + clxLegTimeToNextBong + " seconds");							
                        }
                        else
                        {
                            clxLegNextBongAmt = 0.00;
                            clxLegTimeToNextBong = -1;
							
                            Server.logInfo("No Fixed bong charge 3 - no more bong charges");							
                        }
                    }
                    else if (cumBandDur == BLeg.nBongCharge3Time)
                    {
                        clxLegNextBongAmt = 0.00;
                        clxLegTimeToNextBong = -1;
						
                        Server.logInfo("Fixed bong charge 3 assessed - no more bong charges");						
                    }
                }
                else
                {
                    // Determine which bong surchage is next if using extended bong surcharges.
					
                    Server.logInfo("Extended bong charge assessed");					

				    if (clxLegNumBongCharges == BLeg.strExtBongsNumCharges)
					{
						clxLegNextBongAmt = 0.00;
					    clxLegTimeToNextBong = -1;
						
                       Server.logInfo("Maximum number of extended bong charges reached - no more bong charges");						
					}
					else
					{
						clxLegNextBongAmt = BLeg.fBongAmount;
						clxLegTimeToNextBong = BLeg.nExtBongIntervalSecs;
						
                       Server.logInfo("Next extended bong charge of: " + BLeg.fBongAmount + " in: " + BLeg.nExtBongIntervalSecs + " seconds");						
					}
                }
			}
		}
	}

	// Get rid of fractional seconds in the calculated time remaining.
	
	timeRemaining = Math.floor(timeRemaining);
	
    Server.logInfo("timeRemaining calculations finished: " + timeRemaining + " seconds remaining for call");

	return (rc);
}

function hasMoneyForConfLeg(dbPrepaidBalance, BLegs, newBLegFixedCosts)
{
	// Calculate the time remaining given the passed prepaid balance and set of B Legs.
	// Part 1 - calculate the incurred cost of the B Legs and deduct from the passed
	// prepaid balance deriving the true current prepaid balance.  Part 2, calculate
	// how much time that prepaid balance will allow the call to continue with the
	// current set of B Legs.

	var curTime = 0;					// Current time in seconds.
	var rc = 0;							// Return code.
	
	// Attributes of a complex B Leg, if any.  Populated during Part 1 - used in
	// Part 2.  There can be only 1 complex B Leg.  If it exists, it must be the
	// first B Leg in the passed set of B Legs.  Additionally, it must use either
	// tiered rating or bong charges (either fixed or extended).

	var clxLeg = false;					// First passed is complex?
	var clxLegCurRate = 0.00;			// Current rate being assessed for complex leg.
	var clxLegTimeToNextTier = -1;		// Seconds to next rating tier.
	var clxLegNextBongAmt = 0.00;		// Next bong charge amount to assess.
	var clxLegTimeToNextBong = -1;		// Seconds to next bong charge assessment.
	var clxLegNumBongCharges = 0;		// Number of bong charges assessed (extended only).

	// Other variables populated by Part 1.

	var simLegsRate = 0.00;				// Total metered rate of all simple B Legs.
	var legsCost = 0.00;				// Total incurred cost of all B Legs (result of Part 1).
	var truePrepaidBalance = 0.00;		// Passed prepaid balance less legsCost.
	var curPrepaidBalance = 0.00;		// Passed prepaid balance less legsCost - decremented as
										// time remaining calculated.

	// Variables used in Part 1 loop through passed B Legs.

	var BLeg = null;					// Current B Leg being examined.  Used in Part 2 to 
										// hold the complex leg.
										
	var legDuration = 0;				// Duration of BLeg 									
	var legCost = 0.00;					// Total metered cost of current B Leg.
	var bongCost = 0.00;				// Total bong surcharges of current B Leg.

	var tier1Dur = 0;					// Duration a complex B Leg has spent in tier 1 rate.
	var tier2Dur = 0;					// Duration a complex B Leg has spent in tier 2 rate.
	var tier3Dur = 0;					// Duration a complex B Leg has spent in tier 3 rate.

	var tier1Amt = 0.00;				// Tier 1 rate amt - contains passed tier 1 amt plus total metered.
	var tier2Amt = 0.00;				// Tier 2 rate amt - contains passed tier 2 amt plus total metered.
	var tier3Amt = 0.00;				// Tier 3 rate amt - contains passed tier 3 amt plus total metered.

	var extBongCharges = 0;				// Used to calculate the number of bong charges charged. 
	var extBongDuration = 0;			// ""

	var idx = 0;						// loop counter.
	
	// Variable used in Part 2 calculation of time remaining.
	
	var bandCost = 0.00;				// Cost of a "band" of time.  With a complex B Leg, a 
										// band is from now until the next bong surcharge or
										// rate tier.
	var bandDur = 0;					// The duration of the current "band".
	var cumBandDur = 0;					// Cumulative duration of covered "bands".
    var partialBandDur = 0;             // Portion of "band" that can be covered to exhaust balance.	
	
	var newTier = false;				// Next band is a new rate tier.
	var newBong = false;				// Next band is a new bong charge.
	var tieredRates = "F";				// Tiered rate indicator.	

	Server.logInfo("Beginning hasMoneyForConfLeg calculations");	
	
	// Clear passed reference variable.
	
	timeRemaining = 0;
	
	// Get current time.
	
	curTime = Server.getUTCTime();
	
	// Part 1 - Loop through all passed B Legs, accumulating their cost to date.  If a complex
	// B Leg is present, while figuring it's cost, set it's attributes in the clxLegX variables to
	// be used in Part 2.  Determine true prepaid balance = passed prepaid balance - cost of B Legs

	if (newBLegFixedCosts <= 0)
	{
		// no fixed costs

	   	Server.logInfo("hasMoneyForConfLeg calculations finished: no fixed costs for new leg, allow call");
	   	rc = "T";

		return (rc);
	}

	for (idx = 0; idx < BLegs.length; idx++)
	{
		Server.logInfo("hasMoneyForConfLeg: processing B Leg: " + idx);	

		legCost = bongCost = 0.00;
		tier1Dur = tier2Dur = tier3Dur = 0;
		tier1Amt = tier2Amt = tier3Amt = 0.00;
		extBongCharges = extBongDuration = 0;
		tieredRates = "F";
	
		BLeg = BLegs[idx].oRATE;

		Server.logInfo("idx: " + idx);
		Server.logInfo("BLeg.fTier1RateAmt: " + BLeg.fTier1RateAmt);
		Server.logInfo("BLeg.strTieredRatesFlag: " + BLeg.strTieredRatesFlag);
		Server.logInfo("BLeg.fTotalMeteredRate: " + BLeg.fTotalMeteredRate);
		Server.logInfo("BLegs[idx].strConfCallFlag: " + BLegs[idx].strConfCallFlag);

		// Properly set tiered rate indicator - can only be true for a leg with conference call flag = F;

		if(BLegs[idx].strConfCallFlag == "T" && BLeg.strTieredRatesFlag == "T") 
		{
			tieredRates = "F";
			// BLeg.strTieredRatesFlag = "F";
			Server.logInfo("B Leg " + idx + " identified as simple with tier1 rate. Ignoring tier1 rate. Total Metered Rate is: " + BLeg.fTotalMeteredRate);
		}
		else
			tieredRates = BLeg.strTieredRatesFlag;

		// Validate numeric attributes used in calculations as valid numerics.

		if (isNaN(BLegs[idx].lTimeAnswered))
		{
			Server.logError("B Leg: " + idx + " contains non-numeric time answered - leg discarded"); 		 

			continue;
		}

		if (isNaN(BLeg.fTotalFixedRate))
		{
			BLeg.fTotalFixedRate = 0.00;

			Server.logInfo("B Leg: " + idx + " contains non-numeric total fixed rate amount - set to 0.00");
		}

		if (tieredRates == "F" && isNaN(BLeg.fTotalMeteredRate))
		{
			BLeg.fTotalMeteredRate = 0.00;

			Server.logInfo("B Leg: " + idx + " is non-tiered but contains non-numeric total metered rate amount - set to 0.00");
		}

		if (tieredRates == "T" && isNaN(BLeg.fTier1RateAmt))
		{
			Server.logError("B Leg: " + idx + " is tiered rate but contains non-numeric tier 1 rate amount - leg discarded");

			continue;
		} 

		var badData = false;

		if (tieredRates == "T")
		{
		    if (isNaN(BLeg.nTier2ThresholdSecs))
			{
				BLeg.nTier2ThresholdSecs = 0;
				badData = true;
			}
			if (isNaN(BLeg.nTier3ThresholdSecs))
			{
				BLeg.nTier3ThresholdSecs = 0;
			 	badData = true;
			}
			if (isNaN(BLeg.fTier2RateAmt))
			{
				BLeg.fTier2RateAmt = 0.00;
			 	badData = true;
			}
			if (isNaN(BLeg.fTier3RateAmt))
			{
				BLeg.fTier3RateAmt = 0.00;
			 	badData = true;
			}

			if (badData)
				Server.logInfo("B Leg: " + idx + " is tiered rate but contains non-numeric rating data other than tier 1 rate amount - invalid items set to 0");
		} 

		if (BLeg.strExtBongsFlag == "F")
		{
			badData = false;

			if (isNaN(BLeg.nBongCharge1Time))
			{
				BLeg.nBongCharge1Time = 0;
				BLeg.fBongCharge1Amt = 0.00;

				badData = true;
			}
			if (isNaN(BLeg.nBongCharge2Time))
			{
				BLeg.nBongCharge2Time = 0;
				BLeg.fBongCharge2Amt = 0.00;

				badData = true;
			}
			if (isNaN(BLeg.nBongCharge3Time))
			{
				BLeg.nBongCharge3Time = 0;
				BLeg.fBongCharge3Amt = 0.00;

				badData = true;
			}
			if (isNaN(BLeg.fBongCharge1Amt))
			{
				BLeg.fBongCharge1Amt = 0.00;
				BLeg.nBongCharge1Time = 0;

				badData = true;
			}
			if (isNaN(BLeg.fBongCharge2Amt))
			{
				BLeg.fBongCharge2Amt = 0.00;
				BLeg.nBongCharge2Time = 0;

				badData = true;
			}
			if (isNaN(BLeg.fBongCharge3Amt))
			{
				BLeg.fBongCharge3Amt = 0.00;
				BLeg.nBongCharge3Time = 0;

				badData = true;
			}

			if (badData)
				Server.logInfo("B Leg: " + idx + " is non-extended bong charges but contains non-numeric bong charge data - invalid items set to 0");
		}
		
		if (BLeg.strExtBongsFlag == "T" && (isNaN(BLeg.nExtBongInitialTimeSecs) ||
		                                    isNaN(BLeg.nExtBongIntervalSecs) ||
    		                                isNaN(BLeg.fBongAmount) ||
        		                            ((BLeg.strExtBongsNumCharges != "U") && isNaN(parseInt(BLeg.strExtBongsNumCharges)))))
		{
			Server.logError("B Leg: " + idx + " with extended bong charges contains non-numeric bong charge amount or time data - leg discarded");

			continue;
		}

		// Construct true tiered rate amts = BLeg.fTierXRateAmt + BLeg.fTotalMeteredRate if necessary.

		tier1Amt = BLeg.fTier1RateAmt;
		tier2Amt = BLeg.fTier2RateAmt;
		tier3Amt = BLeg.fTier3RateAmt;

		if (tieredRates == "T" && (!isNaN(BLeg.fTotalMeteredRate)) && BLeg.fTotalMeteredRate > 0.00)
		{
			Server.logInfo("hasMoneyForConfLeg: found total metered rate " + BLeg.fTotalMeteredRate + " for tiered rate leg - adding total metered rate to tiers");	
			Server.logInfo("hasMoneyForConfLeg: tiered rates before: tier1 = " + tier1Amt + " tier2 = " + tier2Amt + " tier3 = " + tier3Amt);

			if (BLeg.fTier1RateAmt > 0)
				tier1Amt += BLeg.fTotalMeteredRate;

			if (BLeg.fTier2RateAmt > 0)
				tier2Amt += BLeg.fTotalMeteredRate;

			if (BLeg.fTier3RateAmt > 0)
				tier3Amt += BLeg.fTotalMeteredRate;

			Server.logInfo("hasMoneyForConfLeg: tiered rates after: tier1 = " + tier1Amt + " tier2 = " + tier2Amt + " tier3 = " + tier3Amt);
		}

		legDuration = curTime - BLegs[idx].lTimeAnswered;
	
		// Add B Leg fixed cost to total legs cost.
	
		legsCost += BLeg.fTotalFixedRate;

		Server.logInfo("B Leg " + idx + " added fixed cost of " + BLeg.fTotalFixedRate);	
	
		// Accumulate the total metered cost to date for simple B Legs.  Also
		// accumulate the total metered rate for all simple B Legs.
	
		if (idx > 0 || (idx == 0 && tieredRates == "F" &&
		                            BLeg.strExtBongsFlag == "F" &&
        	                        BLeg.fBongCharge1Amt == 0.00))
		{
			legCost = ((legDuration / 60.00) * BLeg.fTotalMeteredRate);
			legsCost += legCost;
			simLegsRate += BLeg.fTotalMeteredRate;

			Server.logInfo("B Leg " + idx + " identified as simple with metered rate: " + BLeg.fTotalMeteredRate);		
			Server.logInfo("B Leg " + idx + " added total metered cost of " + legCost + " based on duration of " + legDuration);
			Server.logInfo("B Leg " + idx + " added total leg cost of " + (BLeg.fTotalFixedRate + legCost));
			Server.logInfo("Total legs cost so far:  " + legsCost);
			Server.logInfo("Total simple leg metered rate so far: " + simLegsRate);		
		
			continue;
		}
	
       	// If we got here, we have a complex B Leg - determine it's cost to date and
       	// it's current rating and bong surcharge attributes to be used in Part 2.

		clxLeg = true;

		Server.logInfo("B Leg " + idx + " identified as complex");
	
		if (tieredRates == "F")
		{
			// Handle complex B Leg not using tiered rates.  Multiply the metered
			// rate amount by the B Leg's duration in minutes and add to the total
			// legs cost.  Indicate that this complex B Leg will not change it's
			// rate amount (ie. change to another tier).

       		legCost = ((legDuration / 60.00) * BLeg.fTotalMeteredRate);
       		legsCost += legCost;
		
       		clxLegCurRate = BLeg.fTotalMeteredRate;
       		clxLegTimeToNextTier = -1;		
        		
       		Server.logInfo("B Leg " + idx + " identified as complex with non-tiered metered rate: " + BLeg.fTotalMeteredRate);		
       		Server.logInfo("B Leg " + idx + " added total metered cost of " + legCost + " based on duration of " + legDuration);
       	}
       	else
       	{
   			// Handle complex B Leg using tiered rates.  Figure out which tier the
   			// B Leg is rating at, how much has already been used and how long until
   			// the next rating tier kicks in.
        
   			if (BLeg.nTier2ThresholdSecs > 0 && BLeg.nTier2ThresholdSecs < legDuration)
   			{
   				tier1Dur = BLeg.nTier2ThresholdSecs;
   			}
   			else if (BLeg.nTier2ThresholdSecs > 0)
   			{
   				tier1Dur = legDuration;
        				
       			clxLegCurRate = tier1Amt;
   				clxLegTimeToNextTier = BLeg.nTier2ThresholdSecs - legDuration;
      		}
       		else
       		{
       			tier1Dur = legDuration;
        			
       			clxLegCurRate = tier1Amt;
       			clxLegTimeToNextTier = -1;
       		}
        
       		if (tier1Dur < legDuration)
       		{
       			if (BLeg.nTier3ThresholdSecs > 0 && BLeg.nTier3ThresholdSecs < legDuration)
       			{	
       				tier2Dur = BLeg.nTier3ThresholdSecs - tier1Dur;
					tier3Dur = legDuration - BLeg.nTier3ThresholdSecs;
			
					clxLegCurRate = tier3Amt;
					clxLegTimeToNextTier = -1;
				}
				else if (BLeg.nTier3ThresholdSecs > 0)
				{
					tier2Dur = legDuration - tier1Dur;
					clxLegCurRate = tier2Amt;
					clxLegTimeToNextTier = BLeg.nTier3ThresholdSecs - legDuration;
				}
				else
				{
					tier2Dur = legDuration - tier1Dur;
					clxLegCurRate = tier2Amt;
					clxLegTimeToNextTier = -1;
				}
			}
		
			// Add the cost of this leg to the total legs cost by figuring the cost
			// of each rate tier.
		
			legCost = (((tier1Dur / 60.00) * tier1Amt) +
    	       		   ((tier2Dur / 60.00) * tier2Amt) +
			   		   ((tier3Dur / 60.00) * tier3Amt));
            
       		legsCost += legCost;
						 
			Server.logInfo("B Leg " + idx + " identified as complex with tiered metered rates");		
			Server.logInfo("B Leg " + idx + " added total metered cost of " + legCost + " based on total duration of " + legDuration);
			Server.logInfo("  Tier 1: " + tier1Dur + "@" + tier1Amt);
			Server.logInfo("  Tier 2: " + tier2Dur + "@" + tier2Amt);
			Server.logInfo("  Tier 3: " + tier3Dur + "@" + tier3Amt);
			Server.logInfo("Current metered rate: " + clxLegCurRate + "  Time to next rate tier: " + clxLegTimeToNextTier);			
		}//tiered rates
	
		// Process the bong charges portion of a complex B Leg.

		if (BLeg.strExtBongsFlag == "F")
		{
			// Handle complex B Leg not using extended bong charges.  Determine which,
			// if any of the three fixed bong charges have already been assessed and
			// identify which, if any will come next and when.
			
			Server.logInfo("B Leg " + idx + " identified as complex with fixed (or no) bong charges");			
		
			if (BLeg.nBongCharge3Time > 0 && BLeg.nBongCharge3Time <= legDuration)
			{
				bongCost = BLeg.fBongCharge3Amt + BLeg.fBongCharge2Amt + BLeg.fBongCharge1Amt;
				legsCost += bongCost;
				
               	clxLegNextBongAmt = 0.00;
               	clxLegTimeToNextBong = -1;				
				
				Server.logInfo("B Leg " + idx + " assessed bong charges");
				Server.logInfo("  bong1: " + BLeg.nBongCharge1Time + "@" + BLeg.fBongCharge1Amt);
				Server.logInfo("  bong2: " + BLeg.nBongCharge2Time + "@" + BLeg.fBongCharge2Amt);
				Server.logInfo("  bong3: " + BLeg.nBongCharge3Time + "@" + BLeg.fBongCharge3Amt);				
			}
			else if (BLeg.nBongCharge2Time > 0 && BLeg.nBongCharge2Time <= legDuration)
			{
				bongCost = BLeg.fBongCharge2Amt + BLeg.fBongCharge1Amt;
				legsCost += bongCost;
			
				if (BLeg.nBongCharge3Time > 0)
				{
					clxLegNextBongAmt = BLeg.fBongCharge3Amt;
					clxLegTimeToNextBong = BLeg.nBongCharge3Time - legDuration;
				}
				else
				{
					clxLegNextBongAmt = 0.00;
					clxLegTimeToNextBong = -1;
				}
				
				Server.logInfo("B Leg " + idx + " assessed bong charges");
				Server.logInfo("  bong1: " + BLeg.nBongCharge1Time + "@" + BLeg.fBongCharge1Amt);
				Server.logInfo("  bong2: " + BLeg.nBongCharge2Time + "@" + BLeg.fBongCharge2Amt);
			}
			else if (BLeg.nBongCharge1Time > 0 && BLeg.nBongCharge1Time <= legDuration)
			{
				legsCost += BLeg.fBongCharge1Amt;
			
				if (BLeg.nBongCharge2Time > 0)
				{
					clxLegNextBongAmt = BLeg.fBongCharge2Amt;
					clxLegTimeToNextBong = BLeg.nBongCharge2Time - legDuration;
				}
				else
				{
					clxLegNextBongAmt = 0.00;
					clxLegTimeToNextBong = -1;
				}				
				
				Server.logInfo("B Leg " + idx + " assessed bong charges");
				Server.logInfo("  bong1: " + BLeg.nBongCharge1Time + "@" + BLeg.fBongCharge1Amt);
			}
			else if (BLeg.nBongCharge1Time > 0)
			{
				clxLegNextBongAmt = BLeg.fBongCharge1Amt;
				clxLegTimeToNextBong = BLeg.nBongCharge1Time - legDuration;
				
				Server.logInfo("B Leg " + idx + " assessed no bong charges");				
			}
		}//extended bongs
		else if (BLeg.nExtBongInitialTimeSecs > 0)
		{
			// Handle complex B Leg using extended bong charges.  Determine which,
			// if any of the bong intervals charges have already been assessed and
			// identify which, if any will come next and when.
			
			Server.logInfo("B Leg " + idx + " identified as complex with extended bong charges");			

			if (BLeg.nExtBongInitialTimeSecs <= legDuration)
			{
				extBongCharges++;
				extBongDuration = legDuration - BLeg.nExtBongInitialTimeSecs;
			
				extBongCharges += Math.floor(extBongDuration / BLeg.nExtBongIntervalSecs);
				if (BLeg.strExtBongsNumCharges == "U" || BLeg.strExtBongsNumCharges > extBongCharges)
				{
					bongCost = extBongCharges * BLeg.fBongAmount;
					legsCost += bongCost;
				
					clxLegNextBongAmt = BLeg.fBongAmount;
					clxLegTimeToNextBong = BLeg.nExtBongIntervalSecs - (extBongDuration % BLeg.nExtBongIntervalSecs);
					
					clxLegNumBongCharges = extBongCharges;
				}
				else
				{
					bongCost = BLeg.nExtBongsNumCharges * BLeg.fBongAmount;
					legsCost += bongCost;
				
					clxLegNumBongCharges = BLeg.nExtBongsNumCharges;
					clxLegNextBongAmt = 0.00;					
					clxLegTimeToNextBong = -1;
				}
				
               	Server.logInfo("B Leg " + idx + " assessed " + clxLegNumBongCharges + " bong charges which total " + bongCost);				
			}
			else
			{
				clxLegNextBongAmt = BLeg.fBongAmount;
				clxLegTimeToNextBong = BLeg.nExtBongInitialTimeSecs - legDuration;
				
				Server.logInfo("B Leg " + idx + " assessed no bong charges");				
			}
		}//BLeg.nExtBongInitialTimeSecs > 0

		Server.logInfo("Next bong charge: " + clxLegNextBongAmt + "  Time to next bong charge: " + clxLegTimeToNextBong);		
		Server.logInfo("B Leg " + idx + " added total cost of " + (BLeg.fTotalFixedRate + legCost + bongCost) + " based on duration of " + legDuration);
		Server.logInfo("Total legs cost so far:  " + legsCost);	
	}//call legs loop
		
	// Calculate the true prepaid balance.

	truePrepaidBalance = curPrepaidBalance = (dbPrepaidBalance - legsCost);
	Server.logInfo("Input db prepaid balance: " + dbPrepaidBalance + " less  Cost of current legs: " + legsCost + " = true prepaid balance: " + curPrepaidBalance); 

	if (newBLegFixedCosts >= truePrepaidBalance)
	{
		Server.logInfo("hasMoneyForConfLeg calculations finished: balance is insufficient to cover fixed costs of new leg = " + newBLegFixedCosts);
		rc = "F";
	}
	else
	{
		Server.logInfo("hasMoneyForConfLeg calculations finished: balance is sufficient to cover fixed costs of new leg");
		rc = "T";
	}	

	return (rc);
}


function js_logInfoCallLeg(oCallLeg) {


	Server.logInfo("oCallLeg.bAttemptedToOutdial is: " + oCallLeg.bAttemptedToOutdial);
	Server.logInfo("oCallLeg.bConnected is: " + oCallLeg.bConnected);
	Server.logInfo("oCallLeg.bCurrentlyDialing is: " + oCallLeg.bCurrentlyDialing);
	Server.logInfo("oCallLeg.bLegCallback is: " + oCallLeg.bLegCallback);
	Server.logInfo("oCallLeg.bRefresher is: " + oCallLeg.bRefresher);
	Server.logInfo("oCallLeg.bReverseFromTo is: " + oCallLeg.bReverseFromTo);
	Server.logInfo("oCallLeg.bUac is: " + oCallLeg.bUac);
	
	Server.logInfo("oCallLeg.fCallCost is: " + oCallLeg.fCallCost);
	
	Server.logInfo("oCallLeg.nDestCountryId is: " + oCallLeg.nDestCountryId);
	Server.logInfo("oCallLeg.nDestRegionId is: " + oCallLeg.nDestRegionId);
	Server.logInfo("oCallLeg.nGracePeriod is: " + oCallLeg.nGracePeriod);
	Server.logInfo("oCallLeg.nOrigCountryId is: " + oCallLeg.nOrigCountryId);
	Server.logInfo("oCallLeg.nRingNoAnswerTime is: " + oCallLeg.nRingNoAnswerTime);
	Server.logInfo("oCallLeg.nSessionTimer is: " + oCallLeg.nSessionTimer);
	Server.logInfo("oCallLeg.nSetUpTime is: " + oCallLeg.nSetUpTime);
	Server.logInfo("oCallLeg.nTearDownTime is: " + oCallLeg.nTearDownTime);
	
	Server.logInfo("oCallLeg.lTimeAnswered is: " + oCallLeg.lTimeAnswered);
	Server.logInfo("oCallLeg.lTimeStart is: " + oCallLeg.lTimeStart);
	Server.logInfo("oCallLeg.lTimeEnded is: " + oCallLeg.lTimeEnded);
	
	Server.logInfo("oCallLeg.strBackupRequestUri is: " + oCallLeg.strBackupRequestUri);
	Server.logInfo("oCallLeg.strBillableFlag is: " + oCallLeg.strBillableFlag);
	Server.logInfo("oCallLeg.strCalledNumber is: " + oCallLeg.strCalledNumber);
	Server.logInfo("oCallLeg.strCallingNumber is: " + oCallLeg.strCallingNumber);
	Server.logInfo("oCallLeg.strCallId is: " + oCallLeg.strCallId);
	Server.logInfo("oCallLeg.strCSROutdialFlag is: " + oCallLeg.strCSROutdialFlag);
	Server.logInfo("oCallLeg.strConfCallFlag is: " + oCallLeg.strConfCallFlag);
	Server.logInfo("oCallLeg.strCodec is: " + oCallLeg.strCodec);
	Server.logInfo("oCallLeg.strContact is: " + oCallLeg.strContact);
	Server.logInfo("oCallLeg.strContent is: " + oCallLeg.strContent);
	Server.logInfo("oCallLeg.strContentType is: " + oCallLeg.strContentType);
	Server.logInfo("oCallLeg.strCSeq is: " + oCallLeg.strCSeq);
	Server.logInfo("oCallLeg.strDestAreaCode is: " + oCallLeg.strDestAreaCode);
	Server.logInfo("oCallLeg.strDestCallingCode is: " + oCallLeg.strDestCallingCode);
	Server.logInfo("oCallLeg.strDestDomIntlFlag is: " + oCallLeg.strDestDomIntlFlag);
	Server.logInfo("oCallLeg.strDestTrunkGroup is: " + oCallLeg.strDestTrunkGroup);
	Server.logInfo("oCallLeg.strEventId is: " + oCallLeg.strEventId);
	Server.logInfo("oCallLeg.strFrom is: " + oCallLeg.strFrom);
	Server.logInfo("oCallLeg.strOrigAreaCode is: " + oCallLeg.strOrigAreaCode);
	Server.logInfo("oCallLeg.strOrigCallingCode is: " + oCallLeg.strOrigCallingCode);
	Server.logInfo("oCallLeg.strOriginalContent is: " + oCallLeg.strOriginalContent);
	Server.logInfo("oCallLeg.strOrigTrunkGroup is: " + oCallLeg.strOrigTrunkGroup);
	Server.logInfo("oCallLeg.strOutdialDestNbr is: " + oCallLeg.strOutdialDestNbr);
	Server.logInfo("oCallLeg.strPrimaryRouteCode is: " + oCallLeg.strPrimaryRouteCode);
	Server.logInfo("oCallLeg.strRecordRoute is: " + oCallLeg.strRecordRoute);
	Server.logInfo("oCallLeg.strRefresher is: " + oCallLeg.strRefresher);
	Server.logInfo("oCallLeg.strRemoteCSeq is: " + oCallLeg.strRemoteCSeq);
	Server.logInfo("oCallLeg.strRemoteSdp is: " + oCallLeg.strRemoteSdp);
	Server.logInfo("oCallLeg.strRemoteUri is: " + oCallLeg.strRemoteUri);	
	Server.logInfo("oCallLeg.strRequestUri is: " + oCallLeg.strRequestUri);
	Server.logInfo("oCallLeg.strRoute is: " + oCallLeg.strRoute);
	Server.logInfo("oCallLeg.strSdp is: " + oCallLeg.strSdp);
	Server.logInfo("oCallLeg.strSessionTimerFlag is: " + oCallLeg.strSessionTimerFlag);
	Server.logInfo("oCallLeg.strSIPStatus is: " + oCallLeg.strSIPStatus);
	Server.logInfo("oCallLeg.strStrippedDest is: " + oCallLeg.strStrippedDest);
	Server.logInfo("oCallLeg.strTo is: " + oCallLeg.strTo);
	Server.logInfo("oCallLeg.strVia is: " + oCallLeg.strVia);
	
	return;

}



function js_process_application_property(p_strBooleanApplicationProperty) {

	var bAppProperty = false;

	if(p_strBooleanApplicationProperty.length <= 0) {
		bAppProperty = false;
	} else {
		if("1" == p_strBooleanApplicationProperty || "T" == p_strBooleanApplicationProperty.toUpperCase() || "TRUE" == p_strBooleanApplicationProperty.toUpperCase()) {
			bAppProperty = true;
		} else {
			bAppProperty = false;
		}	
	} 

	return bAppProperty;

} // end function


// TODO: Deprecate this method, change all App Properties to boolean and eliminate strings (eg "T" and "F");

function js_process_string_application_property(p_strBooleanApplicationProperty) {

	if(p_strBooleanApplicationProperty.length <= 0) {
		p_strBooleanApplicationProperty = "F";
	} else {
		if("1" == p_strBooleanApplicationProperty || "T" == p_strBooleanApplicationProperty.toUpperCase() || "TRUE" == p_strBooleanApplicationProperty.toUpperCase()) {
			p_strBooleanApplicationProperty = "T";
		} else {
			p_strBooleanApplicationProperty = "F";
		}	
	} 

	return p_strBooleanApplicationProperty;

} // end function


function js_resolve_address(&p_strAddress) {

	if(p_strAddress.length > 0) {
		p_strAddress = Server.convertIpAddressIntToString(Server.convertIpAddressStringToInt(p_strAddress)); 
	}

	return;

}

function js_log_object( obj, obj_name ) 
{
	Server.logInfo("js_log_object: logging properties of object " + obj_name ) ;
	for( var prop in obj ) {
		Server.logInfo("{" + prop + ":" + obj[prop] + "}" ) ;
	}
	Server.logInfo("js_log_object: end logging object " + obj_name ) ;
}


function js_isInviteOnHold( s ) 
{
	if( -1 != s.indexOf("0.0.0.0") || -1 != s.indexOf("a=sendonly") || -1 != s.indexOf("a=inactive") ) {
		 
		 return true ;
	}
	return false ;
}

function js_getLeadingDigits( s ) 
{
	var r = new String(s) ;
	var nPos = -1 ;
	for( var i = 0; i < r.length; i++ ) {
		var c = r.charAt(i) ;
		if( c < '0' || c > '9' ) {
			nPos = i ;
			break ;
		}
	}
	if( nPos > 0 ) {
		r = r.slice(0, nPos ) ;
	}
	return r ;
}



